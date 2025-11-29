package com.parcial3.app.controladores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.parcial3.app.entidades.Coordinador;
import com.parcial3.app.entidades.Estudiante;
import com.parcial3.app.entidades.EvaluacionSaberPro;
import com.parcial3.app.repositorios.CoordinadorRepositorio;
import com.parcial3.app.repositorios.EstudianteRepositorio;
import com.parcial3.app.repositorios.EvaluacionRepositorio;

@Controller
@RequestMapping("/coordinacion")
public class CoordinadorControlador {

    private final CoordinadorRepositorio coordRepo;
    private final EstudianteRepositorio estRepo;
    private final EvaluacionRepositorio evalRepo;

    public CoordinadorControlador(CoordinadorRepositorio coordRepo,
                                  EstudianteRepositorio estRepo,
                                  EvaluacionRepositorio evalRepo) {
        this.coordRepo = coordRepo;
        this.estRepo = estRepo;
        this.evalRepo = evalRepo;
    }

    // --- LOGIN ---
    @GetMapping("/login")
    public String loginForm() {
        return "login_coordinacion";
    }

    @PostMapping("/login")
    public String login(@RequestParam String usuario,
                        @RequestParam String clave,
                        Model model) {

        Coordinador c = coordRepo.findByUsuario(usuario);

        if (c == null || !c.getClave().equals(clave)) {
            model.addAttribute("error", "Credenciales inválidas");
            return "login_coordinacion";
        }

        return "redirect:/coordinacion/dashboard";
    }

    // --- DASHBOARD ---
    @GetMapping("/dashboard")
    public String dashboard() {
        return "coordinacion/dashboard";
    }
    
    @GetMapping("/crearEstudiante")
    public String mostrarFormularioNuevoEstudiante(Model model) {
        model.addAttribute("estudiante", new Estudiante());
        return "coordinacion/crearEstudiante";
    }

    // Procesar guardado de estudiante (similar al de coordinadores)
    @PostMapping("/crearEstudiante")
    public String guardarEstudiante(@ModelAttribute Estudiante estudiante, 
                                   RedirectAttributes redirectAttributes) {
        try {
            // ✅ CORREGIDO: Usar estRepo (instancia) en lugar de EstudianteRepositorio (clase)
            // ✅ CORREGIDO: Usar existsByUsuario en lugar de existsById
            
            // Validar que el usuario no exista
            if (estRepo.existsByUsuario(estudiante.getUsuario())) {
                redirectAttributes.addFlashAttribute("error", "El usuario ya existe");
                return "redirect:/coordinacion/crearEstudiante";
            }

            // Validar que el código no exista
            if (estRepo.existsByCodigo(estudiante.getCodigo())) {
                redirectAttributes.addFlashAttribute("error", "El código ya existe");
                return "redirect:/coordinacion/crearEstudiante";
            }

            // Validar número de registro si se proporciona
            if (estudiante.getNumeroRegistro() != null && !estudiante.getNumeroRegistro().isEmpty() &&
                estRepo.existsByNumeroRegistro(estudiante.getNumeroRegistro())) {
                redirectAttributes.addFlashAttribute("error", "El número de registro ya existe");
                return "redirect:/coordinacion/crearEstudiante";
            }

            // ✅ CORREGIDO: Usar estRepo.save()
            estRepo.save(estudiante);
            redirectAttributes.addFlashAttribute("success", "Estudiante creado exitosamente");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear el estudiante: " + e.getMessage());
        }
        
        return "redirect:/coordinacion/listarEstudiantes";
    }

    // --- LISTA DE ESTUDIANTES ---

		@GetMapping("/listarEstudiantes")
		public String listarEstudiantes(Model model) {
		    model.addAttribute("estudiantes", estRepo.findAll());
		    return "coordinacion/listarEstudiantes";
		}

    // --- CONSULTA POR NÚMERO DE REGISTRO ---
    @GetMapping("/evaluacion")
    public String buscarEval(@RequestParam("registro") String registro,
                             Model model) {

        model.addAttribute("evaluacion", evalRepo.findByNumeroRegistro(registro));
        return "detalle_evaluacion";
    }
    
    @GetMapping("/editarEstudiante/{id}")
    public String editarEstudiante(@PathVariable Long id, Model model) {
        Estudiante e = estRepo.findById(id).orElse(null);
        
        if (e == null) {
            model.addAttribute("error", "Estudiante no encontrado");
            return "redirect:/coordinacion/listarEstudiantes";
        }

        model.addAttribute("estudiante", e);
        return "coordinacion/editarEstudiante";
    }
    
    @PostMapping("/editarEstudiante/{id}")
    public String actualizarEstudiante(@PathVariable Long id,
                                       @ModelAttribute Estudiante estudiante,
                                       RedirectAttributes ra) {

        Estudiante actual = estRepo.findById(id).orElse(null);

        if (actual == null) {
            ra.addFlashAttribute("error", "Estudiante no encontrado");
            return "redirect:/coordinacion/listarEstudiantes";
        }

        // actualizar campos
        actual.setUsuario(estudiante.getUsuario());
        actual.setPassword(estudiante.getPassword());
        actual.setNombre(estudiante.getNombre());
        actual.setApellido(estudiante.getApellido());
        actual.setCodigo(estudiante.getCodigo());
        actual.setPrograma(estudiante.getPrograma());
        actual.setNumeroRegistro(estudiante.getNumeroRegistro());
        actual.setActivo(estudiante.isActivo());

        estRepo.save(actual);

        ra.addFlashAttribute("success", "Estudiante actualizado correctamente");
        return "redirect:/coordinacion/listarEstudiantes";
    }
    
    @GetMapping("/eliminarEstudiante/{id}")
    public String eliminarEstudiante(@PathVariable Long id, RedirectAttributes ra) {
        try {
            estRepo.deleteById(id);
            ra.addFlashAttribute("success", "Estudiante eliminado correctamente.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "No se pudo eliminar el estudiante.");
        }

        return "redirect:/coordinacion/listarEstudiantes";
    }
    
    @GetMapping("/informeAlumnos")
    public String informeAlumnos(
            @RequestParam(name = "programa", required = false) String programa,
            Model model) {

        List<Estudiante> estudiantes;

        if (programa != null && !programa.equals("todos")) {
            estudiantes = estRepo.findByPrograma(programa);
        } else {
            estudiantes = estRepo.findAll();
        }

        // Evaluaciones por estudiante
        Map<Long, Object> evaluaciones = new HashMap<>();
        for (Estudiante e : estudiantes) {
            var eval = evalRepo.findByNumeroRegistro(e.getNumeroRegistro());
            evaluaciones.put(e.getId(), eval);
        }
     // === Estadísticas: promedio de puntaje por programa ===
        Map<String, List<Integer>> puntajesPorPrograma = new HashMap<>();

        for (Estudiante e : estudiantes) {
            EvaluacionSaberPro eval = evalRepo.findByNumeroRegistro(e.getNumeroRegistro());
            if (eval != null && eval.getPuntaje_total() != null) {

                puntajesPorPrograma
                    .computeIfAbsent(e.getPrograma(), k -> new ArrayList<>())
                    .add(eval.getPuntaje_total());
            }
        }

        Map<String, Double> promedioPorPrograma = new HashMap<>();

        for (var entry : puntajesPorPrograma.entrySet()) {
            List<Integer> puntajes = entry.getValue();
            double prom = puntajes.stream().mapToInt(Integer::intValue).average().orElse(0);
            promedioPorPrograma.put(entry.getKey(), prom);
        }

        model.addAttribute("promedioPorPrograma", promedioPorPrograma);


        // Lista de programas para el <select>
        List<String> programas = estRepo.findAll()
                .stream()
                .map(Estudiante::getPrograma)
                .distinct()
                .toList();

        model.addAttribute("programas", programas);
        model.addAttribute("programaSeleccionado", programa);
        model.addAttribute("estudiantes", estudiantes);
        model.addAttribute("evaluaciones", evaluaciones);

        return "coordinacion/informeAlumnos";
    }

    
    @GetMapping("/detalleEvaluacion/{registro}")
    public String detalleEvaluacion(@PathVariable("registro") String registro, Model model) {

        EvaluacionSaberPro eval = evalRepo.findByNumeroRegistro(registro);

        if (eval == null) {
            model.addAttribute("error", "No existe evaluación para el registro: " + registro);
            return "coordinacion/informeAlumnos";
        }

        model.addAttribute("evaluacion", eval);

        return "coordinacion/detalleEvaluacion";
    }
    private String calcularBeneficio(int puntajeTotal) {

        if (puntajeTotal == 0) {
            return "Anulado";
        }

        if (puntajeTotal >= 1 && puntajeTotal <= 79) {
            return "No se gradúa";
        }

        if (puntajeTotal >= 80 && puntajeTotal <= 179) {
            return "Graduado";
        }

        if (puntajeTotal >= 180 && puntajeTotal <= 210) {
            return "Nivel 1";
        }

        if (puntajeTotal >= 211 && puntajeTotal <= 240) {
            return "Nivel 2";
        }

        if (puntajeTotal >= 241) {
            return "Nivel 3";
        }

        return "Sin beneficio";
    }

    
    @GetMapping("/beneficios")
    public String verBeneficios(Model model) {

        List<Estudiante> estudiantes = estRepo.findAll();
        List<EvaluacionSaberPro> evaluaciones = evalRepo.findAll();

        Map<String, EvaluacionSaberPro> mapaEval = evaluaciones.stream()
                .collect(Collectors.toMap(
                        EvaluacionSaberPro::getNumeroRegistro,
                        e -> e,
                        (a, b) -> a
                ));

        List<Map<String, String>> lista = new ArrayList<>();

        for (Estudiante est : estudiantes) {

            EvaluacionSaberPro eval = mapaEval.get(est.getNumeroRegistro());

            String beneficio = "Sin evaluación";
            if (eval != null) {
                beneficio = calcularBeneficio(eval.getPuntaje_total());
            }

            Map<String, String> fila = new HashMap<>();
            fila.put("nombre", est.getNombre() + " " + est.getApellido());
            fila.put("registro", est.getNumeroRegistro());
            fila.put("beneficio", beneficio);

            lista.add(fila);
        }

        model.addAttribute("listaBeneficios", lista);
        return "coordinacion/beneficios";
    }
    
    
}

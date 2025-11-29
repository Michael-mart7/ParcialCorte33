package com.parcial3.app.controladores;

import com.parcial3.app.entidades.Administrador;
import com.parcial3.app.entidades.Coordinador;
import com.parcial3.app.entidades.Estudiante;
import com.parcial3.app.entidades.EvaluacionSaberPro;

import com.parcial3.app.repositorios.AdministradorRepositorio;
import com.parcial3.app.repositorios.CoordinadorRepositorio;
import com.parcial3.app.repositorios.EstudianteRepositorio;
import com.parcial3.app.repositorios.EvaluacionRepositorio;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
@RequestMapping("/admin")
public class AdminControlador {

    private final AdministradorRepositorio adminRepo;
    private final CoordinadorRepositorio coordRepo;
    private final EstudianteRepositorio estRepo;
    private final EvaluacionRepositorio evalRepo;

    public AdminControlador(AdministradorRepositorio adminRepo,
                            CoordinadorRepositorio coordRepo,
                            EstudianteRepositorio estRepo,
                            EvaluacionRepositorio evalRepo) {
        this.adminRepo = adminRepo;
        this.coordRepo = coordRepo;
        this.estRepo = estRepo;
        this.evalRepo = evalRepo;
    }

    // --- LOGIN ADMIN ----
    @GetMapping("/login")
    public String loginForm() {
        return "login_admin";
    }

    @PostMapping("/login")
    public String login(@RequestParam String usuario,
                        @RequestParam String clave,
                        Model model) {

        Administrador a = adminRepo.findByUsuario(usuario);

        if (a == null || !a.getClave().equals(clave)) {
            model.addAttribute("error", "Credenciales inválidas");
            return "login_admin";
        }

        return "redirect:/admin/dashboard";
    }

    // --- DASHBOARD ADMIN ---
    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        long totalEstudiantes = estRepo.count();
        long totalCoordinadores = coordRepo.count();
        long totalEvaluaciones = evalRepo.count();

        long conBeneficios = evalRepo.findAll().stream()
                .filter(e -> e.getPuntaje_total() != null && e.getPuntaje_total() >= 180)
                .count();

        model.addAttribute("totalEstudiantes", totalEstudiantes);
        model.addAttribute("totalCoordinadores", totalCoordinadores);
        model.addAttribute("totalEvaluaciones", totalEvaluaciones);
        model.addAttribute("conBeneficios", conBeneficios);

        return "admin/dashboard";
    }

    // =========================
    // CRUD ESTUDIANTES (ADMIN)
    // =========================

    @GetMapping("/crearEstudiante")
    public String crearEstudiante(Model model) {
        model.addAttribute("estudiante", new Estudiante());
        return "admin/crearEstudiante"; // reutilizamos vista
    }

    @PostMapping("/crearEstudiante")
    public String guardarEstudiante(@ModelAttribute Estudiante estudiante,
                                    RedirectAttributes ra) {

        if (estRepo.existsByUsuario(estudiante.getUsuario())) {
            ra.addFlashAttribute("error", "El usuario ya existe");
            return "redirect:/admin/crearEstudiante";
        }

        if (estRepo.existsByCodigo(estudiante.getCodigo())) {
            ra.addFlashAttribute("error", "El código ya existe");
            return "redirect:/admin/crearEstudiante";
        }

        if (estudiante.getNumeroRegistro() != null &&
            !estudiante.getNumeroRegistro().isEmpty() &&
            estRepo.existsByNumeroRegistro(estudiante.getNumeroRegistro())) {
            ra.addFlashAttribute("error", "El número de registro ya existe");
            return "redirect:/admin/crearEstudiante";
        }

        estRepo.save(estudiante);
        ra.addFlashAttribute("success", "Estudiante creado exitosamente");
        return "redirect:/admin/listarEstudiantes";
    }

    @GetMapping("/listarEstudiantes")
    public String listarEstudiantes(Model model) {
        model.addAttribute("estudiantes", estRepo.findAll());
        return "admin/listarEstudiantes"; // misma vista
    }

    @GetMapping("/editarEstudiante/{id}")
    public String editarEstudiante(@PathVariable Long id, Model model) {

        Estudiante est = estRepo.findById(id).orElse(null);

        if (est == null) {
            model.addAttribute("error", "Estudiante no encontrado");
            return "redirect:/admin/listarEstudiantes";
        }

        model.addAttribute("estudiante", est);
        return "admin/editarEstudiante"; // misma vista
    }

    @PostMapping("/editarEstudiante/{id}")
    public String actualizarEstudiante(@PathVariable Long id,
                                       @ModelAttribute Estudiante estudiante,
                                       RedirectAttributes ra) {

        Estudiante actual = estRepo.findById(id).orElse(null);

        if (actual == null) {
            ra.addFlashAttribute("error", "Estudiante no encontrado");
            return "redirect:/admin/listarEstudiantes";
        }

        actual.setUsuario(estudiante.getUsuario());
        actual.setPassword(estudiante.getPassword());
        actual.setNombre(estudiante.getNombre());
        actual.setApellido(estudiante.getApellido());
        actual.setCodigo(estudiante.getCodigo());
        actual.setPrograma(estudiante.getPrograma());
        actual.setNumeroRegistro(estudiante.getNumeroRegistro());
        actual.setActivo(estudiante.isActivo());

        estRepo.save(actual);

        ra.addFlashAttribute("success", "Estudiante editado correctamente");
        return "redirect:/admin/listarEstudiantes";
    }

    @GetMapping("/eliminarEstudiante/{id}")
    public String eliminarEstudiante(@PathVariable Long id, RedirectAttributes ra) {

        try {
            estRepo.deleteById(id);
            ra.addFlashAttribute("success", "Estudiante eliminado correctamente");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "No se pudo eliminar");
        }

        return "redirect:/admin/listarEstudiantes";
    }

    // ======================
    // INFORME DE EVALUACIONES
    // ======================

    @GetMapping("/informeAlumnos")
    public String informeAlumnos(Model model) {

        List<Estudiante> estudiantes = estRepo.findAll();
        Map<Long, EvaluacionSaberPro> evaluaciones = new HashMap<>();

        for (Estudiante e : estudiantes) {
            evaluaciones.put(e.getId(), evalRepo.findByNumeroRegistro(e.getNumeroRegistro()));
        }

        model.addAttribute("estudiantes", estudiantes);
        model.addAttribute("evaluaciones", evaluaciones);

        return "admin/informeAlumnos"; // reutilizamos la vista original
    }
    

    @GetMapping("/detalleEvaluacion/{registro}")
    public String detalleEvaluacion(@PathVariable String registro, Model model) {

        EvaluacionSaberPro eval = evalRepo.findByNumeroRegistro(registro);

        if (eval == null) {
            model.addAttribute("error", "No existe evaluación para ese registro");
            return "redirect:/admin/informeAlumnos";
        }

        model.addAttribute("evaluacion", eval);
        return "admin/detalleEvaluacion";
    }
    @GetMapping("/listarCoordinadores")
    public String listarCoordinadores(Model model) {
        model.addAttribute("coordinadores", coordRepo.findAll());
        return "admin/listarCoordinadores";
    }
    
 // ==================== EDITAR COORDINADOR =====================
    @GetMapping("/editarCoordinador/{id}")
    public String editarCoordinador(@PathVariable Long id, Model model) {

        Coordinador c = coordRepo.findById(id).orElse(null);

        if (c == null) {
            model.addAttribute("error", "Coordinador no encontrado");
            return "redirect:/admin/listarCoordinadores";
        }

        model.addAttribute("coordinador", c);
        return "admin/editarCoordinador";
    }

    @PostMapping("/editarCoordinador/{id}")
    public String actualizarCoordinador(
            @PathVariable Long id,
            @ModelAttribute Coordinador coordinador,
            RedirectAttributes ra) {

        Coordinador actual = coordRepo.findById(id).orElse(null);

        if (actual == null) {
            ra.addFlashAttribute("error", "Coordinador no encontrado");
            return "redirect:/admin/listarCoordinadores";
        }

        actual.setUsuario(coordinador.getUsuario());
        actual.setClave(coordinador.getClave());
        actual.setNombre(coordinador.getNombre());
        actual.setTelefono(coordinador.getTelefono()); // si existe

        coordRepo.save(actual);

        ra.addFlashAttribute("success", "Coordinador actualizado correctamente");

        return "redirect:/admin/listarCoordinadores";
    }

       // ==================== ELIMINAR =====================
    @GetMapping("/eliminarCoordinador/{id}")
    public String eliminarCoordinador(@PathVariable Long id, RedirectAttributes ra) {

        try {
            coordRepo.deleteById(id);
            ra.addFlashAttribute("success", "Coordinador eliminado correctamente");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "No se pudo eliminar el coordinador");
        }

        return "redirect:/admin/listarCoordinadores";
    }
    @GetMapping("/formCoordinador")
    public String formCoordinador(Model model) {
        model.addAttribute("coordinador", new Coordinador());
        return "admin/formCoordinador";
    }


}

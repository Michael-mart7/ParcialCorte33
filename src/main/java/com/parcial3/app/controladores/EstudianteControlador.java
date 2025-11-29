package com.parcial3.app.controladores;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.parcial3.app.entidades.Estudiante;
import com.parcial3.app.entidades.EvaluacionSaberPro;
import com.parcial3.app.repositorios.EstudianteRepositorio;
import com.parcial3.app.repositorios.EvaluacionRepositorio;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/estudiante")
@SessionAttributes("evaluacion")   
public class EstudianteControlador {

    private final EstudianteRepositorio estRepo;
    private final EvaluacionRepositorio evalRepo;
    private String calcularBeneficio(int puntaje) {
        if (puntaje >= 241) return "Nivel 3 (Exoneración + 100% Beca)";
        if (puntaje >= 211) return "Nivel 2 (Exoneración + 50% Beca)";
        if (puntaje >= 180) return "Nivel 1 (Exoneración)";
        return "Sin beneficio";
    }


    public EstudianteControlador(EstudianteRepositorio estRepo,
                                 EvaluacionRepositorio evalRepo) {
        this.estRepo = estRepo;
        this.evalRepo = evalRepo;
    }

    @ModelAttribute("evaluacion")
    public Object evaluacion() {
        return null; // inicializa la variable de sesión
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login_estudiante";  // carpeta estudiante/
    }

    @PostMapping("/login")
    public String login(@RequestParam String usuario,
                        @RequestParam String password,
                        Model model,
                        HttpSession session) {

        Estudiante e = estRepo.findByUsuario(usuario);

        if (e == null || !e.getPassword().equals(password)) {
            model.addAttribute("error", "Credenciales inválidas");
            return "estudiante/login_estudiante";
        }

        // Guardar estudiante en sesión
        session.setAttribute("estudianteLogueado", e);

        return "redirect:/estudiante/dashboard";
    }


    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {

        Estudiante e = (Estudiante) session.getAttribute("estudianteLogueado");

        if (e == null) {
            return "redirect:/estudiante/login";
        }

        // Buscar evaluación
        EvaluacionSaberPro evaluacion = evalRepo.findByNumeroRegistro(e.getNumeroRegistro());

        // Si no tiene evaluación asignada
        if (evaluacion == null) {
            model.addAttribute("estudiante", e);
            model.addAttribute("evaluacion", null);
            model.addAttribute("beneficio", "Sin datos de evaluación");
            return "estudiante/dashboard";
        }

        // Calcular beneficio
        String beneficio = calcularBeneficio(evaluacion.getPuntaje_total());

        model.addAttribute("estudiante", e);
        model.addAttribute("evaluacion", evaluacion);
        model.addAttribute("beneficio", beneficio);

        return "estudiante/dashboard";
    }

    
    @GetMapping("/resultados/{registro}")
    public String verResultados(@PathVariable String registro, Model model) {

        EvaluacionSaberPro eval = evalRepo.findByNumeroRegistro(registro);

        if (eval == null) {
            model.addAttribute("error", "No se encontraron resultados para tu número de registro.");
            return "estudiante/dashboard";
        }

        model.addAttribute("evaluacion", eval);

        return "estudiante/resultados";
    }
    
}

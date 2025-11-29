package com.parcial3.app.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogoutControlador {

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/";
    }
}

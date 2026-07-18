package com.gymapp.gymapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gymapp.gymapp.entity.Usuario;
import com.gymapp.gymapp.service.UsuarioService;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // 1. Muestra la pantalla de Login al entrar a http://localhost:8081/login o a la raíz "/"
    @GetMapping({"/", "/login"})
    public String mostrarLogin() {
        return "login"; // Busca templates/login.html
    }

    // 2. Procesa los datos que se envían desde el formulario HTML
    @PostMapping("/login")
    public String procesarLogin(@RequestParam String identificador, 
                                @RequestParam String password, 
                                Model model) {
        
        // Llamamos al servicio para que haga la magia de buscar y validar en Neon.tech
        Usuario usuario = usuarioService.login(identificador, password);

        if (usuario != null) {
            // Guardamos el nombre en el "Model" para poder saludar al usuario en el HTML
            model.addAttribute("nombreUsuario", usuario.getNombre());
            
            // Redireccionamos a la pantalla correspondiente según su rol
            if ("PROFESOR".equalsIgnoreCase(usuario.getRol())) {
                return "profesor"; // Va a templates/profesor.html
            } else if ("ALUMNO".equalsIgnoreCase(usuario.getRol())) {
                return "alumno"; // Va a templates/alumno.html
            }
        }

        // Si falló el login, mandamos un mensaje de error de vuelta al HTML de login
        model.addAttribute("error", "DNI/Email o contraseña incorrectos. Intente nuevamente.");
        return "login"; 
    }
}

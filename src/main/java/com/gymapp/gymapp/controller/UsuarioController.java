package com.gymapp.gymapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gymapp.gymapp.entity.Alumno;
import com.gymapp.gymapp.entity.Profesor;
import com.gymapp.gymapp.entity.Usuario;
import com.gymapp.gymapp.service.UsuarioService;

import jakarta.servlet.http.HttpSession;

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
                                HttpSession session, // <--- 1. Agregamos la sesión aquí
                                Model model) {
        
        Usuario usuario = usuarioService.login(identificador, password);

        if (usuario != null) {
            // <--- 2. Guardamos el usuario en la sesión global
            session.setAttribute("usuarioLogueado", usuario); 
            
            // Redireccionamos a la pantalla correspondiente según su rol
            if ("PROFESOR".equalsIgnoreCase(usuario.getRol())) {
                return "redirect:/profesor"; // <-- Cambiado: en lugar de return "profesor";
            } else if ("ALUMNO".equalsIgnoreCase(usuario.getRol())) {
                return "redirect:/alumno/dashboard"; 
            }
        }

        model.addAttribute("Error", "DNI/Email o contraseña incorrectos. Intente nuevamente.");
        return "login"; 
    }
    
    // 1. Mostrar la pantalla de registro (ahora envía la lista de profesores)
    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("profesores", usuarioService.obtenerProfesores());
        return "registro";
    }

    // 2. Procesar el formulario de registro
    @PostMapping("/registro")
    public String procesarRegistro(@RequestParam String dni,
                                @RequestParam String nombre,
                                @RequestParam String email,
                                @RequestParam String password,
                                @RequestParam String rol,
                                @RequestParam(required = false) String profesorDni, // Capturamos el profesor (opcional)
                                Model model) {

        if (usuarioService.existeDni(dni)) {
            model.addAttribute("Error", "El DNI ingresado ya está registrado.");
            model.addAttribute("profesores", usuarioService.obtenerProfesores()); // Recargar lista
            return "registro";
        }
        
        // Validamos y creamos el objeto según el rol
        Usuario nuevoUsuario;
        
        if ("ALUMNO".equalsIgnoreCase(rol)) {
            Alumno alumno = new Alumno();
            
            // Asignamos el profesor si se seleccionó uno
            if (profesorDni != null && !profesorDni.isEmpty()) {
                Profesor prof = (Profesor) usuarioService.buscarPorDni(profesorDni);
                alumno.setProfesor(prof);
            }
            nuevoUsuario = alumno;
        } else {
            nuevoUsuario = new Profesor();
        }

        // Datos comunes
        nuevoUsuario.setDni(dni);
        nuevoUsuario.setNombre(nombre);
        nuevoUsuario.setEmail(email);
        nuevoUsuario.setPassword(password);

        usuarioService.registrarUsuario(nuevoUsuario);

        model.addAttribute("Exito", "¡Registro completado! Ya puedes iniciar sesión.");
        return "login";
    }

    // 5. Endpoint opcional para cerrar sesión limpia
    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        session.invalidate(); // Destruye la sesión
        return "redirect:/login";
    }
}

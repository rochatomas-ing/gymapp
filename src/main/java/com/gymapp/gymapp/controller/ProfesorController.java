package com.gymapp.gymapp.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gymapp.gymapp.entity.Alumno;
import com.gymapp.gymapp.entity.Usuario;
import com.gymapp.gymapp.service.UsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ProfesorController {
    @Autowired
    private UsuarioService usuarioService;

    // Endpoint para ver el Dashboard Principal del Profesor
    @GetMapping("/profesor")
    public String mostrarDashboardProfesor(HttpSession session, Model model) {
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");

        // Validar seguridad de sesión y rol
        if (usuarioLogueado == null || !"PROFESOR".equals(usuarioLogueado.getRol())) {
            return "redirect:/login";
        }

        model.addAttribute("profesor", usuarioLogueado);
        return "profesor"; // Carga templates/profesor.html
    }

    //Mostrar el apartado alumnos
    @GetMapping("/profesor/alumnos")
    public String listarAlumnosProfesor(@RequestParam(required = false) String nombre,
                                        HttpSession session,
                                        Model model) {
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");

        // Validar seguridad de sesión
        if (usuarioLogueado == null || !"PROFESOR".equals(usuarioLogueado.getRol())) {
            return "redirect:/login";
        }

        // Traer lista filtrada o completa de alumnos del profesor
        List<Alumno> alumnos = usuarioService.buscarAlumnosDeProfesor(usuarioLogueado.getDni(), nombre);

        model.addAttribute("alumnos", alumnos);
        model.addAttribute("nombreFiltro", nombre); 
        model.addAttribute("profesor", usuarioLogueado);
        
        // ¡NUEVO!: Pasamos la fecha actual para el selector de pagos
        model.addAttribute("hoy", java.time.LocalDate.now()); 

        return "profesor-alumnos";
    }

    @PostMapping("/profesor/alumnos/actualizar-cuota")
    public String actualizarCuota(@RequestParam String alumnoDni, 
                                @RequestParam(required = false) 
                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaPago,
                                @RequestParam(required = false) String nombreFiltro,
                                HttpSession session) {
        
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuarioLogueado == null || !"PROFESOR".equals(usuarioLogueado.getRol())) {
            return "redirect:/login";
        }

        // Guardamos con la fecha elegida en el selector
        usuarioService.registrarPagoAlumno(alumnoDni, fechaPago);

        if (nombreFiltro != null && !nombreFiltro.isBlank()) {
            return "redirect:/profesor/alumnos?nombre=" + nombreFiltro;
        }
        return "redirect:/profesor/alumnos";
    }

    // Endpoint para anular/borrar el último pago de un alumno
    @PostMapping("/profesor/alumnos/anular-cuota")
    public String anularCuota(@RequestParam String alumnoDni, 
                              @RequestParam(required = false) String nombreFiltro,
                              HttpSession session) {
        
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuarioLogueado == null || !"PROFESOR".equals(usuarioLogueado.getRol())) {
            return "redirect:/login";
        }

        // Llamamos al servicio para borrar la fecha de pago
        usuarioService.anularPagoAlumno(alumnoDni);

        // Redirigimos conservando el filtro si el profesor estaba buscando a alguien
        if (nombreFiltro != null && !nombreFiltro.isBlank()) {
            return "redirect:/profesor/alumnos?nombre=" + nombreFiltro;
        }
        return "redirect:/profesor/alumnos";
    }
}

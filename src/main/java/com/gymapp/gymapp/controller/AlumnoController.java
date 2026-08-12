package com.gymapp.gymapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gymapp.gymapp.entity.Rutina;
import com.gymapp.gymapp.entity.Usuario;
import com.gymapp.gymapp.service.PdfService;
import com.gymapp.gymapp.service.RutinaService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/alumno")
public class AlumnoController {

    @Autowired
    private RutinaService rutinaService;

    @Autowired
    private PdfService pdfService;

    // 1. Mostrar Vista Principal Alumno
    @GetMapping("/dashboard")
    public String verDashboard(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null) {
            return "redirect:/login";
        }

        Rutina rutina = rutinaService.buscarRutinaPorAlumno(usuario.getDni());
        
        model.addAttribute("alumno", usuario);
        model.addAttribute("rutina", rutina);

        return "alumno";
    }

    // 2. Exportar Rutina a PDF
    @GetMapping("/rutina/pdf")
    public ResponseEntity<byte[]> descargarPdf(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Rutina rutina = rutinaService.buscarRutinaPorAlumno(usuario.getDni());
        if (rutina == null) {
            return ResponseEntity.notFound().build();
        }

        // Generamos el array de bytes del PDF utilizando el servicio
        byte[] pdfBytes = pdfService.generarRutinaPdf(rutina, usuario);

        // Configuramos las cabeceras HTTP de respuesta
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename("Mi_Rutina_" + usuario.getNombre() + ".pdf")
                .build());

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
}
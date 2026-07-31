package com.gymapp.gymapp.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gymapp.gymapp.entity.DiaRutina;
import com.gymapp.gymapp.entity.ItemRutina;
import com.gymapp.gymapp.entity.Rutina;
import com.gymapp.gymapp.entity.Usuario;
import com.gymapp.gymapp.service.RutinaService;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/alumno")
public class AlumnoController {

    @Autowired
    private RutinaService rutinaService;

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
    public void descargarPdf(HttpSession session, HttpServletResponse response) throws IOException {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) return;

        Rutina rutina = rutinaService.buscarRutinaPorAlumno(usuario.getDni());
        if (rutina == null) return;

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=Mi_Rutina.pdf");

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        // Encabezado del PDF
        Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        document.add(new Paragraph("Tu rutina es esta", fontTitulo));
        document.add(new Paragraph("Alumno: " + usuario.getNombre() + " | DNI: " + usuario.getDni() + "\n\n"));

        // Generar Tablas por cada día
        if (rutina.getDias() != null) {
            for (DiaRutina dia : rutina.getDias()) {
                Font fontDia = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13);
                document.add(new Paragraph("Día: " + dia.getNombre(), fontDia));
                document.add(new Paragraph(" "));

                PdfPTable table = new PdfPTable(4); // 4 Columnas exacta a tu modelo
                table.setWidthPercentage(100);
                table.addCell("Ejercicio");
                table.addCell("Peso");
                table.addCell("Repeticiones/Series");
                table.addCell("Aumento de peso semanal");

                if (dia.getItems() != null) {
                    for (ItemRutina item : dia.getItems()) {
                        table.addCell(item.getEjercicio() != null ? item.getEjercicio() : "-");
                        table.addCell(item.getPeso() != null ? item.getPeso() : "-");
                        table.addCell(item.getRepeticionesSeries() != null ? item.getRepeticionesSeries() : "-");
                        table.addCell(item.getAumentoPesoSemanal() != null ? item.getAumentoPesoSemanal() : "-");
                    }
                }

                document.add(table);
                document.add(new Paragraph("\n"));
            }
        }

        document.close();
    }
}
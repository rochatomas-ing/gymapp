package com.gymapp.gymapp.service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;

import org.springframework.stereotype.Service;

import com.gymapp.gymapp.entity.DiaRutina;
import com.gymapp.gymapp.entity.ItemRutina;
import com.gymapp.gymapp.entity.Rutina;
import com.gymapp.gymapp.entity.Usuario;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

@Service
public class PdfService {

    public byte[] generarRutinaPdf(Rutina rutina, Usuario usuario) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 36, 36, 36, 36);

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // 1. TÍTULO Y ENCABEZADO
            Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph titulo = new Paragraph("Genesis Gym - Plan de Entrenamiento", fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(4f);
            document.add(titulo);

            Font fontSubtitulo = FontFactory.getFont(FontFactory.HELVETICA, 11, Color.GRAY);
            Paragraph subtitulo = new Paragraph("Alumno: " + usuario.getNombre() + " | DNI: " + usuario.getDni(), fontSubtitulo);
            subtitulo.setAlignment(Element.ALIGN_CENTER);
            subtitulo.setSpacingAfter(15f);
            document.add(subtitulo);

            // 2. RECORRIDO DE DÍAS
            if (rutina.getDias() != null) {
                int countDia = 1;
                for (DiaRutina dia : rutina.getDias()) {
                    
                    // --- NOMBRE DEL DÍA ---
                    // Se muestra solo el nombre cargado (ej. "Lunes", "Día 1", etc.)
                    Font fontDia = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
                    String nombreDia = (dia.getNombre() != null && !dia.getNombre().trim().isEmpty())? dia.getNombre(): "Día " + countDia;
                    
                    Paragraph pNombreDia = new Paragraph(nombreDia, fontDia);
                    pNombreDia.setSpacingBefore(15f);
                    pNombreDia.setSpacingAfter(10f); // Espacio seguro antes de las secciones
                    document.add(pNombreDia);

                    // --- BLOQUE A: CALENTAMIENTO ---
                    boolean tieneCalentamiento = dia.getItems() != null && dia.getItems().stream()
                            .anyMatch(i -> Boolean.TRUE.equals(i.getEsCalentamiento()));

                    if (tieneCalentamiento) {
                        Font fontSeccion = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, Color.DARK_GRAY);
                        Paragraph pCalentamiento = new Paragraph("Calentamiento", fontSeccion);
                        pCalentamiento.setSpacingBefore(5f);
                        pCalentamiento.setSpacingAfter(6f); // <--- Aleja el texto de la tabla
                        document.add(pCalentamiento);

                        PdfPTable tablaCalentamiento = new PdfPTable(3);
                        tablaCalentamiento.setWidthPercentage(100);
                        tablaCalentamiento.setSpacingAfter(15f); // <--- Separa de la siguiente tabla

                        agregarCeldaEncabezado(tablaCalentamiento, "Ejercicio");
                        agregarCeldaEncabezado(tablaCalentamiento, "Peso");
                        agregarCeldaEncabezado(tablaCalentamiento, "Repeticiones / Series");

                        for (ItemRutina item : dia.getItems()) {
                            if (Boolean.TRUE.equals(item.getEsCalentamiento())) {
                                tablaCalentamiento.addCell(item.getEjercicio() != null ? item.getEjercicio() : "-");
                                tablaCalentamiento.addCell(item.getPeso() != null ? item.getPeso() : "-");
                                tablaCalentamiento.addCell(item.getRepeticionesSeries() != null ? item.getRepeticionesSeries() : "-");
                            }
                        }
                        document.add(tablaCalentamiento);
                    }

                    // --- BLOQUE B: RUTINA PRINCIPAL (6 COLUMNAS) ---
                    Font fontSeccion = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, Color.DARK_GRAY);
                    Paragraph pRutina = new Paragraph("Rutina Principal", fontSeccion);
                    pRutina.setSpacingBefore(5f);
                    pRutina.setSpacingAfter(6f); // <--- Aleja el texto de la tabla
                    document.add(pRutina);

                    PdfPTable tablaPrincipal = new PdfPTable(6);
                    tablaPrincipal.setWidthPercentage(100);
                    tablaPrincipal.setWidths(new float[]{2.5f, 1.5f, 1.8f, 1.2f, 1.2f, 1.2f}); // Proporciones de columna
                    tablaPrincipal.setSpacingAfter(25f); // <--- Separa holgadamente del siguiente día

                    agregarCeldaEncabezado(tablaPrincipal, "Ejercicio");
                    agregarCeldaEncabezado(tablaPrincipal, "Peso Inicial");
                    agregarCeldaEncabezado(tablaPrincipal, "Reps/Series");
                    agregarCeldaEncabezado(tablaPrincipal, "Sem 2");
                    agregarCeldaEncabezado(tablaPrincipal, "Sem 3");
                    agregarCeldaEncabezado(tablaPrincipal, "Sem 4");

                    if (dia.getItems() != null) {
                        for (ItemRutina item : dia.getItems()) {
                            if (!Boolean.TRUE.equals(item.getEsCalentamiento())) {
                                tablaPrincipal.addCell(item.getEjercicio() != null ? item.getEjercicio() : "-");
                                tablaPrincipal.addCell(item.getPeso() != null ? item.getPeso() : "-");
                                tablaPrincipal.addCell(item.getRepeticionesSeries() != null ? item.getRepeticionesSeries() : "-");
                                tablaPrincipal.addCell(item.getSem2() != null ? item.getSem2() : "-");
                                tablaPrincipal.addCell(item.getSem3() != null ? item.getSem3() : "-");
                                tablaPrincipal.addCell(item.getSem4() != null ? item.getSem4() : "-");
                            }
                        }
                    }

                    document.add(tablaPrincipal);
                    countDia++;
                }
            }

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return out.toByteArray();
    }

    private void agregarCeldaEncabezado(PdfPTable table, String texto) {
        PdfPCell cell = new PdfPCell(new Phrase(texto, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10)));
        cell.setBackgroundColor(new Color(230, 230, 230));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5f); // <-- Margen interno para que no quede apretado
        table.addCell(cell);
    }
}
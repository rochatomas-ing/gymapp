package com.gymapp.gymapp.controller;

import java.time.LocalDate;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gymapp.gymapp.entity.Alumno;
import com.gymapp.gymapp.entity.DiaRutina;
import com.gymapp.gymapp.entity.ItemRutina;
import com.gymapp.gymapp.entity.Rutina;
import com.gymapp.gymapp.service.RutinaService;
import com.gymapp.gymapp.service.UsuarioService;

@Controller
@RequestMapping("/profesor/rutinas")
public class RutinaController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RutinaService rutinaService;

    // 1. CREAR RUTINA DESDE CERO
    @GetMapping("/crear/{dni}")
    public String mostrarFormularioCreacion(
            @PathVariable String dni,
            @RequestParam(name = "eliminarAnterior", defaultValue = "false") boolean eliminarAnterior,
            Model model) {

        Alumno alumno = (Alumno) usuarioService.buscarPorDni(dni);

        // Si se confirmó el cartel de advertencia, borramos la rutina previa
        if (eliminarAnterior) {
            rutinaService.eliminarRutinaPorAlumno(dni);
        }

        // Armamos la plantilla vacía
        Rutina rutina = new Rutina();
        rutina.setAlumno(alumno);

        // ✅ Creamos ÚNICAMENTE el Día 1
        DiaRutina dia1 = new DiaRutina("Día 1");

        // Pre-cargamos filas iniciales para el Día 1 (ejemplo: 1 de calentamiento y 1 principal)
        ItemRutina itemCalentamiento = new ItemRutina();
        itemCalentamiento.setEsCalentamiento(true);
        dia1.agregarItem(itemCalentamiento);

        ItemRutina itemPrincipal = new ItemRutina();
        itemPrincipal.setEsCalentamiento(false);
        dia1.agregarItem(itemPrincipal);

        // Agregamos solo este día a la rutina
        rutina.agregarDia(dia1);

        model.addAttribute("rutina", rutina);
        return "crear-rutina";
    }

    @GetMapping("/editar/{dni}")
    public String editarRutina(@PathVariable("dni") String dni, Model model) {
        Rutina rutina = rutinaService.buscarRutinaPorAlumno(dni);

        model.addAttribute("rutina", rutina);
        
        // Retorna la vista directamente dentro de templates/ (o "profesor/editar-rutina" si usas subcartera)
        return "editar-rutina";
    }

    // 3. GUARDAR O ACTUALIZAR
    @PostMapping("/guardar")
    public String guardarRutina(@ModelAttribute Rutina rutina) {
        
        // Re-vinculamos al alumno para asegurar la FK
        if (rutina.getAlumno() != null && rutina.getAlumno().getDni() != null) {
            Alumno alumno = (Alumno) usuarioService.buscarPorDni(rutina.getAlumno().getDni());
            rutina.setAlumno(alumno);
        }

        // Sincronizamos las relaciones bidireccionales y filtramos filas no completadas
        if (rutina.getDias() != null) {
            for (DiaRutina dia : rutina.getDias()) {
                dia.setRutina(rutina);

                if (dia.getItems() != null) {
                    Iterator<ItemRutina> iterator = dia.getItems().iterator();
                    while (iterator.hasNext()) {
                        ItemRutina item = iterator.next();
                        
                        // Si la fila no tiene nombre de ejercicio, no la persistimos
                        if (item.getEjercicio() == null || item.getEjercicio().trim().isEmpty()) {
                            iterator.remove();
                        } else {
                            item.setDiaRutina(dia);
                        }
                    }
                }
            }
        }
        
        if (rutina.getFechaCreacion() == null) {
            rutina.setFechaCreacion(LocalDate.now());
        }

        rutinaService.guardarRutina(rutina);
        
        return "redirect:/profesor/alumnos?exito=RutinaGuardada";
    }
}
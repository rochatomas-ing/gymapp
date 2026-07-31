package com.gymapp.gymapp.controller;

// Imports de Spring Framework para la web y rutas
import java.time.LocalDate;

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
    private UsuarioService usuarioService; // O tu service para Alumnos
    @Autowired
    private RutinaService rutinaService;   // El service que vayas a crear para Rutinas

    @GetMapping("/crear/{dni}")
    public String mostrarFormularioCreacion(
            @PathVariable String dni, 
            @RequestParam(name = "eliminarAnterior", defaultValue = "false") boolean eliminarAnterior,
            Model model) {

        // 1. Buscás al alumno
        Alumno alumno = (Alumno) usuarioService.buscarPorDni(dni);

        // 2. Si apretó Aceptar en el cartel, borramos la rutina vieja
        if (eliminarAnterior) {
            rutinaService.eliminarRutinaPorAlumno(dni);
        }

        // 3. Preparamos la rutina nueva en blanco
        Rutina rutina = new Rutina();
        rutina.setAlumno(alumno);

        DiaRutina diaInicial = new DiaRutina();
        diaInicial.setNombre(""); 
        diaInicial.agregarItem(new ItemRutina());
        diaInicial.agregarItem(new ItemRutina());
        diaInicial.agregarItem(new ItemRutina());

        rutina.agregarDia(diaInicial);

        model.addAttribute("rutina", rutina);

        return "crear-rutina"; 
    }

    // 2. Recibir y guardar la rutina
    @PostMapping("/guardar")
    public String guardarRutina(@ModelAttribute Rutina rutina) {
        
        // 1. Recuperamos el alumno por su DNI para mantener la relación intacta
        if (rutina.getAlumno() != null && rutina.getAlumno().getDni() != null) {
            Alumno alumno = (Alumno) usuarioService.buscarPorDni(rutina.getAlumno().getDni());
            rutina.setAlumno(alumno);
        }

        // 2. Enlazamos la jerarquía completa (Rutina -> Día -> Item)
        if (rutina.getDias() != null) {
            for (DiaRutina dia : rutina.getDias()) {
                dia.setRutina(rutina);
                if (dia.getItems() != null) {
                    for (ItemRutina item : dia.getItems()) {
                        item.setDiaRutina(dia);
                    }
                }
            }
        }
        
        rutina.setFechaCreacion(LocalDate.now());
        rutinaService.guardarRutina(rutina);
        
        // Redirigimos a la vista del profesor enviando el parámetro 'exito'
        return "redirect:/profesor/alumnos?exito=RutinaCreada";
    }
}
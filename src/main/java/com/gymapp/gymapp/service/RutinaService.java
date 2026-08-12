package com.gymapp.gymapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gymapp.gymapp.entity.Rutina;
import com.gymapp.gymapp.repository.RutinaRepository;

@Service
public class RutinaService {

    @Autowired
    private RutinaRepository rutinaRepository;

    /**
     * Guarda la rutina y todos sus items en la base de datos.
     * Usamos @Transactional para asegurar que si algo falla, 
     * no se guarde la rutina a medias.
     */
    @Transactional
    public void guardarRutina(Rutina rutina) {
        // Al usar CascadeType.ALL en la entidad Rutina, 
        // guardar la rutina guarda automáticamente todos sus ItemRutina.
        rutinaRepository.save(rutina);
    }

    /**
     * Busca la rutina asignada a un alumno por su DNI.
     */
    @Transactional
    public Rutina buscarRutinaPorAlumno(String dniAlumno) {
        return rutinaRepository.findByAlumnoDni(dniAlumno);
    }

    @Transactional
    public void eliminarRutinaPorAlumno(String dni) {
        List<Rutina> rutinasExistentes = rutinaRepository.findAllByAlumnoDni(dni);

        if (!rutinasExistentes.isEmpty()) {
            // Elimina todas las rutinas acumuladas del alumno y sus contenidos en cascada
            rutinaRepository.deleteAll(rutinasExistentes);
        }
    }
}
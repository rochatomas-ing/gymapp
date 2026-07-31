package com.gymapp.gymapp.repository;

import com.gymapp.gymapp.entity.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlumnoRepository extends JpaRepository<Alumno, String>{

    // 1. Obtener todos los alumnos asignados a un profesor
    List<Alumno> findByProfesorDni(String profesorDni);

    // 2. Filtrar alumnos de un profesor buscando por nombre (sin importar mayúsculas/minúsculas)
    List<Alumno> findByProfesorDniAndNombreContainingIgnoreCase(String profesorDni, String nombre);
}
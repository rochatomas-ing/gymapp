package com.gymapp.gymapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gymapp.gymapp.entity.Rutina;

@Repository
public interface RutinaRepository extends JpaRepository<Rutina, Long> {
    
    // Spring Boot implementa esto automáticamente por convención de nombres.
    // Te será muy útil más adelante para mostrarle al alumno su rutina actual.
    Rutina findByAlumnoDni(String dniAlumno);
    List<Rutina> findAllByAlumnoDni(String dni);
}

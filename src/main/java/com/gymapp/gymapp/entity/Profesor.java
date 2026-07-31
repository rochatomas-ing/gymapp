package com.gymapp.gymapp.entity;

import java.util.List;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

@Entity
@DiscriminatorValue("PROFESOR")
public class Profesor extends Usuario {
    
    // Un profesor tiene MUCHOS alumnos
    @OneToMany(mappedBy = "profesor") // Se conecta con el atributo 'profesor' de la clase Alumno
    private List<Alumno> alumnos;

    public Profesor() {
        super();
    }

    public List<Alumno> getAlumnos() { return alumnos; }
    public void setAlumnos(List<Alumno> alumnos) { this.alumnos = alumnos; }

    @Override
    public String getRol(){
        return "PROFESOR";
    }
}
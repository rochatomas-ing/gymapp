package com.gymapp.gymapp.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "rutinas")
public class Rutina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "alumno_dni")
    private Alumno alumno;

    @ManyToOne
    @JoinColumn(name = "profesor_dni")
    private Profesor profesor;

    private LocalDate fechaCreacion;

    // Relación con los días de la rutina. Si borramos la rutina, se borran sus días (y en cascada, los ítems).
    @OneToMany(mappedBy = "rutina", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DiaRutina> dias = new ArrayList<>();

    // --- CONSTRUCTORES ---
    public Rutina() {}

    public Rutina(Alumno alumno, Profesor profesor, LocalDate fechaCreacion) {
        this.alumno = alumno;
        this.profesor = profesor;
        this.fechaCreacion = fechaCreacion;
    }

    // --- GETTERS Y SETTERS ---
    public Long getId() { return this.id; }
    public void setId(Long id) { this.id = id; }

    public Alumno getAlumno() { return this.alumno; }
    public void setAlumno(Alumno alumno) { this.alumno = alumno; }

    public Profesor getProfesor() { return this.profesor; }
    public void setProfesor(Profesor profesor) { this.profesor = profesor; }

    public LocalDate getFechaCreacion() { return this.fechaCreacion; }
    public void setFechaCreacion(LocalDate fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public List<DiaRutina> getDias() { return this.dias; }
    public void setDias(List<DiaRutina> dias) { this.dias = dias; }

    // --- MÉTODO AUXILIAR ---
    public void agregarDia(DiaRutina dia) {
        this.dias.add(dia);
        dia.setRutina(this);
    }
}
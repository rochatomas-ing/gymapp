package com.gymapp.gymapp.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@DiscriminatorValue("ALUMNO")
public class Alumno extends Usuario {
    
    // Muchos alumnos tienen UN profesor
    @ManyToOne
    @JoinColumn(name = "profesor_dni") // Nombre de la columna clave foránea en la DB
    private Profesor profesor;

    //Estado de la cuota
    @Column
    private LocalDate fechaUltimoPago;

    public Alumno() {
        super();
    }

    public Profesor getProfesor() { return profesor; }
    public void setProfesor(Profesor profesor) { this.profesor = profesor; }


    public LocalDate getFechaUltimoPago() { return fechaUltimoPago; }
    public void setFechaUltimoPago(LocalDate fechaUltimoPago) { this.fechaUltimoPago = fechaUltimoPago; }

    @Override
    public String getRol(){
        return "ALUMNO";
    }

    // Calcula si está al día basado en los 30 días corridos
    public boolean isCuotaAlDia() {
        if (this.fechaUltimoPago == null) {
            return false; // Nunca pagó
        }
        // Vence exactamente a los 30 días de haber pagado
        LocalDate fechaVencimiento = this.fechaUltimoPago.plusDays(30);
        return !LocalDate.now().isAfter(fechaVencimiento);
    }
    
    // Retorna la fecha de vencimiento calculada para mostrar en la pantalla
    public LocalDate getFechaVencimiento() {
        if (this.fechaUltimoPago == null) return null;
        return this.fechaUltimoPago.plusDays(30);
    }
}
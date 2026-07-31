package com.gymapp.gymapp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "items_rutinas")
public class ItemRutina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ejercicio;
    private String peso;
    private String repeticionesSeries;
    private String aumentoPesoSemanal;

    @ManyToOne
    @JoinColumn(name = "dia_rutina_id")
    private DiaRutina diaRutina;

    // --- CONSTRUCTORES ---
    public ItemRutina() {}

    public ItemRutina(String ejercicio, String peso, String repeticionesSeries, String aumentoPesoSemanal) {
        this.ejercicio = ejercicio;
        this.peso = peso;
        this.repeticionesSeries = repeticionesSeries;
        this.aumentoPesoSemanal = aumentoPesoSemanal;
    }

    // --- GETTERS Y SETTERS ---
    public Long getId() { return this.id; }
    public void setId(Long id) { this.id = id; }

    public String getEjercicio() { return this.ejercicio; }
    public void setEjercicio(String ejercicio) { this.ejercicio = ejercicio; }

    public String getPeso() { return this.peso; }
    public void setPeso(String peso) { this.peso = peso; }

    public String getRepeticionesSeries() { return this.repeticionesSeries; }
    public void setRepeticionesSeries(String repeticionesSeries) { this.repeticionesSeries = repeticionesSeries; }

    public String getAumentoPesoSemanal() { return this.aumentoPesoSemanal; }
    public void setAumentoPesoSemanal(String aumentoPesoSemanal) { this.aumentoPesoSemanal = aumentoPesoSemanal; }

    public DiaRutina getDiaRutina() { return this.diaRutina; }
    public void setDiaRutina(DiaRutina diaRutina) { this.diaRutina = diaRutina; }
}
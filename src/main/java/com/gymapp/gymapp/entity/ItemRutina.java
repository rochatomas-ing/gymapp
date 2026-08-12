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

    private String sem2;
    private String sem3;
    private String sem4;

    // Identifica si es un ítem de Calentamiento (true) o de la Rutina principal (false)
    private boolean esCalentamiento = false;

    @ManyToOne
    @JoinColumn(name = "dia_rutina_id")
    private DiaRutina diaRutina;

    // --- CONSTRUCTORES ---
    public ItemRutina() {}

    public ItemRutina(String ejercicio, String peso, String repeticionesSeries, boolean esCalentamiento) {
        this.ejercicio = ejercicio;
        this.peso = peso;
        this.repeticionesSeries = repeticionesSeries;
        this.esCalentamiento = esCalentamiento;
    }

    public ItemRutina(String ejercicio, String peso, String repeticionesSeries, String sem2, String sem3, String sem4, boolean esCalentamiento) {
        this.ejercicio = ejercicio;
        this.peso = peso;
        this.repeticionesSeries = repeticionesSeries;
        this.sem2 = sem2;
        this.sem3 = sem3;
        this.sem4 = sem4;
        this.esCalentamiento = esCalentamiento;
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

    public String getSem2() { return this.sem2; }
    public void setSem2(String sem2) { this.sem2 = sem2; }

    public String getSem3() { return this.sem3; }
    public void setSem3(String sem3) { this.sem3 = sem3; }

    public String getSem4() { return this.sem4; }
    public void setSem4(String sem4) { this.sem4 = sem4; }

    public boolean getEsCalentamiento() { return this.esCalentamiento; }
    public void setEsCalentamiento(boolean esCalentamiento) { this.esCalentamiento = esCalentamiento; }

    public DiaRutina getDiaRutina() { return this.diaRutina; }
    public void setDiaRutina(DiaRutina diaRutina) { this.diaRutina = diaRutina; }
}
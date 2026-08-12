package com.gymapp.gymapp.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
@Table(name = "dias_rutinas")
public class DiaRutina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre; // Ej: "Lunes", "Día 1", etc.

    @ManyToOne
    @JoinColumn(name = "rutina_id")
    private Rutina rutina;

    @OneToMany(mappedBy = "diaRutina", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemRutina> items = new ArrayList<>();

    // --- CONSTRUCTORES ---
    public DiaRutina() {}

    public DiaRutina(String nombre) {
        this.nombre = nombre;
    }

    // --- GETTERS Y SETTERS ---
    public Long getId() { return this.id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return this.nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Rutina getRutina() { return this.rutina; }
    public void setRutina(Rutina rutina) { this.rutina = rutina; }

    public List<ItemRutina> getItems() { return this.items; }
    public void setItems(List<ItemRutina> items) { this.items = items; }

    // --- MÉTODO AUXILIAR ---
    public void agregarItem(ItemRutina item) {
        this.items.add(item);
        item.setDiaRutina(this);
    }

    // Filtros útiles para Thymeleaf al momento de ver la rutina
    public List<ItemRutina> getCalentamientos() {
        return items.stream()
                    .filter(ItemRutina::getEsCalentamiento)
                    .collect(Collectors.toList());
    }

    public List<ItemRutina> getEjerciciosPrincipales() {
        return items.stream()
                    .filter(item -> !item.getEsCalentamiento())
                    .collect(Collectors.toList());
    }
}
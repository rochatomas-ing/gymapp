package com.gymapp.gymapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios")
// Indicamos la estrategia de tabla única
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
// Creamos una columna oculta que JPA usará para saber qué tipo de usuario es
@DiscriminatorColumn(name = "tipo_usuario", discriminatorType = DiscriminatorType.STRING)
public abstract class Usuario {
    
    @Id
    private String dni;
    
    @Column(nullable = false)
    private String nombre;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String password;

    // --- CONSTRUCTORES ---
    public Usuario() {
        // Constructor vacío obligatorio para Spring JPA
    }

    public Usuario(String dni, String nombre, String email, String password) {
        this.dni = dni;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
    }

    // --- GETTERS Y SETTERS ---
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public abstract String getRol();
}

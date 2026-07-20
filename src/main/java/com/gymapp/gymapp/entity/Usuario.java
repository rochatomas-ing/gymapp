package com.gymapp.gymapp.entity; // <-- package actualizado a la ruta limpia

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    private String dni; // Usamos el DNI como clave primaria (Primary Key)

    @Column(nullable = false)
    private String nombre;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String rol; // "PROFESOR" o "ALUMNO"

    // --- CONSTRUCTORES ---
    public Usuario() {
        // Constructor vacío obligatorio para Spring JPA
    }

    public Usuario(String dni, String nombre, String email, String password, String rol) {
        this.dni = dni;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.rol = rol;
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

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
}

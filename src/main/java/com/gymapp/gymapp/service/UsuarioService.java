package com.gymapp.gymapp.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gymapp.gymapp.entity.Alumno;
import com.gymapp.gymapp.entity.Profesor;
import com.gymapp.gymapp.entity.Usuario;
import com.gymapp.gymapp.repository.AlumnoRepository;
import com.gymapp.gymapp.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AlumnoRepository alumnoRepository;

    /**
     * Intenta iniciar sesión con DNI o Email.
     * Retorna el objeto Usuario si es exitoso (para saber si es Alumno o Profesor),
     * o null si las credenciales son incorrectas.
     */
    public Usuario login(String identificador, String passwordIngresada) {
        Optional<Usuario> usuarioOpt;

        // 1. Buscamos primero por DNI, si no es numérico o no existe, buscamos por Email
        if (usuarioRepository.existsById(identificador)) {
            usuarioOpt = usuarioRepository.findById(identificador);
        } else {
            usuarioOpt = usuarioRepository.findByEmail(identificador);
        }

        // 2. Si encontramos al usuario, comparamos la contraseña
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            
            if (usuario.getPassword().equals(passwordIngresada)) {
                return usuario; // Login exitoso (retorna el usuario con su rol)
            }
        }

        return null; // Login fallido
    }

    /**
     * Registra un nuevo usuario en la base de datos.
     */
    public Usuario registrarUsuario(Usuario usuario) {
        // En un caso real, acá encriptaríamos la contraseña antes de guardar.
        return usuarioRepository.save(usuario);
    }

        // Verifica si ya existe el DNI (que es la Primary Key)
    public boolean existeDni(String dni) {
        return usuarioRepository.existsById(dni);
    }

    // Verifica si ya existe el Email
    public boolean existeEmail(String email) {
        return usuarioRepository.findByEmail(email).isPresent();
    }

    public List<Profesor> obtenerProfesores() {
        return usuarioRepository.obtenerProfesores();
    }

    // Busca un usuario por DNI (nos servirá para recuperar el Profesor seleccionado)
    public Usuario buscarPorDni(String dni) {
        return usuarioRepository.findById(dni).orElse(null);
    }

    // Busca alumnos asignados a un profesor, con o sin filtro de nombre
    public List<Alumno> buscarAlumnosDeProfesor(String profesorDni, String filtroNombre) {
        if (filtroNombre != null && !filtroNombre.trim().isEmpty()) {
            return alumnoRepository.findByProfesorDniAndNombreContainingIgnoreCase(profesorDni, filtroNombre.trim());
        }
        return alumnoRepository.findByProfesorDni(profesorDni);
    }

    // Registra el pago en la fecha específica seleccionada por el profesor
    public void registrarPagoAlumno(String alumnoDni, LocalDate fechaPago) {
        Alumno alumno = alumnoRepository.findById(alumnoDni).orElse(null);
        if (alumno != null) {
            // Si no envió fecha por alguna razón, usa la de hoy por defecto
            alumno.setFechaUltimoPago(fechaPago != null ? fechaPago : LocalDate.now());
            alumnoRepository.save(alumno);
        }
    }

    // Anular un pago cargado por error (lo vuelve a Pendiente)
    public void anularPagoAlumno(String alumnoDni) {
        Alumno alumno = alumnoRepository.findById(alumnoDni).orElse(null); 
        
        if (alumno != null) {
            alumno.setFechaUltimoPago(null);
            alumnoRepository.save(alumno); // Guardamos usando el mismo repo
        }
    }
}
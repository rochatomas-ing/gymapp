package com.gymapp.gymapp.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gymapp.gymapp.entity.Usuario;
import com.gymapp.gymapp.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

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
}
package com.gymapp.gymapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gymapp.gymapp.entity.Usuario;

@Repository
// JpaRepository recibe <Entidad, Tipo de dato de la Clave Primaria>
public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    
    // Spring traduce esto automáticamente a: SELECT * FROM usuarios WHERE email = ?
    Optional<Usuario> findByEmail(String email);
}

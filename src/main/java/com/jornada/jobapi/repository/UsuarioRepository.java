package com.jornada.jobapi.repository;

import com.jornada.jobapi.entity.UsuarioEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository {

    Optional<UsuarioEntity> findByEmail(String email);
}

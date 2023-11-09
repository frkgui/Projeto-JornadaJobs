package com.jornada.jobapi.repository;

import com.jornada.jobapi.entity.UsuarioEntity;
import jdk.dynalink.linker.LinkerServices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Integer> {

    Optional<UsuarioEntity> findByEmail(String email);

    Optional<UsuarioEntity> findByIdUsuario(Integer idLogado);
    List<UsuarioEntity> findByEmpresaVinculada(String empresa);

    List<UsuarioEntity> findByNome(String nome);




}

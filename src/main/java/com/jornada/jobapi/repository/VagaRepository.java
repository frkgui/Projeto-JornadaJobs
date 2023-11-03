package com.jornada.jobapi.repository;

import com.jornada.jobapi.entity.UsuarioEntity;
import com.jornada.jobapi.entity.VagasEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VagaRepository extends JpaRepository<VagasEntity, Integer> {

}

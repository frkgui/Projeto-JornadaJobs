package com.jornada.jobapi.repository;

import com.jornada.jobapi.entity.CargoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioCargoRepository extends JpaRepository<CargoEntity, Integer> {
}

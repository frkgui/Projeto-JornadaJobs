package com.jornada.jobapi.repository;

import com.jornada.jobapi.entity.CargoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioCargoRepository extends JpaRepository<CargoEntity, Integer> {
}

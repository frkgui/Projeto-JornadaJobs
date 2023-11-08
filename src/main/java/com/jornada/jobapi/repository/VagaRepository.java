package com.jornada.jobapi.repository;

import com.jornada.jobapi.dto.StatusVagas;
import com.jornada.jobapi.entity.VagasEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface VagaRepository extends JpaRepository<VagasEntity, Integer> {

    List<VagasEntity> findByDataEncerramentoLessThanAndStatus(Date dataEncerramento, StatusVagas status);

}

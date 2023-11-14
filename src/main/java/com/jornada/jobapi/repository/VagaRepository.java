package com.jornada.jobapi.repository;

import com.jornada.jobapi.dto.StatusVagas;
import com.jornada.jobapi.entity.UsuarioEntity;
import com.jornada.jobapi.entity.VagasEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
@Repository
public interface VagaRepository extends JpaRepository<VagasEntity, Integer> {

    List<VagasEntity> findByDataEncerramentoLessThanAndStatus(Date dataEncerramento, StatusVagas status);
    List<VagasEntity> findByIdRecrutador(UsuarioEntity id);

    List<VagasEntity> findByStatus(StatusVagas statusVagas);
}

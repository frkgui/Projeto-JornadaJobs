package com.jornada.jobapi.repository;

import com.jornada.jobapi.dto.StatusVagas;
import com.jornada.jobapi.entity.UsuarioEntity;
import com.jornada.jobapi.entity.VagasEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
@Repository
public interface VagaRepository extends JpaRepository<VagasEntity, Integer> {

    List<VagasEntity> findByDataEncerramentoLessThanAndStatus(Date dataEncerramento, StatusVagas status);
    List<VagasEntity> findByIdRecrutador(UsuarioEntity id);
    List<VagasEntity> findByStatus(StatusVagas statusVagas);
    @Modifying
    @Query(value = "UPDATE usuario_vagas SET pretensao_salarial = :pretensao WHERE id_usuario = :idUsuario AND id_vagas = :idVaga", nativeQuery = true)
    void atualizarPretensaoSalarial(@Param("idUsuario") Integer idUsuario, @Param("idVaga") Integer idVaga, @Param("pretensao") Integer pretensao);
}

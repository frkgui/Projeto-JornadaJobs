package com.jornada.jobapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.util.Date;

@Data
public class VagasDTO {
    @Schema(description = "Qualquer ID genérico", example = "1")
    private Integer idVagas;
    private String nome;
    private String descricao;
    private String competencias;
    private Date dataEncerramento;
    private Integer quantidadeVagas;
    private Integer quantidadeCandidatos;

    //desativar por fora
//    private StatusVagas status;
}

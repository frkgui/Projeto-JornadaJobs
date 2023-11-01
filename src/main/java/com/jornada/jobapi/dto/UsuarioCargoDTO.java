package com.jornada.jobapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class UsuarioCargoDTO {
    @Positive
    @Schema(description = "Qualquer ID genérico", example = "1")
    private Integer idUser;
    @Positive
    @Schema(description = "Qualquer ID genérico", example = "1")
    private Integer idCargo;
}

//deletar o carlos acha
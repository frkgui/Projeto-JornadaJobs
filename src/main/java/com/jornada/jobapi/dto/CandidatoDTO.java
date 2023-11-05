package com.jornada.jobapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CandidatoDTO {


    private Integer id;

    @Schema(description = "Nome Completo", example = "Pedro Frank Gonçalves de Ferrazza")
    @NotBlank
    @NotNull
    @Size(min = 3, max = 40, message = "Usuário deve conter entre 3 e 40 caracteres")
    private String nome;

    @Schema(description = "Colocar senha do usuário", example = "Senha-Segura123")
    @NotBlank
    @NotNull
    private String senha;
}

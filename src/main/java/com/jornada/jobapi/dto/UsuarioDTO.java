package com.jornada.jobapi.dto;

import com.jornada.jobapi.entity.CargoEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UsuarioDTO {

    @Schema(description = "Qualquer ID genérico", example = "1")
    private Integer id;

    @Schema(description = "Nome Completo", example = "Pedro Frank Gonçalves de Ferrazza")
    @NotBlank
    @NotNull
    @Size(min = 3, max = 40, message = "Usuário deve conter entre 3 e 40 caracteres")
    private String nome;

    @Schema(description = "Colocar e-mail do usuário", example = "name@gmail.com")
    @NotBlank
    @NotNull
    @Email
    private String email;

    @NotNull
    @Schema(description = "Cargo do usuario", example = "RECRUTADOR")
    private CargoEntity cargo;

    @Schema(description = "Colocar senha do usuário", example = "Senha-Segura123")
    @NotBlank
    @NotNull
    private String senha;
}


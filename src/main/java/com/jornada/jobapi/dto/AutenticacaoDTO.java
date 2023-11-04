package com.jornada.jobapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
public class AutenticacaoDTO {

    @Schema(description = "Colocar e-mail do usuário", example = "name@gmail.com")
    @NotBlank
    @NotNull
    @Email
    private String email;

    @Schema(description = "Colocar senha do usuário", example = "Senha123!")
    @NotBlank
    @NotNull
    private String senha;
}

package com.jornada.jobapi.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class AutenticacaoDTO {
    private String email;
    private String senha;
}

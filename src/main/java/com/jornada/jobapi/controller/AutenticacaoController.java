package com.jornada.jobapi.controller;

import com.jornada.jobapi.dto.AutenticacaoDTO;
import com.jornada.jobapi.exception.RegraDeNegocioException;
import com.jornada.jobapi.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/autenticacao")
@RequiredArgsConstructor
public class AutenticacaoController {

    // Login + token
    public final UsuarioService usuarioService;

    @Operation(summary = "Realiza o login e token do Usuário", description = "Este processo realiza o login e gera um token de um usuário válido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Deu certo!"),
            @ApiResponse(responseCode = "400",description = "Erro na validação de dados"),
            @ApiResponse(responseCode = "500",description = "Erro do servidor")
    })
    @PostMapping("/login")
    public String fazerLogin(@RequestBody AutenticacaoDTO autenticacaoDTO) throws RegraDeNegocioException {
        return usuarioService.fazerLogin(autenticacaoDTO);
    }
}

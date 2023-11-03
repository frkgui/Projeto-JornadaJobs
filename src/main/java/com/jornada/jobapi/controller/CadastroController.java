package com.jornada.jobapi.controller;

import com.jornada.jobapi.dto.UsuarioDTO;
import com.jornada.jobapi.exception.RegraDeNegocioException;
import com.jornada.jobapi.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cadastro")
@RequiredArgsConstructor
@Slf4j
public class CadastroController {
    private final UsuarioService usuarioService;

    @Operation(summary = "Cadastra Novo Recrutador", description = "Este processo realiza a inserção de Novo Recrutador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Deu certo!"),
            @ApiResponse(responseCode = "400",description = "Erro na validação de dados"),
            @ApiResponse(responseCode = "500",description = "Erro do servidor")
    })
    @PostMapping("/cadastrar-candidato")
    public UsuarioDTO cadastrarCandidato(@RequestBody @Valid UsuarioDTO dto) throws RegraDeNegocioException {
        log.info("Usuario foi inserido");
        return usuarioService.salvarUsuario(dto,1);
    }
    @Operation(summary = "Cadastra Novo Recrutador", description = "Este processo realiza a inserção de Novo Recrutador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Deu certo!"),
            @ApiResponse(responseCode = "400",description = "Erro na validação de dados"),
            @ApiResponse(responseCode = "500",description = "Erro do servidor")
    })
    @PostMapping("/cadastrar-recrutador")
    public UsuarioDTO cadastrarRecrutador(@RequestBody @Valid UsuarioDTO dto) throws RegraDeNegocioException {
        log.info("Usuario foi inserido");
        return usuarioService.salvarUsuario(dto,3);
    }
    @Operation(summary = "Cadastra Novo Recrutador", description = "Este processo realiza a inserção de Novo Recrutador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Deu certo!"),
            @ApiResponse(responseCode = "400",description = "Erro na validação de dados"),
            @ApiResponse(responseCode = "500",description = "Erro do servidor")
    })
    @PostMapping("/cadastrar-empresa")
    public UsuarioDTO cadastrarEmpresa(@RequestBody @Valid UsuarioDTO dto) throws RegraDeNegocioException {
        log.info("Usuario foi inserido");
        return usuarioService.salvarUsuario(dto,2);
    }
}

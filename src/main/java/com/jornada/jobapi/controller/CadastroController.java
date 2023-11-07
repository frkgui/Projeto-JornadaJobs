package com.jornada.jobapi.controller;

import com.jornada.jobapi.dto.SalvarUsuarioEmpresaDTO;
import com.jornada.jobapi.dto.UsuarioDTO;
import com.jornada.jobapi.dto.UsuarioEmpresaDTO;
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

    @Operation(summary = "Cadastrar um candidato", description = "Este processo realiza a inserção de um novo candidato")
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

    @Operation(summary = "Cadastrar uma empresa", description = "Este processo realiza a inserção de uma nova empresa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Deu certo!"),
            @ApiResponse(responseCode = "400",description = "Erro na validação de dados"),
            @ApiResponse(responseCode = "500",description = "Erro do servidor")
    })
    @PostMapping("/cadastrar-empresa")
    public SalvarUsuarioEmpresaDTO cadastrarEmpresa(@RequestBody @Valid SalvarUsuarioEmpresaDTO dto) throws RegraDeNegocioException {
        log.info("Usuario foi inserido");
        return usuarioService.salvarUsuarioEmpresa(dto,2);
    }

    @Operation(summary = "Cadastrar um recrutador", description = "Este processo realiza a inserção de um novo Recrutador")
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

}

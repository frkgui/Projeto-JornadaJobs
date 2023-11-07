package com.jornada.jobapi.controller;

import com.jornada.jobapi.dto.UsuarioDTO;
import com.jornada.jobapi.dto.UsuarioEmpresaDTO;
import com.jornada.jobapi.exception.RegraDeNegocioException;
import com.jornada.jobapi.service.UsuarioService;
import freemarker.core.OptInTemplateClassResolver;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/empresa")
@RequiredArgsConstructor
@Slf4j
public class EmpresaController {
    private final UsuarioService usuarioService;

    @Operation(summary = "Cadastra um novo Recrutador", description = "Este processo realiza a inserção de um Recrutador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Deu certo!"),
            @ApiResponse(responseCode = "400",description = "Erro na validação de dados"),
            @ApiResponse(responseCode = "500",description = "Erro do servidor")
    })
    @PostMapping("/cadastrar-recrutador")
    public UsuarioDTO cadastrarRecrutador(@RequestBody @Valid UsuarioDTO dto) throws RegraDeNegocioException {
        log.info("Recrutador foi inserido");
        return usuarioService.cadastrarRecrutadorNaEmpresa(dto,3);
    }

    @Operation(summary = "Ver usuarios da empresa", description = "Lista todos os usuarios da empresa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Deu certo!"),
            @ApiResponse(responseCode = "400",description = "Erro na validação de dados"),
            @ApiResponse(responseCode = "500",description = "Erro do servidor")
    })
    @GetMapping
    public List<UsuarioEmpresaDTO> listarUsuariosDaEmpresa() throws RegraDeNegocioException {
        return usuarioService.listarUsuariosDaEmpresa();
    }

    @Operation(summary = "Desativar um recrutador", description = "Este processo realiza a desativação de um Recrutador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Deu certo!"),
            @ApiResponse(responseCode = "400",description = "Erro na validação de dados"),
            @ApiResponse(responseCode = "500",description = "Erro do servidor")
    })
    @DeleteMapping("/desativar-recrutador/{nome}")
    public void desativarRecrutador(@PathVariable("nome") String nome) throws RegraDeNegocioException{
        usuarioService.dasativarRecrutador(nome);
    }

    @Operation(summary = "Deletar uma empresa", description = "Este processo realiza a remoção de uma Empresa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Deu certo!"),
            @ApiResponse(responseCode = "400",description = "Erro na validação de dados"),
            @ApiResponse(responseCode = "500",description = "Erro do servidor")
    })
    @DeleteMapping("/deletar-empresa")
    public void deletarEmpresa(@PathVariable("id") Integer id) throws RegraDeNegocioException{
        usuarioService.remover(id);
    }
}

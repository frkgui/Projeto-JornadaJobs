package com.jornada.jobapi.controller;

import com.jornada.jobapi.dto.UsuarioCandidatoRecrutadorDTO;
import com.jornada.jobapi.dto.UsuarioDTO;
import com.jornada.jobapi.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import com.jornada.jobapi.exception.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/candidato")
@RequiredArgsConstructor
@Slf4j
public class CandidatoController {
    private final UsuarioService usuarioService;

    @Operation(summary = "Ver dados", description = "Lista todos os usuarios na base de dados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Deu certo!"),
            @ApiResponse(responseCode = "400",description = "Erro na validação de dados"),
            @ApiResponse(responseCode = "500",description = "Erro do servidor")
    })
    @GetMapping
    public Optional<UsuarioDTO> listarDadosDoCandidato() throws RegraDeNegocioException {
        return usuarioService.listarDadosDoCandidatoLogado();
    }


    @Operation(summary = "Atualizar nome e senha", description = "Atualiza de acordo com a base de dados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Deu certo!"),
            @ApiResponse(responseCode = "400",description = "Erro na validação de dados"),
            @ApiResponse(responseCode = "500",description = "Erro do servidor")
    })
    @PutMapping
    public UsuarioCandidatoRecrutadorDTO atualizarCandidato(@RequestBody @Valid UsuarioCandidatoRecrutadorDTO dto) throws RegraDeNegocioException {
        return usuarioService.atualizarCandidatoOuRecrutador(dto);
    }

    @Operation(summary = "Deletar candidato", description = "Remove um candidato da base de dados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Candidato removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Candidato não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    @DeleteMapping("/{id}")
    public void removerCandidato(@PathVariable("id") Integer id) throws RegraDeNegocioException {
        usuarioService.removerCandidato(id);
    }


}

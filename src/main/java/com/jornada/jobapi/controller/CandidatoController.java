package com.jornada.jobapi.controller;

import com.jornada.jobapi.dto.AtualizarUsuarioDTO;
import com.jornada.jobapi.dto.UsuarioDTO;
import com.jornada.jobapi.dto.VagasDTO;
import com.jornada.jobapi.service.UsuarioService;
import com.jornada.jobapi.service.VagasService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
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
    private final VagasService vagasService;

    @Operation(summary = "Ver dados", description = "Lista todos os usuarios na base de dados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Deu certo!"),
            @ApiResponse(responseCode = "400",description = "Erro na validação de dados"),
            @ApiResponse(responseCode = "500",description = "Erro do servidor")
    })
    @GetMapping("/dados-pessoais")
    public Optional<UsuarioDTO> listarDadosDoCandidato() throws RegraDeNegocioException {
        return usuarioService.listarDadosDoCandidatoLogado();
    }
    @PostMapping("/candidatar")
    public String candidatarVaga(@Valid Integer idVaga) throws RegraDeNegocioException {
        return vagasService.candidatarVaga(idVaga);
    }

    @Operation(summary = "Listar vagas", description = "Lista todos as vagas na base de dados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Deu certo!"),
            @ApiResponse(responseCode = "400",description = "Erro na validação de dados"),
            @ApiResponse(responseCode = "500",description = "Erro do servidor")
    })
    @GetMapping("/vagas-abertas")
    public List<VagasDTO> listar(){
        return vagasService.listarVagas();
    }

    @GetMapping("/analisar-candidaturas")
    public List<VagasDTO> analisarCandidaturas() throws RegraDeNegocioException {
        return vagasService.vagasCandidatadas();
    }

    @Operation(summary = "Atualizar nome e senha", description = "Atualiza de acordo com a base de dados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Deu certo!"),
            @ApiResponse(responseCode = "400",description = "Erro na validação de dados"),
            @ApiResponse(responseCode = "500",description = "Erro do servidor")
    })
    @PutMapping
    public AtualizarUsuarioDTO atualizarCandidato(@RequestBody @Valid AtualizarUsuarioDTO dto) throws RegraDeNegocioException {
        return usuarioService.atualizarUsuario(dto);
    }

    @Operation(summary = "Deletar candidato", description = "Remove um candidato da base de dados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Candidato removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Candidato não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    @DeleteMapping
    public void removerCandidato() throws RegraDeNegocioException {
        usuarioService.remover();
    }

    @Operation(summary = "Desistir de Vaga", description = "Remove um candidato da base de dados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Candidato removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Candidato não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    @DeleteMapping("/desistir-vaga")
    public String desistirDaVaga(Integer idVaga) throws RegraDeNegocioException {
        vagasService.desistirVaga(idVaga);
        return ("Desistência concluida");
    }


}

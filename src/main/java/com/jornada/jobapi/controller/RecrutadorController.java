package com.jornada.jobapi.controller;

import com.jornada.jobapi.dto.*;
import com.jornada.jobapi.exception.RegraDeNegocioException;
import com.jornada.jobapi.service.EmailService;
import com.jornada.jobapi.service.UsuarioService;
import com.jornada.jobapi.service.VagasService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/recrutador")
@RequiredArgsConstructor
@Slf4j
public class RecrutadorController {
    private final UsuarioService usuarioService;
    private final VagasService vagasService;

    @Operation(summary = "Ver dados", description = "Lista todos os usuarios na base de dados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Deu certo!"),
            @ApiResponse(responseCode = "400",description = "Erro na validação de dados"),
            @ApiResponse(responseCode = "500",description = "Erro do servidor")
    })
    @GetMapping
    public Optional<UsuarioDTO> listarDadosDoCandidato() throws RegraDeNegocioException {
        return usuarioService.listarDadosDoRecrutadorLogado();
    }

    @Operation(summary = "Atualizar nome e senha", description = "Atualiza de acordo com a base de dados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Deu certo!"),
            @ApiResponse(responseCode = "400",description = "Erro na validação de dados"),
            @ApiResponse(responseCode = "500",description = "Erro do servidor")
    })
    @PutMapping
    public AtualizarUsuarioDTO atualizarRecrutador(@RequestBody @Valid AtualizarUsuarioDTO dto) throws RegraDeNegocioException {
        return usuarioService.atualizarUsuario(dto);
    }
    @PostMapping("/criar-vaga")
    public VagasDTO criarVaga(@RequestBody @Valid VagasDTO vagasDTO) throws RegraDeNegocioException {
        log.info("Vaga Criada com Sucesso");
        return vagasService.criarVaga(vagasDTO);
    }

    @Operation(summary = "Deletar um recrutador", description = "Este processo realiza a remoção de um Recrutador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Deu certo!"),
            @ApiResponse(responseCode = "400",description = "Erro na validação de dados"),
            @ApiResponse(responseCode = "500",description = "Erro do servidor")
    })
    @DeleteMapping
    public void deletarRecrutador() throws RegraDeNegocioException{
        vagasService.finalizarVagasDoRecrutador();
    }

    //Analisar Candidatos - Recrutador
    @GetMapping("/analisar-candidatos")
    public List<RetornoVagasDTO> analisarVaga() throws SQLException, RegraDeNegocioException {
        List<RetornoVagasDTO> lista = vagasService.analisarVaga();
        return lista;
    }

    @PostMapping("/escolher-candidato")
    public void escolherCandidato(Integer idVaga, Integer idUsuario) throws RegraDeNegocioException {
        vagasService.escolherCandidato(idVaga,idUsuario);
    }

    @DeleteMapping("/finalizar-vaga")
    public String finalizarVaga(Integer idVaga) throws RegraDeNegocioException{
        return vagasService.finalizarVaga(idVaga);
    }

}

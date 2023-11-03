package com.jornada.jobapi.controller;

import com.jornada.jobapi.dto.UsuarioDTO;
import com.jornada.jobapi.dto.VagasDTO;
import com.jornada.jobapi.exception.RegraDeNegocioException;
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

@RestController
@RequestMapping("/vagas")
@RequiredArgsConstructor
@Slf4j
public class VagaController {

    private final VagasService vagasService;

    @Operation(summary = "Cadastra Novo Recrutador", description = "Este processo realiza a inserção de Novo Recrutador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Deu certo!"),
            @ApiResponse(responseCode = "400",description = "Erro na validação de dados"),
            @ApiResponse(responseCode = "500",description = "Erro do servidor")
    })
//    //Apenas Recrutadores
//    @PostMapping("/cadastrar-vagas")
//    public UsuarioDTO cadastrarVagas(@RequestBody @Valid UsuarioDTO dto) throws RegraDeNegocioException {
//        log.info("Usuario foi inserido");
//        return vagasService.;
//    }

    //Apenas Candidatos
    @PostMapping("/candidatar")
    public Integer candidatarVaga(@RequestBody @Valid Integer idVaga) throws RegraDeNegocioException {
        log.info("Candidatura Realizada com Sucesso");
        return vagasService.candidatarVaga(idVaga);
    }

//    //Analisar Candidatos - Recrutador & Empresa
//    @GetMapping("/analisar-candidatos")
//    public List<VagasDTO> analisarVaga() throws SQLException {
//        List<VagasDTO> lista = vagasService.;
//        return lista;
//    }

//    //Ver Vagas - Apenas Candidato
//    @GetMapping("/ver-vagas")
//    public List<VagasDTO> analisarVaga() throws SQLException {
//        List<VagasDTO> lista = vagasService.;
//        return lista;
//    }

}

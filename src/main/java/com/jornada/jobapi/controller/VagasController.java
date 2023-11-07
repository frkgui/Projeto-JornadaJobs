package com.jornada.jobapi.controller;

import com.jornada.jobapi.dto.VagasDTO;
import com.jornada.jobapi.exception.RegraDeNegocioException;
import com.jornada.jobapi.service.VagasService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vagas")
@RequiredArgsConstructor
@Slf4j
public class VagasController {

    private final VagasService vagasService;

//    @Operation(summary = "Cadastra Novo Recrutador", description = "Este processo realiza a inserção de Novo Recrutador")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200",description = "Deu certo!"),
//            @ApiResponse(responseCode = "400",description = "Erro na validação de dados"),
//            @ApiResponse(responseCode = "500",description = "Erro do servidor")
//    })
//    //Apenas Recrutadores
//    @PostMapping("/cadastrar-vagas")
//    public UsuarioDTO cadastrarVagas(@RequestBody @Valid UsuarioDTO dto) throws RegraDeNegocioException {
//        log.info("Usuario foi inserido");
//        return vagasService.;
//    }

    //Apenas Candidatos
    @PostMapping("/candidatar")
    public VagasDTO candidatarVaga(@RequestBody @Valid VagasDTO vagasDTO) throws RegraDeNegocioException {
        log.info("Candidatura Realizada com Sucesso");
        return vagasService.candidatarVaga(vagasDTO);
    }

//    //Analisar Candidatos - Recrutador & Empresa
//    @GetMapping("/analisar-candidatos")
//    public List<VagasDTO> analisarVaga() throws SQLException {
//        List<VagasDTO> lista = vagasService.;
//        return lista;
//    }

//    //Ver Vagas - Apenas Candidato
    @Operation(summary = "Listar vagas/CANDIDATO", description = "Lista todos as vagas na base de dados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Deu certo!"),
            @ApiResponse(responseCode = "400",description = "Erro na validação de dados"),
            @ApiResponse(responseCode = "500",description = "Erro do servidor")
    })
    @GetMapping
    public List<VagasDTO> listar(){
    return vagasService.listarVagas();
}

}

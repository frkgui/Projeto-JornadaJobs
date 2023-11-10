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

    //    //Ver Vagas - Apenas Candidato
    @Operation(summary = "Listar vagas", description = "Lista todos as vagas na base de dados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Deu certo!"),
            @ApiResponse(responseCode = "400",description = "Erro na validação de dados"),
            @ApiResponse(responseCode = "500",description = "Erro do servidor")
    })
    @GetMapping
    public List<VagasDTO> listar(){
        return vagasService.listarVagas();
    }


//    //Apenas Recrutadores
//    @PostMapping("/cadastrar-vagas")
//    public UsuarioDTO cadastrarVagas(@RequestBody @Valid UsuarioDTO dto) throws RegraDeNegocioException {
//        log.info("Usuario foi inserido");
//        return vagasService.;
//    }

    //Apenas Candidatos
    @PostMapping("/criar-vaga")
    public VagasDTO criarVaga(@RequestBody @Valid VagasDTO vagasDTO) throws RegraDeNegocioException {
        log.info("Vaga Criada com Sucesso");
        return vagasService.criarVaga(vagasDTO);
    }






}

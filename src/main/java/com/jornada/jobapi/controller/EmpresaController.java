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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/empresa")
@RequiredArgsConstructor
@Slf4j
public class EmpresaController {
    private final UsuarioService usuarioService;

    @Operation(summary = "Cadastra Nova Empresa", description = "Este processo realiza a inserção de nova Empresa")
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

    @DeleteMapping("/desativar-recrutador")
    public void desativarRecrutador(@PathVariable("id") Integer id) throws RegraDeNegocioException{
        usuarioService.remover(id);
    }

    @DeleteMapping("/deletar-empresa")
    public void deletarEmpresa(@PathVariable("id") Integer id) throws RegraDeNegocioException{
        usuarioService.remover(id);
    }

}

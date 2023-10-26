package com.jornada.jobapi.controller;

import com.jornada.jobapi.dto.UsuarioDTO;
<<<<<<< HEAD
import com.jornada.jobapi.entity.UsuarioEntity;
import com.jornada.jobapi.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
=======
import com.jornada.jobapi.exception.RegraDeNegocioException;
import com.jornada.jobapi.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
>>>>>>> origin/main

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
@Slf4j
public class UsuarioController{
    private final UsuarioService usuarioService;

    @GetMapping
    public List<UsuarioDTO> listar(){
        return usuarioService.listar();
    }

    private final UsuarioService usuarioService;

    @Operation(summary = "Insere novo Usuário", description = "Este processo realiza a inserção de novo Usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Deu certo!"),
            @ApiResponse(responseCode = "400",description = "Erro na validação de dados"),
            @ApiResponse(responseCode = "500",description = "Erro do servidor")
    })
    @PostMapping
    public UsuarioDTO inserirUsuario(@RequestBody @Valid UsuarioDTO dto) throws RegraDeNegocioException {
        log.info("Usuario foi inserido");
        return usuarioService.salvarUsuario(dto);
    }

    @Operation(summary = "Atualiza usuários", description = "Atualiza usuários na base de dados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Deu certo!"),
            @ApiResponse(responseCode = "400",description = "Erro na validação de dados"),
            @ApiResponse(responseCode = "500",description = "Erro do servidor")
    })
    @PutMapping
    public UsuarioDTO atualizarUsuario(@RequestBody @Valid UsuarioDTO dto) throws RegraDeNegocioException {
        return usuarioService.atualizarUsuario(dto);
    }

    @Operation(summary = "Deleta usuarios", description = "Deleta usuarios na base de dados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Deu certo!"),
            @ApiResponse(responseCode = "400",description = "Erro na validação de dados"),
            @ApiResponse(responseCode = "500",description = "Erro do servidor")
    })
    @DeleteMapping("/{id}")
    public void remover(@PathVariable("id") Integer id) throws RegraDeNegocioException{
        usuarioService.remover(id);
    }

}

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

@RestController
@RequestMapping("/recrutador")
@RequiredArgsConstructor
@Slf4j
public class RecrutadorController {
    private final UsuarioService usuarioService;

    @DeleteMapping("/desativar-recrutador")
    public void desativarRecrutador(@PathVariable("id") Integer id) throws RegraDeNegocioException{
        usuarioService.remover(id);
    }


}

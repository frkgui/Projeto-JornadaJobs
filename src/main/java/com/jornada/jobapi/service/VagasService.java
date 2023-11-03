package com.jornada.jobapi.service;

import com.jornada.jobapi.mapper.UsuarioMapper;
import com.jornada.jobapi.repository.UsuarioRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

@Service
public class VagasService {
    private final UsuarioService usuarioService;
    private final UsuarioMapper usuarioMapper;
    private final UsuarioRepository usuarioRepository;
    private final AuthenticationManager authenticationManager;

    public VagasService(UsuarioService usuarioService, UsuarioMapper usuarioMapper, UsuarioRepository usuarioRepository, AuthenticationManager authenticationManager) {
        this.usuarioService = usuarioService;
        this.usuarioMapper = usuarioMapper;
        this.usuarioRepository = usuarioRepository;
        this.authenticationManager = authenticationManager;
    }

    public Integer candidatar(Integer idVaga) {
        Integer idUser = usuarioService.recuperarIdUsuarioLogado();
        //só necessita fazer a relação
    }

}

package com.jornada.jobapi.service;

import com.jornada.jobapi.dto.UsuarioDTO;
import com.jornada.jobapi.dto.VagasDTO;
import com.jornada.jobapi.entity.UsuarioEntity;
import com.jornada.jobapi.entity.VagasEntity;
import com.jornada.jobapi.exception.RegraDeNegocioException;
import com.jornada.jobapi.mapper.UsuarioMapper;
import com.jornada.jobapi.repository.UsuarioRepository;
import com.jornada.jobapi.repository.VagaRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class VagasService {
    private final UsuarioService usuarioService;
    private final UsuarioMapper usuarioMapper;
    private final VagaRepository vagaRepository;
    private final AuthenticationManager authenticationManager;

    public VagasService(UsuarioService usuarioService, UsuarioMapper usuarioMapper, VagaRepository vagaRepository, AuthenticationManager authenticationManager) {
        this.usuarioService = usuarioService;
        this.usuarioMapper = usuarioMapper;
        this.vagaRepository = vagaRepository;
        this.authenticationManager = authenticationManager;
    }

    public Integer candidatarVaga(Integer idVaga) throws RegraDeNegocioException {
        VagasEntity vagaRecuperada = recuperarVaga(idVaga);
        Integer idUser = usuarioService.recuperarIdUsuarioLogado();


        if (vagaRecuperada.getUsuarios() == null) {
            vagaRecuperada.setUsuarios(new HashSet<>());
        }

        // Criar uma instância do CargoEntity com o ID do cargo igual a 3
        UsuarioEntity usuarioEntity = new UsuarioEntity();
        usuarioEntity.setIdUsuario(idUser);
       // Adicionar o cargo à lista de cargos do usuário
        vagaRecuperada.getUsuarios().add(usuarioEntity);

       VagasEntity vagaAtt = vagaRepository.save(vagaRecuperada);

       return 1;
   }

    public VagasEntity recuperarVaga(Integer idVaga) throws RegraDeNegocioException {
        VagasEntity vagaS = vagaRepository.findById(idVaga).orElseThrow(() -> new RegraDeNegocioException("Vaga não encontrado!"));
        return vagaS;
    }

}

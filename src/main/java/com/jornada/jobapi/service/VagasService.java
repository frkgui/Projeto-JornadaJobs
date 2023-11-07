package com.jornada.jobapi.service;

import com.jornada.jobapi.dto.VagasDTO;
import com.jornada.jobapi.entity.UsuarioEntity;
import com.jornada.jobapi.entity.VagasEntity;
import com.jornada.jobapi.exception.RegraDeNegocioException;
import com.jornada.jobapi.mapper.UsuarioMapper;
import com.jornada.jobapi.mapper.VagasMapper;
import com.jornada.jobapi.repository.VagaRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class VagasService {
    private final UsuarioService usuarioService;
    private final UsuarioMapper usuarioMapper;
    private final VagasMapper vagasMapper;
    private final VagaRepository vagaRepository;
    private final AuthenticationManager authenticationManager;

    public VagasService(UsuarioService usuarioService, UsuarioMapper usuarioMapper, VagasMapper vagasMapper, VagaRepository vagaRepository, AuthenticationManager authenticationManager) {
        this.usuarioService = usuarioService;
        this.usuarioMapper = usuarioMapper;
        this.vagasMapper = vagasMapper;
        this.vagaRepository = vagaRepository;
        this.authenticationManager = authenticationManager;
    }

    public VagasDTO candidatarVaga(VagasDTO vagas) throws RegraDeNegocioException {
        VagasEntity vagasEntity = vagasMapper.toEntity(vagas);
        Integer idUser = usuarioService.recuperarIdUsuarioLogado();


        if (vagasEntity.getUsuarios() == null) {
            vagasEntity.setUsuarios(new HashSet<>());
        }

        // Criar uma instância do CargoEntity com o ID do cargo igual a 3
        UsuarioEntity usuarioEntity = new UsuarioEntity();
        usuarioEntity.setIdUsuario(idUser);
        // Adicionar o cargo à lista de cargos do usuário
        vagasEntity.getUsuarios().add(usuarioEntity);

        VagasEntity vagasAtt = vagaRepository.save(vagasEntity);

        VagasDTO vagasDTO = vagasMapper.toDTO(vagasAtt);
        return vagasDTO;
    }

    public VagasEntity recuperarVaga(Integer idVaga) throws RegraDeNegocioException {
        VagasEntity vagaS = vagaRepository.findById(idVaga).orElseThrow(() -> new RegraDeNegocioException("Vaga não encontrado!"));
        return vagaS;
    }

    public List<VagasDTO> listarVagas() {
        List<VagasEntity> listaEntity = vagaRepository.findAll();
        List<VagasDTO> listaDTO = listaEntity.stream().map(entity -> vagasMapper.toDTO(entity))
                .toList();
        return listaDTO;
    }

}

package com.jornada.jobapi.service;

import com.jornada.jobapi.dto.VagasDTO;
import com.jornada.jobapi.entity.UsuarioEntity;
import com.jornada.jobapi.entity.VagasEntity;
import com.jornada.jobapi.mapper.UsuarioMapper;
import com.jornada.jobapi.repository.UsuarioRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import java.util.HashSet;

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

    public Integer candidatarVaga(Integer idVaga) {
        Integer idUser = usuarioService.recuperarIdUsuarioLogado();
        //só necessita fazer a relação

        VagasEntity vagasEntity = new VagasEntity();



        if (vagasEntity.getUsuarios() == null) {
            vagasEntity.setUsuarios(new HashSet<>());
        }

        // Criar uma instância do CargoEntity com o ID do cargo igual a 3
        UsuarioEntity usuarioEntity = new UsuarioEntity();
        usuarioEntity.getVagas();

//        cargo.setIdCargo(idCargo);
//        // Certifique-se de que a entidade CargoEntity tenha um setter para o ID do cargo
//
//        // Adicionar o cargo à lista de cargos do usuário
//        usuarioEntitySalvo.getCargos().add(cargo);



//    }

}

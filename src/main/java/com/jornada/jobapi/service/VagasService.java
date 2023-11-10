package com.jornada.jobapi.service;

import com.jornada.jobapi.dto.StatusVagas;
import com.jornada.jobapi.dto.VagasDTO;
import com.jornada.jobapi.entity.UsuarioEntity;
import com.jornada.jobapi.entity.VagasEntity;
import com.jornada.jobapi.exception.RegraDeNegocioException;
import com.jornada.jobapi.mapper.UsuarioMapper;
import com.jornada.jobapi.mapper.VagasMapper;
import com.jornada.jobapi.repository.UsuarioRepository;
import com.jornada.jobapi.repository.VagaRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class VagasService {
    private final UsuarioService usuarioService;
    private final UsuarioMapper usuarioMapper;
    private final VagasMapper vagasMapper;
    private final VagaRepository vagaRepository;
    private final UsuarioRepository usuarioRepository;
    private final AuthenticationManager authenticationManager;

    private static final String TIME_ZONE = "America/Sao_Paulo";


    public VagasService(UsuarioService usuarioService, UsuarioMapper usuarioMapper, VagasMapper vagasMapper, VagaRepository vagaRepository, UsuarioRepository usuarioRepository, AuthenticationManager authenticationManager) {
        this.usuarioService = usuarioService;
        this.usuarioMapper = usuarioMapper;
        this.vagasMapper = vagasMapper;
        this.vagaRepository = vagaRepository;
        this.usuarioRepository = usuarioRepository;
        this.authenticationManager = authenticationManager;
    }

    public List<VagasDTO> listarVagas() {
        List<VagasEntity> listaEntity = vagaRepository.findAll();
        List<VagasDTO> listaDTO = listaEntity.stream().map(entity -> vagasMapper.toDTO(entity))
                .toList();
        return listaDTO;
    }


    public VagasDTO criarVaga(VagasDTO vagas) throws RegraDeNegocioException {
        VagasEntity vagasEntity = vagasMapper.toEntity(vagas);
        vagasEntity.setDataCriacao(new Date());
        vagasEntity.setStatus(StatusVagas.ABERTO);
        if (vagasEntity.getQuantidadeMaximaCandidatos() >= vagasEntity.getQuantidadeVagas()){
            vagaRepository.save(vagasEntity);
        }else {
            throw new RegraDeNegocioException("A quantidade de vagas deve ser menor que a quantidade de candidatos");
        }
        VagasDTO vagasDTO = vagasMapper.toDTO(vagasEntity);
        return vagasDTO;
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
    public List<VagasDTO> analisarVaga() throws RegraDeNegocioException {
        String empresa = usuarioService.recuperarUsuarioLogado().getEmpresaVinculada();

        return null;
    }
    public List<VagasDTO> vagasCandidatadas() throws RegraDeNegocioException {
        UsuarioEntity usuario = new UsuarioEntity();
        usuario = usuarioService.recuperarUsuarioLogado(); // ou obtenha o usuário do banco de dados usando o ID
        List<VagasEntity> vagasEntity = usuario.getVagas().stream().map(this::mapToVagasEntity).collect(Collectors.toList());
        List<VagasDTO> listaDTO = vagasEntity.stream().map(entity -> vagasMapper.toDTO(entity))
                .toList();
        return listaDTO;
    }
    private VagasEntity mapToVagasEntity(VagasEntity vaga) {
        VagasEntity vagasEntity = new VagasEntity();

        vagasEntity.setNome(vaga.getNome());
        vagasEntity.setDescricao(vaga.getDescricao());
        vagasEntity.setIdVagas(vaga.getIdVagas());
        vagasEntity.setQuantidadeVagas(vaga.getQuantidadeVagas());
        vagasEntity.setCompetencias(vaga.getCompetencias());
        vagasEntity.setDataEncerramento(vaga.getDataEncerramento());
        vagasEntity.setQuantidadeMaximaCandidatos(vaga.getQuantidadeMaximaCandidatos());

        return vagasEntity;
    }

    public VagasEntity recuperarVaga(Integer idVaga) throws RegraDeNegocioException {
        VagasEntity vagaS = vagaRepository.findById(idVaga).orElseThrow(() -> new RegraDeNegocioException("Vaga não encontrado!"));
        return vagaS;
    }

    @Scheduled(cron = "0 1 0 * * *", zone = TIME_ZONE) // Agendador para executar todos os dias às 00:01
//    @Scheduled(fixedDelay = 5000)
    public void verificarDataDeEncerramento() {
        Date hoje = new Date();
        List<VagasEntity> vagas = vagaRepository.findByDataEncerramentoLessThanAndStatus( hoje, StatusVagas.ABERTO);

        for (int i = 0; i < vagas.size(); i++) {
            VagasEntity vaga = vagas.get(i);
            vaga.setStatus(StatusVagas.FECHADO);
            vagaRepository.save(vaga);
        }
    }
    public Optional<VagasEntity> retornarVaga(Integer idVaga){
        Optional<VagasEntity> vaga = vagaRepository.findById(idVaga);
        return vaga;
    }
    public Integer desistirVaga(Integer idVaga) throws RegraDeNegocioException {
        UsuarioEntity usuario = usuarioService.recuperarUsuarioLogado();
        Optional<VagasEntity> vaga = retornarVaga(idVaga);

        if (vaga.isPresent()) {
            usuario.getVagas().remove(vaga.get());
            usuarioRepository.save(usuario);
            return 1;
        } else {
            throw new RegraDeNegocioException("Vaga não encontrada");
        }
    }

}

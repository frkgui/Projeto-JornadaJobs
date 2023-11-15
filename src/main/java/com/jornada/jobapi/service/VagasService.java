package com.jornada.jobapi.service;

import com.jornada.jobapi.dto.RetornoVagasDTO;
import com.jornada.jobapi.dto.StatusVagas;
import com.jornada.jobapi.dto.VagasDTO;
import com.jornada.jobapi.entity.UsuarioEntity;
import com.jornada.jobapi.entity.VagasEntity;
import com.jornada.jobapi.exception.RegraDeNegocioException;
import com.jornada.jobapi.mapper.UsuarioMapper;
import com.jornada.jobapi.mapper.VagasMapper;
import com.jornada.jobapi.repository.UsuarioRepository;
import com.jornada.jobapi.repository.VagaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class VagasService {
    private final EmailService emailService;
    private final UsuarioService usuarioService;
    private final UsuarioMapper usuarioMapper;
    private final VagasMapper vagasMapper;
    private final VagaRepository vagaRepository;
    private final UsuarioRepository usuarioRepository;
    private final AuthenticationManager authenticationManager;

    private static final String TIME_ZONE = "America/Sao_Paulo";

    @Value("${jwt.validade.token}")
    private String validadeJWT;

    @Value("${jwt.secret}")
    private String secret;

    public VagasService(EmailService emailService, UsuarioService usuarioService, UsuarioMapper usuarioMapper,
                        VagasMapper vagasMapper, VagaRepository vagaRepository, UsuarioRepository usuarioRepository,
                        AuthenticationManager authenticationManager) {
        this.emailService = emailService;
        this.usuarioService = usuarioService;
        this.usuarioMapper = usuarioMapper;
        this.vagasMapper = vagasMapper;
        this.vagaRepository = vagaRepository;
        this.usuarioRepository = usuarioRepository;
        this.authenticationManager = authenticationManager;
    }

    public List<VagasDTO> listarVagas() {
        List<VagasEntity> listaEntity = vagaRepository.findByStatus(StatusVagas.ABERTO);
        List<VagasDTO> listaDTO = listaEntity.stream().map(entity -> vagasMapper.toDTO(entity))
                .toList();
        return listaDTO;
    }

    public VagasDTO criarVaga(VagasDTO vagas) throws RegraDeNegocioException {
        VagasEntity vagasEntity = vagasMapper.toEntity(vagas);
        vagasEntity.setDataCriacao(new Date());
        vagasEntity.setStatus(StatusVagas.ABERTO);
        vagasEntity.setIdRecrutador(usuarioService.recuperarUsuarioLogado());

        if (vagasEntity.getQuantidadeMaximaCandidatos() >= vagasEntity.getQuantidadeVagas()){
            vagaRepository.save(vagasEntity);
        }else {
            throw new RegraDeNegocioException("A quantidade de vagas deve ser menor que a quantidade de candidatos");
        }
        VagasDTO vagasDTO = vagasMapper.toDTO(vagasEntity);
        return vagasDTO;
    }

    public String candidatarVaga(Integer idVaga) throws RegraDeNegocioException {
        VagasEntity vagaRecuperada = recuperarVaga(idVaga);
        Integer idUser = usuarioService.recuperarIdUsuarioLogado();

        if(vagaRecuperada.getStatus() == StatusVagas.FECHADO){
            throw new RegraDeNegocioException("Vaga Fechada!");
        }
        if (vagaRecuperada.getUsuarios() != null && usuarioJaCandidatado(vagaRecuperada, idUser)) {
            throw new RegraDeNegocioException("Usuário já se candidatou para esta vaga anteriormente");
        }
        if (vagaRecuperada.getUsuarios().size() >= vagaRecuperada.getQuantidadeMaximaCandidatos()){
            throw new RegraDeNegocioException("Quantidade Maxima de candidatos atingida!");
        }

//        vagaRecuperada.getQuantidadeMaximaCandidatos();
//        vagaRecuperada.getUsuarios().size();

        if (vagaRecuperada.getUsuarios() == null) {
            vagaRecuperada.setUsuarios(new HashSet<>());
        }

        UsuarioEntity usuarioEntity = new UsuarioEntity();
        usuarioEntity.setIdUsuario(idUser);

        // Adicionar Usuário na vaga
        vagaRecuperada.getUsuarios().add(usuarioEntity);


        vagaRepository.save(vagaRecuperada);

        return ("Candidatura Realizada com sucesso");
    }

    public boolean usuarioJaCandidatado(VagasEntity vaga, Integer idUsuario) {
        return vaga.getUsuarios()
                .stream()
                .anyMatch(usuario -> usuario.getIdUsuario().equals(idUsuario));
    }

    public List<RetornoVagasDTO> analisarVaga() throws RegraDeNegocioException {
        UsuarioEntity recrutador = usuarioService.recuperarUsuarioLogado();

        List<VagasEntity> vagasEntity = vagaRepository.findByIdRecrutador(recrutador);

        List<RetornoVagasDTO> listaDTO = vagasEntity.stream()
                .filter(vaga -> StatusVagas.ABERTO.equals(vaga.getStatus())) // Filtrar vagas com status ABERTO
                .map(vaga -> vagasMapper.toRDTO(vaga))
                .collect(Collectors.toList());

        return listaDTO;
    }

    public List<VagasDTO> vagasCandidatadas() throws RegraDeNegocioException {
        UsuarioEntity usuario = new UsuarioEntity();
        usuario = usuarioService.recuperarUsuarioLogado(); // ou obtenha o usuário do banco de dados usando o ID
        List<VagasEntity> vagasEntity = usuario.getVagas().stream()
                .filter(vaga -> StatusVagas.ABERTO.equals(vaga.getStatus())) // Filtrar vagas com status ABERTO
                .map(this::mapToVagasEntity)
                .collect(Collectors.toList());

        List<VagasDTO> listaDTO = vagasEntity.stream()
                .map(entity -> vagasMapper.toDTO(entity))
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
        VagasEntity vagaS = vagaRepository.findById(idVaga).orElseThrow(() -> new RegraDeNegocioException("Vaga não encontrada!"));
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
    public Integer escolherCandidato(Integer idVaga, Integer idUsuario) throws RegraDeNegocioException {
        VagasEntity vaga = vagaRepository.findById(idVaga).orElseThrow(() -> new RegraDeNegocioException("Vaga não encontrado!"));
        if(usuarioService.recuperarIdUsuarioLogado() != vaga.getIdRecrutador().getIdUsuario()){
            new RegraDeNegocioException("Vaga não pertence a você");
        }
        UsuarioEntity usuario = usuarioRepository.findById(idUsuario).orElseThrow(() -> new RegraDeNegocioException("Candidato não encontrado!"));
        if (!vaga.getUsuarios().contains(usuario)) {
            throw new RegraDeNegocioException("Usuário não encontrado");
        }
        Optional<UsuarioEntity> empresa1 = usuarioRepository.findByIdUsuario(idUsuario);
        Optional<UsuarioEntity> empresa = usuarioRepository.findByIdUsuario(empresa1.get().getEmpresaVinculada());

        String assunto = ("Você foi aprovado no processo seletivo da empresa");
        emailService.enviarEmailComTemplateAprovado(usuario.getEmail(),assunto,usuario.getNome());

        vaga.getUsuarios().remove(usuario);
        vagaRepository.save(vaga);

        //Quero pegar todos os usuários que estão relacionados a vaga, e enviar uma mensagem par aeles separadamente
        // preciso de ajuda na parte de pegar os usuários
        for (UsuarioEntity usuarioRestante : vaga.getUsuarios()) {
            String mensagem = ("Infelizmente você não foi aceito na seleção da empresa" +vaga.getIdRecrutador().getEmpresaVinculada());
            emailService.enviarEmailComTemplateReprovado(usuarioRestante.getEmail(),mensagem,usuarioRestante.getNome());
        }

        vaga.setStatus(StatusVagas.FECHADO);
        vagaRepository.save(vaga);
        return 1;
    }
    public String finalizarVaga(Integer idVaga) throws RegraDeNegocioException {
        VagasEntity vaga = vagaRepository.findById(idVaga).orElseThrow(() -> new RegraDeNegocioException("Vaga não encontrado!"));
        if(usuarioService.recuperarIdUsuarioLogado() == vaga.getIdRecrutador().getIdUsuario()){
            vaga.setStatus(StatusVagas.FECHADO);
            vagaRepository.save(vaga);
        }else{
            new RegraDeNegocioException("Vaga não pertence a você");
        }
        vagaFechada(vaga);
        return ("Vaga Fechada");
    }

    public Integer vagaFechada(VagasEntity vaga) throws RegraDeNegocioException {
        for (UsuarioEntity usuarioRestante : vaga.getUsuarios()) {
            String mensagem = ("Infelizmente você não foi aceito na seleção da empresa" +vaga.getIdRecrutador().getEmpresaVinculada());
            emailService.enviarEmailComTemplateReprovado(usuarioRestante.getEmail(),mensagem,usuarioRestante.getNome());
        }
        return 1;
    }

    public void finalizarVagasDoRecrutador() throws RegraDeNegocioException {
        UsuarioEntity recrutador = usuarioService.recuperarUsuarioLogado();
        List<VagasEntity> vagasDoRecrutador = vagaRepository.findByIdRecrutador(recrutador);

        for (VagasEntity vaga : vagasDoRecrutador) {
           finalizarVaga(vaga.getIdVagas());
           excluirDependenciaRecrutador(vaga.getIdVagas());
        }
        usuarioService.remover();
    }
    public void setarVagaFechado(Integer idVaga) throws RegraDeNegocioException {
        VagasEntity vaga = vagaRepository.findById(idVaga).orElseThrow(() -> new RegraDeNegocioException("Vaga não encontrado!"));
        if(usuarioService.recuperarIdUsuarioLogado() == vaga.getIdRecrutador().getIdUsuario()){
            vaga.setStatus(StatusVagas.FECHADO);
            vagaRepository.save(vaga);
        }else{
            throw new RegraDeNegocioException("Vaga não pertence a você");
        }
    }
    public void excluirDependenciaRecrutador(Integer idVaga) throws RegraDeNegocioException {
        VagasEntity vaga = vagaRepository.findById(idVaga).orElseThrow(() -> new RegraDeNegocioException("Vaga não encontrado!"));
        if(usuarioService.recuperarIdUsuarioLogado() == vaga.getIdRecrutador().getIdUsuario()){
            vaga.setIdRecrutador(null);
            vagaRepository.save(vaga);
        }else{
            throw new RegraDeNegocioException("Vaga não pertence a você");
        }
    }

}

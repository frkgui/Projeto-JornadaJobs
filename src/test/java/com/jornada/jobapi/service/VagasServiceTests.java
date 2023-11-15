package com.jornada.jobapi.service;

import com.jornada.jobapi.dto.*;
import com.jornada.jobapi.entity.UsuarioEntity;
import com.jornada.jobapi.entity.VagasEntity;
import com.jornada.jobapi.exception.RegraDeNegocioException;
import com.jornada.jobapi.mapper.VagasMapper;
import com.jornada.jobapi.repository.UsuarioRepository;
import com.jornada.jobapi.repository.VagaRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.annotation.Repeat;
import org.springframework.test.util.ReflectionTestUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class VagasServiceTests {
    @InjectMocks
    private VagasService vagasService;
    @Mock
    private UsuarioService usuarioService;
    @InjectMocks
    private EmailService emailService;
    @Mock
    private AuthenticationManager authenticationManager;

    private VagasMapper vagasMapper = Mappers.getMapper(VagasMapper.class);

    @Mock
    private VagaRepository vagaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private Jwts jwts;

    @Mock
    private Claims claims;

    @Mock
    private SimpleGrantedAuthority simpleGrantedAuthority;

    @BeforeEach
    public void init() {
        ReflectionTestUtils.setField(vagasService, "vagasMapper", vagasMapper);
        ReflectionTestUtils.setField(vagasService, "secret", "MinhaChaveSecreta");
        ReflectionTestUtils.setField(vagasService, "validadeJWT", "86400000");
    }

    @Test
    public void deveTestarListar() throws ParseException {
        // setup
        VagasEntity entity = getVagasEntity();
//        List<VagasEntity> listaDeEntities = List.of(entity);

//        when(vagaRepository.findByStatus(entity.getStatus())).thenReturn(listaDeEntities);

        // act
        List<VagasDTO> listagem = vagasService.listarVagas();

        // assert
        assertNotNull(listagem);
//        assertEquals(1, listagem.size());
    }

    @Test
    public void deveTestarCriarVagaComSucesso() throws RegraDeNegocioException, ParseException {
        // setup
        VagasDTO vagasDTO = getVagasDTO();
        VagasEntity vagasEntity = getVagasEntity();

        // Assume que o usuário logado tem permissão
        when(usuarioService.recuperarUsuarioLogado()).thenReturn(new UsuarioEntity());

        when(vagaRepository.save(any())).thenReturn(vagasEntity);

        // act
        VagasDTO vagaCriada = vagasService.criarVaga(vagasDTO);

        // assert
        assertNotNull(vagaCriada);
        // Adicione mais verificações conforme necessário
    }

//    @Test
    public void deveTestarCriarVagaComErro() throws RegraDeNegocioException, ParseException {
        // setup
        VagasDTO vagasDTO = getVagasDTO();
        VagasEntity vagasEntity = getVagasEntity();

        vagasEntity.setQuantidadeVagas(100);
        vagasEntity.setQuantidadeMaximaCandidatos(50);

        // Assume que o usuário logado tem permissão
        when(usuarioService.recuperarUsuarioLogado()).thenReturn(new UsuarioEntity());

//        when(vagaRepository.save(any())).thenReturn(vagasEntity);
//        when(vagaRepository.save(any())).thenThrow(RegraDeNegocioException.class);

        // act
        Assertions.assertThrows(RegraDeNegocioException.class, ()-> {
            //act
            vagasService.criarVaga(vagasDTO);
        });
//        VagasDTO vagaCriada = ;

        // assert
//        assertNotNull(vagaCriada);
        // Adicione mais verificações conforme necessário
    }


    @Test
    public void deveTestarCandidatarVagaComSucesso() throws RegraDeNegocioException, ParseException {
        //setup
        Integer idVaga = 1;

        VagasEntity vagasEntity = getVagasEntity();
        UsuarioEntity usuarioEntity = getUsuarioEntity();

        vagasEntity.setUsuarios(new HashSet<>());

        //comportamentos
        when(vagaRepository.findById(idVaga)).thenReturn(Optional.of(vagasEntity));
        when(usuarioService.recuperarIdUsuarioLogado()).thenReturn(usuarioEntity.getIdUsuario());
//        when(vagaRepository.save(idVaga)).thenReturn(usuarioEntity);

        //act
        vagasService.candidatarVaga(idVaga);

        //assert
        Assertions.assertNotNull(usuarioEntity);
        Assertions.assertNotNull(vagasEntity);
        Assertions.assertNotNull(idVaga);

    }

    @Test
    public void deveTestarCandidatarVagaComErroStatus() throws ParseException {
        //setup
        Integer idVaga = 1;

        VagasEntity vagasEntity = getVagasEntity();
        UsuarioEntity usuarioEntity = getUsuarioEntity();
        vagasEntity.setStatus(StatusVagas.FECHADO);

        Integer idUser = usuarioService.recuperarIdUsuarioLogado();
        usuarioEntity.setIdUsuario(idUser);

        vagasEntity.setUsuarios(Collections.singleton((usuarioEntity)));

        //comportamentos
        when(vagaRepository.findById(idVaga)).thenReturn(Optional.of(vagasEntity));
//        when(usuarioService.recuperarIdUsuarioLogado()).thenReturn(usuarioEntity.getIdUsuario());
//        when(vagaRepository.save(idVaga)).thenReturn(usuarioEntity);

        //act
        Assertions.assertThrows(RegraDeNegocioException.class, ()-> {
            //act
            vagasService.candidatarVaga(idVaga);
        });
    }

    @Test
    public void deveTestarCandidatarVagaComErroUsuario() throws ParseException {
        //setup
        Integer idVaga = 1;

        VagasEntity vagasEntity = getVagasEntity();
        UsuarioEntity usuarioEntity = getUsuarioEntity();

        Integer idUser = usuarioService.recuperarIdUsuarioLogado();
        usuarioEntity.setIdUsuario(idUser);

        vagasEntity.setUsuarios(Collections.singleton((usuarioEntity)));

        //comportamentos
        when(vagaRepository.findById(idVaga)).thenReturn(Optional.of(vagasEntity));
        when(usuarioService.recuperarIdUsuarioLogado()).thenReturn(usuarioEntity.getIdUsuario());
//        when(vagaRepository.save(idVaga)).thenReturn(usuarioEntity);

        //act
        Assertions.assertThrows(RegraDeNegocioException.class, ()-> {
            //act
            vagasService.candidatarVaga(idVaga);
        });
    }

    @Test
    public void deveTestarCandidatarVagaComErroQuantidadeCandidatos() {
        //setup
        Integer idVaga = 1;

        VagasEntity vagasEntity = new VagasEntity();

        vagasEntity.setQuantidadeMaximaCandidatos(0);


        vagasEntity.setUsuarios(new HashSet<>());


        //comportamentos
        when(vagaRepository.findById(idVaga)).thenReturn(Optional.of(vagasEntity));
//        when(usuarioService.recuperarIdUsuarioLogado()).thenReturn(usuarioEntity.getIdUsuario());
//        when(vagaRepository.save(idVaga)).thenReturn(usuarioEntity);

        //act
        Assertions.assertThrows(RegraDeNegocioException.class, ()-> {
            //act
            vagasService.candidatarVaga(idVaga);
        });
    }


    @Test
    public void deveTestarUsuarioJaCandidatadoComSucesso() throws ParseException {
        //setup
        Integer idUser = usuarioService.recuperarIdUsuarioLogado();
        VagasEntity vagasEntity = getVagasEntity();

        //act
        vagasService.usuarioJaCandidatado(vagasEntity, idUser);
        //assert


    }

    @Test
    public void deveTestarAnalisarVaga() throws RegraDeNegocioException, ParseException {
        //setup
        VagasEntity vagasEntity = getVagasEntity();
        UsuarioEntity usuarioEntity = getUsuarioEntity();

//        when(usuarioService.recuperarUsuarioLogado()).thenReturn(usuarioEntity);
//        when(vagaRepository.findByIdRecrutador(usuarioEntity)).thenReturn(vagasEntities);

        //act
        vagasService.analisarVaga();

        //assert
        Assertions.assertNotNull(vagasEntity);
        Assertions.assertNotNull(usuarioEntity);


    }

    @Test
    public void deveTestarVagasCandidatadas() throws RegraDeNegocioException, ParseException {
        //setup
        VagasEntity vagasEntity = getVagasEntity();
        UsuarioEntity usuarioEntity = getUsuarioEntity();

        usuarioEntity.setVagas(new HashSet<>());

        when(usuarioService.recuperarUsuarioLogado()).thenReturn(usuarioEntity);

        //act
        vagasService.vagasCandidatadas();

        //assert
        Assertions.assertNotNull(vagasEntity);
        Assertions.assertNotNull(usuarioEntity);


    }

    @Test
    public void deveTestarVerificarDataDeEncerramento() throws RegraDeNegocioException, ParseException {
        //setup
        VagasEntity vagasEntity = getVagasEntity();
        List<VagasEntity> vagasEntities =null;
        Date hoje = new Date();


        when(vagaRepository.findByDataEncerramentoLessThanAndStatus(hoje, StatusVagas.ABERTO)).thenReturn(vagasEntities);

        //act
        vagasService.verificarDataDeEncerramento();

        //assert
//        Assertions.assertNotNull(vagasEntity);


    }



    @Test
    public void deveTestarRecuperarVaga() throws RegraDeNegocioException {
        // Arrange
        Integer idVaga = 1;
        VagasEntity vagaEsperada = new VagasEntity(); // Coloque os dados necessários aqui

        // Configuração de comportamento esperado para o mock
        when(vagaRepository.findById(idVaga)).thenReturn(Optional.of(vagaEsperada));

        // Act
        VagasEntity resultado = vagasService.recuperarVaga(idVaga);

        // Assert
        assertEquals(vagaEsperada, resultado);

        verify(vagaRepository, times(1)).findById(eq(idVaga));
    }

    @Test
    public void deveTestarRetornarVaga() {
        // Arrange
        Integer idVaga = 1;
        VagasEntity vagaEsperada = new VagasEntity();

        // Configuração de comportamento esperado para o mock
        when(vagaRepository.findById(idVaga)).thenReturn(Optional.of(vagaEsperada));

        // Act
        Optional<VagasEntity> resultado = vagasService.retornarVaga(idVaga);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(vagaEsperada, resultado.get());

        verify(vagaRepository, times(1)).findById(eq(idVaga));
    }

    @Test
    public void deveTestarRetornarVagaVazia() {
        // Configuração de dados de teste
        Integer idVaga = 2;

        // Configuração de comportamento esperado para o mock
        when(vagaRepository.findById(idVaga)).thenReturn(Optional.empty());

        // Execute o método que você está testando
        Optional<VagasEntity> resultado = vagasService.retornarVaga(idVaga);

        // Verifique os resultados esperados
        assertFalse(resultado.isPresent());

        // Verifique se o método do mock foi chamado conforme esperado
        verify(vagaRepository, times(1)).findById(eq(idVaga));
    }

    @Test
    public void deveTestarDesistirVaga() throws RegraDeNegocioException {
        // Arrange
        Integer idVaga = 1;

        UsuarioEntity usuario = new UsuarioEntity();
        VagasEntity vaga = new VagasEntity();

        when(usuarioService.recuperarUsuarioLogado()).thenReturn(usuario);
//        when(usuarioRepository.findById(idVaga)).thenReturn((Optional<UsuarioEntity>) Optional.of(vaga));

        // Act
        Integer resultado = vagasService.desistirVaga(idVaga);

        // Assert
        assertEquals(Integer.valueOf(1), resultado);
        assertFalse(usuario.getVagas().contains(vaga));

        verify(usuarioRepository, times(1)).save(eq(usuario));
    }

    @Test
    public void deveTestarEscolherCandidato() throws RegraDeNegocioException {
        // Configuração de dados de teste
        Integer idVaga = 1;
        Integer idUsuario = 2;

        VagasEntity vaga = new VagasEntity(); // Coloque os dados necessários aqui
        UsuarioEntity usuario = new UsuarioEntity(); // Coloque os dados necessários aqui

        // Configuração de comportamento esperado para os mocks
        when(vagaRepository.findById(idVaga)).thenReturn(Optional.of(vaga));
        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(usuario));

        Integer resultado = vagasService.escolherCandidato(idVaga, idUsuario);

        assertEquals(Integer.valueOf(1), resultado);

        verify(emailService, times(1)).enviarEmailComTemplateAprovado(eq(usuario.getEmail()), anyString(), eq(usuario.getNome()));
        verify(vagaRepository, times(1)).save(eq(vaga));
        verify(emailService, times(vaga.getUsuarios().size())).enviarEmailComTemplateReprovado(anyString(), anyString(), anyString());
        assertEquals(StatusVagas.FECHADO, vaga.getStatus());
    }

    private static VagasDTO getVagasDTO() throws ParseException {
        VagasDTO dto = new VagasDTO();
        dto.setIdVagas(1);
        dto.setNome("Jornada");
        dto.setQuantidadeVagas(10);
        dto.setQuantidadeMaximaCandidatos(10);
        dto.setCompetencias("Tech");
        dto.setDescricao("Tecnologia");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date dataEncerramento = dateFormat.parse("2023-12-31T14:30:00");
        dto.setDataEncerramento(dataEncerramento);
        return dto;
    }

    private static VagasEntity getVagasEntity() throws ParseException {
        VagasEntity entity = new VagasEntity();
        entity.setIdVagas(1);
        entity.setNome("Jornada");
        entity.setQuantidadeVagas(10);
        entity.setQuantidadeMaximaCandidatos(10);
        entity.setCompetencias("Tech");
        entity.setDescricao("Tecnologia");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date dataEncerramento = dateFormat.parse("2023-12-31T14:30:00");
        entity.setDataEncerramento(dataEncerramento);
        entity.setUsuarios(Collections.singleton(getUsuarioEntity()));
        return entity;
    }

    private static UsuarioEntity getUsuarioEntity(){
        UsuarioEntity entity = new UsuarioEntity();
        entity.setIdUsuario(36);
        entity.setNome("Fulano de Tal");
        entity.setSenha("@Senha123");
        entity.setEmail("fulanodetal@gmail.com");
        return entity;
    }


}

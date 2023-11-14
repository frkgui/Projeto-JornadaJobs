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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.util.ReflectionTestUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class VagasServiceTests {
    @InjectMocks
    private VagasService vagasService;
    @InjectMocks
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
        List<VagasEntity> listaDeEntities = List.of(entity);
        when(vagaRepository.findAll()).thenReturn(listaDeEntities);

        // act
        List<VagasDTO> listagem = vagasService.listarVagas();

        // assert
        assertNotNull(listagem);
        assertEquals(1, listagem.size());
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

    @Test
    public void deveTestarCandidatarVaga() throws RegraDeNegocioException {
        // Arrange
        Integer idVaga = 1; // provide necessary data
        when(vagaRepository.findById(idVaga)).thenReturn(java.util.Optional.of(new VagasEntity()));
        when(usuarioService.recuperarIdUsuarioLogado()).thenReturn(1);

        // Act
        Integer result = vagasService.candidatarVaga(idVaga);

        // Assert
        assertEquals(1, result);
        // Add more assertions as needed
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

//    @Test
//    public void deveTestarDesistirVaga() throws RegraDeNegocioException {
//        // Arrange
//        Integer idVaga = 1;
//
//        UsuarioEntity usuario = new UsuarioEntity();
//        VagasEntity vaga = new VagasEntity();
//
//        when(usuarioService.recuperarUsuarioLogado()).thenReturn(usuario);
//        when(usuarioRepository.findById(idVaga)).thenReturn((Optional<UsuarioEntity>) Optional.of(vaga));
//
//        // Act
//        Integer resultado = vagasService.desistirVaga(idVaga);
//
//        // Assert
//        assertEquals(Integer.valueOf(1), resultado);
//        assertFalse(usuario.getVagas().contains(vaga));
//
//        verify(usuarioRepository, times(1)).save(eq(usuario));
//    }

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
        return entity;
    }


}

package com.jornada.jobapi.service;

import com.jornada.jobapi.dto.VagasDTO;
import com.jornada.jobapi.entity.UsuarioEntity;
import com.jornada.jobapi.entity.VagasEntity;
import com.jornada.jobapi.exception.RegraDeNegocioException;
import com.jornada.jobapi.mapper.VagasMapper;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VagasServiceTests {
    @InjectMocks
    private VagasService vagasService;

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private AuthenticationManager authenticationManager;

    private VagasMapper vagasMapper = Mappers.getMapper(VagasMapper.class);

    @Mock
    private VagaRepository vagaRepository;

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
    public void candidatarVagaTest() throws RegraDeNegocioException {
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

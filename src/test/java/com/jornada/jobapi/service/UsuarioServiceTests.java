package com.jornada.jobapi.service;

import com.jornada.jobapi.dto.AutenticacaoDTO;
import com.jornada.jobapi.dto.UsuarioDTO;
import com.jornada.jobapi.entity.UsuarioEntity;
import com.jornada.jobapi.exception.RegraDeNegocioException;
import com.jornada.jobapi.mapper.UsuarioMapper;
import com.jornada.jobapi.repository.UsuarioRepository;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTests {

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private AuthenticationManager authenticationManager;

    private UsuarioMapper usuarioMapper = Mappers.getMapper(UsuarioMapper.class);

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
        ReflectionTestUtils.setField(usuarioService, "usuarioMapper", usuarioMapper);
        ReflectionTestUtils.setField(usuarioService, "secret", "MinhaChaveSecreta");
        ReflectionTestUtils.setField(usuarioService, "validadeJWT", "86400000");
    }

    @Test
    public void deveTestarFazerLoginComSucesso() throws RegraDeNegocioException {

        // Setup
        AutenticacaoDTO dto = new AutenticacaoDTO();
        dto.setEmail("@gmail.com");
        dto.setSenha("12345");

        Authentication userAuthentication = mock(Authentication.class);
        UsuarioEntity usuarioEntity = mock(UsuarioEntity.class);

        when(authenticationManager.authenticate(any())).thenReturn(userAuthentication);
        when(userAuthentication.getPrincipal()).thenReturn(usuarioEntity);

        // Act
        String autenticacaoDTO = usuarioService.fazerLogin(dto);

        // Assert
        assertNotNull(autenticacaoDTO);
    }

    @Test
    public void deveTestarValidarTokenComSucesso() {
        //setup
        String token = "";

        //act
        UsernamePasswordAuthenticationToken user = usuarioService.validarToken(token);
        //assert

        assertNotNull(token);
    }
//
//    @Test
//    public void testarSalvarOuAtualizarUserComSucesso() throws RegraDeNegocioException {
//
//        // setup
//        UsuarioDTO dto = getUsuarioDTO();
//        UsuarioEntity entity = getUsuarioEntity();
//
//        // comportamentos
//        when(usuarioRepository.save(any())).thenReturn(entity);
//
//        // act
//        UsuarioDTO retorno = usuarioService.salvarUsuario(dto);
//
//        // assert
//        assertNotNull(retorno);
//        assertEquals(1, retorno.getIdUsuario());
//        assertEquals("Fulano de Tal", retorno.getNome());
//        assertEquals("12345", retorno.getSenha());
//        assertEquals("fulanodetal@gmail.com", retorno.getEmail());
//    }

    private static UsuarioDTO getUsuarioDTO(){
        UsuarioDTO dto = new UsuarioDTO();
        dto.setIdUsuario(1);
        dto.setNome("Fulano de Tal");
        dto.setSenha("12345");
        dto.setEmail("fulanodetal@gmail.com");
        return dto;
    }

    private static UsuarioEntity getUsuarioEntity(){
        UsuarioEntity entity = new UsuarioEntity();
        entity.setIdUsuario(1);
        entity.setNome("Fulano de Tal");
        entity.setSenha("12345");
        entity.setEmail("fulanodetal@gmail.com");
        return entity;
    }

}

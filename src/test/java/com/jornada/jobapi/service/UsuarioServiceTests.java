package com.jornada.jobapi.service;

import com.jornada.jobapi.dto.AutenticacaoDTO;
import com.jornada.jobapi.entity.UsuarioEntity;
import com.jornada.jobapi.exception.RegraDeNegocioException;
import com.jornada.jobapi.mapper.UsuarioMapper;
import com.jornada.jobapi.repository.UsuarioRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.util.ReflectionTestUtils;

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
        Assertions.assertNotNull(autenticacaoDTO);
    }

}

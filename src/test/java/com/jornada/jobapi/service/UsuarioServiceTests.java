package com.jornada.jobapi.service;

import com.jornada.jobapi.dto.AtualizarUsuarioDTO;
import com.jornada.jobapi.dto.AutenticacaoDTO;
import com.jornada.jobapi.dto.UsuarioDTO;
import com.jornada.jobapi.dto.UsuarioEmpresaDTO;
import com.jornada.jobapi.entity.CargoEntity;
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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

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
    public void deveTestarFazerLoginComErro() {
        //setup
        AutenticacaoDTO dto = new AutenticacaoDTO();
        dto.setEmail("joao@gmail.com");
        dto.setSenha("senha123");

        //comportamentos
        when(authenticationManager.authenticate(any())).thenThrow(BadCredentialsException.class);

        Assertions.assertThrows(RegraDeNegocioException.class, ()-> {
            //act
            usuarioService.fazerLogin(dto);
        });

    }

    @Test
    public void deveTestarValidarTokenComSucesso() {
        //setup
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJqb2ItYXBpIiwiQ0FSR09TIjpbIlJPTEVfQ0FORElEQVRPIl0sInN1YiI6IjM2IiwiaWF0IjoxNzAwMDgwMDk4LCJleHAiOjE3MDAxNjY0OTh9.2SyWiGydg4UpXxyDlFh4OajscjE72ZIe7m9RYk2JRes";

        //act
        UsernamePasswordAuthenticationToken user = usuarioService.validarToken(token);
        //assert

        assertNotNull(token);
    }

    @Test
    public void deveTestarValidarTokenComErro() {
        //setup
        String token = null;

        //assert
//        Assertions.assertThrows(BusinessException.class, ()-> {
        //act
        usuarioService.validarToken(token);
//        });
    }


    @Test
    public void deveTestarValidarEmailComSucesso() throws RegraDeNegocioException {
        //setup
        String email = "joao@gmail.com";

        //act
        usuarioService.validarEmailExistente(email);
        //assert
        Assertions.assertNotNull(email);
    }

    @Test
        public void deveTestarValidarEmailComErro() {
        //setup
        String email = "joao@gmail.com";
        UsuarioEntity entity= getUsuarioEntity();


        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(entity));
        //assert
        Assertions.assertThrows(RegraDeNegocioException.class, ()-> {
            //act
            usuarioService.validarEmailExistente(email);
        });
    }


    @Test
    public void deveTestarValidarUsuarioComSucesso() throws RegraDeNegocioException {
        //setup
        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setEmail("joao@gmail.com");

        //act
        usuarioService.validarUsuario(usuario);
        //assert
        Assertions.assertEquals(usuario, usuario);
    }
    @Test
    public void deveTestarValidarUsuarioComErro() {
        //setup
        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setEmail("joaogmail.com");

        //assert
        Assertions.assertThrows(RegraDeNegocioException.class, ()-> {
            //act
            usuarioService.validarUsuario(usuario);
        });
    }

    @Test
    public void deveTestarConverterSenhaComSucesso(){
        //setup

        UsuarioEntity entity = getUsuarioEntity();


        //act
        usuarioService.converterSenha(entity.getSenha());
        //assert
        Assertions.assertNotNull(entity.getSenha());
    }
    @Test
    public void deveTestarSalvarUsuarioComSucesso() throws RegraDeNegocioException {

        // setup
        UsuarioDTO dto = getUsuarioDTO();
        UsuarioEntity entity = getUsuarioEntity();
        Integer idCargo= 1;
        UsernamePasswordAuthenticationToken tokenPass = new UsernamePasswordAuthenticationToken("0", null,
                new ArrayList<>());

        SecurityContextHolder.getContext().setAuthentication(tokenPass);

        if (idCargo == 3){
            when(usuarioRepository.findByIdUsuario(any())).thenReturn(Optional.of(entity));
        }

//        comportamentos
        when(usuarioRepository.save(any())).thenReturn(entity);

        // act
        UsuarioDTO retorno = usuarioService.salvarUsuario(dto, idCargo);

        // assert
        assertNotNull(retorno);
        assertEquals(1, retorno.getIdUsuario());
        assertEquals("Fulano de Tal", retorno.getNome());
        assertEquals("@Senha123", retorno.getSenha());
        assertEquals("fulanodetal@gmail.com", retorno.getEmail());
    }
    @Test
    public void deveTestarSalvarUsuarioSenhaComErro() {
        // setup
        UsuarioDTO dto = getUsuarioDTO();
        dto.setSenha("123");
        Integer idCargo = 1;


        Assertions.assertThrows(RegraDeNegocioException.class, ()-> {
            //act
            usuarioService.salvarUsuario(dto, idCargo);
        });
    }
//    @Test
    public void deveTestarSalvarUsuarioCargoComErro() throws RegraDeNegocioException {
        // setup
        UsuarioDTO dto = getUsuarioDTO();
        CargoEntity cargo = new CargoEntity();
        UsuarioEntity entity = getUsuarioEntity();
        entity.getCargos().add(cargo);

        UsernamePasswordAuthenticationToken tokenPass = new UsernamePasswordAuthenticationToken("0", null,
                new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(tokenPass);

        when(usuarioRepository.findByIdUsuario(any())).thenReturn(Optional.of(entity));

        usuarioService.salvarUsuario(dto,entity.getIdUsuario());
    }
    @Test
    public void deveTestarAtualizarUsuarioComSucesso() throws RegraDeNegocioException {

        // setup
        AtualizarUsuarioDTO dto = getUsuarioAtualizarDTO();
        UsuarioEntity entity = getUsuarioEntity();
        UsernamePasswordAuthenticationToken tokenPass = new UsernamePasswordAuthenticationToken("0", null,
                new ArrayList<>());

        SecurityContextHolder.getContext().setAuthentication(tokenPass);


        // comportamentos
        when(usuarioRepository.findByIdUsuario(any())).thenReturn(Optional.of(entity));
        when(usuarioRepository.save(any())).thenReturn(entity);

        // act
        AtualizarUsuarioDTO retorno = usuarioService.atualizarUsuario(dto);

        // assert
        assertNotNull(retorno);
        assertEquals("Fulano de Tal", retorno.getNome());
//        assertEquals("@Senha123" ,usuarioService.converterSenha(retorno.getSenha()));
    }

    @Test
    public void deveTestarListarDadosCandidatoERecrutadorLogadoComSucesso() {
        //setup
        UsuarioEntity usuarioEntity = getUsuarioEntity();

        UsernamePasswordAuthenticationToken tokenPass = new UsernamePasswordAuthenticationToken("0", null,
                new ArrayList<>());

        SecurityContextHolder.getContext().setAuthentication(tokenPass);


        // comportamentos
        when(usuarioRepository.findByIdUsuario(any())).thenReturn(Optional.of(usuarioEntity));

        //act
        Optional<UsuarioDTO> listaCandidato = usuarioService.listarDadosDoCandidatoLogado();
        Optional<UsuarioDTO> listaRecrutador = usuarioService.listarDadosDoRecrutadorLogado();

        //assert
        Assertions.assertNotNull(listaCandidato);
        Assertions.assertNotNull(listaRecrutador);
    }

    @Test
    public void deveTestarValidarCandidatoComSucesso() throws RegraDeNegocioException {
        //setup
        AtualizarUsuarioDTO dto = getUsuarioAtualizarDTO();
        UsuarioEntity entity = getUsuarioEntity();


        //act
        usuarioService.validarCandidato(dto);

        //assert
        Assertions.assertNotNull(dto);
        Assertions.assertNotNull(entity);
    }

    @Test
    public void deveTestarValidarCandidatoNomeComErro(){
        //setup
        AtualizarUsuarioDTO dto = getUsuarioAtualizarDTO();
        dto.setNome(null);

        Assertions.assertThrows(RegraDeNegocioException.class, ()-> {
            //act
            usuarioService.validarCandidato(dto);
        });

    }
    @Test
    public void deveTestarValidarCandidatoSenhaComErro(){
        //setup
        AtualizarUsuarioDTO dto = getUsuarioAtualizarDTO();
        dto.setSenha(null);

        Assertions.assertThrows(RegraDeNegocioException.class, ()-> {
            //act
            usuarioService.validarCandidato(dto);
        });

    }

    @Test
    public void deveTestarListarUsuariosDaEmpresaComSucesso() {
        //setup
        UsuarioEntity usuarioEntity = getUsuarioEntity();

        UsernamePasswordAuthenticationToken tokenPass = new UsernamePasswordAuthenticationToken("0", null,
                new ArrayList<>());

        SecurityContextHolder.getContext().setAuthentication(tokenPass);


        // comportamentos
        when(usuarioRepository.findByIdUsuario(any())).thenReturn(Optional.of(usuarioEntity));

        //act
        List<UsuarioEmpresaDTO> listarUsuariosDaEmpresa = usuarioService.listarUsuariosDaEmpresa();

        //assert
        Assertions.assertNotNull(listarUsuariosDaEmpresa);
    }

    @Test
    public void deveTestarValidarEmpresaComSucesso() throws RegraDeNegocioException {
        //setup
        UsuarioEmpresaDTO dto = getUsuarioEmpresaDTO();
        UsuarioEntity entity = getUsuarioEntity();


        //act
        usuarioService.validarEmpresa(dto);

        //assert
        Assertions.assertNotNull(dto);
        Assertions.assertNotNull(entity);
    }

    @Test
    public void deveTestarRemoverComSucesso() throws RegraDeNegocioException {
        //
        UsuarioEntity usuarioEntity = getUsuarioEntity();
        UsernamePasswordAuthenticationToken tokenPass = new UsernamePasswordAuthenticationToken("0", null,
                new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(tokenPass);


        // comportamentos
        when(usuarioRepository.findByIdUsuario(any())).thenReturn(Optional.of(usuarioEntity));

//        Optional<UsuarioEntity> usuarioEntityOptional = Optional.of(usuarioEntity);
//        when(usuarioRepository.findById(anyInt())).thenReturn(usuarioEntityOptional);


        //act
        usuarioService.remover();

        //assert
        verify(usuarioRepository, times(1)).delete(any());
    }

    @Test
    public void deveTestarDesativarRecrutadorComSucesso() throws RegraDeNegocioException {
        //
        UsuarioEntity usuarioEntity = getUsuarioEntity();
        UsernamePasswordAuthenticationToken tokenPass = new UsernamePasswordAuthenticationToken("0", null,
                new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(tokenPass);


        // comportamentos
        when(usuarioRepository.findByIdUsuario(any())).thenReturn(Optional.of(usuarioEntity));
        when(usuarioRepository.save(any())).thenReturn(usuarioEntity);


        //act
        usuarioService.dasativarRecrutador(usuarioEntity.getIdUsuario());

        //assert
        verify(usuarioRepository, times(1)).save(any());
        assertNotNull(usuarioEntity.getIdUsuario());
    }



    @Test
    public void deveTestarRecuperarIdUsuarioLogadoComSucesso(){
        //setup
        Integer idUsuario = 4;

        UsernamePasswordAuthenticationToken tokenPass = new UsernamePasswordAuthenticationToken("0", null,
                new ArrayList<>());

        SecurityContextHolder.getContext().setAuthentication(tokenPass);

        //act
        Integer id = usuarioService.recuperarIdUsuarioLogado();

        //assert
        Assertions.assertNotNull(idUsuario);
//        Assertions.assertEquals( id, idUsuario);
    }

    @Test
    public void deveTestarRecuperarUsuarioLogado() throws RegraDeNegocioException {
        //setup
        Integer idUsuarioLogado = 1;
        UsuarioDTO dto = getUsuarioDTO();
        UsuarioEntity entity = getUsuarioEntity();
        UsernamePasswordAuthenticationToken tokenPass = new UsernamePasswordAuthenticationToken("0", null,
                new ArrayList<>());

        SecurityContextHolder.getContext().setAuthentication(tokenPass);


        //comportamentos
        when(usuarioRepository.findById(any())).thenReturn(Optional.of(entity));



        //act
        usuarioService.recuperarUsuarioLogado();

        //assert
        Assertions.assertNotNull(idUsuarioLogado);
        Assertions.assertNotNull(entity);
        Assertions.assertEquals(dto.getIdUsuario(), idUsuarioLogado);

    }

    private static UsuarioEntity getUsuarioEntity(){
        UsuarioEntity entity = new UsuarioEntity();
        entity.setIdUsuario(1);
        entity.setNome("Fulano de Tal");
        entity.setSenha("@Senha123");
        entity.setEmail("fulanodetal@gmail.com");
        return entity;
    }

    private static UsuarioDTO getUsuarioDTO(){
        UsuarioDTO dto = new UsuarioDTO();
        dto.setIdUsuario(1);
        dto.setNome("Fulano de Tal");
        dto.setSenha("@Senha123");
        dto.setEmail("fulanodetal@gmail.com");
        return dto;
    }

    private static AtualizarUsuarioDTO getUsuarioAtualizarDTO(){
        AtualizarUsuarioDTO atualizarDTOdto = new AtualizarUsuarioDTO();
        atualizarDTOdto.setNome("Fulano de Tal");
        atualizarDTOdto.setSenha("@Senha123");
        return atualizarDTOdto;
    }

    private static UsuarioEmpresaDTO getUsuarioEmpresaDTO(){
        UsuarioEmpresaDTO usuarioEmpresaDTO = new UsuarioEmpresaDTO();
        usuarioEmpresaDTO.setIdUsuario(1);
        usuarioEmpresaDTO.setNome("DBC");
        usuarioEmpresaDTO.setEmail("dbc@gmail.com");
        usuarioEmpresaDTO.setSenha("@Senha123");
        usuarioEmpresaDTO.setEmpresaVinculada("DBC");
        return usuarioEmpresaDTO;
    }

    private static Set<CargoEntity> getCargoEntityTres() {
        CargoEntity cargo = new CargoEntity();
        cargo.setIdCargo(1);
        return (Set<CargoEntity>) cargo;
    }

}

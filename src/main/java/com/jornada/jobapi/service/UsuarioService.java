package com.jornada.jobapi.service;

import com.jornada.jobapi.dto.AutenticacaoDTO;
import com.jornada.jobapi.dto.UsuarioDTO;
import com.jornada.jobapi.entity.CargoEntity;
import com.jornada.jobapi.entity.UsuarioEntity;
import com.jornada.jobapi.exception.RegraDeNegocioException;
import com.jornada.jobapi.mapper.UsuarioMapper;
import com.jornada.jobapi.repository.UsuarioRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;

@Service
public class UsuarioService {

    private final UsuarioMapper usuarioMapper;
    private final UsuarioRepository usuarioRepository;
    private final AuthenticationManager authenticationManager;


    public UsuarioService(@Lazy UsuarioRepository usuarioRepository,
                       @Lazy AuthenticationManager authenticationManager,
                       @Lazy UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.authenticationManager = authenticationManager;
        this.usuarioMapper = usuarioMapper;
    }

    @Value("${jwt.validade.token}")
    private String validadeJWT;

    @Value("${jwt.secret}")
    private String secret;

    public String fazerLogin(AutenticacaoDTO autenticacaoDTO) throws RegraDeNegocioException {
        UsernamePasswordAuthenticationToken dtoDoSpring = new UsernamePasswordAuthenticationToken(
                autenticacaoDTO.getEmail(),
                autenticacaoDTO.getSenha()
        );
        try {
            Authentication autenticacao = authenticationManager.authenticate(dtoDoSpring);

            Object usuarioAutenticado = autenticacao.getPrincipal();
            UsuarioEntity usuarioEntity = (UsuarioEntity) usuarioAutenticado;

            List<String> nomeDosCargos = usuarioEntity.getCargos().stream()
                    .map(cargo -> cargo.getNome()).toList();
            Date dataAtual = new Date();
            Date dataExpiracao = new Date(dataAtual.getTime() + Long.parseLong(validadeJWT));

            //1 dia

            String jwtGerado =Jwts.builder()
                    .setIssuer("job-api")
                    .claim("CARGOS", nomeDosCargos)
                    .setSubject(usuarioEntity.getIdUsuario().toString())
                    .setIssuedAt(dataAtual)
                    .setExpiration(dataExpiracao)
                    .signWith(SignatureAlgorithm.HS256, secret)
                    .compact();
            return jwtGerado;
        }catch (AuthenticationException ex){
            ex.printStackTrace(); // Isso imprimirá o rastreamento da pilha no console
            throw new RegraDeNegocioException("E-mail e Senha Inválidos");

        }


    }

    public UsernamePasswordAuthenticationToken validarToken(String token){
        if(token == null){
            return null;
        }
        String tokenLimpo = token.replace("Bearer " ,"");
        Claims claims = Jwts.parser()
                .setSigningKey(secret) //utiliza a secret
                .parseClaimsJws(tokenLimpo) //decriptografa e valida o token...
                .getBody(); //recupera o payload

        String idUsuario = claims.getSubject(); // id do usuario
        List<String> cargos = claims.get("CARGOS", List.class);

        List<SimpleGrantedAuthority> listaDeCargos = cargos.stream()
                .map(cargoStr -> new SimpleGrantedAuthority(cargoStr))
                .toList();

        UsernamePasswordAuthenticationToken tokenSpring = new UsernamePasswordAuthenticationToken(idUsuario, null,
                listaDeCargos);

        return tokenSpring;
    }

    public boolean validarEmailExistente(String Email) throws RegraDeNegocioException {
        Optional<UsuarioEntity> emailExistente = usuarioRepository.findByEmail(Email);
        if (emailExistente.isPresent()) {
            throw new RegraDeNegocioException("Email já cadastrado");
        }
        return true;
    }

    public void validarUsuario(UsuarioDTO usuario) throws RegraDeNegocioException {
        if (!usuario.getEmail().contains("@")){
            throw new RegraDeNegocioException("Precisa ter @");
        }
    }

    public String converterSenha(String senha){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String senhaCriptografada = bCryptPasswordEncoder.encode(senha);
        return senhaCriptografada;
    }




    public UsuarioDTO salvarUsuario(UsuarioDTO usuario) throws RegraDeNegocioException{
        String senhaSegura = usuario.getSenha();

//        if (!senhaSegura.matches(".*[A-Z].*") || // Pelo menos uma letra maiúscula
//                !senhaSegura.matches(".*[a-z].*") || // Pelo menos uma letra minúscula
//                !senhaSegura.matches(".*\\d.*") ||   // Pelo menos um número
//                !senhaSegura.matches(".*[!@#$%^&*()].*")) { // Pelo menos um caractere especial
//            throw new RegraDeNegocioException("A senha não atende aos critérios de segurança.");
//        }

        validarUsuario(usuario);
        //converter dto para entity
        UsuarioEntity usuarioEntityConvertido = usuarioMapper.toEntity(usuario);
        // Verificar Existência E-mail
//        validarEmailExistente(usuario.getEmail());
        //Converter Senha
        String senha = usuarioEntityConvertido.getSenha();
        String senhaCriptografada = converterSenha(senha);
        usuarioEntityConvertido.setSenha(senhaCriptografada);
        UsuarioEntity usuarioEntitySalvo = usuarioRepository.save(usuarioEntityConvertido);

        // Inicialize a lista de cargos se for nula
        if (usuarioEntitySalvo.getCargos() == null) {
            usuarioEntitySalvo.setCargos(new HashSet<>());
        }

        // Criar uma instância do CargoEntity com o ID do cargo igual a 3
        CargoEntity cargo = new CargoEntity();
        cargo.setIdCargo(1); // Certifique-se de que a entidade CargoEntity tenha um setter para o ID do cargo

        // Adicionar o cargo à lista de cargos do usuário
        usuarioEntitySalvo.getCargos().add(cargo);

        // Atualizar o usuário para salvar a relação com o cargo
        usuarioEntitySalvo = usuarioRepository.save(usuarioEntitySalvo);

        // Converter Entity para DTO
        UsuarioDTO usuarioRetornado = usuarioMapper.toDTO(usuarioEntitySalvo);
        return usuarioRetornado;
    }

    public UsuarioDTO atualizarUsuario(@RequestBody UsuarioDTO usuarioDTO) throws RegraDeNegocioException {
        String senhaSegura = usuarioDTO.getSenha();

//        if (!senhaSegura.matches(".*[A-Z].*") || // Pelo menos uma letra maiúscula
//                !senhaSegura.matches(".*[a-z].*") || // Pelo menos uma letra minúscula
//                !senhaSegura.matches(".*\\d.*") ||   // Pelo menos um número
//                !senhaSegura.matches(".*[!@#$%^&*()].*")) { // Pelo menos um caractere especial
//            throw new RegraDeNegocioException("A senha não atende aos critérios de segurança.");
//        }

        validarUser(usuarioDTO);
        UsuarioEntity entidade = usuarioMapper.toEntity(usuarioDTO);

        String senha = entidade.getSenha();
        String senhaCriptografada = geradorDeSenha(senha);
        entidade.setSenha(senhaCriptografada);

//        entidade.setEnabled(true);

        UsuarioEntity salvo = usuarioRepository.save(entidade);
        UsuarioDTO dtoSalvo = usuarioMapper.toDTO(salvo);
        return dtoSalvo;    }

    public String geradorDeSenha(String senha) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String senhaCriptografada = bCryptPasswordEncoder.encode(senha);
        return senhaCriptografada;
    }

    public List<UsuarioDTO> listar(){
        List<UsuarioEntity> listaEntity = usuarioRepository.findAll();
        List<UsuarioDTO> listaDTO = listaEntity.stream().map(entity -> usuarioMapper.toDTO(entity))
                .toList();
        return listaDTO;
    }

    public void validarUser(UsuarioDTO usuario) throws RegraDeNegocioException {
        if (usuario.getEmail().contains("@gmail")
                || usuario.getEmail().contains("@hotmail")
                || usuario.getEmail().contains("@outlook")) {
        } else {
            throw new RegraDeNegocioException("Precisa ser @gmail, @hotmail ou @outlook");
        }
    }

    public boolean validarIdUser(Integer id) throws RegraDeNegocioException {
        if (usuarioRepository.findById(id).isEmpty()) {
            throw new RegraDeNegocioException("ID inválido, usuário não existe!");
        }
        return true;
    }

    public void remover(Integer id) throws RegraDeNegocioException {
        UsuarioEntity usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Usuário não encontrado"));

        usuarioRepository.delete(usuario);
    }

    public Optional<UsuarioEntity> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public void desativarUsuario(Integer idInformado) throws RegraDeNegocioException {
        UsuarioEntity entity = usuarioRepository.findById(idInformado)
                .orElseThrow(() -> new RegraDeNegocioException("Usuario não encontrado"));
//        entity.setEnabled(false);
        usuarioRepository.save(entity);
    }

    public Integer recuperarIdUsuarioLogado(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object idUsuario = authentication.getPrincipal();
        String idUsuarioString = (String) idUsuario;
        return Integer.parseInt(idUsuarioString);
    }


    public String recuperarNomeUsuarioLogado(){
        Integer idUsuarioLogado = recuperarIdUsuarioLogado();
        Optional<UsuarioEntity> usuario =usuarioRepository.findByIdUsuario(idUsuarioLogado);
        return usuario.get().getNome();
    }

    public UsuarioDTO recuperarUsuarioLogado() throws RegraDeNegocioException {
        Integer idUsuarioLogado =recuperarIdUsuarioLogado();
        UsuarioEntity idUsuarioEntity = usuarioRepository.findById(idUsuarioLogado).orElseThrow(() -> new RegraDeNegocioException("Usuario não encontrado!"));
        UsuarioDTO idUsuarioDTOLogado = usuarioMapper.toDTO(idUsuarioEntity);
        return idUsuarioDTOLogado;
    }

}


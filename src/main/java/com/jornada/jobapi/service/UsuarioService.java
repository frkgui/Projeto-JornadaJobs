package com.jornada.jobapi.service;

import com.jornada.jobapi.dto.*;
import com.jornada.jobapi.entity.CargoEntity;
import com.jornada.jobapi.entity.UsuarioEntity;
import com.jornada.jobapi.exception.RegraDeNegocioException;
import com.jornada.jobapi.mapper.CandidatoMapper;
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
import java.util.stream.Collectors;

@Service
public class UsuarioService {

//    -- Variáveis de instância --
    private final UsuarioMapper usuarioMapper;
    private final UsuarioRepository usuarioRepository;
    private final AuthenticationManager authenticationManager;


//    -- Funções gerais --
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
        if (usuario.getEmail().contains("@gmail")
                || usuario.getEmail().contains("@hotmail")
                || usuario.getEmail().contains("@outlook")) {
        } else {
            throw new RegraDeNegocioException("Precisa ser @gmail, @hotmail ou @outlook");
        }
    }

    public String converterSenha(String senha){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String senhaCriptografada = bCryptPasswordEncoder.encode(senha);
        return senhaCriptografada;
    }


//    -- Funções de CRUD (GERAL) --

    //USADO PARA CADASTRO DE CANDIDATO SEM AUTENTICAR
    public UsuarioDTO salvarUsuario(UsuarioDTO usuario,Integer idCargo) throws RegraDeNegocioException{
        String senhaSegura = usuario.getSenha();

        if (!senhaSegura.matches(".*[A-Z].*") || // Pelo menos uma letra maiúscula
                !senhaSegura.matches(".*[a-z].*") || // Pelo menos uma letra minúscula
                !senhaSegura.matches(".*\\d.*") ||   // Pelo menos um número
                !senhaSegura.matches(".*[!@#$%^&*()].*")) { // Pelo menos um caractere especial
            throw new RegraDeNegocioException("A senha não atende aos critérios de segurança.");
        }

        validarUsuario(usuario);
        //converter dto para entity
        UsuarioEntity usuarioEntityConvertido = usuarioMapper.toEntity(usuario);

        // Verificar Existência E-mail
        validarEmailExistente(usuario.getEmail());

        //Converter Senha
        String senha = usuarioEntityConvertido.getSenha();
        String senhaCriptografada = converterSenha(senha);
        usuarioEntityConvertido.setSenha(senhaCriptografada);
        UsuarioEntity usuarioEntitySalvo = usuarioRepository.save(usuarioEntityConvertido);

        if(idCargo == 3){
            //Intanciando para saber qual o id logado
            Integer idUsuarioLogado = recuperarIdUsuarioLogado();
            Optional<UsuarioEntity> entity = usuarioRepository.findByIdUsuario(idUsuarioLogado);
            if (entity.get().cargos != null) {
                usuarioEntitySalvo.setEmpresaVinculada(entity.get().getEmpresaVinculada()); //colocando o nome da empresa na variavel empresa vinculada
            }
        }
        if(idCargo == 2){
            usuarioEntitySalvo.setEmpresaVinculada(usuarioEntitySalvo.getIdUsuario());
        }

        // Inicialize a lista de cargos se for nula
        if (usuarioEntitySalvo.getCargos() == null) {
            usuarioEntitySalvo.setCargos(new HashSet<>());
        }

        // Criar uma instância do CargoEntity com o ID do cargo igual a 3
        CargoEntity cargo = new CargoEntity();
        cargo.setIdCargo(idCargo);
        // Certifique-se de que a entidade CargoEntity tenha um setter para o ID do cargo

        // Adicionar o cargo à lista de cargos do usuário
        usuarioEntitySalvo.getCargos().add(cargo);

        // Atualizar o usuário para salvar a relação com o cargo
        usuarioEntitySalvo = usuarioRepository.save(usuarioEntitySalvo);

        // Converter Entity para DTO
        UsuarioDTO usuarioRetornado = usuarioMapper.toDTO(usuarioEntitySalvo);
        return usuarioRetornado;
    }
//    -- CRUD DE CANDIDATO E RECRUTADOR --

    public AtualizarUsuarioDTO atualizarUsuario(@RequestBody AtualizarUsuarioDTO usuario) throws RegraDeNegocioException{
        validarCandidato(usuario);

        //Intanciando para saber qual o id logado
        Integer idUsuarioLogado = recuperarIdUsuarioLogado();
        Optional<UsuarioEntity> entity = usuarioRepository.findByIdUsuario(idUsuarioLogado);
        UsuarioEntity usuarioEntity = new UsuarioEntity();

        if (entity.isPresent()) {
            usuarioEntity = entity.get();
            usuarioEntity.setNome(usuario.getNome());

            String senha = usuario.getSenha();
            String senhaCriptografada = converterSenha(senha);
            usuarioEntity.setSenha(senhaCriptografada);

            usuarioRepository.save(usuarioEntity);
        }
        return  usuarioMapper.atualizarUsuarioToDTO(usuarioEntity);
    }


    public Optional<UsuarioDTO> listarDadosDoCandidatoLogado() {
        Integer idUsuarioLogado = recuperarIdUsuarioLogado();
        Optional<UsuarioEntity> listaEntities = usuarioRepository.findByIdUsuario(idUsuarioLogado);
        Optional<UsuarioDTO> listaDTO = listaEntities.map(entity
                -> usuarioMapper.toDTO(entity));

        return listaDTO;
    }


    public Optional<UsuarioDTO> listarDadosDoRecrutadorLogado() {
        Integer idUsuarioLogado = recuperarIdUsuarioLogado();
        Optional<UsuarioEntity> listaEntities = usuarioRepository.findByIdUsuario(idUsuarioLogado);
        Optional<UsuarioDTO> listaDTO = listaEntities.map(entity
                -> usuarioMapper.toDTO(entity));
        return listaDTO;
    }

    public void validarCandidato(AtualizarUsuarioDTO usuarioCandidatoDTO) throws RegraDeNegocioException {
        if (usuarioCandidatoDTO.getNome() == null || usuarioCandidatoDTO.getNome().isEmpty()) {
            throw new RegraDeNegocioException("O nome é obrigatório.");
        }
        if (usuarioCandidatoDTO.getSenha() == null || usuarioCandidatoDTO.getSenha().isEmpty()) {
            throw new RegraDeNegocioException("A senha é obrigatória.");
        }

    }

    //APENAS EMPRESA
    public List<UsuarioEmpresaDTO> listarUsuariosDaEmpresa()  {
        //recupera o id que foi logado
        Integer idUsuarioLogado = recuperarIdUsuarioLogado();
        Optional<UsuarioEntity> entidadeLogada = usuarioRepository.findByIdUsuario(idUsuarioLogado);

//      instancia o nome da empresa na variavel
        Integer empresaVinculada = entidadeLogada.get().getEmpresaVinculada();

//      procura no banco os usuario que tem a mesma empresa empresa vinculada
        List<UsuarioEntity> entidade = usuarioRepository.findByEmpresaVinculada(empresaVinculada);
//      transformando em dto
        List<UsuarioEmpresaDTO> empresaDTO = entidade.stream().map(entity -> usuarioMapper.empresaToDTO(entity)).collect(Collectors.toList());
        return empresaDTO;
    }


    public void validarEmpresa(UsuarioEmpresaDTO empresaDTO) throws RegraDeNegocioException {
        if (empresaDTO.getNome() == null || empresaDTO.getNome().isEmpty()) {
            throw new RegraDeNegocioException("O nome da empresa é obrigatório.");
        }
        if (empresaDTO.getEmail() == null || empresaDTO.getEmail().isEmpty()) {
            throw new RegraDeNegocioException("O e-mail da empresa é obrigatório.");
        }

        Optional<UsuarioEntity> empresaExistente = findByEmail(empresaDTO.getEmail());
        if (empresaExistente.isPresent()) {
            throw new RegraDeNegocioException("Já existe uma empresa com o mesmo e-mail.");
        }
    }
    public void remover() throws RegraDeNegocioException {
        Integer idUsuarioLogado = recuperarIdUsuarioLogado();
        UsuarioEntity usuario = usuarioRepository.findByIdUsuario(idUsuarioLogado)
                .orElseThrow(() -> new RegraDeNegocioException("Usuário não encontrado"));
        
        usuarioRepository.delete(usuario);
    }

    public Optional<UsuarioEntity> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    //APENAS EMPRESA
    public void dasativarRecrutador(Integer idUsuario) throws RegraDeNegocioException {
        UsuarioEntity entity = usuarioRepository.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new RegraDeNegocioException("Usuario não encontrado"));
        entity.setEmpresaVinculada(null);
        usuarioRepository.save(entity);
    }

    public Integer recuperarIdUsuarioLogado(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() != null) {
            Object idUsuario = authentication.getPrincipal();
            String idUsuarioString = (String) idUsuario;
            return Integer.parseInt(idUsuarioString);
        }
        return null;
    }

    public UsuarioEntity recuperarUsuarioLogado() throws RegraDeNegocioException {
        Integer idUsuarioLogado = recuperarIdUsuarioLogado();
        if (idUsuarioLogado != null) {
            UsuarioEntity idUsuarioEntity = usuarioRepository.findById(idUsuarioLogado).orElseThrow(() -> new RegraDeNegocioException("Usuario não encontrado!"));
            UsuarioDTO idUsuarioDTOLogado = usuarioMapper.toDTO(idUsuarioEntity);
            return idUsuarioEntity;
        } else {
            throw new RegraDeNegocioException("Não foi possível recuperar o ID do usuário logado");
        }
    }

}


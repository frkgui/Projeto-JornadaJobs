package com.jornada.jobapi.security;

import com.jornada.jobapi.entity.UsuarioEntity;
import com.jornada.jobapi.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements UserDetailsService {
    private final UsuarioService usuarioService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UsuarioEntity> usuarioEntityOptional = usuarioService.findByEmail(username);
        if (usuarioEntityOptional.isPresent()){
            return usuarioEntityOptional.get();
        }
        throw new UsernameNotFoundException("Usuario não encontrado");
    }


}

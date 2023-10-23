package com.jornada.jobapi.security;
import com.jornada.jobapi.service.UsuarioService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class TokenAuthenticatonFilter extends OncePerRequestFilter {
//
    public final UsuarioService usuarioService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String tokenBearer = request.getHeader("Authorization");

//        // Validação de TOKEN
            UsernamePasswordAuthenticationToken tokenSpring = usuarioService.validarToken(tokenBearer);
            SecurityContextHolder.getContext().setAuthentication(tokenSpring);

        filterChain.doFilter(request,response);
    }
}

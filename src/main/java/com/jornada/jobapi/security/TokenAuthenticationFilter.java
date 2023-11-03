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
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final UsuarioService usuarioService;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {


        String tokenBearer = request.getHeader("Authorization");

        //validar o token
        UsernamePasswordAuthenticationToken tokenSpring = usuarioService.validarToken(tokenBearer);
        //set na autenticao dentro do spring
        SecurityContextHolder.getContext().setAuthentication(tokenSpring);

        //executar o proximo filtro
        filterChain.doFilter(request, response);
    }
}

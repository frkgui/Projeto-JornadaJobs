package com.jornada.jobapi.security;

import com.jornada.jobapi.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    public final UsuarioService usuarioService;

    @Bean // Filtro de requisições
    public SecurityFilterChain filterChain(HttpSecurity  http)throws Exception{

        // Configuração de permissões/filtros
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(Customizer.withDefaults());
        // Permissão de acesso ao "/autenticação"
        http.authorizeHttpRequests((authz)->
                        authz.requestMatchers("/autenticacao/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/usuario/**").hasRole("EMPRESA")
                                .requestMatchers(HttpMethod.PUT, "/usuario/**").hasRole("EMPRESA")
                                .requestMatchers(HttpMethod.DELETE, "/usuario/**").hasRole("EMPRESA")
                                .requestMatchers("/usuario/**").hasAnyRole("CANDIDATO","EMPRESA","RECRUTADOR")
                                .anyRequest().authenticated());
        // Filtro de autenticação ao Token
        http.addFilterBefore(new TokenAuthenticatonFilter(usuarioService), UsernamePasswordAuthenticationFilter.class);

       return http.build(); // Retorna a http (requisição)
    }

    // Liberação de acesso do Swagger
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web) -> web.ignoring().requestMatchers(
                "/v3/api-docs",
                "v3/api-docs/**",
                "/swagger-resources/**",
                "/swagger-ui/**",
                "/");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

}

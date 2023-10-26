package com.jornada.jobapi.entity;

import com.jornada.jobapi.dto.TipoUsuario;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

@Entity(name = "Usuario")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioEntity implements UserDetails {

    @Id
    @Column(name = "id_usuario")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gerador_usuario")
    @SequenceGenerator(name = "gerador_usuario", sequenceName = "seq_usuario", allocationSize = 1)
    private Integer idUsuario;
    @Column(name = "nome_completo")
    private String nome;
    @Column(name = "email")
    private String email;
    @Column(name = "id_cargo")
    private Integer idCargo;
    @Column(name = "senha")
    private String senha;


    @ManyToMany
    @JoinTable(name = "Usuario_Vagas",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_vagas"))
    public Set<VagasEntity> vagas;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}


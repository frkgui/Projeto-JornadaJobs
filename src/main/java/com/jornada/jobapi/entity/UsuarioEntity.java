package com.jornada.jobapi.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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


    @Column(name = "senha")
    private String senha;

    @Column(name = "empresa_vinculada")
    private String empresaVinculada;


    @ManyToMany
    @JoinTable(name = "Usuario_Vagas",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_vagas"))
    public Set<VagasEntity> vagas;

    @ManyToMany
    @JoinTable(name = "Usuario_Cargo",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_cargo"))
    public Set<CargoEntity> cargos;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return cargos;
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


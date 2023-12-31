package com.jornada.jobapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jornada.jobapi.service.UsuarioService;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

@Getter
@Setter
@Entity(name = "Cargo")
public class CargoEntity implements GrantedAuthority {
    @Id
    @Column(name = "id_cargo")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CARGO_SEQUENCIA")
    @SequenceGenerator(name = "CARGO_SEQUENCIA", sequenceName = "seq_cargo", allocationSize = 1)
    private Integer idCargo;
    @Column(name = "nome")
    private String nome;


    @Override
    public String getAuthority() {
        return nome;
    }
}

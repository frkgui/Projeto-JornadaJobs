package com.jornada.jobapi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

@Setter
@Getter
@Entity(name="Cargo")
public class CargosEntity implements GrantedAuthority {

    @Id
    private Integer idCargo;

    private String nome;

    @Override
    public String getAuthority() {
        return null;
    }
}

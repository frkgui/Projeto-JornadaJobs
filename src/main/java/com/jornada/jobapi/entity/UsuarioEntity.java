package com.jornada.jobapi.entity;

import com.jornada.jobapi.dto.TipoUsuario;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity(name = "usuario")
@Data
public class UsuarioEntity {

    private Integer id;
    private String nome;
    private String email;
    private TipoUsuario tipoUsuario;
    private String senha;
}

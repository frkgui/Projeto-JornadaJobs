package com.jornada.jobapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "USUARIO_CARGO")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioCargoEntity {
    @Id
    @Column(name = "ID_USUARIO")
    private Integer idUsuario;
    @Column(name = "ID_CARGO")
    private String idCargo;
}

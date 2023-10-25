package com.jornada.jobapi.entity;

import com.jornada.jobapi.dto.TipoUsuario;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity(name = "Usuario")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioEntity {

    @Id
    @Column(name = "id_usuario")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gerador_usuario")
    @SequenceGenerator(name = "gerador_usuario", sequenceName = "seq_usuario", allocationSize = 1)
    private Integer idUsuario;
    private String nome;
    private String email;
    private TipoUsuario tipoUsuario;
    private String senha;
    private Boolean enabled;


    @ManyToMany
    @JoinTable(name = "Usuario_Cargo",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_cargo"))
    public Set<CargoEntity> cargos;
}

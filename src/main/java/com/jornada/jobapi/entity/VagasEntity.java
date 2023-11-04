package com.jornada.jobapi.entity;

import com.jornada.jobapi.dto.StatusVagas;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity(name = "Vagas")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VagasEntity {

    @Id
    @Column(name = "id_vagas")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gerador_vagas")
    @SequenceGenerator(name = "gerador_vagas", sequenceName = "seq_vagas", allocationSize = 1)
    private Integer idVagas;
    @Column(name = "nome")
    private String nome;
    @Column(name = "descricao")
    private String descricao;
    @Column(name = "competencias")
    private List<String> competencias;
    @Column(name = "data_criacao")
    private Date dataCriacao;
    @Column(name = "data_encerramento")
    private Date dataEncerramento;
    @Column(name = "quantidade_vagas")
    private Integer quantidadeVagas;
    @Column(name = "quantidade_maxima_candidatos")
    private Integer quantidadeMaximaCandidatos;
    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private StatusVagas status;

    @ManyToMany
    @JoinTable(name = "Usuario_Vagas",
            joinColumns = @JoinColumn(name = "id_vagas"),
            inverseJoinColumns = @JoinColumn(name = "id_usuario"))
    public Set<UsuarioEntity> usuarios;
}

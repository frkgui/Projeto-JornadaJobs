package com.jornada.jobapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.util.Date;

@Data
public class VagasDTO {
    @Schema(description = "Qualquer ID genérico", example = "1")
    private Integer idVagas;
    @Schema(description = "Nome da vaga", example = "Desenvolvedor Web Full Stack")
    private String nome;
    @Schema(description = "Descricao da vaga", example = "A Empresa XYZ está procurando um Desenvolvedor Web Full Stack altamente motivado e experiente para se juntar à nossa equipe de desenvolvimento. O candidato ideal terá habilidades sólidas em desenvolvimento web, tanto no front-end quanto no back-end, e será capaz de trabalhar em projetos desafiadores e inovadores.")
    private String descricao;
    @Schema(description = "Competências da vaga", example = "Trabalho em equipe")
    private String competencias;
    @Schema(description = "Data de encerramento das vagas", example = "2023-12-31 14:30:00")
    private Date dataEncerramento;
    @Schema(description = "Quantidade de vagas", example = "20")
    private Integer quantidadeVagas;
    @Schema(description = "Quantidade máxima de candidatos ", example = "10")
    private Integer quantidadeMaximaCandidatos;
}

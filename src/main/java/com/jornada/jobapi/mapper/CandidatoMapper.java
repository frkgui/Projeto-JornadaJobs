package com.jornada.jobapi.mapper;

import com.jornada.jobapi.dto.UsuarioCandidatoRecrutadorDTO;
import com.jornada.jobapi.entity.UsuarioEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CandidatoMapper {

    UsuarioEntity toEntity(UsuarioCandidatoRecrutadorDTO dto);

    UsuarioCandidatoRecrutadorDTO toDTO(UsuarioEntity entity);
}

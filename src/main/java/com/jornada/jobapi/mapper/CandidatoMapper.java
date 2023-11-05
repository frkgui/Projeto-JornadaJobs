package com.jornada.jobapi.mapper;

import com.jornada.jobapi.dto.UsuarioCandidatoDTO;
import com.jornada.jobapi.entity.UsuarioEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CandidatoMapper {

    UsuarioEntity toEntity(UsuarioCandidatoDTO dto);

    UsuarioCandidatoDTO toDTO(UsuarioEntity entity);
}

package com.jornada.jobapi.mapper;

import com.jornada.jobapi.dto.CandidatoDTO;
import com.jornada.jobapi.entity.UsuarioEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CandidatoMapper {

    UsuarioEntity toEntity(CandidatoDTO dto);

    CandidatoDTO toDTO(UsuarioEntity entity);
}

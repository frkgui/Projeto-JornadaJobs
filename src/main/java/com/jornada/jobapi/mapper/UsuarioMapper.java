package com.jornada.jobapi.mapper;

import com.jornada.jobapi.dto.UsuarioDTO;
import com.jornada.jobapi.entity.UsuarioEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    UsuarioEntity paraEntity(UsuarioDTO dto);

    UsuarioDTO paraDTO(UsuarioEntity entity);
}

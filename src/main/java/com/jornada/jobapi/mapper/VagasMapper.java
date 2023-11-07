package com.jornada.jobapi.mapper;

import com.jornada.jobapi.dto.UsuarioDTO;
import com.jornada.jobapi.dto.VagasDTO;
import com.jornada.jobapi.entity.UsuarioEntity;
import com.jornada.jobapi.entity.VagasEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VagasMapper {
    VagasEntity toEntity(VagasDTO dto);

    VagasDTO toDTO(VagasEntity entity);
}

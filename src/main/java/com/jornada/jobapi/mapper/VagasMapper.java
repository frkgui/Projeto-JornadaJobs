package com.jornada.jobapi.mapper;

import com.jornada.jobapi.dto.RetornoVagasDTO;
import com.jornada.jobapi.dto.VagasDTO;
import com.jornada.jobapi.entity.VagasEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VagasMapper {
    VagasEntity toEntity(VagasDTO dto);

    VagasDTO toDTO(VagasEntity entity);

    VagasEntity toREntity(RetornoVagasDTO Rdto);
    RetornoVagasDTO toRDTO(VagasEntity entity);
}

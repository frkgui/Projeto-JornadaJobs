package com.jornada.jobapi.mapper;

import com.jornada.jobapi.dto.UsuarioCandidatoRecrutadorDTO;
import com.jornada.jobapi.dto.UsuarioDTO;
import com.jornada.jobapi.entity.UsuarioEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    UsuarioEntity toEntity(UsuarioDTO dto);

    UsuarioDTO toDTO(UsuarioEntity entity);


    UsuarioEntity candidatoToEntity(UsuarioCandidatoRecrutadorDTO dto);

    UsuarioCandidatoRecrutadorDTO candidatoToDTO(UsuarioEntity entity);
}

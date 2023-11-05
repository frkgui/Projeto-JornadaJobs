package com.jornada.jobapi.mapper;

import com.jornada.jobapi.dto.UsuarioCandidatoRecrutadorDTO;
import com.jornada.jobapi.dto.UsuarioDTO;
import com.jornada.jobapi.dto.UsuarioEmpresaDTO;
import com.jornada.jobapi.entity.UsuarioEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    UsuarioEntity toEntity(UsuarioDTO dto);

    UsuarioDTO toDTO(UsuarioEntity entity);


    UsuarioEntity candidatoERecrutadorToEntity(UsuarioCandidatoRecrutadorDTO dto);

    UsuarioCandidatoRecrutadorDTO candidatoERecrutadorToDTO(UsuarioEntity entity);

    UsuarioEntity empresaToEntity(UsuarioEmpresaDTO dto);
    UsuarioEmpresaDTO empresaToDTO(UsuarioEntity entity);
}

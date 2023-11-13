package com.jornada.jobapi.mapper;

import com.jornada.jobapi.dto.*;
import com.jornada.jobapi.entity.UsuarioEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    UsuarioEntity toEntity(UsuarioDTO dto);

    UsuarioDTO toDTO(UsuarioEntity entity);


    UsuarioEntity atualizarUsuarioToEntity(UsuarioAtualizarDTO dto);

    UsuarioAtualizarDTO atualizarUsuarioToDTO(UsuarioEntity entity);

    UsuarioEntity empresaToEntity(UsuarioEmpresaDTO dto);
    UsuarioEmpresaDTO empresaToDTO(UsuarioEntity entity);

    UsuarioEntity salvarEmpresaToEntity(SalvarUsuarioEmpresaDTO dto);
    SalvarUsuarioEmpresaDTO salvarEmpresaToDTO(UsuarioEntity entity);
}

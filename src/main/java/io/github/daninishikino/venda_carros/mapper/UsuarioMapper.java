package io.github.daninishikino.venda_carros.mapper;

import io.github.daninishikino.venda_carros.controller.DTO.UsuarioDTO;
import io.github.daninishikino.venda_carros.model.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    Usuario toEntity(UsuarioDTO dto);

    UsuarioDTO toDTO(Usuario usuario);
}

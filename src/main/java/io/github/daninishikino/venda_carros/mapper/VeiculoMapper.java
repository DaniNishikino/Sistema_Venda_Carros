package io.github.daninishikino.venda_carros.mapper;

import io.github.daninishikino.venda_carros.controller.DTO.VeiculoDTO;
import io.github.daninishikino.venda_carros.model.Veiculo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VeiculoMapper {

    Veiculo toEntity(VeiculoDTO dto);
    VeiculoDTO toDTO(Veiculo veiculo);
}

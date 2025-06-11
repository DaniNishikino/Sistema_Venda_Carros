package io.github.daninishikino.venda_carros.controller.DTO.response;

import io.github.daninishikino.venda_carros.controller.DTO.request.VeiculoDTO;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record VendaResponseDTO(
        UUID id,
        VeiculoDTO veiculoDTO,
        UsuarioResponseDTO usuarioResponseDTO,
        OffsetDateTime dataVenda,
        BigDecimal valorVenda
) {
}

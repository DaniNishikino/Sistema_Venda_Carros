package io.github.daninishikino.venda_carros.controller.DTO.response;

import io.github.daninishikino.venda_carros.model.enums.Papel;

import java.util.UUID;

public record UsuarioResponseDTO(
        UUID id,
        String nome,
        String login,
        Papel papel
) {
}

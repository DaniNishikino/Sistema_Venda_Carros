package io.github.daninishikino.venda_carros.controller.DTO;

import io.github.daninishikino.venda_carros.model.enums.Papel;

import java.util.UUID;

public record UsuarioDTO(
        UUID id,
        String nome,
        String login,
        String senha,
        Papel papel
) {}

package io.github.daninishikino.venda_carros.controller.DTO;

import io.github.daninishikino.venda_carros.model.enums.Papel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UsuarioDTO(

        UUID id,
        @NotBlank(message = "O nome é obrigatório")
        String nome,

        @NotBlank(message = "O Login é obrigatório")
        String login,

        @NotBlank(message = "A senha é obrigatória")
        String senha,

        @NotNull(message = "O papel é obrigatorio")
        Papel papel
) {}

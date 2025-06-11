package io.github.daninishikino.venda_carros.controller.DTO.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record VeiculoDTO(
        UUID id,
        @NotBlank(message = "A marca é obrigatória")
        String marca,

        @NotBlank(message = "O modelo é obrigatório")
        String modelo,

        @NotNull(message = "O ano de fabricação é obrigatório")
        Integer anoFabricacao,

        @NotNull(message = "O ano do modelo é obrigatorio")
        Integer anoModelo,

        @NotBlank(message = "A cor do carro é obrigatoria")
        String cor,

        @NotBlank(message = "A placa é obrigatória")
        String placa,

        @NotNull(message = "O preço do carro é obrigatorio")
        BigDecimal preco,

        @NotBlank(message = "A descrição é obrigatória")
        String descricao,

        @NotNull(message = "A disponibilidade é obrigatória")
        Boolean disponivel,
        OffsetDateTime dataCadastro
) {}

package io.github.daninishikino.venda_carros.controller.DTO;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record VeiculoDTO(
        UUID id,
        @NotBlank(message = "A marca é obrigatória")
        String marca,

        @NotBlank(message = "O modelo é obrigatório")
        String modelo,

        @NotBlank(message = "O ano de fabricação é obrigatório")
        Integer anoFabricacao,

        @NotBlank(message = "O ano do modelo é obrigatorio")
        Integer anoModelo,

        @NotBlank(message = "A cor do carro é obrigatoria")
        String cor,

        @NotBlank(message = "A placa é obrigatória")
        String placa,

        @NotBlank(message = "O preço do carro é obrigatorio")
        BigDecimal preco,

        @NotBlank(message = "A descrição é obrigatória")
        String descricao,

        @NotBlank(message = "A disponibilidade é obrigatória")
        Boolean disponivel,
        OffsetDateTime dataCadastro
) {}

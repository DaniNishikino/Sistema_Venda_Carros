package io.github.daninishikino.venda_carros.controller.DTO;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record VeiculoDTO(
        UUID id,
        String marca,
        String modelo,
        Integer anoFabricacao,
        Integer anoModelo,
        String cor,
        String placa,
        BigDecimal preco,
        String descricao,
        Boolean disponivel,
        OffsetDateTime dataCadastro
) {}

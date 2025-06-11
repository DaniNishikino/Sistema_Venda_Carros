package io.github.daninishikino.venda_carros.controller.DTO.request;

import jakarta.validation.constraints.NotBlank;

public record VendaRequestDTO(
        @NotBlank(message = "A Placa do veiculo não pode estar vazia.")
        String placaVeiculo
) {
}

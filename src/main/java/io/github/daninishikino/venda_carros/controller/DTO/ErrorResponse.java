package io.github.daninishikino.venda_carros.controller.DTO;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(
        LocalDateTime timestamp,
        Integer status,
        List<String > errors,
        String message,
        String path
) {}

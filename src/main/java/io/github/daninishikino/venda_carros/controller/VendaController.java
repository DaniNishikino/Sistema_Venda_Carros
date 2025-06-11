package io.github.daninishikino.venda_carros.controller;

import io.github.daninishikino.venda_carros.controller.DTO.request.VendaRequestDTO;
import io.github.daninishikino.venda_carros.controller.DTO.response.VendaResponseDTO;
import io.github.daninishikino.venda_carros.service.VendaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vendas")
@RequiredArgsConstructor
public class VendaController {
    private final VendaService vendaService;

    @PostMapping
    @PreAuthorize("hasAnyRole('VENDEDOR', 'GERENTE')")
    public ResponseEntity<VendaResponseDTO> realizarVenda(@RequestBody @Valid VendaRequestDTO dto){
        VendaResponseDTO vendaResponseDTO = vendaService.realizarVenda(dto);
        return ResponseEntity.ok(vendaResponseDTO);
    }
}

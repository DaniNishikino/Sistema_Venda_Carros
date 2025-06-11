package io.github.daninishikino.venda_carros.controller;


import io.github.daninishikino.venda_carros.controller.DTO.VeiculoDTO;
import io.github.daninishikino.venda_carros.mapper.VeiculoMapper;
import io.github.daninishikino.venda_carros.service.VeiculoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/veiculos")
@RequiredArgsConstructor
public class VeiculoController {

    private final VeiculoService service;

    @GetMapping
    public ResponseEntity<List<VeiculoDTO>> buscarTodos(){
        List<VeiculoDTO> veiculos = service.buscarTodos();
        return ResponseEntity.ok(veiculos);
    }

    @GetMapping("/{placa}")
    @PreAuthorize("hasAnyRole('GERENTE', 'VENDEDOR')")
    public ResponseEntity<VeiculoDTO> buscarPorPlaca(@PathVariable String placa){
        VeiculoDTO response = service.buscarPelaPlaca(placa);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasRole('GERENTE')")
    public ResponseEntity<Void> salvarVeiculo(@RequestBody @Valid VeiculoDTO veiculoDTO){
        service.salvar(veiculoDTO);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{placa}")
    @PreAuthorize("hasRole('GERENTE')")
    public ResponseEntity<VeiculoDTO> atualizarVeiculo(@PathVariable String placa, @RequestBody @Valid VeiculoDTO veiculoDTO){
        return ResponseEntity.ok(service.atualizar(placa, veiculoDTO));
    }

    @DeleteMapping("/{placa}")
    @PreAuthorize("hasRole('GERENTE')")
    public ResponseEntity<Void> deletarVeiculo(@PathVariable String placa){
        service.deletar(placa);
        return ResponseEntity.noContent().build();
    }



}

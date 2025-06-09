package io.github.daninishikino.venda_carros.controller;


import io.github.daninishikino.venda_carros.controller.DTO.VeiculoDTO;
import io.github.daninishikino.venda_carros.mapper.VeiculoMapper;
import io.github.daninishikino.venda_carros.model.Veiculo;
import io.github.daninishikino.venda_carros.service.VeiculoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/veiculos")
@RequiredArgsConstructor
public class VeiculoController {

    private final VeiculoService service;
    private final VeiculoMapper mapper;

    @GetMapping
    public ResponseEntity<List<VeiculoDTO>> buscarTodos(){
        List<VeiculoDTO> veiculos = service.buscarTodos();
        return ResponseEntity.ok(veiculos);
    }

    @GetMapping("/{placa}")
    public ResponseEntity<VeiculoDTO> buscarPorPlaca(@PathVariable String placa){
        VeiculoDTO response = service.buscarPelaPlaca(placa);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Void> salvarVeiculo(@RequestBody VeiculoDTO veiculoDTO){
        service.salvar(mapper.toEntity(veiculoDTO));
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{placa}")
    public ResponseEntity<VeiculoDTO> atualizarVeiculo(@PathVariable String placa, @RequestBody VeiculoDTO veiculoDTO){
        return ResponseEntity.ok(service.atualizar(placa, mapper.toEntity(veiculoDTO)));
    }

    @DeleteMapping("/{placa}")
    public ResponseEntity<Void> deletarVeiculo(@PathVariable String placa){
        service.deletar(placa);
        return ResponseEntity.noContent().build();
    }



}

package io.github.daninishikino.venda_carros.service;


import io.github.daninishikino.venda_carros.controller.DTO.VeiculoDTO;
import io.github.daninishikino.venda_carros.mapper.VeiculoMapper;
import io.github.daninishikino.venda_carros.model.Veiculo;
import io.github.daninishikino.venda_carros.repository.VeiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VeiculoService {
    private final VeiculoRepository repository;
    private final VeiculoMapper mapper;

    public void salvar(Veiculo veiculo){
        repository.save(veiculo);
    }

    public VeiculoDTO buscarPelaPlaca(String numeroPlaca){
        return mapper.toDTO(repository.findByPlaca(numeroPlaca).orElseThrow(() ->
                new IllegalArgumentException("Placa não encontrada")));
    }

    public List<VeiculoDTO> buscarTodos(){
        List<Veiculo> veiculos = repository.findAll();
        return veiculos.stream().map(mapper::toDTO).toList();
    }

    public VeiculoDTO atualizar(String placa, Veiculo veiculo){
        Veiculo veiculoParaAtualizar = repository.findByPlaca(placa).orElseThrow(() -> new IllegalArgumentException("Placa não encontrada"));
        veiculo.setId(veiculoParaAtualizar.getId());
        veiculo.setDataCadastro(veiculoParaAtualizar.getDataCadastro());

        return mapper.toDTO(repository.save(veiculo));
    }

    public void deletar(String placa){
        Veiculo veiculoParaDeletar = repository.findByPlaca(placa).orElseThrow(() -> new IllegalArgumentException("Placa não encontrada"));

        repository.delete(veiculoParaDeletar);
    }


}

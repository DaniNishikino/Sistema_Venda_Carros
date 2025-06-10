package io.github.daninishikino.venda_carros.service;


import io.github.daninishikino.venda_carros.controller.DTO.VeiculoDTO;
import io.github.daninishikino.venda_carros.exceptions.veiculo.DadosSensiveisException;
import io.github.daninishikino.venda_carros.exceptions.veiculo.VeiculoNaoEncontradoException;
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

    public void salvar(VeiculoDTO veiculo){
        repository.save(mapper.toEntity(veiculo));
    }

    public VeiculoDTO buscarPelaPlaca(String numeroPlaca){
        return mapper.toDTO(repository.findByPlaca(numeroPlaca).orElseThrow(() ->
                new VeiculoNaoEncontradoException("Placa não encontrada")));
    }

    public List<VeiculoDTO> buscarTodos(){
        List<Veiculo> veiculos = repository.findAll();
        return veiculos.stream().map(mapper::toDTO).toList();
    }

    public VeiculoDTO atualizar(String placa, VeiculoDTO veiculo){
        Veiculo veiculoParaAtualizar = repository.findByPlaca(placa).orElseThrow(() ->
                new IllegalArgumentException("Placa não encontrada"));
        if (!veiculoParaAtualizar.getMarca().equals(veiculo.marca())
                || (!veiculoParaAtualizar.getModelo().equals(veiculo.modelo()))
                || (!veiculoParaAtualizar.getAnoFabricacao().equals(veiculo.anoFabricacao()))
                || (!veiculoParaAtualizar.getAnoModelo().equals(veiculo.anoModelo()))){
            throw new DadosSensiveisException("Dados sensiveis não podem ser alterados");
        }

        veiculoParaAtualizar.setDescricao(veiculo.descricao());
        veiculoParaAtualizar.setCor(veiculo.cor());
        veiculoParaAtualizar.setPlaca(veiculo.placa());
        veiculoParaAtualizar.setPreco(veiculo.preco());
        veiculoParaAtualizar.setDisponivel(veiculo.disponivel());

        repository.save(veiculoParaAtualizar);
        return mapper.toDTO(veiculoParaAtualizar);

    }

    public void deletar(String placa){
        Veiculo veiculoParaDeletar = repository.findByPlaca(placa).orElseThrow(() ->
                new VeiculoNaoEncontradoException("Placa não encontrada"));

        repository.delete(veiculoParaDeletar);
    }


}

package io.github.daninishikino.venda_carros.service;


import io.github.daninishikino.venda_carros.controller.DTO.request.VeiculoDTO;
import io.github.daninishikino.venda_carros.exceptions.veiculo.VeiculosDadosSensiveisException;
import io.github.daninishikino.venda_carros.exceptions.veiculo.VeiculoNaoEncontradoException;
import io.github.daninishikino.venda_carros.mapper.VeiculoMapper;
import io.github.daninishikino.venda_carros.model.Veiculo;
import io.github.daninishikino.venda_carros.repository.VeiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Serviço responsável por operações relacionadas a veículos no sistema.
 * Gerencia operações CRUD de veículos e regras de negócio relacionadas.
 */
@Service
@RequiredArgsConstructor
public class VeiculoService {
    private final VeiculoRepository repository;
    private final VeiculoMapper mapper;

    /**
     * Salva um novo veículo no sistema.
     *
     * @param veiculo O DTO com os dados do veículo a ser salvo
     */
    public void salvar(VeiculoDTO veiculo){
        repository.save(mapper.toEntity(veiculo));
    }

    /**
     * Busca um veículo pela sua placa única.
     *
     * @param numeroPlaca A placa do veículo a ser buscado
     * @return O DTO do veículo correspondente
     * @throws VeiculoNaoEncontradoException Se o veículo não for encontrado
     */
    public VeiculoDTO buscarPelaPlaca(String numeroPlaca){
        return mapper.toDTO(repository.findByPlaca(numeroPlaca).orElseThrow(() ->
                new VeiculoNaoEncontradoException("Placa não encontrada")));
    }

    /**
     * Recupera todos os veículos cadastrados no sistema.
     *
     * @return Lista de DTOs com os dados de todos os veículos
     */
    public List<VeiculoDTO> buscarTodos(){
        List<Veiculo> veiculos = repository.findAll();
        return veiculos.stream().map(mapper::toDTO).toList();
    }

    /**
     * Atualiza os dados de um veículo existente, impedindo alterações em dados sensíveis.
     * Dados sensíveis incluem: marca, modelo, ano de fabricação e ano do modelo.
     *
     * @param placa A placa do veículo a ser atualizado
     * @param veiculo Os novos dados do veículo
     * @return DTO com os dados atualizados do veículo
     * @throws VeiculoNaoEncontradoException Se o veículo não for encontrado
     * @throws VeiculosDadosSensiveisException Se houver tentativa de alterar dados sensíveis
     */
    public VeiculoDTO atualizar(String placa, VeiculoDTO veiculo){
        Veiculo veiculoParaAtualizar = repository.findByPlaca(placa).orElseThrow(() ->
                new VeiculoNaoEncontradoException("Placa não encontrada"));
        if (!veiculoParaAtualizar.getMarca().equals(veiculo.marca())
                || (!veiculoParaAtualizar.getModelo().equals(veiculo.modelo()))
                || (!veiculoParaAtualizar.getAnoFabricacao().equals(veiculo.anoFabricacao()))
                || (!veiculoParaAtualizar.getAnoModelo().equals(veiculo.anoModelo()))){
            throw new VeiculosDadosSensiveisException("Dados sensiveis não podem ser alterados");
        }

        veiculoParaAtualizar.setDescricao(veiculo.descricao());
        veiculoParaAtualizar.setCor(veiculo.cor());
        veiculoParaAtualizar.setPlaca(veiculo.placa());
        veiculoParaAtualizar.setPreco(veiculo.preco());
        veiculoParaAtualizar.setDisponivel(veiculo.disponivel());

        repository.save(veiculoParaAtualizar);
        return mapper.toDTO(veiculoParaAtualizar);

    }

    /**
     * Remove um veículo do sistema.
     *
     * @param placa A placa do veículo a ser removido
     * @throws VeiculoNaoEncontradoException Se o veículo não for encontrado
     */
    public void deletar(String placa){
        Veiculo veiculoParaDeletar = repository.findByPlaca(placa).orElseThrow(() ->
                new VeiculoNaoEncontradoException("Placa não encontrada"));

        repository.delete(veiculoParaDeletar);
    }


}

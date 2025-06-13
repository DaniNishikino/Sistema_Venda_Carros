package io.github.daninishikino.venda_carros.service;

import io.github.daninishikino.venda_carros.controller.DTO.request.VendaRequestDTO;
import io.github.daninishikino.venda_carros.controller.DTO.response.UsuarioResponseDTO;
import io.github.daninishikino.venda_carros.controller.DTO.response.VendaResponseDTO;
import io.github.daninishikino.venda_carros.exceptions.usuario.UsuarioNaoEncontradoException;
import io.github.daninishikino.venda_carros.exceptions.veiculo.VeiculoNaoDisponivelException;
import io.github.daninishikino.venda_carros.exceptions.veiculo.VeiculoNaoEncontradoException;
import io.github.daninishikino.venda_carros.mapper.VeiculoMapper;
import io.github.daninishikino.venda_carros.model.Usuario;
import io.github.daninishikino.venda_carros.model.Veiculo;
import io.github.daninishikino.venda_carros.model.Venda;
import io.github.daninishikino.venda_carros.repository.UsuarioRepository;
import io.github.daninishikino.venda_carros.repository.VeiculoRepository;
import io.github.daninishikino.venda_carros.repository.VendaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço responsável por operações relacionadas a vendas de veículos.
 * Gerencia o processo de venda e suas regras de negócio.
 */
@Service
@RequiredArgsConstructor
public class VendaService {
    @PersistenceContext
    private EntityManager entityManager;
    private final VendaRepository vendaRepository;
    private final VeiculoRepository veiculoRepository;
    private final UsuarioRepository usuarioRepository;
    private final VeiculoMapper veiculoMapper;


    /**
     * Realiza a venda de um veículo, verificando disponibilidade e obtendo usuário logado.
     * O processo inclui:
     * 1. Verificação da existência do veículo pela placa
     * 2. Verificação da disponibilidade do veículo
     * 3. Identificação do usuário (vendedor) logado
     * 4. Marcação do veículo como indisponível
     * 5. Registro da venda com valor igual ao preço do veículo
     *
     * @param dto DTO com a placa do veículo a ser vendido
     * @return DTO com os dados completos da venda realizada
     * @throws VeiculoNaoEncontradoException Se o veículo não for encontrado
     * @throws VeiculoNaoDisponivelException Se o veículo não estiver disponível para venda
     * @throws UsuarioNaoEncontradoException Se o usuário logado não for encontrado
     */
    @Transactional
    public VendaResponseDTO realizarVenda(VendaRequestDTO dto){
        Veiculo veiculo = veiculoRepository.findByPlaca(dto.placaVeiculo())
                .orElseThrow(() -> new VeiculoNaoEncontradoException("Placa não encontrada"));

        if (!veiculo.getDisponivel()){
            throw new VeiculoNaoDisponivelException("Veiculo já vendido ou indisponivel.");
        }
        String loginUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByLogin(loginUsuario)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario logado não existe"));

        veiculo.setDisponivel(false);
        veiculoRepository.save(veiculo);

        Venda venda = new Venda();
        venda.setVeiculo(veiculo);
        venda.setUsuario(usuario);
        venda.setValorVenda(veiculo.getPreco());
        vendaRepository.save(venda);
        entityManager.flush();
        entityManager.refresh(venda);
        UsuarioResponseDTO usuarioResponseDTO = new UsuarioResponseDTO(usuario.getId(), usuario.getNome(),
                usuario.getLogin(), usuario.getPapel());
        return new VendaResponseDTO(
                venda.getId(),
                veiculoMapper.toDTO(veiculo),
                usuarioResponseDTO,
                venda.getDataVenda(),
                venda.getValorVenda());
    }
    public List<VendaResponseDTO> buscarTodasVendas() {
        List<Venda> vendas = vendaRepository.findAll();
        return vendas.stream().map(venda -> {
            UsuarioResponseDTO usuarioDTO = new UsuarioResponseDTO(
                    venda.getUsuario().getId(),
                    venda.getUsuario().getNome(),
                    venda.getUsuario().getLogin(),
                    venda.getUsuario().getPapel()
            );
            return new VendaResponseDTO(
                    venda.getId(),
                    veiculoMapper.toDTO(venda.getVeiculo()),
                    usuarioDTO,
                    venda.getDataVenda(),
                    venda.getValorVenda()
            );
        }).collect(Collectors.toList());
    }


}

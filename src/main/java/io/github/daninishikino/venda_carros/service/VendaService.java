package io.github.daninishikino.venda_carros.service;

import io.github.daninishikino.venda_carros.controller.DTO.request.VendaRequestDTO;
import io.github.daninishikino.venda_carros.controller.DTO.response.UsuarioResponseDTO;
import io.github.daninishikino.venda_carros.controller.DTO.response.VendaResponseDTO;
import io.github.daninishikino.venda_carros.exceptions.usuario.UsuarioNaoEncontradoException;
import io.github.daninishikino.venda_carros.exceptions.veiculo.VeiculoNaoDisponivelException;
import io.github.daninishikino.venda_carros.exceptions.veiculo.VeiculoNaoEncontradoException;
import io.github.daninishikino.venda_carros.mapper.UsuarioMapper;
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

import javax.swing.text.html.parser.Entity;

@Service
@RequiredArgsConstructor
public class VendaService {
    @PersistenceContext
    private EntityManager entityManager;
    private final VendaRepository vendaRepository;
    private final VeiculoRepository veiculoRepository;
    private final UsuarioRepository usuarioRepository;
    private final VeiculoMapper veiculoMapper;


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


}

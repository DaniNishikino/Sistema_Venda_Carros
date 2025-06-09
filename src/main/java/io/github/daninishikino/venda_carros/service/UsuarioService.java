package io.github.daninishikino.venda_carros.service;

import io.github.daninishikino.venda_carros.controller.DTO.UsuarioDTO;
import io.github.daninishikino.venda_carros.mapper.UsuarioMapper;
import io.github.daninishikino.venda_carros.model.Usuario;
import io.github.daninishikino.venda_carros.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository repository;
    private final UsuarioMapper mapper;

    public void salvar(UsuarioDTO dto){
        repository.save(mapper.toEntity(dto));
    }
    public UsuarioDTO buscarPorLogin(String login){
        Usuario usuarioParaBuscar = repository.findByLogin(login).orElseThrow(() -> new IllegalArgumentException("Usuario não encontrado"));
        return mapper.toDTO(usuarioParaBuscar);
    }
}

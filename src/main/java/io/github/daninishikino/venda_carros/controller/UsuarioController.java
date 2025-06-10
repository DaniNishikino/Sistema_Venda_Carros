package io.github.daninishikino.venda_carros.controller;

import io.github.daninishikino.venda_carros.controller.DTO.UsuarioDTO;
import io.github.daninishikino.venda_carros.mapper.UsuarioMapper;
import io.github.daninishikino.venda_carros.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService service;
    private final UsuarioMapper mapper;

    @GetMapping("/{login}")
    @PreAuthorize("hasAnyRole('GERENTE', 'VENDEDOR')")
    public ResponseEntity<UsuarioDTO> buscarPorLogin(@PathVariable String login){
        UsuarioDTO usuarioDTO = mapper.toDTO(service.buscarPorLogin(login));

        return ResponseEntity.ok(usuarioDTO);
    }
    @PostMapping
    public ResponseEntity<Void> criarUsuario(@RequestBody UsuarioDTO dto){
        service.salvar(mapper.toEntity(dto));
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{login}")
    public ResponseEntity<UsuarioDTO> atualizarUsuario(@PathVariable String login, @RequestBody UsuarioDTO usuarioDTO){
        UsuarioDTO usuario = service.atualizarUsuario(login, usuarioDTO);
        return ResponseEntity.ok(usuario);
    }
    @DeleteMapping("/{login}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable String login){
        service.deletarUsuario(login);
        return ResponseEntity.noContent().build();
    }

}

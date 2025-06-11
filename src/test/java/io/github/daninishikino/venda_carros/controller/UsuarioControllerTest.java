package io.github.daninishikino.venda_carros.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.daninishikino.venda_carros.controller.DTO.request.UsuarioDTO;
import io.github.daninishikino.venda_carros.mapper.UsuarioMapper;
import io.github.daninishikino.venda_carros.model.Usuario;
import io.github.daninishikino.venda_carros.model.enums.Papel;
import io.github.daninishikino.venda_carros.service.UsuarioService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private UsuarioMapper usuarioMapper;

    @Test
    @DisplayName("Deve criar um novo usuário com sucesso")
    public void deveCriarUsuarioComSucesso() throws Exception {

        UsuarioDTO usuarioDTO = new UsuarioDTO(
                null,
                "Teste Usuario",
                "testuser",
                "senha123",
                Papel.CLIENTE
        );

        Usuario usuario = new Usuario();
        usuario.setId(UUID.randomUUID());
        usuario.setNome("Teste Usuario");
        usuario.setLogin("testuser");
        usuario.setSenha("senha123");
        usuario.setPapel(Papel.CLIENTE);

        when(usuarioMapper.toEntity(any(UsuarioDTO.class))).thenReturn(usuario);
        doNothing().when(usuarioService).salvar(any(Usuario.class));


        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioDTO)))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "GERENTE")
    @DisplayName("Deve buscar usuário por login com sucesso")
    public void deveBuscarUsuarioPorLoginComSucesso() throws Exception {

        String login = "testuser";
        Usuario usuario = new Usuario();
        usuario.setId(UUID.randomUUID());
        usuario.setNome("Teste Usuario");
        usuario.setLogin(login);
        usuario.setPapel(Papel.CLIENTE);

        UsuarioDTO usuarioDTO = new UsuarioDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getLogin(),
                "senha123",
                usuario.getPapel()
        );

        when(usuarioService.buscarPorLogin(login)).thenReturn(usuario);
        when(usuarioMapper.toDTO(any(Usuario.class))).thenReturn(usuarioDTO);


        mockMvc.perform(get("/usuarios/{login}", login)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value(usuario.getNome()))
                .andExpect(jsonPath("$.login").value(usuario.getLogin()))
                .andExpect(jsonPath("$.papel").value(usuario.getPapel().name()));
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    @DisplayName("Deve negar acesso ao buscar por login para usuário sem permissão")
    public void deveNegarAcessoAoBuscarPorLogin() throws Exception {
        mockMvc.perform(get("/usuarios/qualquer"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    @DisplayName("Deve atualizar usuário com sucesso")
    public void deveAtualizarUsuarioComSucesso() throws Exception {

        String login = "testuser";
        UsuarioDTO usuarioDTO = new UsuarioDTO(
                UUID.randomUUID(),
                "Usuario Atualizado",
                "testuser",
                "novaSenha123",
                Papel.CLIENTE
        );

        when(usuarioService.atualizarUsuario(eq(login), any(UsuarioDTO.class))).thenReturn(usuarioDTO);


        mockMvc.perform(put("/usuarios/{login}", login)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value(usuarioDTO.nome()))
                .andExpect(jsonPath("$.login").value(usuarioDTO.login()));
    }

    @Test
    @WithMockUser(roles = "GERENTE")
    @DisplayName("Deve deletar usuário com sucesso")
    public void deveDeletarUsuarioComSucesso() throws Exception {

        String login = "testuser";
        doNothing().when(usuarioService).deletarUsuario(login);


        mockMvc.perform(delete("/usuarios/{login}", login))
                .andExpect(status().isNoContent());
    }
}

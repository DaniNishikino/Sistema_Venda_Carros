package io.github.daninishikino.venda_carros.controller.view;

import io.github.daninishikino.venda_carros.controller.DTO.request.UsuarioDTO;
import io.github.daninishikino.venda_carros.exceptions.usuario.UsuariosComVendasException;
import io.github.daninishikino.venda_carros.mapper.UsuarioMapper;
import io.github.daninishikino.venda_carros.model.Usuario;
import io.github.daninishikino.venda_carros.model.enums.Papel;
import io.github.daninishikino.venda_carros.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioViewController {

    private final UsuarioService usuarioService;
    private final UsuarioMapper usuarioMapper;

    @GetMapping("/registo")
    public String registroForm(Model model){
        model.addAttribute("usuario", new UsuarioDTO(null, "", "", "", Papel.CLIENTE));
        return "usuario/registro";
    }
    @PostMapping("/registro")
    public String registrarUsuario(@Valid @ModelAttribute("usuario") UsuarioDTO usuarioDTO,
                                   BindingResult result){
        if (result.hasErrors()){
            return "usuario/registro";
        }
        usuarioService.salvar(usuarioMapper.toEntity(usuarioDTO));
        return "redirect:/login";
    }
    @GetMapping("/admin/cadastrar")
    @PreAuthorize("hasRole('GERENTE')")
    public String cadastrarUsuarioForm(Model model){
        model.addAttribute("usuario", new UsuarioDTO(null, "", "", "", Papel.CLIENTE));
        return "usuario/cadastro-usuarios-gerente";
    }

    @PostMapping("/admin/cadastrar")
    @PreAuthorize("hasRole('GERENTE')")
    public String cadastrarUsuario(@Valid @ModelAttribute("usuario") UsuarioDTO usuarioDTO,
                                   BindingResult result){
        if (result.hasErrors()){
            return "usuario/cadastro-usuarios-gerente";
        }
        usuarioService.salvar(usuarioMapper.toEntity(usuarioDTO));
        return "redirect:/usuarios/admin/listar";
    }
    @GetMapping("/perfil")
    public String perfilUsuario(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("usuario", usuarioMapper.toDTO(usuarioService.buscarPorLogin(auth.getName())));
        return "usuario/perfil";
    }
    @GetMapping("/editar")
    public String editarPerfilForm(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("usuario", usuarioMapper.toDTO(usuarioService.buscarPorLogin(auth.getName())));
        return "usuario/editar";
    }
    @PostMapping("/atualizar")
    public String atualizarPerfil(@Valid @ModelAttribute("usuario") UsuarioDTO usuarioDTO,
                                  BindingResult result){
        if (result.hasErrors()){
            return "usuario/editar";
        }
        Authentication auth = pegarUsuarioLogado();
        usuarioService.atualizarUsuario(auth.getName(), usuarioDTO);
        return "redirect:/usuarios/perfil";
    }
    @GetMapping("/admin/listar")
    public String listarUsuarios(Model model){
        List<Usuario> usuarios = usuarioService.buscarTodos();
        model.addAttribute("usuarios", usuarios.stream().map(usuarioMapper::toDTO).toList());

        return "usuario/listar";
    }
    @DeleteMapping("/admin/{login}")
    public String deletarUsuarioView(@PathVariable String login, RedirectAttributes attributes) {
        try {
            usuarioService.deletarUsuario(login);
            return "redirect:/usuarios/admin/listar";
        } catch (UsuariosComVendasException e) {
            attributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/usuarios/admin/listar";
        }
    }


    private Authentication pegarUsuarioLogado(){
        return SecurityContextHolder.getContext().getAuthentication();
    }
}

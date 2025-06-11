package io.github.daninishikino.venda_carros.service;

import io.github.daninishikino.venda_carros.controller.DTO.request.UsuarioDTO;
import io.github.daninishikino.venda_carros.exceptions.usuario.AcessoNegadoException;
import io.github.daninishikino.venda_carros.exceptions.usuario.UsuarioNaoEncontradoException;
import io.github.daninishikino.venda_carros.mapper.UsuarioMapper;
import io.github.daninishikino.venda_carros.model.Usuario;
import io.github.daninishikino.venda_carros.model.enums.Papel;
import io.github.daninishikino.venda_carros.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository repository;
    private final PasswordEncoder encoder;
    private final UsuarioMapper mapper;

    public void salvar(Usuario usuario){
        String papelUsuarioLogado = obterPapelUsuarioLogado();
        Papel papelParaCriar = usuario.getPapel();
        if ((papelParaCriar == Papel.GERENTE || papelParaCriar == Papel.VENDEDOR)
            && !papelUsuarioLogado.equals("GERENTE")){
            throw new AcessoNegadoException("Apenas gerente pode criar usuarios gerente ou vendedor");
        }

        usuario.setSenha(encoder.encode(usuario.getSenha()));
        repository.save(usuario);
    }

    public Usuario buscarPorLogin(String login){
        return repository.findByLogin(login).orElseThrow(() ->
                new UsuarioNaoEncontradoException("Usuario não encontrado"));
    }

    public UsuarioDTO atualizarUsuario(String login, UsuarioDTO dto) {
        String papelUsuarioLogado = obterPapelUsuarioLogado();
        Papel papelParaCriar = mapper.toEntity(dto).getPapel();
        if ((papelParaCriar == Papel.GERENTE || papelParaCriar == Papel.VENDEDOR)
                && !papelUsuarioLogado.equals("GERENTE")){
            throw new AcessoNegadoException("Apenas gerente pode atualizar usuarios gerente ou vendedor");
        }
        Usuario usuarioParaAtualizar = repository.findByLogin(login).orElseThrow(()->
                new UsuarioNaoEncontradoException("Usuario não encontrado"));

        usuarioParaAtualizar.setNome(dto.nome());
        usuarioParaAtualizar.setSenha(encoder.encode(dto.senha()));
        usuarioParaAtualizar.setPapel(dto.papel());
        usuarioParaAtualizar.setLogin(dto.login());

        return mapper.toDTO(repository.save(usuarioParaAtualizar));
    }

    public void deletarUsuario(String login){
        String papelUsuarioLogado = obterPapelUsuarioLogado();
        String loginUsuarioLogado = obterLoginUsuarioLogado();


        Usuario usuarioParaDeletar = repository.findByLogin(login).orElseThrow(() ->
                new UsuarioNaoEncontradoException("Usuario não encontrado"));
        if(papelUsuarioLogado.equals("CLIENTE")) {
            if(!usuarioParaDeletar.getLogin().equals(loginUsuarioLogado)) {
                throw new AcessoNegadoException("Cliente só pode deletar o próprio cadastro.");
            }
            repository.delete(usuarioParaDeletar);
            return;
        }

        if(papelUsuarioLogado.equals("VENDEDOR")) {
            if(!usuarioParaDeletar.getPapel().name().equals("CLIENTE")) {
                throw new AcessoNegadoException("Vendedor só pode deletar clientes.");
            }
            repository.delete(usuarioParaDeletar);
            return;
        }

        if(papelUsuarioLogado.equals("GERENTE")) {

            repository.delete(usuarioParaDeletar);
            return;
        }

        throw new AcessoNegadoException("Acesso não permitido.");
    }


    private String obterLoginUsuarioLogado() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }



    private String obterPapelUsuarioLogado(){
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().stream()
                .findFirst().get().getAuthority().replace("ROLE_", "");
    }


}

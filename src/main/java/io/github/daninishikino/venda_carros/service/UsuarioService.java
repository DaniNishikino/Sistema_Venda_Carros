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

/**
 * Serviço responsável por operações relacionadas a usuários no sistema.
 * Gerencia autenticação, autorização e operações CRUD de usuários.
 */
@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository repository;
    private final PasswordEncoder encoder;
    private final UsuarioMapper mapper;

    /**
     * Salva um novo usuário no sistema com verificações de permissão.
     * Apenas GERENTE pode criar usuários com papel GERENTE ou VENDEDOR.
     *
     * @param usuario O objeto Usuario a ser salvo
     * @throws AcessoNegadoException Se o usuário atual não tem permissão para criar o tipo de usuário solicitado
     */
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

    /**
     * Busca um usuário pelo seu login único.
     *
     * @param login O login do usuário a ser buscado
     * @return O objeto Usuario correspondente
     * @throws UsuarioNaoEncontradoException Se o usuário não for encontrado
     */
    public Usuario buscarPorLogin(String login){
        return repository.findByLogin(login).orElseThrow(() ->
                new UsuarioNaoEncontradoException("Usuario não encontrado"));
    }

    /**
     * Atualiza os dados de um usuário existente, com verificações de permissão.
     *
     * @param login O login do usuário a ser atualizado
     * @param dto Os novos dados do usuário
     * @return DTO do usuário atualizado
     * @throws AcessoNegadoException Se o usuário atual não tem permissão para esta operação
     * @throws UsuarioNaoEncontradoException Se o usuário não for encontrado
     */
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

    /**
     * Remove um usuário do sistema, com verificações de permissão.
     * - Clientes só podem deletar o próprio cadastro
     * - Vendedores só podem deletar clientes
     * - Gerentes podem deletar qualquer usuário
     *
     * @param login O login do usuário a ser deletado
     * @throws AcessoNegadoException Se o usuário atual não tem permissão para esta operação
     * @throws UsuarioNaoEncontradoException Se o usuário não for encontrado
     */
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

    /**
     * Obtém o login do usuário atualmente autenticado.
     *
     * @return O login do usuário autenticado
     */
    private String obterLoginUsuarioLogado() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }


    /**
     * Obtém o papel (role) do usuário atualmente autenticado.
     *
     * @return O papel do usuário autenticado (GERENTE, VENDEDOR ou CLIENTE)
     */
    private String obterPapelUsuarioLogado(){
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().stream()
                .findFirst().get().getAuthority().replace("ROLE_", "");
    }


}

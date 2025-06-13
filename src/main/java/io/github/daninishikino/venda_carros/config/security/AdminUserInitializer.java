package io.github.daninishikino.venda_carros.config.security;

import io.github.daninishikino.venda_carros.model.Usuario;
import io.github.daninishikino.venda_carros.model.enums.Papel;
import io.github.daninishikino.venda_carros.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminUserInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        boolean existeGerente = usuarioRepository.findAll().stream()
                .anyMatch(u -> u.getPapel() == Papel.GERENTE);

        if (!existeGerente) {

            Usuario admin = new Usuario();
            admin.setNome("Administrador");
            //admin.setLogin("admin@sistema.com");
            admin.setLogin("admin");
            admin.setSenha(passwordEncoder.encode("senha123"));
            admin.setPapel(Papel.GERENTE);

            usuarioRepository.save(admin);

            System.out.println("Usuário administrador padrão criado com sucesso!");
        }
    }
}

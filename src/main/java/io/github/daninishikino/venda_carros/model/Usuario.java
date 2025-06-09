package io.github.daninishikino.venda_carros.model;

import io.github.daninishikino.venda_carros.model.enums.Papel;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "Usuario")
@Data
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 150, name = "nome")
    private String nome;

    @Column(nullable = false, unique = true, length = 150)
    private String login;

    @Column(nullable = false, length = 100)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Papel papel;
}

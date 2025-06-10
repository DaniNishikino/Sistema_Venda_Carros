package io.github.daninishikino.venda_carros.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "Veiculo")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Veiculo {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 100, nullable = false, name = "marca", updatable = false)
    private String marca;

    @Column(length = 100, nullable = false, name = "modelo", updatable = false)
    private String modelo;

    @Column(nullable = false, name = "ano_fabricacao", updatable = false)
    private Integer anoFabricacao;

    @Column(nullable = false, name = "ano_modelo", updatable = false)
    private Integer anoModelo;

    @Column(nullable = false, length = 50, name = "cor")
    private String cor;

    @Column(nullable = false, length = 10, name = "placa")
    private String placa;

    @Column(nullable = false, scale = 2, precision = 12, name = "preco")
    private BigDecimal preco;

    @Column(nullable = false, name = "descricao", columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "disponivel")
    private Boolean disponivel;

    @CreationTimestamp
    @Column(name = "data_cadastro", nullable = false, updatable = false)
    private OffsetDateTime dataCadastro;


}

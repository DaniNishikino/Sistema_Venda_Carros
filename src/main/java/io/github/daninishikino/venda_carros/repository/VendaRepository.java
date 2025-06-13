package io.github.daninishikino.venda_carros.repository;

import io.github.daninishikino.venda_carros.model.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VendaRepository extends JpaRepository<Venda, UUID> {
    List<Venda> findByUsuarioId(UUID usuarioId);
}

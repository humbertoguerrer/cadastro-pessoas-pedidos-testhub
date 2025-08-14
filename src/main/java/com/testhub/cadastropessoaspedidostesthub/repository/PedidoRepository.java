package com.testhub.cadastropessoaspedidostesthub.repository;

import com.testhub.cadastropessoaspedidostesthub.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByPessoaId(Long pessoaId);
}

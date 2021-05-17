package com.commerce.demo.repository;

import com.commerce.demo.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> { // clase Cliente que tiene un PK de tipo Integer

    @Query("SELECT p FROM Pedido p WHERE p.estado = :estado")
    public List<Pedido> findPedidoByEstado(@Param("estado") String estado);
}

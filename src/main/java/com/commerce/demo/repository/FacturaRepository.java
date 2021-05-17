package com.commerce.demo.repository;

import com.commerce.demo.model.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Integer> { // clase Cliente que tiene un PK de tipo Integer

}

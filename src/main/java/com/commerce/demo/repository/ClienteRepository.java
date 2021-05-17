package com.commerce.demo.repository;

import com.commerce.demo.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> { // clase Cliente que tiene un PK de tipo Integer

}

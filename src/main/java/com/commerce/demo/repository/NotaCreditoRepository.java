package com.commerce.demo.repository;

import com.commerce.demo.model.NotaCredito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotaCreditoRepository extends JpaRepository <NotaCredito, Integer>{

}

package com.commerce.demo.repository;

import com.commerce.demo.model.Cabecera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CabeceraRepository extends JpaRepository<Cabecera, Integer> {

    }

package com.commerce.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Data
@NoArgsConstructor
@Entity
//@Table(name = "clientes", schema = "commerce", catalog = "")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer numeroCliente;
    private String domicilio;
    private String condicionImpositiva;
    private String tipoDocumento;
    private String numeroDocumento;

    //@Column(name = "numeroCliente", nullable = false)
    //@Column(name = "domicilio", nullable = false, length = 45)
}

package com.commerce.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Data
@NoArgsConstructor
@Entity

public class Cabecera {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCabecera;

    private Date fechaEmision;
    private String numeroDocumento;
    private String codigoEmision;
    private String letra;

    @ManyToOne(fetch = FetchType.EAGER) //todo el contenido, contrario LAZY
    @JoinColumn(name = "numeroCliente")
    private Cliente cliente;

}

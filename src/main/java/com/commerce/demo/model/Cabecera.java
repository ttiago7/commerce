package com.commerce.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity
public class Cabecera {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer numeroDocumento;

    private Date fechaEmision;
    private String codigoEmision;
    private String letra;

    @ManyToOne(fetch = FetchType.EAGER) //todo el contenido, contrario LAZY
    @JoinColumn(name = "numeroCliente")
    private Cliente cliente;

}

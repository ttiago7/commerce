package com.commerce.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@Entity

public class Factura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idFactura;

    @OneToOne(fetch = FetchType.EAGER) //todo el contenido, contrario LAZY
    @JoinColumn(name = "numeroDocumento")
    private Cabecera cabecera;

    @OneToOne(fetch = FetchType.EAGER) //todo el contenido, contrario LAZY
    @JoinColumn(name = "numeroPedido")
    private Pedido pedido;

    @ManyToMany
    public List<DetalleFactura> detalleFactura;

    private BigDecimal total;//calculado
    private BigDecimal totalIva;//calculado

}

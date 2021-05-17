package com.commerce.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Entity

public class DetalleFactura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDetalleFactura;

//    @ManyToOne(fetch = FetchType.EAGER) //todo el contenido, contrario LAZY
//    @JoinColumn(name = "idFactura")
//    private Factura factura;

    @OneToOne(fetch = FetchType.EAGER) //todo el contenido, contrario LAZY
    @JoinColumn(name = "codigo")
    private Producto producto;

    private BigDecimal precioUnitario;
    private BigDecimal porcentajeIva;
    private int cantidad;
    private BigDecimal precioVenta;
    private BigDecimal precioNeto;
    private BigDecimal montoIva;

}

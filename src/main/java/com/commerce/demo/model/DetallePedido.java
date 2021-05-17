package com.commerce.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
public class DetallePedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDetallePedido;

    @OneToOne(fetch = FetchType.EAGER) //todo el contenido, contrario LAZY
    @JoinColumn(name = "codigo")
    private Producto producto;

    private int cantidad;
}

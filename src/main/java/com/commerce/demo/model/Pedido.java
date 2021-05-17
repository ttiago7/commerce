package com.commerce.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@Entity

public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer numeroPedido;

    @ManyToOne(fetch = FetchType.EAGER) //todo el contenido, contrario LAZY
    @JoinColumn(name = "numeroCliente")
    private Cliente cliente;

    @ManyToMany
    private List<DetallePedido> detallePedido;

    private String estado;

}

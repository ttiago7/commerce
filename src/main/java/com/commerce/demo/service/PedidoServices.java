package com.commerce.demo.service;

import com.commerce.demo.model.Cliente;
import com.commerce.demo.model.DetallePedido;
import com.commerce.demo.model.Pedido;
import com.commerce.demo.model.Producto;
import com.commerce.demo.repository.DetallePedidoRepository;
import com.commerce.demo.repository.PedidoRepository;
import com.commerce.demo.util.EntityURLBuilder;
import com.commerce.demo.util.PostResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.LinkedList;
import java.util.List;

@Service
public class PedidoServices {
    private static final String PEDIDO_PATH = "pedido";//es la url q ponelmemos en postman

    @Autowired
    private PedidoRepository pedidoRepository;
    private ClienteService clienteService;
    private ProductoService productService;
    private DetallePedidoRepository detallePedidoRepository;

    public PedidoServices(PedidoRepository pedidoRepository, ClienteService clienteService, ProductoService productService, DetallePedidoRepository detallePedidoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.clienteService = clienteService;
        this.productService = productService;
        this.detallePedidoRepository = detallePedidoRepository;
    }

    public PostResponse addPedido(Pedido pedido) {
        List<DetallePedido> detallePedido = new LinkedList<>();
        for (int i = 0; i < pedido.getDetallePedido().size(); i++) {
            detallePedido.add(detallePedidoRepository.save(pedido.getDetallePedido().get(i)));
        }

        pedido.setDetallePedido(detallePedido);

        final Pedido pedidoSaved = pedidoRepository.save(pedido);
        return PostResponse.builder()
                .status(HttpStatus.CREATED)
                .url(EntityURLBuilder.buildURL(PEDIDO_PATH, pedidoSaved.getNumeroPedido().toString()))
                .build();
    }

    public List<Pedido> getAllPedidos() {
        return pedidoRepository.findAll();
    }

    public Pedido getPedido(Integer numero) {
        return pedidoRepository.findById(numero).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
    }

    public void deletePedidoByNum(Integer numero) {
        pedidoRepository.deleteById(numero);
    }

    public Pedido updateState(Pedido pedido) {
        return pedidoRepository.findById(pedido.getNumeroPedido())
                .map(pedido1 -> {
                    pedido1.setEstado(pedido.getEstado());
                    return pedidoRepository.save(pedido1);
                })
                .orElseGet(() -> {
                    //Si no se encuentra
                    return null;
                });
    }

    public List<Pedido> getPedidoByEstado (String estado){
        return pedidoRepository.findPedidoByEstado(estado);
    }
}

package com.commerce.demo.controller;

import com.commerce.demo.model.Pedido;
import com.commerce.demo.service.PedidoServices;
import com.commerce.demo.util.PostResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // le decimos q es un controller
@RequestMapping("/orders") //
public class PedidoController {
    @Autowired // ic - i de dependencia
    private PedidoServices pedidoService;


    @PostMapping
    @Operation(summary = "Post a order")// notation de Swagger para la documentacion
    @ApiResponses(value = {// Documentacion
            @ApiResponse(responseCode = "201", description = "Order successfully created",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))})
    })
    public ResponseEntity<?> addPedido(@RequestBody Pedido pedido) {//con el ? es para no especificar el tipo de objeto q vamos a devolver
        //con final no lo podes redefinir
        final PostResponse postResponse = pedidoService.addPedido(pedido);
        return new ResponseEntity(postResponse.getUrl(), postResponse.getStatus());
    }


    @GetMapping("/all")
    @Operation(summary = "Complete list of orders")
    public List<Pedido> getAll() {//get all armara los hijos segun el tipo de cada uno d los objetos
        return pedidoService.getAllPedidos();
    }


    @GetMapping("/{number}")
    @Operation(summary = "Get by number of order")
    public Pedido getPedido(@PathVariable Integer number) {
        Pedido response = pedidoService.getPedido(number);
        if (response != null) {
            return response;
        } else {
            return null;
        }
    }

    @DeleteMapping("/{number}")
    @Operation(summary = "Delete order by number of order")
    public String  deleteProducto(@PathVariable Integer number) {
        pedidoService.deletePedidoByNum(number);
        return ("Order number: " + number + " successfully deleted");
    }

    @PutMapping()
    @Operation(summary = "Update state of a order")
    public String updateState(@RequestBody Pedido pedido) {
        pedidoService.updateState(pedido);
        return ("Order number: " +pedido.getNumeroPedido()+" state change to " + pedido.getEstado());
    }

    @GetMapping("/state/{state}")
    @Operation(summary = "Get orders by state")
    public ResponseEntity<List<Pedido>> findPedidoByEstado (@PathVariable String state){
        System.out.println(state);
        return new ResponseEntity<List<Pedido>>(pedidoService.getPedidoByEstado(state), HttpStatus.OK);
    }
}

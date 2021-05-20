package com.commerce.demo.controller;

import com.commerce.demo.model.Cliente;
import com.commerce.demo.service.ClienteService;
import com.commerce.demo.util.PostResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // le decimos q es un controller
@RequestMapping("/clientes") //
public class ClienteController {
    @Autowired // ic - i de dependencia
    private ClienteService clienteService;

    @CrossOrigin
    @PostMapping
    @Operation(summary = "Post a client")// notation de Swagger para la documentacion
    @ApiResponses(value = {// Documentacion
            @ApiResponse(responseCode = "201", description = "Cliente creado exitosamente",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))})
    })
    public ResponseEntity<?> addCliente(@RequestBody Cliente writer) {//con el ? es para no especificar el tipo de objeto q vamos a devolver
        //con final no lo podes redefinir
        final PostResponse postResponse = clienteService.addCliente(writer);
        return new ResponseEntity(postResponse.getUrl(), postResponse.getStatus());
    }


    @GetMapping("/all")
    @Operation(summary = "Complete list of clients")
    public List<Cliente> getAll() {//get all armara los hijos segun el tipo de cada uno d los objetos
        return clienteService.getAllClientes();
    }


    @GetMapping("/{id}")
    @Operation(summary = "Get client by id")
    public Cliente getClienteById(@PathVariable Integer id) {
        Cliente response = clienteService.getCliente(id);
        if (response != null) {
            return response;
        } else {
            return null;
        }
    }

    @CrossOrigin
    @DeleteMapping("/{number}")
    @Operation(summary = "Delete client by number of client")
    public String  deleteProducto(@PathVariable Integer number) {
        clienteService.deleteClienteByid(number);
        return ("Cliente numero: " + number + " borrado exitosamente");
    }

    @GetMapping("/numero/{numero}")
    @Operation(summary = "Get client by id")
    public Cliente getClienteByNumeroDocumento(@PathVariable String numero) {
        Cliente response = clienteService.findClienteByNumeroDocumento(numero);
        if (response != null) {
            return response;
        } else {
            return null;
        }
    }
}


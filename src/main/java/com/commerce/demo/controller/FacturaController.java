package com.commerce.demo.controller;

import com.commerce.demo.model.Factura;
import com.commerce.demo.service.FacturaService;
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
import java.util.concurrent.CompletableFuture;

@RestController // le decimos q es un controller
@RequestMapping("/invoices") //
public class FacturaController {
    @Autowired
    private FacturaService facturaService;

    @CrossOrigin
    @PostMapping
    @Operation(summary = "Post a invoice")// notation de Swagger para la documentacion
//    @ApiResponses(value = {// Documentacion
//            @ApiResponse(responseCode = "201", description = "Invoice successfully created",
//                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))})
//    })
    public CompletableFuture<Factura> addInvoice(@RequestBody Factura invoice) throws InterruptedException {//con el ? es para no especificar el tipo de objeto q vamos a devolver
        //con final no lo podes redefinir
        return facturaService.addInvoice(invoice);
    }


    @GetMapping("/all")
    @Operation(summary = "Complete list of invoices")
    public List<Factura> getAllInvoices() {//get all armara los hijos segun el tipo de cada uno d los objetos
        return facturaService.getAllInvoices();
    }


    @GetMapping("/{id}")
    @Operation(summary = "Get client by id")
    public Factura getInvoice(@PathVariable Integer id) {
        Factura response = facturaService.getInvoice(id);
        if (response != null) {
            return response;
        } else {
            return null;
        }
    }

    @CrossOrigin
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete invoice by id")
    public String  deleteProducto(@PathVariable Integer id) {
        facturaService.deleteInvoiceByid(id);
        return ("Invoice id: " + id + " successfully deleted");
    }
}

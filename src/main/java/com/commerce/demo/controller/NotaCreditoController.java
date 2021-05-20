package com.commerce.demo.controller;

import com.commerce.demo.model.Factura;
import com.commerce.demo.model.NotaCredito;
import com.commerce.demo.service.FacturaService;
import com.commerce.demo.service.NotaCreditoService;
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
@RequestMapping("/creditNote") //
public class NotaCreditoController {
    @Autowired
    private NotaCreditoService notaCreditoService;

    @CrossOrigin
    @PostMapping
    @Operation(summary = "Post a credit note")// notation de Swagger para la documentacion
    @ApiResponses(value = {// Documentacion
            @ApiResponse(responseCode = "201", description = "Credit note successfully created",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))})
    })
    public ResponseEntity<?> addCreditNote(@RequestBody NotaCredito notaCredito) {//con el ? es para no especificar el tipo de objeto q vamos a devolver
        //con final no lo podes redefinir
        final PostResponse postResponse = notaCreditoService.addCreditNote(notaCredito);
        return new ResponseEntity(postResponse.getUrl(), postResponse.getStatus());
    }


    @GetMapping("/all")
    @Operation(summary = "Complete list of credits notes")
    public List<NotaCredito> getAllCreditsNotes() {//get all armara los hijos segun el tipo de cada uno d los objetos
        return notaCreditoService.getAllCreditsNotes();
    }


    @GetMapping("/{id}")
    @Operation(summary = "Get credit note by id")
    public NotaCredito getCreditNote(@PathVariable Integer id) {
        NotaCredito response = notaCreditoService.getCreditNote(id);
        if (response != null) {
            return response;
        } else {
            return null;
        }
    }

    @CrossOrigin
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete credit note by id")
    public String  deleteNotaCredito(@PathVariable Integer id) {
        notaCreditoService.deleteCreditNoteByid(id);
        return ("Credit note id: " + id + " successfully deleted");
    }
}

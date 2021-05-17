package com.commerce.demo.service;

import com.commerce.demo.model.*;
import com.commerce.demo.repository.CabeceraRepository;
import com.commerce.demo.repository.NotaCreditoRepository;
import com.commerce.demo.util.EntityURLBuilder;
import com.commerce.demo.util.PostResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Date;
import java.util.List;

@Service
public class NotaCreditoService {
    private static final String NOTA_CREDITO_PATH = "creditNote";//es la url q ponelmemos en postman

    @Autowired
    private NotaCreditoRepository notaCreditoRepository;
    private CabeceraRepository cabeceraRepository;

    public NotaCreditoService(NotaCreditoRepository notaCreditoRepository, CabeceraRepository cabeceraRepository) {
        this.notaCreditoRepository = notaCreditoRepository;
        this.cabeceraRepository = cabeceraRepository;
    }

    public PostResponse addCreditNote(NotaCredito notaCredito) {
        Cabecera cab = notaCredito.getCabecera();

        Cliente cliente = cab.getCliente();

        String letra = "";
        switch (cliente.getCondicionImpositiva()) {
            case "IVA Responsable Inscripto":
                letra = "A";
                break;
            case "Monotributo":
                letra = "B";
                break;
            case "IVA no Responsable":
                letra = "X";
                break;
            default:
                break;
        }
        cab.setFechaEmision(new Date());
        cab.setLetra(letra);

        Cabecera cabeceraSaved = cabeceraRepository.save(cab);

        final NotaCredito notaCreditoSaved = notaCreditoRepository.save(notaCredito);
        return PostResponse.builder()
                .status(HttpStatus.CREATED)
                .url(EntityURLBuilder.buildURL(NOTA_CREDITO_PATH, notaCreditoSaved.getIdNotaCredito().toString()))
                .build();
    }

    public List<NotaCredito> getAllCreditsNotes() {
        return notaCreditoRepository.findAll();
    }

    public NotaCredito getCreditNote(Integer id) {
        return notaCreditoRepository.findById(id).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
    }

    public void deleteCreditNoteByid(Integer id) {
        notaCreditoRepository.deleteById(id);
    }
}

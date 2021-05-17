package com.commerce.demo.service;

import com.commerce.demo.model.Cliente;
import com.commerce.demo.repository.ClienteRepository;
import com.commerce.demo.util.EntityURLBuilder;
import com.commerce.demo.util.PostResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Service
public class ClienteService {
    private static final String CLIENTE_PATH = "cliente";//es la url q ponelmemos en postman

    @Autowired
    private ClienteRepository clienteRepository;

    public PostResponse addCliente(Cliente cliente) {
        final Cliente clienteSaved = clienteRepository.save(cliente);
        return PostResponse.builder()
                .status(HttpStatus.CREATED)
                .url(EntityURLBuilder.buildURL(CLIENTE_PATH, clienteSaved.getNumeroCliente().toString()))
                .build();
    }

    public List<Cliente> getAllClientes() {
        return clienteRepository.findAll();
    }

    public Cliente getCliente(Integer id) {
        return clienteRepository.findById(id).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
    }

    public void deleteClienteByid(Integer id) {
        clienteRepository.deleteById(id);
    }

}

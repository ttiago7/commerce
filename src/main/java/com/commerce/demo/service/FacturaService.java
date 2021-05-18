package com.commerce.demo.service;

import com.commerce.demo.model.*;
import com.commerce.demo.repository.CabeceraRepository;
import com.commerce.demo.repository.DetalleFacturaRepository;
import com.commerce.demo.repository.FacturaRepository;
import com.commerce.demo.util.EntityURLBuilder;
import com.commerce.demo.util.PostResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class FacturaService {
    private static final String FACTURA_PATH = "invoice";//es la url q ponelmemos en postman

    @Autowired
    private FacturaRepository facturaRepository;
    private CabeceraRepository cabeceraRepository;
    private DetalleFacturaRepository detalleFacturaRepository;

    public FacturaService(FacturaRepository facturaRepository, CabeceraRepository cabeceraRepository, DetalleFacturaRepository detalleFacturaRepository) {
        this.facturaRepository = facturaRepository;
        this.cabeceraRepository = cabeceraRepository;
        this.detalleFacturaRepository = detalleFacturaRepository;
    }

    @Async("threadPoolExecutor")
    public CompletableFuture<Factura> addInvoice(Factura factura) throws InterruptedException{
//    public PostResponse addInvoice(Factura factura) {//
        Cabecera cab = factura.getCabecera();

        Cliente cliente = cab.getCliente();

        BigDecimal porcentajeIva = new BigDecimal(0);
        String letra = "";
        switch (cliente.getCondicionImpositiva()) {
            case "IVA Responsable Inscripto":
                porcentajeIva = new BigDecimal(10.05);
                letra = "A";
                break;
            case "Monotributo":
                porcentajeIva = new BigDecimal(21.00);
                letra = "B";
                break;
            case "IVA no Responsable":
                porcentajeIva = new BigDecimal(70.00);
                letra = "X";
                break;
            default:
                break;
        }
        cab.setFechaEmision(new Date());
        cab.setLetra(letra);

        Cabecera cabeceraSaved = cabeceraRepository.save(cab);

        List<DetalleFactura> detalle = new LinkedList<>();
        BigDecimal total = new BigDecimal(0);
        BigDecimal totalIva = new BigDecimal(0);
        for (int i = 0; i < factura.getDetalleFactura().size(); i++) {
            DetalleFactura detalleFactura = factura.getDetalleFactura().get(i);

            Producto prod = detalleFactura.getProducto();
            detalleFactura.setPrecioUnitario(prod.getPrecio());
            detalleFactura.setPorcentajeIva(porcentajeIva);

            BigDecimal precioNeto = prod.getPrecio().multiply(BigDecimal.valueOf(detalleFactura.getCantidad()));// precio * cantidad
            BigDecimal montoIva = precioNeto.multiply(porcentajeIva).divide(new BigDecimal(100));//% de IVA según categoría
            BigDecimal precioVenta = precioNeto.add(montoIva);//Precio Neto + Iva

            detalleFactura.setPrecioVenta(precioVenta);
            detalleFactura.setPrecioNeto(precioNeto);
            detalleFactura.setMontoIva(montoIva);

            total = total.add(precioVenta);
            totalIva = totalIva.add(montoIva);

            detalleFacturaRepository.save(detalleFactura);
            detalle.add(detalleFactura);
        }

        factura.setCabecera(cabeceraSaved);
        factura.setDetalleFactura(detalle);
        factura.setTotal(total);
        factura.setTotalIva(totalIva);

        final Factura facturaSaved = facturaRepository.save(factura);

        return CompletableFuture.completedFuture(facturaSaved);
//        return PostResponse.builder()
//                .status(HttpStatus.CREATED)
//                .url(EntityURLBuilder.buildURL(FACTURA_PATH, facturaSaved.getIdFactura().toString()))
//                .build();
    }

    public List<Factura> getAllInvoices() {
        return facturaRepository.findAll();
    }

    public Factura getInvoice(Integer id) {
        return facturaRepository.findById(id).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
    }

    public void deleteInvoiceByid(Integer id) {
        facturaRepository.deleteById(id);
    }
}

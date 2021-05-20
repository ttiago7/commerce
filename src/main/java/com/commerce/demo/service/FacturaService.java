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
//        Cabecera cab = factura.getCabecera();
        Cabecera cab = new Cabecera();

        Pedido pedido = factura.getPedido();
        System.out.println(pedido);
        Cliente cliente = pedido.getCliente();
        System.out.println(cliente);

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
        cab.setCodigoEmision(String.valueOf((int)(Math.random()*(9999-1000+1)+8)) +"-"+String.valueOf((int)(Math.random()*(9999-1000+1)+8)));
        cab.setCliente(cliente);
        cab.setFechaEmision(new Date());
        cab.setLetra(letra);

        Cabecera cabeceraSaved = cabeceraRepository.save(cab);

        List<DetalleFactura> detalle = new LinkedList<>();
        BigDecimal total = new BigDecimal(0);
        BigDecimal totalIva = new BigDecimal(0);

        for (int i = 0; i < factura.getPedido().getDetallePedido().size(); i++) {
            DetallePedido detallePedido = factura.getPedido().getDetallePedido().get(i);
            DetalleFactura detalleFactura = new DetalleFactura();

            Producto prod = detallePedido.getProducto();
            detalleFactura.setProducto(prod);
            detalleFactura.setCantidad(detallePedido.getCantidad());

            detalleFactura.setPrecioUnitario(prod.getPrecio());
            detalleFactura.setPorcentajeIva(porcentajeIva.setScale(2, BigDecimal.ROUND_FLOOR));

            BigDecimal precioNeto = prod.getPrecio().multiply(BigDecimal.valueOf(detalleFactura.getCantidad()));// precio * cantidad
            BigDecimal montoIva = precioNeto.multiply(porcentajeIva).divide(new BigDecimal(100));//% de IVA según categoría
            BigDecimal precioVenta = precioNeto.add(montoIva);//Precio Neto + Iva

            detalleFactura.setPrecioVenta(precioVenta.setScale(2, BigDecimal.ROUND_FLOOR));
            detalleFactura.setPrecioNeto(precioNeto.setScale(2, BigDecimal.ROUND_FLOOR));
            detalleFactura.setMontoIva(montoIva.setScale(2, BigDecimal.ROUND_FLOOR));

            total = total.add(precioVenta);
            totalIva = totalIva.add(montoIva);

            detalleFacturaRepository.save(detalleFactura);
            detalle.add(detalleFactura);
        }

        factura.setCabecera(cabeceraSaved);
        factura.setDetalleFactura(detalle);
        factura.setTotal(total.setScale(2, BigDecimal.ROUND_FLOOR));
        factura.setTotalIva(totalIva.setScale(2, BigDecimal.ROUND_FLOOR));

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

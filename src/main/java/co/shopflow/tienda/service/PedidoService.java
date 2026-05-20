package co.shopflow.tienda.service;

import co.shopflow.tienda.config.RabbitMQConfig;
import co.shopflow.tienda.model.DetallePedido;
import co.shopflow.tienda.model.Pedido;
import co.shopflow.tienda.model.Producto;
import co.shopflow.tienda.repository.PedidoRepository;
import co.shopflow.tienda.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;
    private final RabbitTemplate rabbitTemplate;

    // COMMAND: Crear pedido
    public Pedido crearPedido(Pedido pedido) {
        double total = 0;

        if (pedido.getDetalles() == null || pedido.getDetalles().isEmpty()) {
            throw new RuntimeException("El pedido debe tener al menos un producto");
        }

        for (DetallePedido detalle : pedido.getDetalles()) {
            int cantidad = detalle.getCantidad() != null ? detalle.getCantidad() : 0;
            if (cantidad <= 0) {
                throw new RuntimeException("La cantidad debe ser mayor a 0");
            }
            detalle.setCantidad(cantidad);

            if (detalle.getProductoId() == null || detalle.getProductoId() <= 0) {
                throw new RuntimeException("Debe especificar un producto valido");
            }

            Producto producto = productoRepository.findById(detalle.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + detalle.getProductoId()));

            if (producto.getStock() < detalle.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para: " + producto.getNombre());
            }

            producto.setStock(producto.getStock() - detalle.getCantidad());
            productoRepository.save(producto);

            detalle.setProductoNombre(producto.getNombre());
            detalle.setPrecioUnitario(producto.getPrecio());
            detalle.setSubtotal(producto.getPrecio() * detalle.getCantidad());
            total += detalle.getSubtotal();
        }

        pedido.setTotal(total);
        pedido.setEstado("PENDIENTE");
        Pedido pedidoGuardado = pedidoRepository.save(pedido);

        // Publicar evento en RabbitMQ
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, "", pedidoGuardado);

        return pedidoGuardado;
    }

    // QUERY: Obtener historial por cliente
    public List<Pedido> obtenerPedidosPorCliente(String email) {
        return pedidoRepository.findByClienteEmail(email);
    }

    // QUERY: Obtener todos los pedidos
    public List<Pedido> obtenerTodos() {
        return pedidoRepository.findAll();
    }
}
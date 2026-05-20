package co.shopflow.tienda.controller;

import co.shopflow.tienda.model.Pedido;
import co.shopflow.tienda.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    // COMMAND: Crear pedido
    @PostMapping
    public ResponseEntity<Pedido> crearPedido(@RequestBody Pedido pedido) {
        return ResponseEntity.ok(pedidoService.crearPedido(pedido));
    }

    // QUERY: Historial por cliente
    @GetMapping("/cliente/{email}")
    public ResponseEntity<List<Pedido>> obtenerPorCliente(@PathVariable String email) {
        return ResponseEntity.ok(pedidoService.obtenerPedidosPorCliente(email));
    }

    // QUERY: Todos los pedidos
    @GetMapping
    public ResponseEntity<List<Pedido>> obtenerTodos() {
        return ResponseEntity.ok(pedidoService.obtenerTodos());
    }
}
package co.shopflow.tienda.controller;

import co.shopflow.tienda.model.Producto;
import co.shopflow.tienda.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    // COMMAND: Crear producto
    @PostMapping
    public ResponseEntity<Producto> crearProducto(@RequestBody Producto producto) {
        return ResponseEntity.ok(productoService.crearProducto(producto));
    }

    // QUERY: Obtener catalogo
    @GetMapping
    public ResponseEntity<List<Producto>> obtenerCatalogo() {
        return ResponseEntity.ok(productoService.obtenerCatalogo());
    }

    // QUERY: Obtener por ID
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.obtenerPorId(id));
    }
}
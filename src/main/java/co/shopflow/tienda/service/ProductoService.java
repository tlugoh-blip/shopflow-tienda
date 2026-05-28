package co.shopflow.tienda.service;

import co.shopflow.tienda.model.Producto;
import co.shopflow.tienda.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;

    // COMMAND: Crear producto
    public Producto crearProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    // COMMAND: Actualizar producto
    public Producto actualizarProducto(Long id, Producto datos) {
        Producto p = obtenerPorId(id);
        p.setNombre(datos.getNombre());
        p.setDescripcion(datos.getDescripcion());
        p.setPrecio(datos.getPrecio());
        p.setStock(datos.getStock());
        p.setImagen(datos.getImagen());
        return productoRepository.save(p);
    }

    // COMMAND: Eliminar producto
    public void eliminarProducto(Long id) {
        productoRepository.deleteById(id);
    }

    // QUERY: Obtener catalogo completo
    public List<Producto> obtenerCatalogo() {
        return productoRepository.findAll();
    }

    // QUERY: Obtener producto por ID
    public Producto obtenerPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }
}
package co.shopflow.tienda.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String clienteNombre;
    private String clienteEmail;
    private String direccionEntrega;
    private String estado; // PENDIENTE, PAGADO, ENVIADO

    private LocalDateTime fechaCreacion = LocalDateTime.now();
    private Double total;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "pedido_id")
    private List<DetallePedido> detalles;
}
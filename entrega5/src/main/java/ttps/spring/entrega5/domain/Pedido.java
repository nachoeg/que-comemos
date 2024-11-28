package ttps.spring.entrega5.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import java.util.Set;


@Entity
public class Pedido {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private Double monto;

    @Column(nullable = false)
    private String estado;

    @ManyToMany
    @JoinTable(
            name = "PedidoMenu",
            joinColumns = @JoinColumn(name = "pedidoId"),
            inverseJoinColumns = @JoinColumn(name = "menuId")
    )
    private Set<Menu> menus;

    @ManyToMany
    @JoinTable(
            name = "PedidoComida",
            joinColumns = @JoinColumn(name = "pedidoId"),
            inverseJoinColumns = @JoinColumn(name = "comidaId")
    )
    private Set<Comida> comidas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(final LocalDate fecha) {
        this.fecha = fecha;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(final Double monto) {
        this.monto = monto;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(final String estado) {
        this.estado = estado;
    }

    public Set<Menu> getMenus() {
        return menus;
    }

    public void setMenus(final Set<Menu> menus) {
        this.menus = menus;
    }

    public Set<Comida> getComidas() {
        return comidas;
    }

    public void setComidas(final Set<Comida> comidas) {
        this.comidas = comidas;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(final Usuario usuario) {
        this.usuario = usuario;
    }

}

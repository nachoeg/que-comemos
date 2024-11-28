package ttps.spring.entrega5.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;


public class PedidoDTO {

    private Long id;

    @NotNull
    private LocalDate fecha;

    @NotNull
    private Double monto;

    @NotNull
    @Size(max = 255)
    private String estado;

    private List<Long> menus;

    private List<Long> comidas;

    @NotNull
    private Integer usuario;

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

    public List<Long> getMenus() {
        return menus;
    }

    public void setMenus(final List<Long> menus) {
        this.menus = menus;
    }

    public List<Long> getComidas() {
        return comidas;
    }

    public void setComidas(final List<Long> comidas) {
        this.comidas = comidas;
    }

    public Integer getUsuario() {
        return usuario;
    }

    public void setUsuario(final Integer usuario) {
        this.usuario = usuario;
    }

}

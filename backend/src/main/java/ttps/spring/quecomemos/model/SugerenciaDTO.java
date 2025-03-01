package ttps.spring.quecomemos.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public class SugerenciaDTO {

    private Long id;

    @Size(max = 255)
    private String tipo;

    @Size(max = 255)
    private String descripcion;

    private LocalDate fecha;

    @NotNull
    private Integer usuario;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(final String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(final String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(final LocalDate fecha) {
        this.fecha = fecha;
    }

    public Integer getUsuario() {
        return usuario;
    }

    public void setUsuario(final Integer usuario) {
        this.usuario = usuario;
    }

}

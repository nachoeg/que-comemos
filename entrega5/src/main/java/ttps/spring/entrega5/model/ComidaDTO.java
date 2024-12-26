package ttps.spring.entrega5.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public class ComidaDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String nombre;

    @NotNull
    private Double precio;

    @Size(max = 255)
    private String foto;


    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(final String nombre) {
        this.nombre = nombre;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(final Double precio) {
        this.precio = precio;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(final String foto) {
        this.foto = foto;
    }


}

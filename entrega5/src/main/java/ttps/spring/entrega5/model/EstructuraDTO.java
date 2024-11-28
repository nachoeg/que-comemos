package ttps.spring.entrega5.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public class EstructuraDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String nombre;

    @NotNull
    private Long menu;

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

    public Long getMenu() {
        return menu;
    }

    public void setMenu(final Long menu) {
        this.menu = menu;
    }

}

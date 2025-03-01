package ttps.spring.quecomemos.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class EstructuraDTO {

    @NotNull
    @NotBlank
    @Size(max = 255)
    private String nombre;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(final String nombre) {
        this.nombre = nombre;
    }

}

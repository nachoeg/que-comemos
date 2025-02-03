package ttps.spring.entrega5.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import ttps.spring.entrega5.domain.DiaSemana;

public class MenuDTO {

    @NotNull
    @NotBlank
    @Size(max = 255)
    private String nombre;

    @NotNull
    @PositiveOrZero
    private Double precio;

    @NotNull
    private DiaSemana dia;

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

    public DiaSemana getDia() {
        return dia;
    }

    public void setDia(DiaSemana dia) {
        this.dia = dia;
    }

}

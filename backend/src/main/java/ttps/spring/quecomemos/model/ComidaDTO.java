package ttps.spring.quecomemos.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public class ComidaDTO {

  @NotBlank
  @NotNull
  @Size(max = 255)
  private String nombre;

  @NotNull
  @PositiveOrZero
  private Double precio;

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

}

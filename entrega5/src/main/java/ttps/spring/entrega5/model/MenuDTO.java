package ttps.spring.entrega5.model;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import ttps.spring.entrega5.model.EstructuraDTO;

public class MenuDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String nombre;

    @NotNull
    @PositiveOrZero
    private Double precio;
    
    private List<EstructuraDTO> estructuras;

    public List<EstructuraDTO> getEstructuras() {
		return estructuras;
	}

	public void setEstructuras(List<EstructuraDTO> estructuras) {
		this.estructuras = estructuras;
	}

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

}

package ttps.spring.entrega5.model;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ttps.spring.entrega5.model.ComidaDTO;


public class EstructuraDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String nombre;
    
    private List<ComidaDTO> comida;

    public List<ComidaDTO> getComida() {
		return comida;
	}

	public void setComida(List<ComidaDTO> comida) {
		this.comida = comida;
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
    
    



}

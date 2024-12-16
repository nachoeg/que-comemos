package ttps.spring.entrega5.model;

import java.util.List;

public class MenuConEstructurasDTO {
    private Long id;
    private String nombre;
    private Double precio;
    private List<EstructuraConComidasDTO> estructuras;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Double getPrecio() {
		return precio;
	}
	public void setPrecio(Double precio) {
		this.precio = precio;
	}
	public List<EstructuraConComidasDTO> getEstructuras() {
		return estructuras;
	}
	public void setEstructuras(List<EstructuraConComidasDTO> estructuras) {
		this.estructuras = estructuras;
	}

}

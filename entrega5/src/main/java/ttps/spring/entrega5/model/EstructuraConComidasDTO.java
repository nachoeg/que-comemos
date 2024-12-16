package ttps.spring.entrega5.model;

import java.util.List;

public class EstructuraConComidasDTO {
	 private Long id;
	 private String nombre;
	 private List<ComidaDTO> comidas;
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
	public List<ComidaDTO> getComidas() {
		return comidas;
	}
	public void setComidas(List<ComidaDTO> comidas) {
		this.comidas = comidas;
	}

}

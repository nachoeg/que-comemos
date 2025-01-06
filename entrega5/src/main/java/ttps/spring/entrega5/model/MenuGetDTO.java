package ttps.spring.entrega5.model;

import java.util.List;

import jakarta.validation.constraints.Size;

public class MenuGetDTO extends MenuDTO {

  private Long id;

  private List<EstructuraGetDTO> estructuras;

  @Size(max = 255)
  private String foto;

  public Long getId() {
    return id;
  }

  public void setId(final Long id) {
    this.id = id;
  }

  public List<EstructuraGetDTO> getEstructuras() {
    return estructuras;
  }

  public void setEstructuras(List<EstructuraGetDTO> estructuras) {
    this.estructuras = estructuras;
  }

  public String getFoto() {
    return foto;
  }

  public void setFoto(final String foto) {
    this.foto = foto;
  }

}

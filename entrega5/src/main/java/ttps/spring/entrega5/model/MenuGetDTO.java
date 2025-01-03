package ttps.spring.entrega5.model;

import java.util.List;

public class MenuGetDTO extends MenuDTO {

  private Long id;

  private List<EstructuraGetDTO> estructuras;

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

}

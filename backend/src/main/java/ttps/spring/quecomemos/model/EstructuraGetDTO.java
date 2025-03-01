package ttps.spring.quecomemos.model;

import java.util.List;

public class EstructuraGetDTO extends EstructuraDTO {

    private Long id;

    private List<ComidaGetDTO> comidas;

    public List<ComidaGetDTO> getComidas() {
        return comidas;
    }

    public void setComidas(List<ComidaGetDTO> comidas) {
        this.comidas = comidas;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

}

package ttps.spring.quecomemos.model;

import jakarta.validation.constraints.Size;

public class ComidaGetDTO extends ComidaDTO {

    private Long id;

    @Size(max = 255)
    private String foto;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(final String foto) {
        this.foto = foto;
    }

}

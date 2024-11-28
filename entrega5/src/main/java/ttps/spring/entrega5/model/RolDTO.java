package ttps.spring.entrega5.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.Size;


public class RolDTO {
	
    private Long id;

    @Size(max = 255)
    private String nombreRol;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(final String nombreRol) {
        this.nombreRol = nombreRol;
    }

}

package ttps.spring.entrega5.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public class UsuarioDTO {
    private Integer id;

    @UsuarioDniUnique
    private Integer dni;

    @Size(max = 255)
    private String nombre;

    @Size(max = 255)
    private String apellido;

    @Size(max = 255)
    private String email;

    @Size(max = 255)
    private String clave;

    @Size(max = 255)
    private String foto;

    @NotNull
    private Long rol;

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public Integer getDni() {
        return dni;
    }

    public void setDni(final Integer dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(final String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(final String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(final String clave) {
        this.clave = clave;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(final String foto) {
        this.foto = foto;
    }

    public Long getRol() {
        return rol;
    }

    public void setRol(final Long rol) {
        this.rol = rol;
    }

}

package ttps.spring.entrega5.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.Set;

@Entity
public class Usuario {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private Integer dni;

    @Column
    private String nombre;

    @Column
    private String apellido;

    @Column(unique = true)
    private String email;

    @Column
    private String clave;

    @Column
    private String foto;

    @OneToMany(mappedBy = "usuario")
    private Set<Sugerencia> sugerencias;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rol_id", nullable = false)
    private Rol rol;

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

    public Set<Sugerencia> getSugerencias() {
        return sugerencias;
    }

    public void setSugerencias(final Set<Sugerencia> sugerencias) {
        this.sugerencias = sugerencias;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(final Rol rol) {
        this.rol = rol;
    }

}

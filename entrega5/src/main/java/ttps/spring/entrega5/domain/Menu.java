package ttps.spring.entrega5.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Menu {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private Double precio;

    @Column
    private String foto;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Estructura> estructuras;

    public Menu() {
        estructuras = new ArrayList<>();
        Estructura entrada = new Estructura();
        Estructura principal = new Estructura();
        Estructura postre = new Estructura();
        Estructura bebida = new Estructura();
        entrada.setNombre("Entrada");
        principal.setNombre("Principal");
        postre.setNombre("Postre");
        bebida.setNombre("Bebida");
        estructuras.add(entrada);
        estructuras.add(principal);
        estructuras.add(postre);
        estructuras.add(bebida);
        entrada.setMenu(this);
        principal.setMenu(this);
        postre.setMenu(this);
        bebida.setMenu(this);
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

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(final Double precio) {
        if (precio < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        this.precio = precio;
    }

    public List<Estructura> getEstructuras() {
        return estructuras;
    }

    public void setEstructuras(List<Estructura> estructuras) {
        this.estructuras = estructuras;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(final String foto) {
        this.foto = foto;
    }

    public void addEstructura(Estructura estructura) {
        estructuras.add(estructura);
        estructura.setMenu(this);
    }

    public void removeEstructura(Estructura estructura) {
        estructuras.remove(estructura);
        estructura.setMenu(null);
    }

}

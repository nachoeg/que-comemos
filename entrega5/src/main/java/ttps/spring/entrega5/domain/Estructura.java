package ttps.spring.entrega5.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import ttps.spring.entrega5.repos.EstructuraRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.Cascade;


@Entity
public class Estructura {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @OneToMany(mappedBy = "estructura", cascade=CascadeType.ALL, orphanRemoval = true)
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    private Set<Comida> comida;

    @ManyToOne()
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

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

    public Set<Comida> getComida() {
        return comida;
    }

    public void setComida(final Set<Comida> comida) {
        this.comida = comida;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(final Menu menu) {
        this.menu = menu;
    }
	
	public List<Comida> getComidas() {
	    // Convertir el Set a una List y devolverla
	    return new ArrayList<>(comida);
	}

}

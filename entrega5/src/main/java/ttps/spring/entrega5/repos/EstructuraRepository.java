package ttps.spring.entrega5.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ttps.spring.entrega5.domain.Comida;
import ttps.spring.entrega5.domain.Estructura;

public interface EstructuraRepository extends JpaRepository<Estructura, Long> {

  List<Estructura> findAllByComidas(Comida comida);

}

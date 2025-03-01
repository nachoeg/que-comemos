package ttps.spring.quecomemos.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ttps.spring.quecomemos.domain.Comida;
import ttps.spring.quecomemos.domain.Estructura;

public interface EstructuraRepository extends JpaRepository<Estructura, Long> {

  List<Estructura> findAllByComidas(Comida comida);

}

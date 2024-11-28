package ttps.spring.entrega5.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ttps.spring.entrega5.domain.Estructura;
import ttps.spring.entrega5.domain.Menu;


public interface EstructuraRepository extends JpaRepository<Estructura, Long> {

    Estructura findFirstByMenu(Menu menu);

}

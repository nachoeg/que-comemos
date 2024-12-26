package ttps.spring.entrega5.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import ttps.spring.entrega5.domain.Comida;
import ttps.spring.entrega5.domain.Estructura;
import ttps.spring.entrega5.domain.Menu;


public interface EstructuraRepository extends JpaRepository<Estructura, Long> {


}

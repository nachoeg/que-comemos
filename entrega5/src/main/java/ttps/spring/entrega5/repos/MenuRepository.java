package ttps.spring.entrega5.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ttps.spring.entrega5.domain.DiaSemana;
import ttps.spring.entrega5.domain.Menu;


public interface MenuRepository extends JpaRepository<Menu, Long> {
	List<Menu> findByDia(DiaSemana diaSemana);
}

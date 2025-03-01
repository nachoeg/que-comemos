package ttps.spring.quecomemos.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ttps.spring.quecomemos.domain.DiaSemana;
import ttps.spring.quecomemos.domain.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long> {
	List<Menu> findByDia(DiaSemana diaSemana);
}

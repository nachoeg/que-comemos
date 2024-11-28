package ttps.spring.entrega5.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ttps.spring.entrega5.domain.Menu;


public interface MenuRepository extends JpaRepository<Menu, Long> {
}

package ttps.spring.quecomemos.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ttps.spring.quecomemos.domain.Rol;

public interface RolRepository extends JpaRepository<Rol, Long> {
}

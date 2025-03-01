package ttps.spring.quecomemos.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import ttps.spring.quecomemos.domain.Comida;

public interface ComidaRepository extends JpaRepository<Comida, Long> {
	boolean existsByNombre(String nombre);
}

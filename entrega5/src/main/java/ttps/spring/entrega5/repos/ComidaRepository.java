package ttps.spring.entrega5.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import ttps.spring.entrega5.domain.Comida;

public interface ComidaRepository extends JpaRepository<Comida, Long> {
	boolean existsByNombre(String nombre);
}

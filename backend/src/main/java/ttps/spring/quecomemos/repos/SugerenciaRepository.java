package ttps.spring.quecomemos.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ttps.spring.quecomemos.domain.Sugerencia;
import ttps.spring.quecomemos.domain.Usuario;

public interface SugerenciaRepository extends JpaRepository<Sugerencia, Long> {

    Sugerencia findFirstByUsuario(Usuario usuario);

}

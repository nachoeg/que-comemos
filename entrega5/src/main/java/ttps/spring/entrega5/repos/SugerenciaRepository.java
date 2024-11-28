package ttps.spring.entrega5.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ttps.spring.entrega5.domain.Sugerencia;
import ttps.spring.entrega5.domain.Usuario;


public interface SugerenciaRepository extends JpaRepository<Sugerencia, Long> {

    Sugerencia findFirstByUsuario(Usuario usuario);

}

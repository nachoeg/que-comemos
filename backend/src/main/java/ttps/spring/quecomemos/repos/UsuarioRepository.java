package ttps.spring.quecomemos.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ttps.spring.quecomemos.domain.Rol;
import ttps.spring.quecomemos.domain.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Usuario findFirstByRol(Rol rol);

    Usuario findByEmail(String email);

    boolean existsByDni(Integer dni);

    boolean existsByEmail(String email);

}

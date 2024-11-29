package ttps.spring.entrega5.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ttps.spring.entrega5.domain.Rol;
import ttps.spring.entrega5.domain.Usuario;


public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Usuario findFirstByRol(Rol rol);
    
    Usuario findByEmail(String email);

    boolean existsByDni(Integer dni);

}

package ttps.spring.entrega5.repos;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import ttps.spring.entrega5.domain.Comida;
import ttps.spring.entrega5.domain.Menu;
import ttps.spring.entrega5.domain.Pedido;
import ttps.spring.entrega5.domain.Usuario;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    Pedido findFirstByMenus(Menu menu);

    Pedido findFirstByComidas(Comida comida);

    Pedido findFirstByUsuario(Usuario usuario);

    List<Pedido> findAllByMenus(Menu menu);

    List<Pedido> findAllByComidas(Comida comida);

    List<Pedido> findAllByUsuario(Usuario usuario);

    List<Pedido> findByFechaBetween(LocalDate startDate, LocalDate today);

}

package ttps.spring.quecomemos.repos;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import ttps.spring.quecomemos.domain.Comida;
import ttps.spring.quecomemos.domain.Menu;
import ttps.spring.quecomemos.domain.Pedido;
import ttps.spring.quecomemos.domain.Usuario;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    Pedido findFirstByMenus(Menu menu);

    Pedido findFirstByComidas(Comida comida);

    Pedido findFirstByUsuario(Usuario usuario);

    List<Pedido> findAllByMenus(Menu menu);

    List<Pedido> findAllByComidas(Comida comida);

    List<Pedido> findAllByUsuario(Usuario usuario);

    List<Pedido> findByFechaBetween(LocalDate startDate, LocalDate today);

}

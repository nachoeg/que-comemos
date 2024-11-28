package ttps.spring.entrega5.service;

import jakarta.transaction.Transactional;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ttps.spring.entrega5.domain.Comida;
import ttps.spring.entrega5.domain.Menu;
import ttps.spring.entrega5.domain.Pedido;
import ttps.spring.entrega5.domain.Usuario;
import ttps.spring.entrega5.model.PedidoDTO;
import ttps.spring.entrega5.repos.ComidaRepository;
import ttps.spring.entrega5.repos.MenuRepository;
import ttps.spring.entrega5.repos.PedidoRepository;
import ttps.spring.entrega5.repos.UsuarioRepository;
import ttps.spring.entrega5.util.NotFoundException;


@Service
@Transactional
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final MenuRepository menuRepository;
    private final ComidaRepository comidaRepository;
    private final UsuarioRepository usuarioRepository;

    public PedidoService(final PedidoRepository pedidoRepository,
            final MenuRepository menuRepository, final ComidaRepository comidaRepository,
            final UsuarioRepository usuarioRepository) {
        this.pedidoRepository = pedidoRepository;
        this.menuRepository = menuRepository;
        this.comidaRepository = comidaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<PedidoDTO> findAll() {
        final List<Pedido> pedidoes = pedidoRepository.findAll(Sort.by("id"));
        return pedidoes.stream()
                .map(pedido -> mapToDTO(pedido, new PedidoDTO()))
                .toList();
    }

    public PedidoDTO get(final Long id) {
        return pedidoRepository.findById(id)
                .map(pedido -> mapToDTO(pedido, new PedidoDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final PedidoDTO pedidoDTO) {
        final Pedido pedido = new Pedido();
        mapToEntity(pedidoDTO, pedido);
        return pedidoRepository.save(pedido).getId();
    }

    public void update(final Long id, final PedidoDTO pedidoDTO) {
        final Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(pedidoDTO, pedido);
        pedidoRepository.save(pedido);
    }

    public void delete(final Long id) {
        pedidoRepository.deleteById(id);
    }

    private PedidoDTO mapToDTO(final Pedido pedido, final PedidoDTO pedidoDTO) {
        pedidoDTO.setId(pedido.getId());
        pedidoDTO.setFecha(pedido.getFecha());
        pedidoDTO.setMonto(pedido.getMonto());
        pedidoDTO.setEstado(pedido.getEstado());
        pedidoDTO.setMenus(pedido.getMenus().stream()
                .map(menu -> menu.getId())
                .toList());
        pedidoDTO.setComidas(pedido.getComidas().stream()
                .map(comida -> comida.getId())
                .toList());
        pedidoDTO.setUsuario(pedido.getUsuario() == null ? null : pedido.getUsuario().getId());
        return pedidoDTO;
    }

    private Pedido mapToEntity(final PedidoDTO pedidoDTO, final Pedido pedido) {
        pedido.setFecha(pedidoDTO.getFecha());
        pedido.setMonto(pedidoDTO.getMonto());
        pedido.setEstado(pedidoDTO.getEstado());
        final List<Menu> menus = menuRepository.findAllById(
                pedidoDTO.getMenus() == null ? Collections.emptyList() : pedidoDTO.getMenus());
        if (menus.size() != (pedidoDTO.getMenus() == null ? 0 : pedidoDTO.getMenus().size())) {
            throw new NotFoundException("one of menus not found");
        }
        pedido.setMenus(new HashSet<>(menus));
        final List<Comida> comidas = comidaRepository.findAllById(
                pedidoDTO.getComidas() == null ? Collections.emptyList() : pedidoDTO.getComidas());
        if (comidas.size() != (pedidoDTO.getComidas() == null ? 0 : pedidoDTO.getComidas().size())) {
            throw new NotFoundException("one of comidas not found");
        }
        pedido.setComidas(new HashSet<>(comidas));
        final Usuario usuario = pedidoDTO.getUsuario() == null ? null : usuarioRepository.findById(pedidoDTO.getUsuario())
                .orElseThrow(() -> new NotFoundException("usuario not found"));
        pedido.setUsuario(usuario);
        return pedido;
    }

}

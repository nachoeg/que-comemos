package ttps.spring.entrega5.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.google.zxing.WriterException;

import ttps.spring.entrega5.domain.Comida;
import ttps.spring.entrega5.domain.Menu;
import ttps.spring.entrega5.domain.Pedido;
import ttps.spring.entrega5.domain.Usuario;
import ttps.spring.entrega5.model.ComidaPedidoDTO;
import ttps.spring.entrega5.model.MenuPedidoDTO;
import ttps.spring.entrega5.model.PedidoDTO;
import ttps.spring.entrega5.repos.ComidaRepository;
import ttps.spring.entrega5.repos.MenuRepository;
import ttps.spring.entrega5.repos.PedidoRepository;
import ttps.spring.entrega5.repos.UsuarioRepository;
import ttps.spring.entrega5.util.NotFoundException;
import ttps.spring.entrega5.util.QRService;

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

    public PedidoDTO create(final PedidoDTO pedidoDTO) {
        Pedido pedido = new Pedido();
        mapToEntity(pedidoDTO, pedido);
        double montoRecalculado = calcularMonto(pedidoDTO.getMenus(), pedidoDTO.getComidas());
        pedido.setMonto(montoRecalculado);

        pedido = pedidoRepository.save(pedido); // Guarda el pedido y obtén la entidad persistida con el ID

        PedidoDTO pedidoDTOConId = new PedidoDTO(); // Crea un PedidoDTO vacío
        return mapToDTO(pedido, pedidoDTOConId);
    }

    public void update(final Long id, final PedidoDTO pedidoDTO) {
        final Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(pedidoDTO, pedido);
        double montoRecalculado = calcularMonto(pedidoDTO.getMenus(), pedidoDTO.getComidas());
        pedido.setMonto(montoRecalculado);
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
        pedidoDTO.setMenus(pedido.getMenus().stream().map(menu -> {
            MenuPedidoDTO menuPedidoDTO = new MenuPedidoDTO();
            menuPedidoDTO.setId(menu.getId());
            menuPedidoDTO.setCantidad(1);
            menuPedidoDTO.setNombre(menu.getNombre());
            menuPedidoDTO.setPrecio(menu.getPrecio());
            return menuPedidoDTO;
        }).toList());
        pedidoDTO.setComidas(pedido.getComidas().stream().map(comida -> {
            ComidaPedidoDTO comidaPedidoDTO = new ComidaPedidoDTO();
            comidaPedidoDTO.setId(comida.getId());
            comidaPedidoDTO.setCantidad(1);
            comidaPedidoDTO.setNombre(comida.getNombre());
            comidaPedidoDTO.setPrecio(comida.getPrecio());
            return comidaPedidoDTO;
        }).toList());
        pedidoDTO.setUsuario(pedido.getUsuario() == null ? null : pedido.getUsuario().getId());
        return pedidoDTO;
    }

    private Pedido mapToEntity(final PedidoDTO pedidoDTO, final Pedido pedido) {
        pedido.setFecha(pedidoDTO.getFecha());
        pedido.setMonto(pedidoDTO.getMonto());
        pedido.setEstado(pedidoDTO.getEstado());
        Set<Menu> menus = new HashSet<>();
        if (pedidoDTO.getMenus() != null) {
            for (MenuPedidoDTO menuPedidoDTO : pedidoDTO.getMenus()) {
                Menu menu = menuRepository.findById(menuPedidoDTO.getId())
                        .orElseThrow(() -> new NotFoundException("Menu not found"));
                menus.add(menu);
            }
        }
        pedido.setMenus(menus);

        Set<Comida> comidas = new HashSet<>();
        if (pedidoDTO.getComidas() != null) {
            for (ComidaPedidoDTO comidaPedidoDTO : pedidoDTO.getComidas()) {
                Comida comida = comidaRepository.findById(comidaPedidoDTO.getId())
                        .orElseThrow(() -> new NotFoundException("Comida not found"));
                comidas.add(comida);
            }
        }
        pedido.setComidas(comidas);

        final Usuario usuario = pedidoDTO.getUsuario() == null ? null
                : usuarioRepository.findById(pedidoDTO.getUsuario())
                        .orElseThrow(() -> new NotFoundException("usuario not found"));
        pedido.setUsuario(usuario);
        return pedido;
    }

    private double calcularMonto(List<MenuPedidoDTO> menus, List<ComidaPedidoDTO> comidas) {
        double total = 0;

        if (menus != null) {
            for (MenuPedidoDTO menu : menus) {
                Optional<Menu> menuEntity = menuRepository.findById(menu.getId());
                if (menuEntity.isPresent()) {
                    total += menuEntity.get().getPrecio() * menu.getCantidad();
                } else {
                    throw new NotFoundException("Menu not found");
                }
            }
        }

        if (comidas != null) {
            for (ComidaPedidoDTO comida : comidas) {
                Optional<Comida> comidaEntity = comidaRepository.findById(comida.getId());
                if (comidaEntity.isPresent()) {
                    total += comidaEntity.get().getPrecio() * comida.getCantidad();
                } else {
                    throw new NotFoundException("Comida not found");
                }
            }
        }

        return total;
    }

    public void updateEstado(final Long id, final String estado) {
        final Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        pedido.setEstado(estado);
        pedidoRepository.save(pedido);
    }

}

package ttps.spring.entrega5.service;

import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ttps.spring.entrega5.domain.Estructura;
import ttps.spring.entrega5.domain.Menu;
import ttps.spring.entrega5.model.MenuDTO;
import ttps.spring.entrega5.repos.EstructuraRepository;
import ttps.spring.entrega5.repos.MenuRepository;
import ttps.spring.entrega5.repos.PedidoRepository;
import ttps.spring.entrega5.util.NotFoundException;
import ttps.spring.entrega5.util.ReferencedWarning;


@Service
@Transactional
public class MenuService {

    private final MenuRepository menuRepository;
    private final PedidoRepository pedidoRepository;
    private final EstructuraRepository estructuraRepository;

    public MenuService(final MenuRepository menuRepository, final PedidoRepository pedidoRepository,
            final EstructuraRepository estructuraRepository) {
        this.menuRepository = menuRepository;
        this.pedidoRepository = pedidoRepository;
        this.estructuraRepository = estructuraRepository;
    }

    public List<MenuDTO> findAll() {
        final List<Menu> menus = menuRepository.findAll(Sort.by("id"));
        return menus.stream()
                .map(menu -> mapToDTO(menu, new MenuDTO()))
                .toList();
    }

    public MenuDTO get(final Long id) {
        return menuRepository.findById(id)
                .map(menu -> mapToDTO(menu, new MenuDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final MenuDTO menuDTO) {
        final Menu menu = new Menu();
        mapToEntity(menuDTO, menu);
        return menuRepository.save(menu).getId();
    }

    public void update(final Long id, final MenuDTO menuDTO) {
        final Menu menu = menuRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(menuDTO, menu);
        menuRepository.save(menu);
    }

    public void delete(final Long id) {
        final Menu menu = menuRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        // remove many-to-many relations at owning side
        pedidoRepository.findAllByMenus(menu)
                .forEach(pedido -> pedido.getMenus().remove(menu));
        menuRepository.delete(menu);
    }

    private MenuDTO mapToDTO(final Menu menu, final MenuDTO menuDTO) {
        menuDTO.setId(menu.getId());
        menuDTO.setNombre(menu.getNombre());
        menuDTO.setPrecio(menu.getPrecio());
        return menuDTO;
    }

    private Menu mapToEntity(final MenuDTO menuDTO, final Menu menu) {
        menu.setNombre(menuDTO.getNombre());
        menu.setPrecio(menuDTO.getPrecio());
        return menu;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Menu menu = menuRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Estructura menuEstructura = estructuraRepository.findFirstByMenu(menu);
        if (menuEstructura != null) {
            referencedWarning.setKey("menu.estructura.menu.referenced");
            referencedWarning.addParam(menuEstructura.getId());
            return referencedWarning;
        }
        return null;
    }

}

package ttps.spring.entrega5.service;

import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import ttps.spring.entrega5.domain.Comida;
import ttps.spring.entrega5.domain.Estructura;
import ttps.spring.entrega5.domain.Menu;
import ttps.spring.entrega5.model.ComidaDTO;
import ttps.spring.entrega5.model.EstructuraConComidasDTO;
import ttps.spring.entrega5.model.EstructuraDTO;
import ttps.spring.entrega5.model.MenuConEstructurasDTO;
import ttps.spring.entrega5.model.MenuDTO;
import ttps.spring.entrega5.repos.EstructuraRepository;
import ttps.spring.entrega5.repos.MenuRepository;
import ttps.spring.entrega5.repos.PedidoRepository;
import ttps.spring.entrega5.repos.ComidaRepository;
import ttps.spring.entrega5.util.NotFoundException;
import ttps.spring.entrega5.util.ReferencedWarning;

@Service
@Transactional
public class MenuService {
	@Autowired
	private final EstructuraService estructuraService;
	@Autowired
	private final ComidaService comidaService;
	
	private final MenuRepository menuRepository;
	private final PedidoRepository pedidoRepository;
	private final EstructuraRepository estructuraRepository;

	

	public MenuService(final MenuRepository menuRepository, final PedidoRepository pedidoRepository,
			final EstructuraRepository estructuraRepository) {
		this.estructuraService = null;
		this.comidaService = null;
		this.menuRepository = menuRepository;
		this.pedidoRepository = pedidoRepository;
		this.estructuraRepository = estructuraRepository;
	}

	
	  public List<MenuDTO> findAll() { final List<Menu> menus =
	  menuRepository.findAll(Sort.by("id")); return menus.stream() .map(menu ->
	  mapToDTO(menu, new MenuDTO())) .toList(); 
	  
	  }
	 

	  public Optional<MenuDTO> get(final Long id) {
		    return menuRepository.findById(id)
		        .map(menu -> {
		            MenuDTO menuDTO = new MenuDTO();
		            menuDTO.setId(menu.getId());
		            menuDTO.setNombre(menu.getNombre());
		            menuDTO.setPrecio(menu.getPrecio());

		         // Create a new list to avoid modification of the original entity's structures
		            List<EstructuraDTO> estructurasDto = new ArrayList<>();
		            for (Estructura estructura : menu.getEstructuras()) {
		                EstructuraDTO estructuraDTO = new EstructuraDTO();
		                estructuraDTO.setId(estructura.getId());
		                estructuraDTO.setNombre(estructura.getNombre());
		             // Map Comidas
		    	        if (estructura.getComida() != null) {
		    	            List<ComidaDTO> comidasDto = new ArrayList<>();
		    	            for (Comida comida : estructura.getComida()) {
		    	                ComidaDTO comidaDTO = new ComidaDTO();
		    	                comidaDTO.setId(comida.getId());
		    	                comidaDTO.setNombre(comida.getNombre());
		    	                comidaDTO.setPrecio(comida.getPrecio());
		    	                // Map other attributes of Comida to ComidaDTO if needed
		    	                comidasDto.add(comidaDTO);
		    	            }
		    	            estructuraDTO.setComida(comidasDto);
		    	        }

		                estructurasDto.add(estructuraDTO);
		            }
		            menuDTO.setEstructuras(estructurasDto);
		            return menuDTO;
		        });
		}

	public Long create(final MenuDTO menuDTO) {
		final Menu menu = new Menu();
		mapToEntity(menuDTO, menu);
		return menuRepository.save(menu).getId();
	}

	public void update(final Long id, final MenuDTO menuDTO) {
		final Menu menu = menuRepository.findById(id).orElseThrow(NotFoundException::new);
		mapToEntity(menuDTO, menu);
		menuRepository.save(menu);
	}

	public void delete(final Long id) {
		final Menu menu = menuRepository.findById(id).orElseThrow(NotFoundException::new);
		menu.getEstructuras().size();
		// remove many-to-many relations at owning side
		pedidoRepository.findAllByMenus(menu).forEach(pedido -> pedido.getMenus().remove(menu));
		menuRepository.delete(menu);
	}

	private MenuDTO mapToDTO(final Menu menu, final MenuDTO menuDTO) {
		menuDTO.setId(menu.getId());
		menuDTO.setNombre(menu.getNombre());
		menuDTO.setPrecio(menu.getPrecio());
		// Create a new list to avoid modification of the original entity's structures
	    List<EstructuraDTO> estructurasDto = new ArrayList<>();
	    for (Estructura estructura : menu.getEstructuras()) {
	        EstructuraDTO estructuraDTO = new EstructuraDTO();
	        estructuraDTO.setId(estructura.getId());
	        estructuraDTO.setNombre(estructura.getNombre());
	     // Map Comidas
	        if (estructura.getComida() != null) {
	            List<ComidaDTO> comidasDto = new ArrayList<>();
	            for (Comida comida : estructura.getComida()) {
	                ComidaDTO comidaDTO = new ComidaDTO();
	                comidaDTO.setId(comida.getId());
	                comidaDTO.setNombre(comida.getNombre());
	                comidaDTO.setPrecio(comida.getPrecio());
	                // Map other attributes of Comida to ComidaDTO if needed
	                comidasDto.add(comidaDTO);
	            }
	            estructuraDTO.setComida(comidasDto);
	        }

	        estructurasDto.add(estructuraDTO);
	    }
	    menuDTO.setEstructuras(estructurasDto);

		return menuDTO;
	}

	private Menu mapToEntity(final MenuDTO menuDTO, final Menu menu) {
	    menu.setNombre(menuDTO.getNombre());
	    menu.setPrecio(menuDTO.getPrecio());

	    // Mapeo de Estructuras
	    if (menuDTO.getEstructuras() != null) {
	        List<Estructura> estructuras = new ArrayList<>();
	        for (EstructuraDTO estructuraDTO : menuDTO.getEstructuras()) {
	            Estructura estructura = new Estructura();
	            estructuraService.mapToEntity(estructuraDTO, estructura);

	            // Mapeo de Comidas
	            if (estructuraDTO.getComida() != null) {
	                List<Comida> comidas = new ArrayList<>();
	                for (ComidaDTO comidaDTO : estructuraDTO.getComida()) {
	                    Comida comida = new Comida();
	                    comidaService.mapToEntity(comidaDTO, comida);
	                    comidas.add(comida);
	                }
	                estructura.setComida(comidas);
	            }

	            estructuras.add(estructura);
	        }
	        menu.setEstructuras(estructuras);
	    }

	    return menu;
	}


	public ReferencedWarning getReferencedWarning(final Long id) {
		final ReferencedWarning referencedWarning = new ReferencedWarning();
		final Menu menu = menuRepository.findById(id).orElseThrow(NotFoundException::new);
		/*
		 * final Estructura menuEstructura = estructuraRepository.findFirstByMenu(menu);
		 * if (menuEstructura != null) {
		 * referencedWarning.setKey("menu.estructura.menu.referenced");
		 * referencedWarning.addParam(menuEstructura.getId()); return referencedWarning;
		 * }
		 */
		return null;
	}
	/*
	 * public List<MenuConEstructurasDTO> findAllWithEstructurasYComidas() { final
	 * List<Menu> menus = menuRepository.findAll(Sort.by("id"));
	 * 
	 * return menus.stream() .map(menu -> { MenuConEstructurasDTO menuDTO = new
	 * MenuConEstructurasDTO(); menuDTO.setId(menu.getId());
	 * menuDTO.setNombre(menu.getNombre()); menuDTO.setPrecio(menu.getPrecio());
	 * 
	 * 
	 * // Genera lista de esctructuras de un menu List<EstructuraConComidasDTO>
	 * estructurasDTO = estructuraService.findAllByMenu(menu.getId());
	 * 
	 * // por cada estructura, genera la lista de comidas asociada if
	 * (!estructurasDTO.isEmpty()) { for (EstructuraConComidasDTO estructuraDTO :
	 * estructurasDTO) { List<ComidaDTO> comidasDTO =
	 * comidaService.findAllByEstructura(estructuraDTO.getId());
	 * estructuraDTO.setComidas(comidasDTO); } }
	 * 
	 * menuDTO.setEstructuras(estructurasDTO); return menuDTO; })
	 * .collect(Collectors.toList()); }
	 */
}

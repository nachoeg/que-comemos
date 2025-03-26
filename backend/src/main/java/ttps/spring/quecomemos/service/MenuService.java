package ttps.spring.quecomemos.service;

import jakarta.transaction.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ttps.spring.quecomemos.domain.Comida;
import ttps.spring.quecomemos.domain.DiaSemana;
import ttps.spring.quecomemos.domain.Estructura;
import ttps.spring.quecomemos.domain.Menu;
import ttps.spring.quecomemos.model.ComidaGetDTO;
import ttps.spring.quecomemos.model.EstructuraGetDTO;
import ttps.spring.quecomemos.model.MenuDTO;
import ttps.spring.quecomemos.model.MenuGetDTO;
import ttps.spring.quecomemos.repos.MenuRepository;
import ttps.spring.quecomemos.repos.PedidoRepository;
import ttps.spring.quecomemos.util.ImgurUploader;
import ttps.spring.quecomemos.util.ReferencedWarning;

@Service
@Transactional
public class MenuService {

	private final MenuRepository menuRepository;
	private final PedidoRepository pedidoRepository;
	private final ImgurUploader imgurUploader;

	public MenuService(final MenuRepository menuRepository, final PedidoRepository pedidoRepository,
			ImgurUploader imgurUploader) {
		this.menuRepository = menuRepository;
		this.pedidoRepository = pedidoRepository;
		this.imgurUploader = imgurUploader;
	}

	public List<MenuGetDTO> findAll() {
		final List<Menu> menus = menuRepository.findAll(Sort.by("id"));
		return menus.stream().map(menu -> mapToDTO(menu, new MenuGetDTO())).toList();

	}

	public Optional<MenuGetDTO> get(final Long id) {
		return menuRepository.findById(id)
				.map(menu -> {
					MenuGetDTO menuDTO = new MenuGetDTO();
					menuDTO.setId(menu.getId());
					menuDTO.setNombre(menu.getNombre());
					menuDTO.setPrecio(menu.getPrecio());
					menuDTO.setDia(menu.getDia());

					// Create a new list to avoid modification of the original entity's structures
					List<EstructuraGetDTO> estructurasDto = new ArrayList<>();
					for (Estructura estructura : menu.getEstructuras()) {
						EstructuraGetDTO estructuraDTO = new EstructuraGetDTO();
						estructuraDTO.setId(estructura.getId());
						estructuraDTO.setNombre(estructura.getNombre());
						// Map Comidas
						if (estructura.getComidas() != null) {
							List<ComidaGetDTO> comidasDto = new ArrayList<>();
							for (Comida comida : estructura.getComidas()) {
								ComidaGetDTO comidaDTO = new ComidaGetDTO();
								comidaDTO.setId(comida.getId());
								comidaDTO.setNombre(comida.getNombre());
								comidaDTO.setPrecio(comida.getPrecio());
								comidaDTO.setFoto(comida.getFoto());
								comidasDto.add(comidaDTO);
							}
							estructuraDTO.setComidas(comidasDto);
						}

						estructurasDto.add(estructuraDTO);
					}
					menuDTO.setEstructuras(estructurasDto);
					return menuDTO;
				});
	}

	public Long create(final MenuDTO menuDTO, MultipartFile foto) throws IOException {
		final Menu menu = new Menu();
		mapToEntity(menuDTO, menu);
		if (foto != null && !foto.isEmpty()) {
			String fotoUrl = imgurUploader.upload(foto);
			menu.setFoto(fotoUrl);
		}
		return menuRepository.save(menu).getId();
	}

	public void update(final Long id, final MenuDTO menuDTO, MultipartFile foto) throws IOException {
		final Menu menu = menuRepository.findById(id)
				.orElseThrow(() -> new EmptyResultDataAccessException("Menu no encontrado", 1));
		mapToEntity(menuDTO, menu);
		if (foto != null && !foto.isEmpty()) {
			String fotoUrl = imgurUploader.upload(foto);
			menu.setFoto(fotoUrl);
		}
		menuRepository.save(menu);
	}

	public void delete(final Long id) {
		final Menu menu = menuRepository.findById(id)
				.orElseThrow(() -> new EmptyResultDataAccessException("Menu no encontrado", 1));
		menu.getEstructuras().size();
		// remove many-to-many relations at owning side
		pedidoRepository.findAllByMenus(menu).forEach(pedido -> pedido.getMenus().remove(menu));
		menuRepository.delete(menu);
	}

	private MenuGetDTO mapToDTO(final Menu menu, final MenuGetDTO menuDTO) {
		menuDTO.setId(menu.getId());
		menuDTO.setNombre(menu.getNombre());
		menuDTO.setPrecio(menu.getPrecio());
		menuDTO.setFoto(menu.getFoto());
		menuDTO.setDia(menu.getDia());
		// Create a new list to avoid modification of the original entity's structures
		List<EstructuraGetDTO> estructurasDto = new ArrayList<>();
		for (Estructura estructura : menu.getEstructuras()) {
			EstructuraGetDTO estructuraDTO = new EstructuraGetDTO();
			estructuraDTO.setId(estructura.getId());
			estructuraDTO.setNombre(estructura.getNombre());
			if (estructura.getComidas() != null) {
				List<ComidaGetDTO> comidasDto = new ArrayList<>();
				for (Comida comida : estructura.getComidas()) {
					ComidaGetDTO comidaDTO = new ComidaGetDTO();
					comidaDTO.setId(comida.getId());
					comidaDTO.setNombre(comida.getNombre());
					comidaDTO.setPrecio(comida.getPrecio());
					comidasDto.add(comidaDTO);
				}
				estructuraDTO.setComidas(comidasDto);
			}

			estructurasDto.add(estructuraDTO);
		}
		menuDTO.setEstructuras(estructurasDto);

		return menuDTO;
	}

	private Menu mapToEntity(final MenuDTO menuDTO, final Menu menu) {
		menu.setNombre(menuDTO.getNombre());
		menu.setPrecio(menuDTO.getPrecio());
		menu.setDia(menuDTO.getDia());
		return menu;
	}

	public ReferencedWarning getReferencedWarning(final Long id) {
		menuRepository.findById(id)
				.orElseThrow(() -> new EmptyResultDataAccessException("Menu no encontrado", 1));

		return null;
	}

	public List<MenuGetDTO> findByDia(final String dia) {
		DiaSemana diaSemana = DiaSemana.fromString(dia); // Convierte String a DiaSemana
		return menuRepository.findByDia(diaSemana).stream()
				.map(menu -> mapToDTO(menu, new MenuGetDTO()))
				.collect(Collectors.toList());
	}

	public List<MenuGetDTO> findAllActive() { // For regular users - all active menus
		return menuRepository.findAll().stream()
				.filter(menu -> menu.getDia() != DiaSemana.Desactivado)
				.map(menu -> mapToDTO(menu, new MenuGetDTO()))
				.collect(Collectors.toList());
	}

}

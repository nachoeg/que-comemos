package ttps.spring.quecomemos.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ttps.spring.quecomemos.domain.Comida;
import ttps.spring.quecomemos.domain.Estructura;
import ttps.spring.quecomemos.domain.Menu;
import ttps.spring.quecomemos.model.EstructuraDTO;
import ttps.spring.quecomemos.model.EstructuraGetDTO;
import ttps.spring.quecomemos.model.ComidaGetDTO;
import ttps.spring.quecomemos.repos.ComidaRepository;
import ttps.spring.quecomemos.repos.EstructuraRepository;
import ttps.spring.quecomemos.repos.MenuRepository;

@Transactional
@Service
public class EstructuraService {

	private final EstructuraRepository estructuraRepository;
	private final ComidaRepository comidaRepository;
	private final MenuRepository menuRepository;

	public EstructuraService(final EstructuraRepository estructuraRepository, final ComidaRepository comidaRepository,
			final MenuRepository menuRepository) {
		this.estructuraRepository = estructuraRepository;
		this.comidaRepository = comidaRepository;
		this.menuRepository = menuRepository;
	}

	public List<EstructuraGetDTO> findAll() {
		final List<Estructura> estructuras = estructuraRepository.findAll(Sort.by("id"));
		return estructuras.stream().map(estructura -> mapToDTO(estructura, new EstructuraGetDTO())).toList();
	}

	public Optional<EstructuraGetDTO> get(final Long id) {
		return estructuraRepository.findById(id).map(estructura -> mapToDTO(estructura, new EstructuraGetDTO()));
	}

	public Long create(final Long idMenu, final EstructuraDTO estructuraCrearDTO) {
		final Menu menu = menuRepository.findById(idMenu)
				.orElseThrow(() -> new EmptyResultDataAccessException("Menu no encontrado", 1));
		final Estructura estructura = new Estructura();
		mapToEntity(estructuraCrearDTO, estructura);
		menu.addEstructura(estructura); // Agrega la estructura al menú
		menuRepository.save(menu); // Guarda el menú actualizado
		return estructura.getId(); // Devuelve el id de la estructura creada
	}

	public void update(final Long id, final EstructuraGetDTO estructuraDTO) {
		final Estructura estructura = estructuraRepository.findById(id)
				.orElseThrow(() -> new EmptyResultDataAccessException("Estructura no encontrada", 1));
		mapToEntity(estructuraDTO, estructura);
		estructuraRepository.save(estructura);
	}

	public void delete(final Long id) {
		final Estructura estructura = estructuraRepository.findById(id)
				.orElseThrow(() -> new EmptyResultDataAccessException("Estructura no encontrada", 1));
		estructuraRepository.delete(estructura);
	}

	public void addComida(final Long id, final Long comidaId) {
		final Estructura estructura = estructuraRepository.findById(id)
				.orElseThrow(() -> new EmptyResultDataAccessException("Estructura no encontrada", 1));
		final Comida comida = comidaRepository.findById(comidaId)
				.orElseThrow(() -> new EmptyResultDataAccessException("Comida no encontrada", 1));
		estructura.getComidas().add(comida);
		estructuraRepository.save(estructura);
	}

	public void removeComida(final Long id, final Long comidaId) {
		final Estructura estructura = estructuraRepository.findById(id)
				.orElseThrow(() -> new EmptyResultDataAccessException("Estructura no encontrada", 1));
		final Comida comida = comidaRepository.findById(comidaId)
				.orElseThrow(() -> new EmptyResultDataAccessException("Comida no encontrada", 1));
		estructura.getComidas().remove(comida);
		estructuraRepository.save(estructura);
	}

	private EstructuraGetDTO mapToDTO(final Estructura estructura, final EstructuraGetDTO estructuraDTO) {
		estructuraDTO.setId(estructura.getId());
		estructuraDTO.setNombre(estructura.getNombre());
		estructuraDTO.setComidas(estructura.getComidas().stream().map(comida -> {
			final ComidaGetDTO comidaDTO = new ComidaGetDTO();
			comidaDTO.setId(comida.getId());
			comidaDTO.setNombre(comida.getNombre());
			comidaDTO.setPrecio(comida.getPrecio());
			return comidaDTO;
		}).collect(Collectors.toList()));
		return estructuraDTO;
	}

	private Estructura mapToEntity(final EstructuraGetDTO estructuraDTO, final Estructura estructura) {
		estructura.setNombre(estructuraDTO.getNombre());
		estructura.setComidas(estructuraDTO.getComidas().stream().map(comidaDTO -> {
			final Comida comida = new Comida();
			comida.setNombre(comidaDTO.getNombre());
			comida.setPrecio(comidaDTO.getPrecio());
			return comida;
		}).collect(Collectors.toList()));
		return estructura;
	}

	private Estructura mapToEntity(final EstructuraDTO estructuraDTO, final Estructura estructura) {
		estructura.setNombre(estructuraDTO.getNombre());
		return estructura;
	}

}

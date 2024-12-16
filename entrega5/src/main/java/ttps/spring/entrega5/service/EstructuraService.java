package ttps.spring.entrega5.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ttps.spring.entrega5.domain.Comida;
import ttps.spring.entrega5.domain.Estructura;
import ttps.spring.entrega5.domain.Menu;
import ttps.spring.entrega5.model.EstructuraConComidasDTO;
import ttps.spring.entrega5.model.EstructuraDTO;
import ttps.spring.entrega5.repos.ComidaRepository;
import ttps.spring.entrega5.repos.EstructuraRepository;
import ttps.spring.entrega5.repos.MenuRepository;
import ttps.spring.entrega5.util.NotFoundException;
import ttps.spring.entrega5.util.ReferencedWarning;


@Service
public class EstructuraService {

    private final EstructuraRepository estructuraRepository;
    private final MenuRepository menuRepository;
    private final ComidaRepository comidaRepository;

    public EstructuraService(final EstructuraRepository estructuraRepository,
            final MenuRepository menuRepository, final ComidaRepository comidaRepository) {
        this.estructuraRepository = estructuraRepository;
        this.menuRepository = menuRepository;
        this.comidaRepository = comidaRepository;
    }

    public List<EstructuraDTO> findAll() {
        final List<Estructura> estructuras = estructuraRepository.findAll(Sort.by("id"));
        return estructuras.stream()
                .map(estructura -> mapToDTO(estructura, new EstructuraDTO()))
                .toList();
    }

    public EstructuraDTO get(final Long id) {
        return estructuraRepository.findById(id)
                .map(estructura -> mapToDTO(estructura, new EstructuraDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final EstructuraDTO estructuraDTO) {
        final Estructura estructura = new Estructura();
        mapToEntity(estructuraDTO, estructura);
        return estructuraRepository.save(estructura).getId();
    }

    public void update(final Long id, final EstructuraDTO estructuraDTO) {
        final Estructura estructura = estructuraRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(estructuraDTO, estructura);
        estructuraRepository.save(estructura);
    }

    public void delete(final Long id) {
        estructuraRepository.deleteById(id);
    }

    private EstructuraDTO mapToDTO(final Estructura estructura, final EstructuraDTO estructuraDTO) {
        estructuraDTO.setId(estructura.getId());
        estructuraDTO.setNombre(estructura.getNombre());
        estructuraDTO.setMenu(estructura.getMenu() == null ? null : estructura.getMenu().getId());
        return estructuraDTO;
    }

    private Estructura mapToEntity(final EstructuraDTO estructuraDTO, final Estructura estructura) {
        estructura.setNombre(estructuraDTO.getNombre());
        final Menu menu = estructuraDTO.getMenu() == null ? null : menuRepository.findById(estructuraDTO.getMenu())
                .orElseThrow(() -> new NotFoundException("menu not found"));
        estructura.setMenu(menu);
        return estructura;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Estructura estructura = estructuraRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Comida estructuraComida = comidaRepository.findFirstByEstructura(estructura);
        if (estructuraComida != null) {
            referencedWarning.setKey("estructura.comida.estructura.referenced");
            referencedWarning.addParam(estructuraComida.getId());
            return referencedWarning;
        }
        return null;
    }

    public List<EstructuraConComidasDTO> findAllByMenu(Long menuId) {
        final List<Estructura> estructuras = estructuraRepository.findAllByMenu_Id(menuId);
        return estructuras.stream()
            .map(estructura -> mapToEstructuraConComidasDTO(estructura))
            .collect(Collectors.toList());
    }

    private EstructuraConComidasDTO mapToEstructuraConComidasDTO(Estructura estructura) {
        EstructuraConComidasDTO estructuraDTO = new EstructuraConComidasDTO();
        estructuraDTO.setId(estructura.getId());
        estructuraDTO.setNombre(estructura.getNombre());
        // ... (set other relevant fields)
        return estructuraDTO;
    }
}

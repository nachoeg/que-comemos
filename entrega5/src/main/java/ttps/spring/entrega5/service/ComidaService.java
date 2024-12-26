package ttps.spring.entrega5.service;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ttps.spring.entrega5.domain.Comida;
import ttps.spring.entrega5.domain.Estructura;
import ttps.spring.entrega5.model.ComidaDTO;
import ttps.spring.entrega5.repos.ComidaRepository;
import ttps.spring.entrega5.repos.EstructuraRepository;
import ttps.spring.entrega5.repos.PedidoRepository;
import ttps.spring.entrega5.util.NotFoundException;


@Service
@Transactional
public class ComidaService {

    private final ComidaRepository comidaRepository;
    private final PedidoRepository pedidoRepository;

    public ComidaService(final ComidaRepository comidaRepository,
            final PedidoRepository pedidoRepository) {
        this.comidaRepository = comidaRepository;
        
        this.pedidoRepository = pedidoRepository;
    }

    public List<ComidaDTO> findAll() {
        final List<Comida> comidas = comidaRepository.findAll(Sort.by("id"));
        return comidas.stream()
                .map(comida -> mapToDTO(comida, new ComidaDTO()))
                .toList();
    }

    public ComidaDTO get(final Long id) {
        return comidaRepository.findById(id)
                .map(comida -> mapToDTO(comida, new ComidaDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final ComidaDTO comidaDTO) {
        final Comida comida = new Comida();
        mapToEntity(comidaDTO, comida);
        return comidaRepository.save(comida).getId();
    }

    public void update(final Long id, final ComidaDTO comidaDTO) {
        final Comida comida = comidaRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(comidaDTO, comida);
        comidaRepository.save(comida);
    }

    public void delete(final Long id) {
        final Comida comida = comidaRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        // remove many-to-many relations at owning side
        pedidoRepository.findAllByComidas(comida)
                .forEach(pedido -> pedido.getComidas().remove(comida));
        comidaRepository.delete(comida);
    }

    private ComidaDTO mapToDTO(final Comida comida, final ComidaDTO comidaDTO) {
        comidaDTO.setId(comida.getId());
        comidaDTO.setNombre(comida.getNombre());
        comidaDTO.setPrecio(comida.getPrecio());
        comidaDTO.setFoto(comida.getFoto());
        return comidaDTO;
    }

    Comida mapToEntity(final ComidaDTO comidaDTO, final Comida comida) {
        comida.setNombre(comidaDTO.getNombre());
        comida.setPrecio(comidaDTO.getPrecio());
        comida.setFoto(comidaDTO.getFoto());
        return comida;
    }
    

}

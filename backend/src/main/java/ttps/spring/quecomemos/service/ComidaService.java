package ttps.spring.quecomemos.service;

import jakarta.transaction.Transactional;

import java.io.IOException;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ttps.spring.quecomemos.domain.Comida;
import ttps.spring.quecomemos.model.ComidaDTO;
import ttps.spring.quecomemos.model.ComidaGetDTO;
import ttps.spring.quecomemos.repos.ComidaRepository;
import ttps.spring.quecomemos.repos.EstructuraRepository;
import ttps.spring.quecomemos.repos.PedidoRepository;
import ttps.spring.quecomemos.util.ImgurUploader;

@Service
@Transactional
public class ComidaService {

    private final ComidaRepository comidaRepository;
    private final PedidoRepository pedidoRepository;
    private final EstructuraRepository estructuraRepository;
    private final ImgurUploader imgurUploader;

    public ComidaService(final ComidaRepository comidaRepository,
            final PedidoRepository pedidoRepository, final EstructuraRepository estructuraRepository,
            ImgurUploader imgurUploader) {
        this.comidaRepository = comidaRepository;
        this.pedidoRepository = pedidoRepository;
        this.estructuraRepository = estructuraRepository;
        this.imgurUploader = imgurUploader;
    }

    public List<ComidaGetDTO> findAll() {
        final List<Comida> comidas = comidaRepository.findAll(Sort.by("id"));
        return comidas.stream()
                .map(comida -> mapToDTO(comida, new ComidaGetDTO()))
                .toList();
    }

    public ComidaGetDTO get(final Long id) {
        return comidaRepository.findById(id)
                .map(comida -> mapToDTO(comida, new ComidaGetDTO()))
                .orElseThrow(() -> new EmptyResultDataAccessException("Comida no encontrada", 1));
    }

    public Long create(final ComidaDTO comidaDTO, MultipartFile foto) throws IOException {
        final Comida comida = new Comida();
        mapToEntity(comidaDTO, comida);
        if (foto != null && !foto.isEmpty()) {
            String fotoUrl = imgurUploader.upload(foto);
            comida.setFoto(fotoUrl);
        }
        return comidaRepository.save(comida).getId();
    }

    public void update(final Long id, final ComidaDTO comidaDTO, MultipartFile foto) throws IOException {
        final Comida comida = comidaRepository.findById(id)
                .orElseThrow(() -> new EmptyResultDataAccessException("Comida no encontrada", 1));
        mapToEntity(comidaDTO, comida);
        if (foto != null && !foto.isEmpty()) {
            String fotoUrl = imgurUploader.upload(foto);
            comida.setFoto(fotoUrl);
        }
        comidaRepository.save(comida);
    }

    public void delete(final Long id) {
        final Comida comida = comidaRepository.findById(id)
                .orElseThrow(() -> new EmptyResultDataAccessException("Comida no encontrada", 1));
        // remove many-to-many relations at owning side
        pedidoRepository.findAllByComidas(comida)
                .forEach(pedido -> pedido.getComidas().remove(comida));
        estructuraRepository.findAllByComidas(comida)
                .forEach(estructura -> estructura.getComidas().remove(comida));
        comidaRepository.delete(comida);
    }

    public boolean existsByNombre(String nombre) {
        return comidaRepository.existsByNombre(nombre);
    }

    private ComidaGetDTO mapToDTO(final Comida comida, final ComidaGetDTO comidaDTO) {
        comidaDTO.setId(comida.getId());
        comidaDTO.setNombre(comida.getNombre());
        comidaDTO.setPrecio(comida.getPrecio());
        comidaDTO.setFoto(comida.getFoto());
        return comidaDTO;
    }

    Comida mapToEntity(final ComidaDTO comidaDTO, final Comida comida) {
        comida.setNombre(comidaDTO.getNombre());
        comida.setPrecio(comidaDTO.getPrecio());
        return comida;
    }

}

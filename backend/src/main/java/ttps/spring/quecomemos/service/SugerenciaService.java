package ttps.spring.quecomemos.service;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ttps.spring.quecomemos.domain.Sugerencia;
import ttps.spring.quecomemos.domain.Usuario;
import ttps.spring.quecomemos.model.SugerenciaDTO;
import ttps.spring.quecomemos.repos.SugerenciaRepository;
import ttps.spring.quecomemos.repos.UsuarioRepository;

@Service
public class SugerenciaService {

    private final SugerenciaRepository sugerenciaRepository;
    private final UsuarioRepository usuarioRepository;

    public SugerenciaService(final SugerenciaRepository sugerenciaRepository,
            final UsuarioRepository usuarioRepository) {
        this.sugerenciaRepository = sugerenciaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<SugerenciaDTO> findAll() {
        final List<Sugerencia> sugerencias = sugerenciaRepository.findAll(Sort.by("id"));
        return sugerencias.stream()
                .map(sugerencia -> mapToDTO(sugerencia, new SugerenciaDTO()))
                .toList();
    }

    public SugerenciaDTO get(final Long id) {
        return sugerenciaRepository.findById(id)
                .map(sugerencia -> mapToDTO(sugerencia, new SugerenciaDTO()))
                .orElseThrow(() -> new EmptyResultDataAccessException("Sugerencia no encontrada", 1));
    }

    public Long create(final SugerenciaDTO sugerenciaDTO) {
        final Sugerencia sugerencia = new Sugerencia();
        mapToEntity(sugerenciaDTO, sugerencia);
        return sugerenciaRepository.save(sugerencia).getId();
    }

    public void update(final Long id, final SugerenciaDTO sugerenciaDTO) {
        final Sugerencia sugerencia = sugerenciaRepository.findById(id)
                .orElseThrow(() -> new EmptyResultDataAccessException("Sugerencia no encontrada", 1));
        mapToEntity(sugerenciaDTO, sugerencia);
        sugerenciaRepository.save(sugerencia);
    }

    public void delete(final Long id) {
        sugerenciaRepository.deleteById(id);
    }

    private SugerenciaDTO mapToDTO(final Sugerencia sugerencia, final SugerenciaDTO sugerenciaDTO) {
        sugerenciaDTO.setId(sugerencia.getId());
        sugerenciaDTO.setTipo(sugerencia.getTipo());
        sugerenciaDTO.setDescripcion(sugerencia.getDescripcion());
        sugerenciaDTO.setFecha(sugerencia.getFecha());
        sugerenciaDTO.setUsuario(sugerencia.getUsuario() == null ? null : sugerencia.getUsuario().getId());
        return sugerenciaDTO;
    }

    private Sugerencia mapToEntity(final SugerenciaDTO sugerenciaDTO, final Sugerencia sugerencia) {
        sugerencia.setTipo(sugerenciaDTO.getTipo());
        sugerencia.setDescripcion(sugerenciaDTO.getDescripcion());
        sugerencia.setFecha(sugerenciaDTO.getFecha());
        final Usuario usuario = sugerenciaDTO.getUsuario() == null ? null
                : usuarioRepository.findById(sugerenciaDTO.getUsuario())
                        .orElseThrow(() -> new EmptyResultDataAccessException("usuario no encontrado", 1));
        sugerencia.setUsuario(usuario);
        return sugerencia;
    }

}

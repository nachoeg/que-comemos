package ttps.spring.entrega5.service;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ttps.spring.entrega5.domain.Rol;
import ttps.spring.entrega5.domain.Usuario;
import ttps.spring.entrega5.model.RolDTO;
import ttps.spring.entrega5.repos.RolRepository;
import ttps.spring.entrega5.repos.UsuarioRepository;
import ttps.spring.entrega5.util.ReferencedWarning;


@Service
public class RolService {

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;

    public RolService(final RolRepository rolRepository,
            final UsuarioRepository usuarioRepository) {
        this.rolRepository = rolRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<RolDTO> findAll() {
        final List<Rol> rols = rolRepository.findAll(Sort.by("id"));
        return rols.stream()
                .map(rol -> mapToDTO(rol, new RolDTO()))
                .toList();
    }

    public RolDTO get(final Long id) {
        return rolRepository.findById(id)
                .map(rol -> mapToDTO(rol, new RolDTO()))
                .orElseThrow(() -> new EmptyResultDataAccessException("Rol no encontrado", 1));
    }

    public Long create(final RolDTO rolDTO) {
        final Rol rol = new Rol();
        mapToEntity(rolDTO, rol);
        return rolRepository.save(rol).getId();
    }

    public void update(final Long id, final RolDTO rolDTO) {
        final Rol rol = rolRepository.findById(id)
        		.orElseThrow(() -> new EmptyResultDataAccessException("Rol no encontrado", 1));
        mapToEntity(rolDTO, rol);
        rolRepository.save(rol);
    }

    public void delete(final Long id) {
        rolRepository.deleteById(id);
    }

    private RolDTO mapToDTO(final Rol rol, final RolDTO rolDTO) {
        rolDTO.setId(rol.getId());
        rolDTO.setNombreRol(rol.getNombreRol());
        return rolDTO;
    }

    private Rol mapToEntity(final RolDTO rolDTO, final Rol rol) {
        rol.setNombreRol(rolDTO.getNombreRol());
        return rol;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Rol rol = rolRepository.findById(id)
        		.orElseThrow(() -> new EmptyResultDataAccessException("Rol no encontrado", 1));
        final Usuario rolUsuario = usuarioRepository.findFirstByRol(rol);
        if (rolUsuario != null) {
            referencedWarning.setKey("rol.usuario.rol.referenced");
            referencedWarning.addParam(rolUsuario.getId());
            return referencedWarning;
        }
        return null;
    }

}

package ttps.spring.entrega5.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ttps.spring.entrega5.domain.Pedido;
import ttps.spring.entrega5.domain.Rol;
import ttps.spring.entrega5.domain.Sugerencia;
import ttps.spring.entrega5.domain.Usuario;
import ttps.spring.entrega5.model.UsuarioDTO;
import ttps.spring.entrega5.repos.PedidoRepository;
import ttps.spring.entrega5.repos.RolRepository;
import ttps.spring.entrega5.repos.SugerenciaRepository;
import ttps.spring.entrega5.repos.UsuarioRepository;
import ttps.spring.entrega5.util.ImgurUploader;
import ttps.spring.entrega5.util.PasswordService;
import ttps.spring.entrega5.util.ReferencedWarning;


@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final SugerenciaRepository sugerenciaRepository;
    private final PedidoRepository pedidoRepository;
    private final ImgurUploader imgurUploader;
    @Autowired
    private PasswordService passwordService;

    public UsuarioService(final UsuarioRepository usuarioRepository,
            final RolRepository rolRepository, final SugerenciaRepository sugerenciaRepository,
			final PedidoRepository pedidoRepository,
			ImgurUploader imgurUploader) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.sugerenciaRepository = sugerenciaRepository;
        this.pedidoRepository = pedidoRepository;
        this.imgurUploader = imgurUploader;
    }

    public List<UsuarioDTO> findAll() {
        final List<Usuario> usuarios = usuarioRepository.findAll(Sort.by("id"));
        return usuarios.stream()
                .map(usuario -> mapToDTO(usuario, new UsuarioDTO()))
                .toList();
    }

    public UsuarioDTO get(final Integer id) {
        return usuarioRepository.findById(id)
                .map(usuario -> mapToDTO(usuario, new UsuarioDTO()))
                .orElseThrow(() -> new EmptyResultDataAccessException("Usuario no encontrado", 1));
    }

    public Integer create(final UsuarioDTO usuarioDTO) {
        final Usuario usuario = new Usuario();
        mapToEntity(usuarioDTO, usuario);
        String encodedPassword = passwordService.hashPassword(usuarioDTO.getClave()); // Usar PasswordService para hashear clave
        usuario.setClave(encodedPassword);
        return usuarioRepository.save(usuario).getId();
    }

    public void update(final Integer id, final UsuarioDTO usuarioDTO, MultipartFile foto)  throws IOException {
        final Usuario usuario = usuarioRepository.findById(id)
        		.orElseThrow(() -> new EmptyResultDataAccessException("Usuario no encontrado", 1));
        if (foto != null && !foto.isEmpty()) {
        	String fotoUrl = imgurUploader.upload(foto);
            usuario.setFoto(fotoUrl);
        }
        mapToEntity(usuarioDTO, usuario);
        usuarioRepository.save(usuario);
    }
    
    public void updateRol(final Integer id, final String nuevoRolId)  throws IOException {
        final Usuario usuario = usuarioRepository.findById(id)
        		.orElseThrow(() -> new EmptyResultDataAccessException("Usuario no encontrado", 1));
        
        final Rol rol = rolRepository.findById(Long.valueOf(nuevoRolId))
                .orElseThrow(() -> new EmptyResultDataAccessException("rol no encontrado", 1));
        
        usuario.setRol(rol);
        usuarioRepository.save(usuario);
    }

    public void delete(final Integer id) {
        usuarioRepository.deleteById(id);
    }

    private UsuarioDTO mapToDTO(final Usuario usuario, final UsuarioDTO usuarioDTO) {
        usuarioDTO.setId(usuario.getId());
        usuarioDTO.setDni(usuario.getDni());
        usuarioDTO.setNombre(usuario.getNombre());
        usuarioDTO.setApellido(usuario.getApellido());
        usuarioDTO.setEmail(usuario.getEmail());
        usuarioDTO.setClave(usuario.getClave());
        usuarioDTO.setFoto(usuario.getFoto());
        //usuarioDTO.setRol(usuario.getRol() == null ? null : usuario.getRol().getId());
        if (usuario.getRol() != null) {
            usuarioDTO.setRol(usuario.getRol().getId()); // Keep the Rol ID
            usuarioDTO.setRolName(usuario.getRol().getNombreRol()); // Set the role name
        } else {
            usuarioDTO.setRol(null);
            usuarioDTO.setRolName(null);
        }
        return usuarioDTO;
    }

    private Usuario mapToEntity(final UsuarioDTO usuarioDTO, final Usuario usuario) {
        usuario.setDni(usuarioDTO.getDni());
        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setApellido(usuarioDTO.getApellido());
        usuario.setEmail(usuarioDTO.getEmail());
        //usuario.setClave(usuarioDTO.getClave()); si agrego esto se copia la clave sin hashear
        if (usuarioDTO.getFoto() != null) {
        	usuario.setFoto(usuarioDTO.getFoto());
        }
        final Rol rol = usuarioDTO.getRol() == null ? null : rolRepository.findById(usuarioDTO.getRol())
                .orElseThrow(() -> new EmptyResultDataAccessException("rol no encontrado", 1));
        usuario.setRol(rol);
        return usuario;
    }

    public boolean dniExists(final Integer dni) {
        return usuarioRepository.existsByDni(dni);
    }

    public ReferencedWarning getReferencedWarning(final Integer id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Usuario usuario = usuarioRepository.findById(id)
        		.orElseThrow(() -> new EmptyResultDataAccessException("Usuario no encontrado", 1));
        final Sugerencia usuarioSugerencia = sugerenciaRepository.findFirstByUsuario(usuario);
        if (usuarioSugerencia != null) {
            referencedWarning.setKey("usuario.sugerencia.usuario.referenced");
            referencedWarning.addParam(usuarioSugerencia.getId());
            return referencedWarning;
        }
        final Pedido usuarioPedido = pedidoRepository.findFirstByUsuario(usuario);
        if (usuarioPedido != null) {
            referencedWarning.setKey("usuario.pedido.usuario.referenced");
            referencedWarning.addParam(usuarioPedido.getId());
            return referencedWarning;
        }
        return null;
    }

	public UsuarioDTO findByEmail(String email) {
		
		Usuario user = usuarioRepository.findByEmail(email);
			if (user == null) {
	        return null; // Devuelve null directamente si el usuario no existe
	    }
	    return mapToDTO(user, new UsuarioDTO());
	}

	

}

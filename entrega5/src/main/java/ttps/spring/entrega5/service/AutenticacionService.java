package ttps.spring.entrega5.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ttps.spring.entrega5.domain.Usuario;

@Service
public class AutenticacionService {
	@Autowired
    private UsuarioService usuarioService;

	 public Usuario authenticate(String email, String password) {
	        // 1. Retrieve user by username (assuming email is used for login)
	        Usuario user = usuarioService.findByEmail(email);
	        if (user == null) {
	            return null; // User not found
	        }

	        // 2. Compare passwords (assuming passwords are stored in plain text - NOT RECOMMENDED)
	        if (!user.getClave().equals(password)) {
	            return null; // Invalid password
	        }

	        return user;
	    }
}

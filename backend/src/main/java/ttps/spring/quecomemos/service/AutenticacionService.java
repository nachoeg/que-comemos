package ttps.spring.quecomemos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import ttps.spring.quecomemos.model.UsuarioDTO;
import ttps.spring.quecomemos.util.PasswordService;

@Service
public class AutenticacionService {
	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private PasswordService passwordService;

	public UsuarioDTO authenticate(String email, String password) {

		UsuarioDTO user = usuarioService.findByEmail(email);

		if (user == null) {
			throw new EmptyResultDataAccessException("Usuario no encontrado con email: " + email, 1);// Usuario no encontrado
		}

		if (!passwordService.verifyPassword(password, user.getClave())) {
			throw new AuthenticationException("Contraseña inválida"); // Contraseña inválida
		}

		return user;
	}

	public static class AuthenticationException extends RuntimeException {
		public AuthenticationException(String message) {
			super(message);
		}
	}
}

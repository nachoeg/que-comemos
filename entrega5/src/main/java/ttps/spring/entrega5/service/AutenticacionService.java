package ttps.spring.entrega5.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import ttps.spring.entrega5.model.UsuarioDTO;
import ttps.spring.entrega5.util.NotFoundException;
import ttps.spring.entrega5.util.PasswordService;
import ttps.spring.entrega5.util.GlobalExceptionHandler;

@Service
public class AutenticacionService {
	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private PasswordService passwordService;

	public UsuarioDTO authenticate(String email, String password) {
		 try {
	        UsuarioDTO user = usuarioService.findByEmail(email);
	        
	        if (user == null) {
	        	throw new NotFoundException("Usuario no encontrado"); // Usuario no encontrado
	        }
	        
	        if (!passwordService.verifyPassword(password, user.getClave())) {
	        	throw new AuthenticationException("Contraseña inválida"); // Contraseña inválida
	        }


	        return user;
	    } catch (EmptyResultDataAccessException ex) {
	    	throw new NotFoundException("Usuario no encontrado"); // Re-lanza como tu excepción
	    }// Clase interna para excepciones de autenticación (opcional, pero recomendado)
	}
public static class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message) {
        super(message);
    	}
	}
}

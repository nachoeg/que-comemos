package ttps.spring.entrega5.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class PasswordService {

	/*
	 * @Autowired private PasswordEncoder passwordEncoder;
	 */
	 private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); // Instancia BCryptPasswordEncoder

	
	private static final Logger logger = LoggerFactory.getLogger(PasswordService.class);

    public String hashPassword(String rawPassword) {
    	String hashedPassword = passwordEncoder.encode(rawPassword);
        logger.info("Contraseña hasheada: {}", hashedPassword); // Registra la contraseña hasheada
        return hashedPassword;
    }

    public boolean verifyPassword(String rawPassword, String hashedPassword) {
    	boolean match = passwordEncoder.matches(rawPassword, hashedPassword);
        logger.info("Verificación de contraseña: {}", match); // Registra el resultado de la verificación
        return match;
    }
}

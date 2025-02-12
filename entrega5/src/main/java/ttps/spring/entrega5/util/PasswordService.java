package ttps.spring.entrega5.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {

	/*
	 * private final PasswordEncoder passwordEncoder;
	 * 
	 * public PasswordService(PasswordEncoder passwordEncoder) { // Inyección por
	 * constructor this.passwordEncoder = passwordEncoder; }
	 */
	@Autowired
    private PasswordEncoder passwordEncoder;

    public String hashPassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public boolean verifyPassword(String rawPassword, String hashedPassword) {
    	return passwordEncoder.matches(rawPassword, hashedPassword);
    }
}

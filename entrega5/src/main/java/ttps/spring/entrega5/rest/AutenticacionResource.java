package ttps.spring.entrega5.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ttps.spring.entrega5.domain.Usuario;
import ttps.spring.entrega5.model.AutenticacionDTO;
import ttps.spring.entrega5.service.AutenticacionService;
import ttps.spring.entrega5.service.UsuarioService;
import ttps.spring.entrega5.util.ReferencedException;
import ttps.spring.entrega5.util.ReferencedWarning;

@RestController
@RequestMapping(value = "/api/autenticacion", produces = MediaType.APPLICATION_JSON_VALUE)

public class AutenticacionResource {
	private final AutenticacionService autenticacionService;

    public AutenticacionResource(UsuarioService usuarioService, AutenticacionService autenticacionService) {
        this.autenticacionService = autenticacionService;
    }

	/*
	 * @PostMapping("/login") public ResponseEntity<String>
	 * authenticate(@RequestParam String mail, @RequestParam String clave) { Usuario
	 * user = autenticacionService.authenticate(mail, clave);
	 * 
	 * if (user != null) { // Generate a simple token (replace with a more secure
	 * token generation strategy) String token = user.getId() + "123456";
	 * HttpHeaders headers = new HttpHeaders(); headers.add("token", token); return
	 * ResponseEntity.ok().header("token", token).build(); } else { return
	 * ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials"); }
	 * }
	 */
    
    @PostMapping("/login")
    public ResponseEntity<String> authenticate(@RequestBody AutenticacionDTO request) {
        Usuario user = autenticacionService.authenticate(request.getMail(), request.getClave());

        if (user != null) {
            //genera un tocken con id+123456
            String token = user.getId() + "123456";
            HttpHeaders headers = new HttpHeaders();
            headers.add("token", token);
            return ResponseEntity.ok().header("token", token).build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

}

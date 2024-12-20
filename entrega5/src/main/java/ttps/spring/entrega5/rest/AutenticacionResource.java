package ttps.spring.entrega5.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;

import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import ttps.spring.entrega5.model.UsuarioDTO;
import ttps.spring.entrega5.model.AutenticacionDTO;
import ttps.spring.entrega5.service.AutenticacionService;
import ttps.spring.entrega5.service.UsuarioService;
import ttps.spring.entrega5.util.ReferencedException;
import ttps.spring.entrega5.util.ReferencedWarning;
import ttps.spring.entrega5.util.JWTUtil;

@RestController
@RequestMapping(value = "/api/autenticacion", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:4200")
public class AutenticacionResource {
	private final AutenticacionService autenticacionService;
	private final JWTUtil jwtutil;
	
	
	public AutenticacionResource(UsuarioService usuarioService, AutenticacionService autenticacionService) {
        this.autenticacionService = autenticacionService;
		this.jwtutil = new JWTUtil();
    }


    
    @PostMapping("/login")
    public ResponseEntity<Map<String,Object>> authenticate(@RequestBody AutenticacionDTO request) {
        UsuarioDTO user = autenticacionService.authenticate(request.getMail(), request.getClave());

        if (user != null) {
        	String token = jwtutil.generateToken(user);
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            //response.put("nombre", user.getNombre());
            //response.put("apellido", user.getApellido());
            //response.put("rol", user.getRol().toString());
            response.put("user", user);
            return ResponseEntity.ok().body(response);
            
			/*
			 * String token = jwtutil.generateToken(user);
			 * 
			 * HttpHeaders headers = new HttpHeaders(); headers.add("token", token);
			 * //return ResponseEntity.ok().header("token", token).build(); return
			 * ResponseEntity.ok().body(token);
			 */
        } else {
           // return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        	Map<String, Object> errorResponse = new HashMap<>(); 
            errorResponse.put("error", "Invalid credentials");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

}

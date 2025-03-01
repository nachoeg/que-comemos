package ttps.spring.quecomemos.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ttps.spring.quecomemos.model.UsuarioDTO;
import ttps.spring.quecomemos.service.UsuarioService;

@RestController
@RequestMapping(value = "/api/registro", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:4200")
public class RegistroResource {

    private final UsuarioService usuarioService;

    public RegistroResource(final UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createUsuario(@RequestBody @Valid final UsuarioDTO usuarioDTO) {
        final Integer createdId = usuarioService.create(usuarioDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

}

package ttps.spring.entrega5.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

import java.io.IOException;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ttps.spring.entrega5.model.ComidaDTO;
import ttps.spring.entrega5.model.UsuarioDTO;
import ttps.spring.entrega5.service.UsuarioService;
import ttps.spring.entrega5.util.ReferencedException;
import ttps.spring.entrega5.util.ReferencedWarning;


@RestController
@RequestMapping(value = "/api/usuarios", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:4200")
public class UsuarioResource {

    private final UsuarioService usuarioService;

    public UsuarioResource(final UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> getAllUsuarios() {
        return ResponseEntity.ok(usuarioService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> getUsuario(@PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(usuarioService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createUsuario(@RequestBody @Valid final UsuarioDTO usuarioDTO) {
        final Integer createdId = usuarioService.create(usuarioDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }
    
//    @RequestBody @Valid final UsuarioDTO usuarioDTO

    @PutMapping(value = "/{id}", consumes = { "multipart/form-data" })
    public ResponseEntity<Integer> updateUsuario(
    		@PathVariable(name = "id") final Integer id,
    		@RequestPart("usuario") @Valid final UsuarioDTO usuarioDTO,
            @RequestPart(value = "foto", required = false) final MultipartFile foto
            ) throws IOException {
        usuarioService.update(id, usuarioDTO, foto);
        return ResponseEntity.ok(id);
    }
    
    
    @PutMapping(value = "/{id}/rol", consumes = { "multipart/form-data" })
    public ResponseEntity<Integer> updateRolUsuario(
    		@PathVariable(name = "id") final Integer id,
    		@RequestPart("rol") final String rol
            ) throws IOException {
        usuarioService.updateRol(id, rol);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteUsuario(@PathVariable(name = "id") final Integer id) {
        final ReferencedWarning referencedWarning = usuarioService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
	/*
	 * @ExceptionHandler(MethodArgumentNotValidException.class) public
	 * ResponseEntity<String>
	 * handleValidationExceptions(MethodArgumentNotValidException ex) { return new
	 * ResponseEntity<>("Datos de entrada inválidos", HttpStatus.BAD_REQUEST); }
	 */

}

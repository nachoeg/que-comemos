package ttps.spring.quecomemos.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ttps.spring.quecomemos.model.RolDTO;
import ttps.spring.quecomemos.service.RolService;
import ttps.spring.quecomemos.util.ReferencedWarning;

@RestController
@RequestMapping(value = "/api/rols", produces = MediaType.APPLICATION_JSON_VALUE)
public class RolResource {

    private final RolService rolService;

    public RolResource(final RolService rolService) {
        this.rolService = rolService;
    }

    @GetMapping
    public ResponseEntity<List<RolDTO>> getAllRols() {
        return ResponseEntity.ok(rolService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RolDTO> getRol(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(rolService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createRol(@RequestBody @Valid final RolDTO rolDTO) {
        final Long createdId = rolService.create(rolDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateRol(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final RolDTO rolDTO) {
        rolService.update(id, rolDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteRol(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = rolService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw referencedWarning;
        }
        rolService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

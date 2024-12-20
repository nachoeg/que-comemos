package ttps.spring.entrega5.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ttps.spring.entrega5.model.ComidaDTO;
import ttps.spring.entrega5.service.ComidaService;


@RestController
@RequestMapping(value = "/api/comidas", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:4200")
public class ComidaResource {

    private final ComidaService comidaService;

    public ComidaResource(final ComidaService comidaService) {
        this.comidaService = comidaService;
    }

    @GetMapping
    public ResponseEntity<List<ComidaDTO>> getAllComidas() {
        return ResponseEntity.ok(comidaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComidaDTO> getComida(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(comidaService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createComida(@RequestBody @Valid final ComidaDTO comidaDTO) {
        final Long createdId = comidaService.create(comidaDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateComida(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final ComidaDTO comidaDTO) {
        comidaService.update(id, comidaDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteComida(@PathVariable(name = "id") final Long id) {
        comidaService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

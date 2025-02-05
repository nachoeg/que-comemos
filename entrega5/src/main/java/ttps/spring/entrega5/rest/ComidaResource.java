package ttps.spring.entrega5.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ttps.spring.entrega5.model.ComidaDTO;
import ttps.spring.entrega5.model.ComidaGetDTO;
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
    public ResponseEntity<List<ComidaGetDTO>> getAllComidas() {
        return ResponseEntity.ok(comidaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComidaGetDTO> getComida(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(comidaService.get(id));
    }

    @PostMapping(consumes = { "multipart/form-data" })
    @ApiResponse(responseCode = "201")
    public ResponseEntity<?> createComida(@RequestPart("comida") @Valid final ComidaDTO comidaDTO,
            @RequestPart(value = "foto", required = false) final MultipartFile foto) throws IOException {
        if (comidaService.existsByNombre(comidaDTO.getNombre())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Ya existe una comida con el mismo nombre."));
        }

        final Long createdId = comidaService.create(comidaDTO, foto);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = { "multipart/form-data" })
    public ResponseEntity<?> updateComida(@PathVariable(name = "id") final Long id,
            @RequestPart("comida") @Valid final ComidaDTO comidaDTO,
            @RequestPart(value = "foto", required = false) final MultipartFile foto) throws IOException {
        if (comidaService.existsByNombre(comidaDTO.getNombre())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Ya existe una comida con el mismo nombre."));
        }
        comidaService.update(id, comidaDTO, foto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteComida(@PathVariable(name = "id") final Long id) {
        comidaService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

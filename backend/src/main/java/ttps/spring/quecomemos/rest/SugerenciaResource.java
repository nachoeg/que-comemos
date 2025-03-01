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
import ttps.spring.quecomemos.model.SugerenciaDTO;
import ttps.spring.quecomemos.service.SugerenciaService;

@RestController
@RequestMapping(value = "/api/sugerencias", produces = MediaType.APPLICATION_JSON_VALUE)
public class SugerenciaResource {

    private final SugerenciaService sugerenciaService;

    public SugerenciaResource(final SugerenciaService sugerenciaService) {
        this.sugerenciaService = sugerenciaService;
    }

    @GetMapping
    public ResponseEntity<List<SugerenciaDTO>> getAllSugerencias() {
        return ResponseEntity.ok(sugerenciaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SugerenciaDTO> getSugerencia(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(sugerenciaService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createSugerencia(
            @RequestBody @Valid final SugerenciaDTO sugerenciaDTO) {
        final Long createdId = sugerenciaService.create(sugerenciaDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateSugerencia(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final SugerenciaDTO sugerenciaDTO) {
        sugerenciaService.update(id, sugerenciaDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteSugerencia(@PathVariable(name = "id") final Long id) {
        sugerenciaService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

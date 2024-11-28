package ttps.spring.entrega5.rest;

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
import ttps.spring.entrega5.model.EstructuraDTO;
import ttps.spring.entrega5.service.EstructuraService;
import ttps.spring.entrega5.util.ReferencedException;
import ttps.spring.entrega5.util.ReferencedWarning;


@RestController
@RequestMapping(value = "/api/estructuras", produces = MediaType.APPLICATION_JSON_VALUE)
public class EstructuraResource {

    private final EstructuraService estructuraService;

    public EstructuraResource(final EstructuraService estructuraService) {
        this.estructuraService = estructuraService;
    }

    @GetMapping
    public ResponseEntity<List<EstructuraDTO>> getAllEstructuras() {
        return ResponseEntity.ok(estructuraService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstructuraDTO> getEstructura(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(estructuraService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createEstructura(
            @RequestBody @Valid final EstructuraDTO estructuraDTO) {
        final Long createdId = estructuraService.create(estructuraDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateEstructura(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final EstructuraDTO estructuraDTO) {
        estructuraService.update(id, estructuraDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteEstructura(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = estructuraService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        estructuraService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

package ttps.spring.entrega5.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

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
import ttps.spring.entrega5.model.EstructuraGetDTO;
import ttps.spring.entrega5.service.EstructuraService;

@RestController
@RequestMapping(value = "/api/estructuras", produces = MediaType.APPLICATION_JSON_VALUE)
public class EstructuraResource {

    private final EstructuraService estructuraService;

    public EstructuraResource(final EstructuraService estructuraService) {
        this.estructuraService = estructuraService;
    }

    @GetMapping
    public ResponseEntity<List<EstructuraGetDTO>> getAllEstructuras() {
        return ResponseEntity.ok(estructuraService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<EstructuraGetDTO>> getEstructura(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(estructuraService.get(id));
    }

    @PostMapping("/{idMenu}")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createEstructura(@PathVariable(name = "idMenu") final Long idMenu,
            @RequestBody @Valid final EstructuraDTO estructuraCrearDTO) {
        final Long createdId = estructuraService.create(idMenu, estructuraCrearDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateEstructura(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final EstructuraGetDTO estructuraDTO) {
        estructuraService.update(id, estructuraDTO);
        return ResponseEntity.ok(id);
    }

    @PostMapping("/{id}/addComida/{comidaId}")
    public ResponseEntity<Void> addComida(@PathVariable(name = "id") final Long id,
            @PathVariable(name = "comidaId") final Long comidaId) {
        estructuraService.addComida(id, comidaId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/removeComida/{comidaId}")
    public ResponseEntity<Void> removeComida(@PathVariable(name = "id") final Long id,
            @PathVariable(name = "comidaId") final Long comidaId) {
        estructuraService.removeComida(id, comidaId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteEstructura(@PathVariable(name = "id") final Long id) {
        estructuraService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

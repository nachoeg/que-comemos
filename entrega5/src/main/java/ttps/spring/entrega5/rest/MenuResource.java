package ttps.spring.entrega5.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ttps.spring.entrega5.model.ComidaDTO;
import ttps.spring.entrega5.model.MenuDTO;
import ttps.spring.entrega5.model.MenuGetDTO;
import ttps.spring.entrega5.service.MenuService;
import ttps.spring.entrega5.util.ReferencedException;
import ttps.spring.entrega5.util.ReferencedWarning;

@RestController
@RequestMapping(value = "/api/menus", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:4200")
public class MenuResource {

    private final MenuService menuService;

    public MenuResource(final MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping
    public ResponseEntity<List<MenuGetDTO>> getAllMenus() {
        List<MenuGetDTO> menusDTO = menuService.findAll();
        return ResponseEntity.ok(menusDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<MenuGetDTO>> getMenu(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(menuService.get(id));
    }

    @PostMapping(consumes = { "multipart/form-data" })
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createMenu(@RequestPart("menu") @Valid final MenuDTO menuDTO,
            @RequestPart(value = "foto", required = false) final MultipartFile foto) throws IOException {
        final Long createdId = menuService.create(menuDTO, foto);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = { "multipart/form-data" })
    public ResponseEntity<Long> updateMenu(@PathVariable(name = "id") final Long id,
            @RequestPart("menu") @Valid final MenuDTO menuDTO,
            @RequestPart(value = "foto", required = false) final MultipartFile foto) throws IOException {
        menuService.update(id, menuDTO, foto);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteMenu(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = menuService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        menuService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/dia")
    public ResponseEntity<List<MenuGetDTO>> getMenusByDia(
            @RequestParam(name = "dia") final String dia) {
        List<MenuGetDTO> menusDTO = menuService.findByDia(dia); // Llama al servicio
        return ResponseEntity.ok(menusDTO);
    }
}

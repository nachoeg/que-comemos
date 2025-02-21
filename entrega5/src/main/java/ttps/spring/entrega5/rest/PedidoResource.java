package ttps.spring.entrega5.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.zxing.WriterException;

import ttps.spring.entrega5.model.PedidoDTO;
import ttps.spring.entrega5.model.PedidoResponseDTO;
import ttps.spring.entrega5.service.PedidoService;
import ttps.spring.entrega5.service.UsuarioService;
import ttps.spring.entrega5.util.EmailService;
import ttps.spring.entrega5.util.QRService;

@RestController
@RequestMapping(value = "/api/pedidos", produces = MediaType.APPLICATION_JSON_VALUE)
public class PedidoResource {

	private final PedidoService pedidoService;
	private final UsuarioService usuarioService;
	@Autowired
	private QRService qrService;
	@Autowired
	private EmailService emailService;

	public PedidoResource(final PedidoService pedidoService, final UsuarioService usuarioService) {
		this.pedidoService = pedidoService;
		this.usuarioService = usuarioService;
	}

	@GetMapping
	public ResponseEntity<List<PedidoDTO>> getAllPedidos() {
		return ResponseEntity.ok(pedidoService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<PedidoDTO> getPedido(@PathVariable(name = "id") final Long id) {
		return ResponseEntity.ok(pedidoService.get(id));
	}

	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PedidoResponseDTO> createPedido(@RequestBody @Valid final PedidoDTO pedidoDTO)
			throws WriterException, IOException, MessagingException {
		PedidoResponseDTO response = pedidoService.createPedidoWithQR(pedidoDTO);
		pedidoService.enviarCorreoComprobante(response);


		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Long> updatePedido(@PathVariable(name = "id") final Long id,
			@RequestBody @Valid final PedidoDTO pedidoDTO) {
		pedidoService.update(id, pedidoDTO);
		return ResponseEntity.ok(id);
	}

	@DeleteMapping("/{id}")
	@ApiResponse(responseCode = "204")
	public ResponseEntity<Void> deletePedido(@PathVariable(name = "id") final Long id) {
		pedidoService.delete(id);
		return ResponseEntity.noContent().build();
	}

    @PutMapping("/{id}/estado")
    public ResponseEntity<Void> updatePedidoEstado(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final Map<String, String> estado) {
        pedidoService.updateEstado(id, estado.get("estado"));
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/reporte-menus/{periodo}")
    public ResponseEntity<Map<String, Integer>> getReporteMenusPorPeriodo(@PathVariable String periodo) {
        return ResponseEntity.ok(pedidoService.generarReporteMenusPorPeriodo(periodo));
    }

    @GetMapping("/reporte-comidas/{periodo}")
    public ResponseEntity<Map<String, Integer>> getReporteComidasPorPeriodo(@PathVariable String periodo) {
        return ResponseEntity.ok(pedidoService.generarReporteComidasPorPeriodo(periodo));
    }

    @GetMapping("/reporte-montos/{periodo}")
    public ResponseEntity<Map<String, Double>> getReporteMontosPorPeriodo(@PathVariable String periodo) {
        return ResponseEntity.ok(pedidoService.generarReporteMontosPorPeriodo(periodo));
    }
    
	/*
	 * @GetMapping("/reporte-menus") public ResponseEntity<Map<String, Integer>>
	 * getReporteMenus() { return
	 * ResponseEntity.ok(pedidoService.generarReporteMenus()); }
	 * 
	 * @GetMapping("/reporte-comidas") public ResponseEntity<Map<String, Integer>>
	 * getReporteComidas() { return
	 * ResponseEntity.ok(pedidoService.generarReporteComidas()); }
	 * 
	 * @GetMapping("/reporte-montos") public ResponseEntity<Map<String, Integer>>
	 * getReporteMontos() { return
	 * ResponseEntity.ok(pedidoService.generarReporteMontos()); }
	 */

    @GetMapping("/usuario/{userId}")
    public ResponseEntity<List<PedidoDTO>> getPedidosByUsuario(@PathVariable(name = "userId") final int userId) {
        List<PedidoDTO> pedidos = pedidoService.findPedidosByUsuario(userId);
        return ResponseEntity.ok(pedidos);
    }

}

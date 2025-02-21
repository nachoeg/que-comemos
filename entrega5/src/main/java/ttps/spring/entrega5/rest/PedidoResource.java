package ttps.spring.entrega5.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.google.zxing.WriterException;

import ttps.spring.entrega5.model.PedidoDTO;
import ttps.spring.entrega5.model.PedidoResponseDTO;
import ttps.spring.entrega5.service.PedidoService;
import ttps.spring.entrega5.util.EmailService;
import ttps.spring.entrega5.util.QRService;



@RestController
@RequestMapping(value = "/api/pedidos", produces = MediaType.APPLICATION_JSON_VALUE)
public class PedidoResource {
	
    private final PedidoService pedidoService;
    @Autowired
    private QRService qrService;
    @Autowired
    private EmailService emailService;

    public PedidoResource(final PedidoService pedidoService) {
        this.pedidoService = pedidoService;
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
    public ResponseEntity<PedidoResponseDTO> createPedido(@RequestBody @Valid final PedidoDTO pedidoDTO) throws WriterException, IOException, MessagingException {

        PedidoDTO pedidoDTOConId = pedidoService.create(pedidoDTO);
        pedidoDTOConId = pedidoService.get(pedidoDTOConId.getId());
        String textoQR = String.format("ID Pedido: %d\nFecha: %s\nMonto: %.2f", pedidoDTOConId.getId(), pedidoDTOConId.getFecha(), pedidoDTOConId.getMonto());
        byte[] qrImageBytes = qrService.generarQr(textoQR);
        PedidoResponseDTO response = new PedidoResponseDTO(qrImageBytes, pedidoDTOConId);
        
     // Generate email content
        String emailContent = pedidoService.generateEmailContent(pedidoDTOConId);

        // Send email
        String destinatario = "lofap63881@lxheir.com";//https://temp-mail.org/ agregar un email para que lleguen los mensajes o utilizar los del id del usuario del pedido
        String asunto = "Comprobante de Pedido #" + pedidoDTOConId.getId();
        String mensaje = "Adjunto encontrarás el comprobante de tu pedido.";
        emailService.enviarEmailConQR(destinatario, asunto, mensaje, qrImageBytes, emailContent);
        
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

}

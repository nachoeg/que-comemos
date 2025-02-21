package ttps.spring.entrega5.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
	@Autowired
    private JavaMailSender mailSender;

	public void enviarEmailConQR(String destinatario, String asunto, String mensaje, byte[] qrBytes, String htmlContent) throws MessagingException {
        try {
        	MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(destinatario);
            helper.setSubject(asunto);
            helper.setText(mensaje, htmlContent);

            helper.addAttachment("qr_pedido.png", new ByteArrayResource(qrBytes));

            mailSender.send(mimeMessage);
        } catch (MailAuthenticationException e) {
            throw new MessagingException("Error de autenticación al enviar el correo", e);
        } catch (MessagingException e) {
            throw e;
        } catch (Exception e) {
            throw new MessagingException("Error al enviar el correo", e);
        }
		
    }

}

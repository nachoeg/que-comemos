package ttps.spring.entrega5.util;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import ttps.spring.entrega5.filter.JWTAuthenticationFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;

import javax.imageio.ImageIO;

@Service
public class QRService {
	private static final Logger logger = LoggerFactory.getLogger(QRService.class);
	
	 public byte[] generarQr(String texto) throws WriterException, IOException {
		
	        QRCodeWriter qrCodeWriter = new QRCodeWriter();
	        BitMatrix bitMatrix = qrCodeWriter.encode(texto, BarcodeFormat.QR_CODE, 250, 250);

	        BufferedImage imageqr = MatrixToImageWriter.toBufferedImage(bitMatrix);

	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        ImageIO.write(imageqr, "png", baos); 
	        byte[] qrBytes = baos.toByteArray();
	        
	        return qrBytes;
	    }

}

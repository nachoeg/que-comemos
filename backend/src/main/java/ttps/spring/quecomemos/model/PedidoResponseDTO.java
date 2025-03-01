package ttps.spring.quecomemos.model;

import java.util.List;

public class PedidoResponseDTO {
    private byte[] qrCodeImage;
    private PedidoDTO pedido;

    public PedidoResponseDTO() {
    }

    public PedidoResponseDTO(byte[] qrCodeImage, PedidoDTO pedido) {
        this.qrCodeImage = qrCodeImage;
        this.pedido = pedido;
    }

    public byte[] getQrCodeImage() {
        return qrCodeImage;
    }

    public void setQrCodeImage(byte[] qrCodeImage) {
        this.qrCodeImage = qrCodeImage;
    }

    public PedidoDTO getPedido() {
        return pedido;
    }

    public void setPedido(PedidoDTO pedido) {
        this.pedido = pedido;
    }

}

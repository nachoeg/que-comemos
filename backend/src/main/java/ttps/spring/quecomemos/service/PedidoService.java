package ttps.spring.quecomemos.service;

import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.google.zxing.WriterException;

import ttps.spring.quecomemos.domain.Comida;
import ttps.spring.quecomemos.domain.Menu;
import ttps.spring.quecomemos.domain.Pedido;
import ttps.spring.quecomemos.domain.Usuario;
import ttps.spring.quecomemos.model.ComidaPedidoDTO;
import ttps.spring.quecomemos.model.MenuPedidoDTO;
import ttps.spring.quecomemos.model.PedidoDTO;
import ttps.spring.quecomemos.model.PedidoResponseDTO;
import ttps.spring.quecomemos.repos.ComidaRepository;
import ttps.spring.quecomemos.repos.MenuRepository;
import ttps.spring.quecomemos.repos.PedidoRepository;
import ttps.spring.quecomemos.repos.UsuarioRepository;
import ttps.spring.quecomemos.util.EmailService;
import ttps.spring.quecomemos.util.QRService;

@Service
@Transactional
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final MenuRepository menuRepository;
    private final ComidaRepository comidaRepository;
    private final UsuarioRepository usuarioRepository;
    private final EmailService emailService;
    private final QRService qrService;
    private final UsuarioService usuarioService;

    private final Map<Long, Integer> menuCantidades = new HashMap<>();
    private final Map<Long, Integer> comidaCantidades = new HashMap<>();

    public PedidoService(final PedidoRepository pedidoRepository,
            final MenuRepository menuRepository, final ComidaRepository comidaRepository,
            final UsuarioRepository usuarioRepository, final EmailService emailService, final QRService qrService,
            final UsuarioService usuarioService) {
        this.pedidoRepository = pedidoRepository;
        this.menuRepository = menuRepository;
        this.comidaRepository = comidaRepository;
        this.usuarioRepository = usuarioRepository;
        this.emailService = emailService;
        this.qrService = qrService;
        this.usuarioService = usuarioService;
    }

    public List<PedidoDTO> findAll() {
        final List<Pedido> pedidoes = pedidoRepository.findAll(Sort.by("id"));
        return pedidoes.stream()
                .map(pedido -> mapToDTO(pedido, new PedidoDTO()))
                .toList();
    }

    public PedidoDTO get(final Long id) {
        return pedidoRepository.findById(id)
                .map(pedido -> mapToDTO(pedido, new PedidoDTO()))
                .orElseThrow(() -> new EmptyResultDataAccessException("Pedido no encontrado", 1));
    }

    public PedidoDTO create(final PedidoDTO pedidoDTO) {
        Pedido pedido = new Pedido();
        mapToEntity(pedidoDTO, pedido);
        double montoRecalculado = calcularMonto(pedidoDTO.getMenus(), pedidoDTO.getComidas());
        pedido.setMonto(montoRecalculado);

        pedido = pedidoRepository.save(pedido);

        PedidoDTO pedidoDTOConId = new PedidoDTO();
        return mapToDTO(pedido, pedidoDTOConId);
    }

    public void update(final Long id, final PedidoDTO pedidoDTO) {
        final Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new EmptyResultDataAccessException("Pedido no encontrado", 1));
        mapToEntity(pedidoDTO, pedido);
        double montoRecalculado = calcularMonto(pedidoDTO.getMenus(), pedidoDTO.getComidas());
        pedido.setMonto(montoRecalculado);
        pedidoRepository.save(pedido);
    }

    public void delete(final Long id) {
        pedidoRepository.deleteById(id);
    }

    private PedidoDTO mapToDTO(final Pedido pedido, final PedidoDTO pedidoDTO) {
        pedidoDTO.setId(pedido.getId());
        pedidoDTO.setFecha(pedido.getFecha());
        pedidoDTO.setMonto(pedido.getMonto());
        pedidoDTO.setEstado(pedido.getEstado());
        pedidoDTO.setMenus(pedido.getMenus().stream().map(menu -> {
            MenuPedidoDTO menuPedidoDTO = new MenuPedidoDTO();
            menuPedidoDTO.setId(menu.getId());
            menuPedidoDTO.setCantidad(menuCantidades.getOrDefault(menu.getId(), 1));
            menuPedidoDTO.setNombre(menu.getNombre());
            menuPedidoDTO.setPrecio(menu.getPrecio());
            return menuPedidoDTO;
        }).toList());
        pedidoDTO.setComidas(pedido.getComidas().stream().map(comida -> {
            ComidaPedidoDTO comidaPedidoDTO = new ComidaPedidoDTO();
            comidaPedidoDTO.setId(comida.getId());
            comidaPedidoDTO.setCantidad(comidaCantidades.getOrDefault(comida.getId(), 1));
            comidaPedidoDTO.setNombre(comida.getNombre());
            comidaPedidoDTO.setPrecio(comida.getPrecio());
            return comidaPedidoDTO;
        }).toList());
        pedidoDTO.setUsuario(pedido.getUsuario() == null ? null : pedido.getUsuario().getId());
        return pedidoDTO;
    }

    private Pedido mapToEntity(final PedidoDTO pedidoDTO, final Pedido pedido) {
        pedido.setFecha(pedidoDTO.getFecha());
        pedido.setMonto(pedidoDTO.getMonto());
        pedido.setEstado(pedidoDTO.getEstado());
        Set<Menu> menus = new HashSet<>();
        if (pedidoDTO.getMenus() != null) {
            for (MenuPedidoDTO menuPedidoDTO : pedidoDTO.getMenus()) {
                Menu menu = menuRepository.findById(menuPedidoDTO.getId())
                        .orElseThrow(() -> new EmptyResultDataAccessException("Menu no encontrado", 1));
                menus.add(menu);
                menuCantidades.put(menu.getId(), menuPedidoDTO.getCantidad());
            }
        }
        pedido.setMenus(menus);

        Set<Comida> comidas = new HashSet<>();
        if (pedidoDTO.getComidas() != null) {
            for (ComidaPedidoDTO comidaPedidoDTO : pedidoDTO.getComidas()) {
                Comida comida = comidaRepository.findById(comidaPedidoDTO.getId())
                        .orElseThrow(() -> new EmptyResultDataAccessException("Comida no encontrada", 1));
                comidas.add(comida);
                comidaCantidades.put(comida.getId(), comidaPedidoDTO.getCantidad());
            }
        }
        pedido.setComidas(comidas);

        final Usuario usuario = pedidoDTO.getUsuario() == null ? null
                : usuarioRepository.findById(pedidoDTO.getUsuario())
                        .orElseThrow(() -> new EmptyResultDataAccessException("Usuario no encontrado", 1));
        pedido.setUsuario(usuario);
        return pedido;
    }

    private double calcularMonto(List<MenuPedidoDTO> menus, List<ComidaPedidoDTO> comidas) {
        double total = 0;

        if (menus != null) {
            for (MenuPedidoDTO menu : menus) {
                Optional<Menu> menuEntity = menuRepository.findById(menu.getId());
                if (menuEntity.isPresent()) {
                    total += menuEntity.get().getPrecio() * menu.getCantidad();
                } else {
                    throw new EmptyResultDataAccessException("Menu no encontrado", 1);
                }
            }
        }

        if (comidas != null) {
            for (ComidaPedidoDTO comida : comidas) {
                Optional<Comida> comidaEntity = comidaRepository.findById(comida.getId());
                if (comidaEntity.isPresent()) {
                    total += comidaEntity.get().getPrecio() * comida.getCantidad();
                } else {
                    throw new EmptyResultDataAccessException("Comida no encontrada", 1);
                }
            }
        }

        return total;
    }

    public String generateEmailContent(PedidoDTO pedidoDTO) {
        StringBuilder content = new StringBuilder();
        content.append("<h2>Resumen del Pedido</h2>");
        content.append("<table style='border-collapse: collapse; width: 100%;'>");
        content.append(
                "<thead><tr><th style='border: 1px solid black; padding: 8px;'>Producto</th><th style='border: 1px solid black; padding: 8px;'>Cantidad</th><th style='border: 1px solid black; padding: 8px;'>Precio Unitario</th><th style='border: 1px solid black; padding: 8px;'>Subtotal</th></tr></thead>");
        content.append("<tbody>");

        if (pedidoDTO.getMenus() != null) {
            for (MenuPedidoDTO item : pedidoDTO.getMenus()) {
                content.append("<tr>");
                content.append("<td style='border: 1px solid black; padding: 8px;'>").append(item.getNombre())
                        .append("</td>");
                content.append("<td style='border: 1px solid black; padding: 8px;'>").append(item.getCantidad())
                        .append("</td>");
                content.append("<td style='border: 1px solid black; padding: 8px;'>$ ").append(item.getPrecio())
                        .append("</td>");
                content.append("<td style='border: 1px solid black; padding: 8px;'>$ ")
                        .append(item.getPrecio() * item.getCantidad()).append("</td>");
                content.append("</tr>");
            }
        }

        if (pedidoDTO.getComidas() != null) {
            for (ComidaPedidoDTO item : pedidoDTO.getComidas()) {
                content.append("<tr>");
                content.append("<td style='border: 1px solid black; padding: 8px;'>").append(item.getNombre())
                        .append("</td>");
                content.append("<td style='border: 1px solid black; padding: 8px;'>").append(item.getCantidad())
                        .append("</td>");
                content.append("<td style='border: 1px solid black; padding: 8px;'>$ ").append(item.getPrecio())
                        .append("</td>");
                content.append("<td style='border: 1px solid black; padding: 8px;'>$ ")
                        .append(item.getPrecio() * item.getCantidad()).append("</td>");
                content.append("</tr>");
            }
        }

        content.append("</tbody></table>");
        content.append("<p>Total: $ ").append(pedidoDTO.getMonto()).append("</p>");
        content.append("<p>Cantidad de Comidas: ")
                .append(pedidoDTO.getComidas() != null ? pedidoDTO.getComidas().size() : 0).append("</p>");
        content.append("<p>Cantidad de Menus: ").append(pedidoDTO.getMenus() != null ? pedidoDTO.getMenus().size() : 0)
                .append("</p>");
        content.append("<p>Gracias por tu compra.</p>");

        return content.toString();
    }

    public PedidoResponseDTO createPedidoWithQR(final PedidoDTO pedidoDTO) throws WriterException, IOException {
        PedidoDTO pedidoDTOConId = create(pedidoDTO);
        pedidoDTOConId = get(pedidoDTOConId.getId());
        String textoQR = String.format("ID Pedido: %d\nFecha: %s\nMonto: %.2f", pedidoDTOConId.getId(),
                pedidoDTOConId.getFecha(), pedidoDTOConId.getMonto());
        byte[] qrImageBytes = qrService.generarQr(textoQR);
        return new PedidoResponseDTO(qrImageBytes, pedidoDTOConId);
    }

    public void enviarCorreoComprobante(PedidoResponseDTO response) throws MessagingException {
        String emailContent = this.generateEmailContent(response.getPedido());
        String destinatario = usuarioService.get(response.getPedido().getUsuario()).getEmail();
        String asunto = "Comprobante de Pedido #" + response.getPedido().getId();
        String mensaje = "Adjunto encontrarás el comprobante de tu pedido.";
        emailService.enviarEmailConQR(destinatario, asunto, mensaje, response.getQrCodeImage(), emailContent);
    }

    public void updateEstado(final Long id, final String estado) {
        final Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new EmptyResultDataAccessException("Pedido no encontrado", 1));
        pedido.setEstado(estado);
        pedidoRepository.save(pedido);
    }

    public Map<String, Integer> generarReporteMenusPorPeriodo(String periodo) {
        return generarReportePorPeriodo(periodo, pedido -> pedido.getMenus().stream().map(Menu::getNombre).toList());
    }

    public Map<String, Integer> generarReporteComidasPorPeriodo(String periodo) {
        return generarReportePorPeriodo(periodo,
                pedido -> pedido.getComidas().stream().map(Comida::getNombre).toList());
    }

    public Map<String, Double> generarReporteMontosPorPeriodo(String periodo) {
        List<Pedido> pedidos = pedidoRepository.findAll();
        Map<String, Double> reporteMontos = new HashMap<>();
        LocalDate ahora = LocalDate.now();

        for (Pedido pedido : pedidos) {
            LocalDate fechaPedido = pedido.getFecha();
            String key = obtenerClavePeriodo(periodo, ahora, fechaPedido);

            if (key != null) {
                reporteMontos.put(key, reporteMontos.getOrDefault(key, 0.0) + pedido.getMonto());
            }
        }

        return reporteMontos;
    }

    private Map<String, Integer> generarReportePorPeriodo(String periodo,
            java.util.function.Function<Pedido, List<String>> obtenerNombres) {
        List<Pedido> pedidos = pedidoRepository.findAll();
        Map<String, Integer> reporte = new HashMap<>();
        LocalDate ahora = LocalDate.now();

        for (Pedido pedido : pedidos) {
            LocalDate fechaPedido = pedido.getFecha();
            String key = obtenerClavePeriodo(periodo, ahora, fechaPedido);

            if (key != null) {
                obtenerNombres.apply(pedido).forEach(nombre -> {
                    reporte.put(nombre, reporte.getOrDefault(nombre, 0) + 1);
                });
            }
        }

        return reporte;
    }

    private String obtenerClavePeriodo(String periodo, LocalDate ahora, LocalDate fechaPedido) {
        switch (periodo.toLowerCase()) {
            case "diario":
                if (ChronoUnit.DAYS.between(fechaPedido, ahora) == 0) {
                    return fechaPedido.toString();
                }
                break;
            case "semanal":
                if (ChronoUnit.WEEKS.between(fechaPedido, ahora) == 0) {
                    return "Semana de " + ahora
                            .with(java.time.temporal.TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY))
                            .toString();
                }
                break;
            case "mensual":
                if (ChronoUnit.MONTHS.between(fechaPedido, ahora) == 0) {
                    return "Mes de " + ahora.getMonth().toString();
                }
                break;
        }
        return null;
    }

    public List<PedidoDTO> findPedidosByUsuario(final int userId) {
        final Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        List<Pedido> pedidos = pedidoRepository.findAllByUsuario(usuario);
        return pedidos.stream().map(pedido -> mapToDTO(pedido, new PedidoDTO())).collect(Collectors.toList());
    }

}

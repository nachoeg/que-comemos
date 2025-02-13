package ttps.spring.entrega5.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
//import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ttps.spring.entrega5.util.JWTUtil;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servlet Filter implementation class JWTAuthenticationFilter
 */
@Component // Añade @Component
public class JWTAuthenticationFilter implements Filter {
	private final JWTUtil tokenService; // Inyecta TokenService
	private static final Logger logger = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

    public JWTAuthenticationFilter(JWTUtil tokenService) { // Inyección por constructor
        this.tokenService = tokenService;
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		logger.debug("Filtro JWTAuthenticationFilter ejecutado");
		HttpServletRequest req = (HttpServletRequest) request;
		
		 String path = req.getRequestURI(); // Obtén la URI de la petición
			/*
			 * logger.debug("URI de la petición: {}", path);
			 * 
			 * // Excluye las rutas de login y registro if
			 * (path.startsWith("/api/autenticacion/login") || // Usa startsWith para rutas
			 * que empiezan con path.startsWith("/api/usuarios") || // Excluir todas las
			 * peticiones a /api/usuarios (registro)
			 * HttpMethod.OPTIONS.matches(req.getMethod())) { // Permite OPTIONS (preflight
			 * requests) chain.doFilter(request, response); return; }
			 * 
			 * String token = req.getHeader(HttpHeaders.AUTHORIZATION); if (token == null) {
			 * logger.warn("Token no encontrado en la petición"); // Mensaje de advertencia
			 * } else { logger.debug("Token encontrado: {}", token); // Mensaje de debug con
			 * parámetro (cuidado: no loguees el token completo en producción) }
			 * 
			 * 
			 * if (token == null || !tokenService.validateToken(token)) { // Usa
			 * tokenService logger.error("Token inválido"); // Mensaje de error
			 * HttpServletResponse res = (HttpServletResponse) response;
			 * res.setStatus(HttpStatus.FORBIDDEN.value()); return; }
			 * logger.info("Petición autorizada"); // Mensaje informativo
			 */
		 logger.debug("Petición a: {}", path);

	        // Rutas EXCLUIDAS (no requieren autenticación)
	        if (path.startsWith("/api/autenticacion/login") || 
	            path.startsWith("/api/usuarios") || // Registro
	            path.startsWith("/swagger-ui") || // Swagger UI (si lo usas)
	            path.startsWith("/v3/api-docs") || // Swagger API docs
	            
	            HttpMethod.OPTIONS.matches(req.getMethod())) { // Preflight requests
	            chain.doFilter(request, response);
	            return;
	        }

	        // Rutas PROTEGIDAS (requieren autenticación)
	        String token = req.getHeader(HttpHeaders.AUTHORIZATION);

	        if (token == null || !tokenService.validateToken(token)) {
	            logger.warn("Token inválido o no presente para: {}", path);
	            ((HttpServletResponse) response).setStatus(HttpStatus.FORBIDDEN.value());
	            return;
	        }

	        logger.info("Petición autorizada para: {}", path);
        chain.doFilter(request, response);
    
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}


}

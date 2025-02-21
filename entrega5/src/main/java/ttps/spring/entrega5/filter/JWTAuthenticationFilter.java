package ttps.spring.entrega5.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ttps.spring.entrega5.util.JWTUtil;
import ttps.spring.entrega5.util.JWTUtil.TokenExpiredException;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JWTAuthenticationFilter implements Filter {
	private final JWTUtil tokenService;
	private static final Logger logger = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

	public JWTAuthenticationFilter(JWTUtil tokenService) {
		this.tokenService = tokenService;
	}

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		logger.debug("Filtro JWTAuthenticationFilter ejecutado");
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		String path = req.getRequestURI();


		// Rutas EXCLUIDAS (no requieren autenticación)
		if (path.startsWith("/api/autenticacion/login") ||
				path.startsWith("/api/registro") || // Registro
				path.startsWith("/swagger-ui") || // Swagger UI (si lo usas)
				path.startsWith("/v3/api-docs") || // Swagger API docs
				HttpMethod.OPTIONS.matches(req.getMethod())) { // Preflight requests
			chain.doFilter(request, response);
			return;
		}

		// Rutas PROTEGIDAS (requieren autenticación)
		String token = req.getHeader(HttpHeaders.AUTHORIZATION);

		try {
			if (token == null || !tokenService.validateToken(token)) {
				logger.warn("Token inválido o no presente para: {}", path);
				res.setStatus(HttpStatus.FORBIDDEN.value());
				res.getWriter().write("Token inválido o no presente");
				res.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
				return;
			}
		} catch (TokenExpiredException e) {
			logger.warn("Token expirado para: {}", path);
			res.setStatus(HttpStatus.UNAUTHORIZED.value());
			res.getWriter().write("Token expirado");
			res.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
			return;
		}

		logger.info("Petición autorizada para: {}", path);
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
	}
}
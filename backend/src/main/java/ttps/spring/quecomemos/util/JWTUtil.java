package ttps.spring.quecomemos.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servicio para gestionar los tokens de authenticacion
 */
@Service
public class JWTUtil {

    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final Logger logger = LoggerFactory.getLogger(JWTUtil.class);

    /**
     * Genera el token de authorizacion para el usuario
     *
     * @param username Username que se guarda dentro del token
     * @param segundos tiempo de validez del token
     * @return token
     */
    public String generateToken(String username, int segundos) {
        Date exp = getExpiration(new Date(), segundos);
        return Jwts.builder().setSubject(username).signWith(key).setExpiration(exp).compact();
    }

    /**
     * Retorna la suma de <code>segundos</code> a la <code>fecha</code>
     *
     * @param date
     * @param segundos
     * @return
     */
    private Date getExpiration(Date date, int segundos) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, segundos);
        return calendar.getTime();
    }

    public boolean validateToken(String token) throws TokenExpiredException {
        String prefix = "Bearer";
        try {
            if (token.startsWith(prefix)) {
                token = token.substring(prefix.length()).trim();
            }

            Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();

            System.out.println("ID: " + claims.getId());
            System.out.println("Subject: " + claims.getSubject());
            System.out.println("Issuer: " + claims.getIssuer());
            System.out.println("Expiration: " + claims.getExpiration());

            return true;
        } catch (ExpiredJwtException exp) {
            logger.warn("Token expirado: {}", exp.getMessage());
            throw new TokenExpiredException("El Token es valido, pero expiro su tiempo de validez");
        } catch (JwtException e) {
            logger.warn("Error: {}", e.getMessage());
            return false;
        }
    }

    public static class TokenExpiredException extends RuntimeException {
        public TokenExpiredException(String message) {
            super(message);
        }
    }
}
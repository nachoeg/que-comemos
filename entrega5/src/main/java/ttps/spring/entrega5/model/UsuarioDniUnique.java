package ttps.spring.entrega5.model;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;
import org.springframework.web.servlet.HandlerMapping;
import ttps.spring.entrega5.service.UsuarioService;


/**
 * Validate that the dni value isn't taken yet.
 */
@Target({ FIELD, METHOD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = UsuarioDniUnique.UsuarioDniUniqueValidator.class
)
public @interface UsuarioDniUnique {

    String message() default "{Exists.usuario.dni}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class UsuarioDniUniqueValidator implements ConstraintValidator<UsuarioDniUnique, Integer> {

        private final UsuarioService usuarioService;
        private final HttpServletRequest request;

        public UsuarioDniUniqueValidator(final UsuarioService usuarioService,
                final HttpServletRequest request) {
            this.usuarioService = usuarioService;
            this.request = request;
        }

        @Override
        public boolean isValid(final Integer value, final ConstraintValidatorContext cvContext) {
            if (value == null) {
                // no value present
                return true;
            }
            @SuppressWarnings("unchecked") final Map<String, String> pathVariables =
                    ((Map<String, String>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE));
            final String currentId = pathVariables.get("id");
            if (currentId != null && value.equals(usuarioService.get(Integer.parseInt(currentId)).getDni())) {
                // value hasn't changed
                return true;
            }
            return !usuarioService.dniExists(value);
        }

    }

}

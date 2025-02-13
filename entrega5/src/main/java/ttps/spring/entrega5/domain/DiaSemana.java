package ttps.spring.entrega5.domain;

public enum DiaSemana {
  Lunes,
  Martes,
  Miércoles,
  Jueves,
  Viernes,
  Sábado,
  Domingo,
  Desactivado;
	public static DiaSemana fromString(String dia) {
        try {
            return DiaSemana.valueOf(dia);
        } catch (IllegalArgumentException e) {
            // Manejar el caso en que el string no coincide con ningún valor del enum
            throw new IllegalArgumentException("Día de la semana inválido: " + dia);
        }
    }
}

package utils;

/**
 * InputValidator.java
 * Utilidades para validar entrada del usuario.
 */
public final class InputValidator {

    // Prevenir instanciación
    private InputValidator() {
        throw new UnsupportedOperationException("Esta es una clase de utilidades");
    }

    /**
     * Valida que una cadena no sea nula o vacía.
     *
     * @param input cadena a validar
     * @return true si es válida
     */
    public static boolean isValidString(String input) {
        return input != null && !input.trim().isEmpty();
    }

    /**
     * Valida que un número esté en un rango específico.
     *
     * @param number número a validar
     * @param min valor mínimo
     * @param max valor máximo
     * @return true si está en el rango
     */
    public static boolean isInRange(int number, int min, int max) {
        return number >= min && number <= max;
    }

    /**
     * Valida un comando del juego.
     *
     * @param command comando a validar
     * @return true si es válido
     */
    public static boolean isValidCommand(String command) {
        return GameConstants.isValidCommand(command);
    }
}
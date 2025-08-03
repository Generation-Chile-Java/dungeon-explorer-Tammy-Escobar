package interfaces;

import models.player.Player;

/**
 * GameObject.java
 * Interface que define el contrato para todos los objetos interactuables del juego.
 * Sigue el principio de Inversión de Dependencias (DIP).
 */
public interface GameObject {

    /**
     * Obtiene el nombre único del objeto.
     *
     * @return nombre del objeto
     */
    String getName();

    /**
     * Obtiene la descripción detallada del objeto.
     *
     * @return descripción del objeto
     */
    String getDescription();

    /**
     * Ejecuta la acción de usar el objeto.
     * Implementa el patrón Command para encapsular acciones.
     *
     * @param player el jugador que usa el objeto
     */
    void use(Player player);

    /**
     * Obtiene el valor numérico del objeto (vida, daño, etc.).
     *
     * @return valor del objeto
     */
    int getValue();

    /**
     * Obtiene el tipo de objeto para clasificación.
     *
     * @return tipo de objeto
     */
    ObjectType getObjectType();

    /**
     * Verifica si el objeto es consumible (se elimina al usar).
     *
     * @return true si es consumible, false si es reutilizable
     */
    boolean isConsumable();

    /**
     * Obtiene la rareza del objeto.
     *
     * @return nivel de rareza
     */
    Rarity getRarity();

    /**
     * Enumeración que define los tipos de objetos disponibles.
     */
    enum ObjectType {
        TREASURE("Tesoro"),
        KEY("Llave"),
        TOOL("Herramienta"),
        WEAPON("Arma"),
        CONSUMABLE("Consumible");

        private final String displayName;

        ObjectType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    /**
     * Enumeración que define la rareza de los objetos.
     */
    enum Rarity {
        COMMON("Común", "⚪"),
        UNCOMMON("Poco Común", "🟢"),
        RARE("Raro", "🔵"),
        EPIC("Épico", "🟣"),
        LEGENDARY("Legendario", "🟡");

        private final String name;
        private final String symbol;

        Rarity(String name, String symbol) {
            this.name = name;
            this.symbol = symbol;
        }

        public String getName() {
            return name;
        }

        public String getSymbol() {
            return symbol;
        }
    }
}
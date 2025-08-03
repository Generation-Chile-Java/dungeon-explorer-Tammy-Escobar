package interfaces;

import models.player.Player;

/**
 * Room.java
 * Interface que define el contrato para todas las salas en la mazmorra.
 * Aplica el principio de Segregación de Interfaces (ISP).
 */
public interface Room {

    /**
     * Obtiene la descripción de la sala.
     *
     * @return descripción textual de la sala
     */
    String getDescription();

    /**
     * Maneja la interacción del jugador con la sala.
     * Implementa el patrón Strategy para diferentes comportamientos.
     *
     * @param player el jugador que interactúa con la sala
     */
    void interact(Player player);

    /**
     * Obtiene el nombre único de la sala.
     *
     * @return nombre de la sala
     */
    String getName();

    /**
     * Verifica si la sala ha sido visitada anteriormente.
     *
     * @return true si la sala ha sido visitada, false en caso contrario
     */
    boolean isVisited();

    /**
     * Establece el estado de visitado de la sala.
     *
     * @param visited estado de visitado a establecer
     */
    void setVisited(boolean visited);

    /**
     * Obtiene el tipo de sala para identificación.
     *
     * @return tipo de sala como enumeración
     */
    RoomType getRoomType();

    /**
     * Verifica si la sala está disponible para interacción.
     *
     * @return true si está disponible, false si está bloqueada
     */
    boolean isAccessible();

    /**
     * Enumeración que define los tipos de salas disponibles.
     */
    enum RoomType {
        EMPTY("Vacía", 1),
        TREASURE("Con Tesoro", 3),
        ENEMY("Con Enemigo", 2),
        LOCKED("Bloqueada", 4),
        SECRET("Secreta", 5);

        private final String description;
        private final int priority;

        RoomType(String description, int priority) {
            this.description = description;
            this.priority = priority;
        }

        public String getDescription() {
            return description;
        }

        public int getPriority() {
            return priority;
        }
    }
}
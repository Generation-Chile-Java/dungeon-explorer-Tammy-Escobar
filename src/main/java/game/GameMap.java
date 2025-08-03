package game;

import interfaces.Room;
import java.util.Arrays;

/**
 * GameMap.java
 * Representa un mapa del juego con una matriz de salas.
 * Maneja la navegación y posicionamiento en el mundo del juego.
 */
public class GameMap {

    private final String name;
    private final String description;
    private final int width;
    private final int height;
    private final Room[][] rooms;

    /**
     * Constructor para crear un mapa del juego.
     *
     * @param name nombre del mapa
     * @param width ancho del mapa
     * @param height alto del mapa
     */
    public GameMap(String name, int width, int height) {
        this.name = name != null ? name : "Mapa Sin Nombre";
        this.description = "Un mundo lleno de desafíos y aventuras para desarrolladores.";
        this.width = Math.max(1, width);
        this.height = Math.max(1, height);
        this.rooms = new Room[this.height][this.width];
    }

    /**
     * Constructor con descripción personalizada.
     */
    public GameMap(String name, String description, int width, int height) {
        this.name = name != null ? name : "Mapa Sin Nombre";
        this.description = description != null ? description : "Un mundo misterioso.";
        this.width = Math.max(1, width);
        this.height = Math.max(1, height);
        this.rooms = new Room[this.height][this.width];
    }

    // ============ GETTERS ============

    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }

    // ============ MÉTODOS DE GESTIÓN DE SALAS ============

    /**
     * Establece una sala en una posición específica.
     *
     * @param x coordenada X
     * @param y coordenada Y
     * @param room sala a colocar
     */
    public void setRoom(int x, int y, Room room) {
        if (isValidPosition(x, y)) {
            rooms[y][x] = room;
        }
    }

    /**
     * Obtiene la sala en una posición específica.
     *
     * @param x coordenada X
     * @param y coordenada Y
     * @return sala en la posición o null si no hay sala
     */
    public Room getRoom(int x, int y) {
        if (isValidPosition(x, y)) {
            return rooms[y][x];
        }
        return null;
    }

    /**
     * Verifica si una posición es válida en el mapa.
     *
     * @param x coordenada X
     * @param y coordenada Y
     * @return true si la posición está dentro de los límites
     */
    public boolean isValidPosition(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    /**
     * Verifica si hay una sala en una posición específica.
     *
     * @param x coordenada X
     * @param y coordenada Y
     * @return true si hay una sala en esa posición
     */
    public boolean hasRoom(int x, int y) {
        return isValidPosition(x, y) && rooms[y][x] != null;
    }

    /**
     * Remueve una sala de una posición específica.
     *
     * @param x coordenada X
     * @param y coordenada Y
     */
    public void removeRoom(int x, int y) {
        if (isValidPosition(x, y)) {
            rooms[y][x] = null;
        }
    }

    // ============ MÉTODOS DE UTILIDAD ============

    /**
     * Cuenta el número total de salas en el mapa.
     *
     * @return número de salas
     */
    public int getTotalRooms() {
        int count = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (rooms[y][x] != null) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Cuenta el número de salas visitadas.
     *
     * @return número de salas visitadas
     */
    public int getVisitedRooms() {
        int count = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Room room = rooms[y][x];
                if (room != null && room.isVisited()) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Limpia todo el mapa (remueve todas las salas).
     */
    public void clear() {
        for (int y = 0; y < height; y++) {
            Arrays.fill(rooms[y], null);
        }
    }

    /**
     * Obtiene información detallada del mapa.
     */
    public String getDetailedInfo() {
        StringBuilder info = new StringBuilder();
        info.append("🗺️ Mapa: ").append(name).append("\n");
        info.append("📝 ").append(description).append("\n");
        info.append("📐 Dimensiones: ").append(width).append("x").append(height).append("\n");
        info.append("🏠 Salas totales: ").append(getTotalRooms()).append("\n");
        info.append("👁️ Salas visitadas: ").append(getVisitedRooms()).append("\n");

        return info.toString();
    }

    @Override
    public String toString() {
        return String.format("GameMap[%s: %dx%d, %d salas]",
                name, width, height, getTotalRooms());
    }
}
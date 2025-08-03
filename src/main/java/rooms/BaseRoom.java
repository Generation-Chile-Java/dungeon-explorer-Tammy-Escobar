package rooms;

import interfaces.Room;
import interfaces.Interactable;
import interfaces.Interactable.InteractionResult;
import interfaces.Interactable.InteractionType;
import models.player.Player;
import utils.GameConstants;
import java.util.*;

/**
 * BaseRoom.java
 * Clase abstracta base para todas las salas del juego.
 * Implementa el patrón Template Method y funcionalidad común.
 */
public abstract class BaseRoom implements Room {

    // Propiedades básicas de la sala
    protected final String id;
    protected final String name;
    protected final String description;
    protected final RoomType roomType;

    // Estado de la sala
    protected boolean visited;
    protected boolean accessible;
    protected int visitCount;

    // Conexiones con otras salas
    protected final Map<Direction, Room> connections;

    // Metadatos de la sala
    protected final Map<String, Object> metadata;
    protected final Set<String> tags;

    /**
     * Constructor base para todas las salas.
     *
     * @param name nombre de la sala
     * @param description descripción de la sala
     * @param roomType tipo de sala
     */
    protected BaseRoom(String name, String description, RoomType roomType) {
        this.id = generateId();
        this.name = validateString(name, "Sala Sin Nombre");
        this.description = validateString(description, "Una sala misteriosa.");
        this.roomType = roomType != null ? roomType : RoomType.EMPTY;
        this.visited = false;
        this.accessible = true;
        this.visitCount = 0;
        this.connections = new EnumMap<>(Direction.class);
        this.metadata = new HashMap<>();
        this.tags = new HashSet<>();
    }

    /**
     * Genera un ID único para la sala.
     */
    private String generateId() {
        return "ROOM_" + System.nanoTime() + "_" + (int)(Math.random() * 1000);
    }

    /**
     * Valida que una cadena no sea nula o vacía.
     */
    private String validateString(String input, String defaultValue) {
        return (input != null && !input.trim().isEmpty()) ? input.trim() : defaultValue;
    }

    // ============ IMPLEMENTACIÓN DE INTERFACES ============

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public RoomType getRoomType() {
        return roomType;
    }

    @Override
    public boolean isVisited() {
        return visited;
    }

    @Override
    public void setVisited(boolean visited) {
        this.visited = visited;
        if (visited) {
            visitCount++;
        }
    }

    @Override
    public boolean isAccessible() {
        return accessible;
    }

    // ============ TEMPLATE METHOD PARA INTERACCIÓN ============

    /**
     * Método template que define el flujo de interacción.
     * Las subclases implementan los métodos específicos.
     */
    @Override
    public final void interact(Player player) {
        if (!canInteract(player)) {
            showCannotInteractMessage(player);
            return;
        }

        // Marcar como visitada si es la primera vez
        if (!visited) {
            onFirstVisit(player);
            setVisited(true);
            player.getStats().incrementRoomsExplored();
        } else {
            onSubsequentVisit(player);
        }

        // Ejecutar la interacción específica de la sala
        InteractionResult result = executeRoomInteraction(player);

        // Procesar el resultado
        handleInteractionResult(result, player);

        // Post-procesamiento
        onInteractionComplete(player);
    }

    // ============ MÉTODOS ABSTRACTOS ============

    /**
     * Ejecuta la interacción específica de cada tipo de sala.
     * Debe ser implementado por cada subclase.
     *
     * @param player jugador que interactúa
     * @return resultado de la interacción
     */
    protected abstract InteractionResult executeRoomInteraction(Player player);

    // ============ MÉTODOS HOOK (OPCIONALES) ============

    /**
     * Se ejecuta en la primera visita a la sala.
     */
    protected void onFirstVisit(Player player) {
        System.out.println("👁️ Primera vez que exploras: " + name);
    }

    /**
     * Se ejecuta en visitas posteriores.
     */
    protected void onSubsequentVisit(Player player) {
        System.out.println("🔄 Has regresado a: " + name);
    }

    /**
     * Se ejecuta al completar la interacción.
     */
    protected void onInteractionComplete(Player player) {
        // Implementación por defecto vacía
    }

    /**
     * Verifica si el jugador puede interactuar con la sala.
     */
    public boolean canInteract(Player player) {
        return accessible && player != null && player.canInteract();
    }

    /**
     * Muestra mensaje cuando no se puede interactuar.
     */
    protected void showCannotInteractMessage(Player player) {
        if (!accessible) {
            System.out.println("🚫 Esta sala no está disponible en este momento.");
        } else if (player == null || !player.canInteract()) {
            System.out.println("🚫 No puedes interactuar en tu estado actual.");
        }
    }

    /**
     * Maneja el resultado de la interacción.
     */
    protected void handleInteractionResult(InteractionResult result, Player player) {
        if (result.isSuccess()) {
            System.out.println("✅ " + result.getMessage());
        } else {
            System.out.println("❌ " + result.getMessage());
        }
    }

    // ============ GESTIÓN DE CONEXIONES ============

    /**
     * Conecta esta sala con otra en una dirección específica.
     *
     * @param direction dirección de la conexión
     * @param room sala a conectar
     */
    public void connectTo(Direction direction, Room room) {
        if (direction != null && room != null) {
            connections.put(direction, room);
        }
    }

    /**
     * Obtiene la sala conectada en una dirección.
     *
     * @param direction dirección a verificar
     * @return sala conectada o null
     */
    public Room getConnectedRoom(Direction direction) {
        return connections.get(direction);
    }

    /**
     * Verifica si hay una conexión en una dirección.
     *
     * @param direction dirección a verificar
     * @return true si hay conexión
     */
    public boolean hasConnection(Direction direction) {
        return connections.containsKey(direction);
    }

    /**
     * Obtiene todas las conexiones de la sala.
     *
     * @return mapa inmutable de conexiones
     */
    public Map<Direction, Room> getConnections() {
        return Collections.unmodifiableMap(connections);
    }

    /**
     * Obtiene las direcciones disponibles desde esta sala.
     *
     * @return conjunto de direcciones disponibles
     */
    public Set<Direction> getAvailableDirections() {
        return Collections.unmodifiableSet(connections.keySet());
    }

    // ============ GESTIÓN DE METADATOS ============

    /**
     * Establece un metadato de la sala.
     *
     * @param key clave del metadato
     * @param value valor del metadato
     */
    public void setMetadata(String key, Object value) {
        if (key != null && !key.trim().isEmpty()) {
            metadata.put(key, value);
        }
    }

    /**
     * Obtiene un metadato de la sala.
     *
     * @param key clave del metadato
     * @return valor del metadato o null
     */
    public Object getMetadata(String key) {
        return metadata.get(key);
    }

    /**
     * Verifica si existe un metadato.
     *
     * @param key clave a verificar
     * @return true si existe
     */
    public boolean hasMetadata(String key) {
        return metadata.containsKey(key);
    }

    // ============ GESTIÓN DE TAGS ============

    /**
     * Añade un tag a la sala.
     *
     * @param tag tag a añadir
     */
    public void addTag(String tag) {
        if (tag != null && !tag.trim().isEmpty()) {
            tags.add(tag.trim().toLowerCase());
        }
    }

    /**
     * Verifica si la sala tiene un tag específico.
     *
     * @param tag tag a verificar
     * @return true si tiene el tag
     */
    public boolean hasTag(String tag) {
        return tag != null && tags.contains(tag.trim().toLowerCase());
    }

    /**
     * Obtiene todos los tags de la sala.
     *
     * @return conjunto inmutable de tags
     */
    public Set<String> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    // ============ MÉTODOS DE UTILIDAD PARA INTERACCIÓN ============

    /**
     * Obtiene el prompt de interacción.
     */
    public String getInteractionPrompt() {
        return "Presiona F para explorar " + name;
    }

    /**
     * Obtiene la prioridad de interacción.
     */
    public int getInteractionPriority() {
        return roomType.getPriority();
    }

    // ============ MÉTODOS DE UTILIDAD ============

    /**
     * Obtiene el ID único de la sala.
     */
    public String getId() {
        return id;
    }

    /**
     * Obtiene el número de veces que se ha visitado la sala.
     */
    public int getVisitCount() {
        return visitCount;
    }

    /**
     * Establece la accesibilidad de la sala.
     */
    public void setAccessible(boolean accessible) {
        this.accessible = accessible;
    }

    /**
     * Resetea el estado de la sala.
     */
    public void reset() {
        visited = false;
        visitCount = 0;
        accessible = true;
    }

    /**
     * Obtiene información detallada de la sala.
     */
    public String getDetailedInfo() {
        StringBuilder info = new StringBuilder();
        info.append("🏠 ").append(name).append("\n");
        info.append("📝 ").append(description).append("\n");
        info.append("🏷️ Tipo: ").append(roomType.getDescription()).append("\n");
        info.append("👁️ Visitada: ").append(visited ? "Sí" : "No").append("\n");
        info.append("🔢 Visitas: ").append(visitCount).append("\n");
        info.append("🚪 Accesible: ").append(accessible ? "Sí" : "No").append("\n");

        if (!connections.isEmpty()) {
            info.append("🧭 Conexiones: ").append(connections.size()).append("\n");
            connections.forEach((dir, room) ->
                    info.append("   ").append(dir.getSymbol()).append(" ").append(room.getName()).append("\n")
            );
        }

        if (!tags.isEmpty()) {
            info.append("🏷️ Tags: ").append(String.join(", ", tags));
        }

        return info.toString();
    }

    @Override
    public String toString() {
        return String.format("%s[%s] - %s",
                roomType.getDescription(),
                name,
                visited ? "Visitada" : "No visitada");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        BaseRoom baseRoom = (BaseRoom) obj;
        return id.equals(baseRoom.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    // ============ ENUMERACIONES ============

    /**
     * Enumeración para direcciones de movimiento.
     */
    public enum Direction {
        NORTH("Norte", "⬆️", "w"),
        SOUTH("Sur", "⬇️", "s"),
        EAST("Este", "➡️", "d"),
        WEST("Oeste", "⬅️", "a"),
        UP("Arriba", "🔼", "u"),
        DOWN("Abajo", "🔽", "dn");

        private final String name;
        private final String symbol;
        private final String command;

        Direction(String name, String symbol, String command) {
            this.name = name;
            this.symbol = symbol;
            this.command = command;
        }

        public String getName() { return name; }
        public String getSymbol() { return symbol; }
        public String getCommand() { return command; }

        /**
         * Obtiene la dirección opuesta.
         */
        public Direction getOpposite() {
            return switch (this) {
                case NORTH -> SOUTH;
                case SOUTH -> NORTH;
                case EAST -> WEST;
                case WEST -> EAST;
                case UP -> DOWN;
                case DOWN -> UP;
            };
        }

        /**
         * Obtiene dirección por comando.
         */
        public static Direction fromCommand(String command) {
            if (command == null) return null;

            for (Direction dir : values()) {
                if (dir.command.equalsIgnoreCase(command.trim())) {
                    return dir;
                }
            }
            return null;
        }
    }
}
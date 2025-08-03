package rooms;

import interfaces.Room;
import interfaces.Interactable.InteractionResult;
import interfaces.Interactable.InteractionType;
import models.player.Player;

/**
 * LockedRoom.java
 * Representa una sala bloqueada que requiere una llave específica para acceder.
 * Actúa como proxy para otra sala hasta que se desbloquee.
 */
public class LockedRoom extends BaseRoom {

    private final String requiredKey;
    private final Room actualRoom;
    private boolean unlocked;
    private final String unlockMessage;

    /**
     * Constructor para crear una sala bloqueada.
     *
     * @param name nombre de la puerta/entrada
     * @param description descripción de la puerta bloqueada
     * @param requiredKey nombre de la llave requerida
     * @param actualRoom sala real detrás de la puerta
     */
    public LockedRoom(String name, String description, String requiredKey, Room actualRoom) {
        super(name, description, RoomType.LOCKED);

        this.requiredKey = requiredKey != null ? requiredKey : "Llave Desconocida";
        this.actualRoom = actualRoom;
        this.unlocked = false;
        this.unlockMessage = generateUnlockMessage();

        // Añadir tags apropiados
        addTag("locked");
        addTag("requires_key");
        if (requiredKey != null) {
            addTag(requiredKey.toLowerCase().replace(" ", "_"));
        }
    }

    /**
     * Genera mensaje de desbloqueo personalizado.
     */
    private String generateUnlockMessage() {
        return "✨ ¡La " + requiredKey + " funciona perfectamente! La puerta se abre revelando secretos ocultos...";
    }

    // ============ IMPLEMENTACIÓN DE INTERACCIÓN ============

    @Override
    protected InteractionResult executeRoomInteraction(Player player) {
        // Si ya está desbloqueada, interactuar con la sala real
        if (unlocked) {
            return delegateToActualRoom(player);
        }

        // Mostrar información de la puerta bloqueada
        showLockedDoorInfo();

        // Verificar si el jugador tiene la llave requerida
        if (player.hasItem(requiredKey)) {
            return attemptUnlock(player);
        } else {
            return showLockRequirements();
        }
    }

    /**
     * Muestra información sobre la puerta bloqueada.
     */
    private void showLockedDoorInfo() {
        System.out.println("🚪 " + getDescription());
        System.out.println("🔒 Esta entrada está sellada con un mecanismo de seguridad avanzado.");
    }

    /**
     * Intenta desbloquear la puerta.
     */
    private InteractionResult attemptUnlock(Player player) {
        System.out.println("🗝️ Tienes la " + requiredKey + "...");
        System.out.println("🤔 ¿Quieres usar la llave para abrir esta puerta? (s/n)");

        // En un juego real, esto vendría del input del usuario
        // Por ahora, asumimos que sí quiere usarla
        boolean wantsToUnlock = true;

        if (wantsToUnlock) {
            return performUnlock(player);
        } else {
            return InteractionResult.success(
                    "Decides conservar la llave por ahora.",
                    InteractionType.DIALOGUE
            );
        }
    }

    /**
     * Realiza el proceso de desbloqueo.
     */
    private InteractionResult performUnlock(Player player) {
        System.out.println("🔓 Insertas la " + requiredKey + " en la cerradura...");

        // Simular proceso de desbloqueo
        try {
            Thread.sleep(1000); // Pausa dramática
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Consumir la llave
        player.removeItem(requiredKey);

        // Desbloquear la sala
        unlocked = true;

        // Cambiar tags
        tags.remove("locked");
        addTag("unlocked");

        // Mostrar mensaje de éxito
        System.out.println(unlockMessage);

        // Otorgar experiencia por resolver el puzzle
        player.getStats().addExperience(30);

        // Inmediatamente interactuar con la sala real
        if (actualRoom != null) {
            System.out.println("\n" + "═".repeat(50));
            actualRoom.interact(player);
        }

        return InteractionResult.success(
                "¡Puerta desbloqueada exitosamente!",
                InteractionType.UNLOCK
        );
    }

    /**
     * Muestra los requisitos para desbloquear.
     */
    private InteractionResult showLockRequirements() {
        System.out.println("🚫 La puerta permanece sellada.");
        System.out.println("🔍 Necesitas: " + requiredKey);
        System.out.println("💡 Explora el área para encontrar la llave requerida.");

        return InteractionResult.failure(
                "Acceso denegado - se requiere " + requiredKey
        );
    }

    /**
     * Delega la interacción a la sala real cuando está desbloqueada.
     */
    private InteractionResult delegateToActualRoom(Player player) {
        if (actualRoom != null) {
            actualRoom.interact(player);
            return InteractionResult.success(
                    "Interacción con sala desbloqueada",
                    InteractionType.DIALOGUE
            );
        } else {
            System.out.println("🕳️ La puerta se abre, pero no hay nada detrás...");
            return InteractionResult.success(
                    "Sala vacía detrás de la puerta",
                    InteractionType.DIALOGUE
            );
        }
    }

    // ============ MÉTODOS ESPECÍFICOS DE SALA BLOQUEADA ============

    /**
     * Verifica si la sala está desbloqueada.
     */
    public boolean isUnlocked() {
        return unlocked;
    }

    /**
     * Obtiene la llave requerida.
     */
    public String getRequiredKey() {
        return requiredKey;
    }

    /**
     * Obtiene la sala real detrás de la puerta.
     */
    public Room getActualRoom() {
        return actualRoom;
    }

    /**
     * Fuerza el desbloqueo (para debugging o eventos especiales).
     */
    public void forceUnlock() {
        unlocked = true;
        tags.remove("locked");
        addTag("unlocked");
        System.out.println("🔧 Puerta forzada a abrirse (comando especial).");
    }

    // ============ MÉTODOS SOBRESCRITOS ============

    @Override
    protected void onFirstVisit(Player player) {
        super.onFirstVisit(player);

        if (!unlocked) {
            System.out.println("🔍 Examinas la entrada bloqueada en busca de pistas...");
            givKeyHint();
        }
    }

    /**
     * Da una pista sobre dónde encontrar la llave.
     */
    private void givKeyHint() {
        String hint = switch (requiredKey) {
            case "Llave Secreta" -> "💡 Pista: Las llaves secretas suelen estar en habitaciones con tesoros.";
            case "Llave del Conocimiento" -> "💡 Pista: El conocimiento se adquiere superando desafíos.";
            case "Llave del Conocimiento Junior" -> "💡 Pista: Los tesoros verdaderos revelan llaves del conocimiento.";
            case "Llave de Snippet" -> "💡 Pista: Los fragmentos de código se encuentran en lugares inesperados.";
            default -> "💡 Pista: Explora todas las habitaciones y derrota a los enemigos.";
        };

        System.out.println(hint);
    }

    @Override
    protected void onSubsequentVisit(Player player) {
        super.onSubsequentVisit(player);

        if (unlocked) {
            System.out.println("🚪 La puerta permanece abierta, revelando los secretos que hay detrás.");
        } else {
            System.out.println("🔒 La puerta sigue bloqueada, esperando la llave correcta.");

            // Verificar si ahora tiene la llave
            if (player.hasItem(requiredKey)) {
                System.out.println("✨ ¡Ahora tienes la " + requiredKey + "! Puedes usar 'F' para abrir la puerta.");
            }
        }
    }

    @Override
    public String getInteractionPrompt() {
        if (unlocked) {
            return String.format("Presiona F para explorar %s (DESBLOQUEADA)", getName());
        } else {
            return String.format("Presiona F para examinar %s (Requiere: %s)", getName(), requiredKey);
        }
    }

    @Override
    public boolean isAccessible() {
        // La sala siempre es accesible para examinar, pero el contenido requiere llave
        return true;
    }
}
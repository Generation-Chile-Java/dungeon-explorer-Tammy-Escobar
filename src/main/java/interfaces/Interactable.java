package interfaces;

import models.player.Player;

public interface Interactable {

    InteractionResult interact(Player player);
    boolean canInteract(Player player);
    String getInteractionPrompt();
    int getInteractionPriority();

    class InteractionResult {
        private final boolean success;
        private final String message;
        private final InteractionType type;

        public InteractionResult(boolean success, String message, InteractionType type) {
            this.success = success;
            this.message = message;
            this.type = type;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public InteractionType getType() { return type; }

        public static InteractionResult success(String message, InteractionType type) {
            return new InteractionResult(true, message, type);
        }

        public static InteractionResult failure(String message) {
            return new InteractionResult(false, message, InteractionType.NONE);
        }
    }

    enum InteractionType {
        NONE("Ninguna"),
        DIALOGUE("Diálogo"),
        COMBAT("Combate"),
        LOOT("Botín"),
        UNLOCK("Desbloquear"),
        HEAL("Curación"),
        DAMAGE("Daño"),
        TELEPORT("Teletransporte");

        private final String description;
        InteractionType(String description) { this.description = description; }
        public String getDescription() { return description; }
    }
}
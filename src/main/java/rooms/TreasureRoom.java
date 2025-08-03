package rooms;

import interfaces.GameObject;
import interfaces.Interactable.InteractionResult;
import interfaces.Interactable.InteractionType;
import models.player.Player;
import models.items.Treasure;
import models.items.Key;
import java.util.*;

/**
 * TreasureRoom.java
 * Representa una sala que contiene tesoros u objetos valiosos.
 * Extiende BaseRoom y maneja la lógica específica de recompensas.
 */
public class TreasureRoom extends BaseRoom {

    // Tesoros y objetos en la sala
    private final List<GameObject> treasures;
    private final Set<String> collectedTreasures;

    // Configuración de la sala
    private final boolean allowMultipleLooting;
    private final int maxLootAttempts;
    private int lootAttempts;

    // Estado de la sala
    private boolean treasuresRevealed;
    private boolean requiresSearch;
    private final Random random;

    /**
     * Constructor principal para crear una sala de tesoros.
     */
    public TreasureRoom(String name, String description, List<GameObject> treasures) {
        super(name, description, RoomType.TREASURE);

        this.treasures = treasures != null ? new ArrayList<>(treasures) : new ArrayList<>();
        this.collectedTreasures = new HashSet<>();
        this.allowMultipleLooting = false;
        this.maxLootAttempts = 1;
        this.lootAttempts = 0;
        this.requiresSearch = false;
        this.treasuresRevealed = true;
        this.random = new Random();

        addTag("treasure");
        addTag("loot");
    }

    /**
     * Constructor simplificado para sala con un solo tesoro.
     */
    public TreasureRoom(String name, String description, GameObject treasure) {
        this(name, description,
                treasure != null ? List.of(treasure) : new ArrayList<>());
    }

    // ============ IMPLEMENTACIÓN DE INTERACCIÓN ============

    @Override
    protected InteractionResult executeRoomInteraction(Player player) {
        if (treasures.isEmpty()) {
            return handleEmptyTreasureRoom();
        }

        if (!canLoot()) {
            return handleLootingExhausted();
        }

        return handleTreasureCollection(player);
    }

    private InteractionResult handleEmptyTreasureRoom() {
        System.out.println("💔 Esta sala una vez contuvo grandes tesoros, pero ahora está vacía.");
        return InteractionResult.success(
                "Has explorado la sala vacía. Tal vez otros aventureros llegaron antes.",
                InteractionType.DIALOGUE
        );
    }

    private InteractionResult handleLootingExhausted() {
        System.out.println("🚫 Ya has explorado completamente esta sala.");
        return InteractionResult.success(
                "No hay más tesoros que recolectar aquí.",
                InteractionType.DIALOGUE
        );
    }

    private InteractionResult handleTreasureCollection(Player player) {
        lootAttempts++;

        showAvailableTreasures();
        return collectTreasures(player);
    }

    private void showAvailableTreasures() {
        System.out.println("💰 Tesoros disponibles:");

        for (int i = 0; i < treasures.size(); i++) {
            GameObject treasure = treasures.get(i);
            String rarity = treasure.getRarity().getSymbol();
            String status = collectedTreasures.contains(treasure.getName()) ? " (ya recolectado)" : "";

            System.out.printf("   %d. %s %s - %s%s%n",
                    i + 1, rarity, treasure.getName(),
                    treasure.getDescription(), status);
        }
    }

    private InteractionResult collectTreasures(Player player) {
        int treasuresCollected = 0;

        for (GameObject treasure : treasures) {
            if (!collectedTreasures.contains(treasure.getName())) {
                if (attemptTreasureCollection(treasure, player)) {
                    collectedTreasures.add(treasure.getName());
                    treasuresCollected++;
                }
            }
        }

        for (int i = 0; i < treasuresCollected; i++) {
            player.getStats().incrementTreasuresFound();
        }

        if (treasuresCollected > 0) {
            String message = String.format("¡Has recolectado %d tesoro(s)! Tu inventario ha sido actualizado.",
                    treasuresCollected);
            return InteractionResult.success(message, InteractionType.LOOT);
        } else {
            return InteractionResult.failure("No pudiste recolectar ningún tesoro en esta ocasión.");
        }
    }

    private boolean attemptTreasureCollection(GameObject treasure, Player player) {
        if (player.getInventory().isFull()) {
            System.out.println("🎒 Tu inventario está lleno. No puedes recoger " + treasure.getName());
            return false;
        }

        boolean added = player.addToInventory(treasure);

        if (added) {
            System.out.println("✨ ¡Has obtenido: " + treasure.getName() + "!");
            System.out.println("📜 " + treasure.getDescription());
        }

        return added;
    }

    // ============ MÉTODOS ESPECÍFICOS ============

    public boolean canLoot() {
        if (treasures.isEmpty()) return false;
        if (maxLootAttempts == -1) return true;
        return lootAttempts < maxLootAttempts;
    }

    public void addTreasure(GameObject treasure) {
        if (treasure != null) {
            treasures.add(treasure);
        }
    }

    public List<GameObject> getAvailableTreasures() {
        return treasures.stream()
                .filter(treasure -> !collectedTreasures.contains(treasure.getName()))
                .toList();
    }

    public Set<String> getCollectedTreasures() {
        return Collections.unmodifiableSet(collectedTreasures);
    }

    // ============ MÉTODOS SOBRESCRITOS ============

    @Override
    protected void onFirstVisit(Player player) {
        super.onFirstVisit(player);
        System.out.println("💰 Entras en una sala que brilla con promesas de tesoros...");
    }

    @Override
    protected void onSubsequentVisit(Player player) {
        super.onSubsequentVisit(player);

        if (collectedTreasures.isEmpty()) {
            System.out.println("💎 Los tesoros siguen esperando ser descubiertos...");
        } else if (getAvailableTreasures().isEmpty()) {
            System.out.println("🏛️ Has reclamado todos los tesoros de esta sala.");
        } else {
            System.out.println("💰 Aún quedan tesoros por recolectar aquí.");
        }
    }

    @Override
    public String getInteractionPrompt() {
        if (!treasures.isEmpty() && canLoot()) {
            int available = getAvailableTreasures().size();
            return String.format("Presiona F para explorar %s (%d tesoro(s) disponible(s))",
                    getName(), available);
        } else {
            return super.getInteractionPrompt();
        }
    }

    // ============ FACTORY METHODS ============

    public static class TreasureRoomFactory {

        public static TreasureRoom createBeginnerTreasureRoom() {
            List<GameObject> treasures = List.of(
                    new Treasure("Maven", "Gestión de dependencias sin dolor de cabeza.", 20),
                    new Key("Llave Básica", "Una llave simple pero funcional.", "Puerta Básica")
            );

            return new TreasureRoom(
                    "Cámara del Tesoro Novato",
                    "Una pequeña cámara con tesoros ideales para principiantes.",
                    treasures
            );
        }

        public static TreasureRoom createDeveloperToolsVault() {
            List<GameObject> treasures = List.of(
                    new Treasure("Git", "Control de versiones fundamental.", 25),
                    new Treasure("IntelliJ IDEA", "El IDE que hace programar un placer.", 30),
                    new Treasure("Docker", "Contenedores que funcionan en cualquier lugar.", 35)
            );

            return new TreasureRoom(
                    "Bóveda de Herramientas de Desarrollo",
                    "Una bóveda que contiene las herramientas más valiosas para desarrolladores.",
                    treasures
            );
        }
    }
}
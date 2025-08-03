package models.player;

import interfaces.GameObject;
import utils.GameConstants;
import java.util.UUID;

public class Player {

    private final String id;
    private String name;
    private PlayerLevel level;
    private PlayerStats stats;
    private Inventory inventory;
    private Position position;
    private PlayerState state;

    public Player(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = validateName(name);
        this.level = PlayerLevel.TRAINEE;
        this.stats = new PlayerStats(100);
        this.inventory = new Inventory();
        this.position = new Position(1, 1);
        this.state = PlayerState.ACTIVE;
    }

    private String validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "Desarrollador";
        }
        return name.trim();
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public PlayerLevel getLevel() { return level; }
    public PlayerStats getStats() { return stats; }
    public Inventory getInventory() { return inventory; }
    public Position getPosition() { return position; }
    public PlayerState getState() { return state; }

    public boolean isAlive() { return stats.getHealth() > 0 && state != PlayerState.DEAD; }
    public boolean canMove() { return isAlive() && state == PlayerState.ACTIVE; }
    public boolean canInteract() { return isAlive() && (state == PlayerState.ACTIVE || state == PlayerState.IN_COMBAT); }

    public void takeDamage(int damage) {
        if (damage <= 0) return;
        int actualDamage = Math.max(1, damage - stats.getDefense());
        stats.reduceHealth(actualDamage);
        if (!isAlive()) state = PlayerState.DEAD;
        System.out.println("💔 " + name + " recibió " + actualDamage + " puntos de daño. Vida: " + stats.getHealth());
    }

    public void heal(int amount) {
        if (amount <= 0) return;
        int actualHeal = stats.heal(amount);
        System.out.println("💚 " + name + " recuperó " + actualHeal + " puntos de vida. Vida: " + stats.getHealth());
    }

    public boolean addToInventory(GameObject item) {
        boolean added = inventory.addItem(item);
        if (added) {
            System.out.println("📦 " + item.getName() + " añadido al inventario.");
        } else {
            System.out.println("🎒 No hay espacio en el inventario para " + item.getName());
        }
        return added;
    }

    public boolean hasItem(String itemName) { return inventory.hasItem(itemName); }
    public boolean removeItem(String itemName) { return inventory.removeItem(itemName); }

    public void levelUp() {
        PlayerLevel nextLevel = level.getNext();
        if (nextLevel != null) {
            level = nextLevel;
            stats.increasePower(10);
            System.out.println("🎉 ¡Felicitaciones! Has avanzado a " + level.getDisplayName() + "!");
        }
    }

    public void setPosition(int x, int y) { this.position = new Position(x, y); }

    public void showStatus() {
        System.out.println("\n═══════════════════════════════════");
        System.out.println("👤 Jugador: " + name + " (" + level.getDisplayName() + ")");
        System.out.println("❤️  Vida: " + stats.getHealth() + "/" + stats.getMaxHealth());
        System.out.println("⚔️  Poder: " + stats.getPower());
        System.out.println("🛡️  Defensa: " + stats.getDefense());
        System.out.println("📍 Posición: " + position);
        System.out.println("🎒 Inventario: " + inventory.getSize() + "/" + inventory.getCapacity() + " objetos");
        inventory.displayItems();
        System.out.println("═══════════════════════════════════\n");
    }

    public static class Position {
        private final int x, y;
        public Position(int x, int y) { this.x = x; this.y = y; }
        public int getX() { return x; }
        public int getY() { return y; }
        @Override
        public String toString() { return "(" + x + ", " + y + ")"; }
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Position position = (Position) obj;
            return x == position.x && y == position.y;
        }
        @Override
        public int hashCode() { return 31 * x + y; }
    }

    public enum PlayerLevel {
        TRAINEE("Trainee", "👶", 1),
        JUNIOR("Junior", "👨‍💻", 2),
        SENIOR("Senior", "👨‍🏫", 3);

        private final String displayName;
        private final String icon;
        private final int levelNumber;

        PlayerLevel(String displayName, String icon, int levelNumber) {
            this.displayName = displayName;
            this.icon = icon;
            this.levelNumber = levelNumber;
        }

        public String getDisplayName() { return displayName; }
        public String getIcon() { return icon; }
        public int getLevelNumber() { return levelNumber; }

        public PlayerLevel getNext() {
            return switch (this) {
                case TRAINEE -> JUNIOR;
                case JUNIOR -> SENIOR;
                case SENIOR -> null;
            };
        }
    }

    public enum PlayerState {
        ACTIVE("Activo"),
        IN_COMBAT("En Combate"),
        RESTING("Descansando"),
        DEAD("Muerto"),
        LEVEL_TRANSITION("Transición de Nivel");

        private final String description;
        PlayerState(String description) { this.description = description; }
        public String getDescription() { return description; }
    }
}
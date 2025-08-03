package models.items;

import models.player.Player;
import java.util.Set;
import java.util.HashSet;

/**
 * Key.java
 * Representa llaves que pueden abrir puertas y desbloquear contenido.
 * Implementa lógica específica para objetos de acceso.
 */
public class Key extends BaseGameObject {

    // Propiedades específicas de la llave
    private final KeyType keyType;
    private final Set<String> unlockableTargets;
    private final boolean singleUse;
    private final int unlockPower;

    /**
     * Constructor principal para crear una llave.
     *
     * @param name nombre de la llave
     * @param description descripción de la llave
     * @param keyType tipo de llave
     * @param unlockableTargets objetivos que puede desbloquear
     * @param singleUse si la llave se consume al usar
     * @param rarity rareza de la llave
     */
    public Key(String name, String description, KeyType keyType,
               Set<String> unlockableTargets, boolean singleUse, Rarity rarity) {
        super(name, description, 0, ObjectType.KEY, rarity, singleUse, singleUse ? 1 : -1);
        this.keyType = keyType != null ? keyType : KeyType.STANDARD;
        this.unlockableTargets = unlockableTargets != null ? new HashSet<>(unlockableTargets) : new HashSet<>();
        this.singleUse = singleUse;
        this.unlockPower = calculateUnlockPower();
    }

    /**
     * Constructor simplificado para llaves básicas.
     */
    public Key(String name, String description, String target) {
        this(name, description, KeyType.STANDARD, Set.of(target), true, Rarity.COMMON);
    }

    /**
     * Constructor para llaves múltiples.
     */
    public Key(String name, String description, Set<String> targets) {
        this(name, description, KeyType.MASTER, targets, false, Rarity.RARE);
    }

    /**
     * Calcula el poder de desbloqueo basado en el tipo y objetivos.
     */
    private int calculateUnlockPower() {
        int basePower = keyType.getBasePower();
        int targetMultiplier = unlockableTargets.size();
        return basePower * Math.max(1, targetMultiplier);
    }

    // ============ IMPLEMENTACIÓN DE EFECTO ============

    @Override
    protected boolean executeEffect(Player player) {
        if (player == null) {
            return false;
        }

        // Las llaves no tienen efecto directo al ser "usadas" desde el inventario
        // Su funcionalidad principal es ser verificadas por puertas/salas
        showKeyInfo();
        return true;
    }

    /**
     * Muestra información sobre la llave.
     */
    private void showKeyInfo() {
        System.out.println("🗝️ " + name + " está lista para usar.");
        System.out.println("🔓 Puede desbloquear: " + String.join(", ", unlockableTargets));

        if (keyType == KeyType.MASTER) {
            System.out.println("👑 Esta es una llave maestra con poderes especiales.");
        }
    }

    // ============ MÉTODOS ESPECÍFICOS DE LLAVE ============

    /**
     * Verifica si esta llave puede desbloquear un objetivo específico.
     *
     * @param target objetivo a verificar
     * @return true si puede desbloquear el objetivo
     */
    public boolean canUnlock(String target) {
        if (target == null || target.trim().isEmpty()) {
            return false;
        }

        // Verificación exacta
        if (unlockableTargets.contains(target)) {
            return true;
        }

        // Verificación parcial para llaves maestras
        if (keyType == KeyType.MASTER) {
            return unlockableTargets.stream()
                    .anyMatch(unlockable -> target.toLowerCase().contains(unlockable.toLowerCase()) ||
                            unlockable.toLowerCase().contains(target.toLowerCase()));
        }

        return false;
    }

    /**
     * Intenta desbloquear un objetivo específico.
     *
     * @param target objetivo a desbloquear
     * @param player jugador que usa la llave
     * @return resultado del desbloqueo
     */
    public UnlockResult attemptUnlock(String target, Player player) {
        if (!canUnlock(target)) {
            return new UnlockResult(false, "Esta llave no puede desbloquear: " + target, this);
        }

        if (!canBeUsed()) {
            return new UnlockResult(false, "Esta llave ya no puede ser usada.", this);
        }

        // Simular proceso de desbloqueo
        boolean success = performUnlock(target);

        if (success) {
            String message = generateSuccessMessage(target);

            // Consumir la llave si es de un solo uso
            if (singleUse) {
                markAsConsumed();
            }

            // Dar experiencia al jugador
            if (player != null) {
                player.getStats().addExperience(unlockPower * 2);
            }

            return new UnlockResult(true, message, this);
        } else {
            return new UnlockResult(false, "Falló el desbloqueo de: " + target, this);
        }
    }

    /**
     * Realiza el proceso de desbloqueo.
     */
    private boolean performUnlock(String target) {
        // Lógica de desbloqueo basada en el tipo de llave
        return switch (keyType) {
            case STANDARD -> true; // Las llaves estándar siempre funcionan
            case MASTER -> true;   // Las llaves maestras siempre funcionan
            case MAGICAL -> Math.random() > 0.1; // 90% de éxito para llaves mágicas
            case ANCIENT -> Math.random() > 0.05; // 95% de éxito para llaves ancianas
            case SKELETON -> Math.random() > 0.2; // 80% de éxito para ganzúas
        };
    }

    /**
     * Genera mensaje de éxito personalizado.
     */
    private String generateSuccessMessage(String target) {
        return switch (keyType) {
            case STANDARD -> "🔓 La " + name + " abre " + target + " sin problemas.";
            case MASTER -> "👑 La llave maestra " + name + " desbloquea " + target + " con autoridad.";
            case MAGICAL -> "✨ La " + name + " brilla y " + target + " se abre mágicamente.";
            case ANCIENT -> "🏛️ Los antiguos mecanismos de " + target + " reconocen la " + name + ".";
            case SKELETON -> "🔧 La " + name + " manipula hábilmente los mecanismos de " + target + ".";
        };
    }

    /**
     * Marca la llave como consumida.
     */
    private void markAsConsumed() {
        usageCount++;
        if (singleUse) {
            // La llave se marca como usada en el sistema base
        }
    }

    // ============ GETTERS ESPECÍFICOS ============

    public KeyType getKeyType() {
        return keyType;
    }

    public Set<String> getUnlockableTargets() {
        return new HashSet<>(unlockableTargets);
    }

    public boolean isSingleUse() {
        return singleUse;
    }

    public int getUnlockPower() {
        return unlockPower;
    }

    // ============ MÉTODOS SOBRESCRITOS ============

    @Override
    protected void onSuccessfulUse(Player player) {
        System.out.println("🗝️ " + name + " está preparada para abrir cerraduras.");
    }

    @Override
    public BaseGameObject clone() {
        return new Key(name, description, keyType, unlockableTargets, singleUse, rarity);
    }

    @Override
    public String getDetailedInfo() {
        StringBuilder info = new StringBuilder(super.getDetailedInfo());
        info.append("\n🔑 Tipo: ").append(keyType.getDisplayName());
        info.append("\n🎯 Objetivos: ").append(unlockableTargets.size());
        info.append("\n⚡ Poder: ").append(unlockPower);
        info.append("\n♻️ Reutilizable: ").append(singleUse ? "No" : "Sí");

        if (!unlockableTargets.isEmpty()) {
            info.append("\n🔓 Puede abrir:");
            unlockableTargets.forEach(target -> info.append("\n   • ").append(target));
        }

        return info.toString();
    }

    // ============ CLASES AUXILIARES ============

    /**
     * Resultado de un intento de desbloqueo.
     */
    public static class UnlockResult {
        private final boolean success;
        private final String message;
        private final Key keyUsed;

        public UnlockResult(boolean success, String message, Key keyUsed) {
            this.success = success;
            this.message = message;
            this.keyUsed = keyUsed;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public Key getKeyUsed() { return keyUsed; }
    }

    // ============ FACTORY METHODS ============

    /**
     * Factory para crear llaves predefinidas del juego.
     */
    public static class KeyFactory {

        public static Key createSecretKey() {
            return new Key(
                    "Llave Secreta",
                    "Una llave antigua que brilla con sabiduría ancestral.",
                    KeyType.ANCIENT,
                    Set.of("Habitación Secreta", "Puerta Secreta"),
                    true,
                    Rarity.UNCOMMON
            );
        }

        public static Key createKnowledgeKey() {
            return new Key(
                    "Llave del Conocimiento",
                    "La llave que abre las puertas del entendimiento.",
                    KeyType.MASTER,
                    Set.of("Puerta del Conocimiento", "Sanctum del Saber"),
                    true,
                    Rarity.EPIC
            );
        }

        public static Key createSnippetKey() {
            return new Key(
                    "Llave de Snippet",
                    "Una llave que contiene fragmentos de código poderoso.",
                    KeyType.MAGICAL,
                    Set.of("Puerta de Snippet", "Habitación de Código"),
                    false,
                    Rarity.RARE
            );
        }

        public static Key createMasterKey() {
            return new Key(
                    "Llave Maestra",
                    "Una llave que puede abrir cualquier cerradura.",
                    KeyType.MASTER,
                    Set.of("Cualquier Puerta", "Acceso Universal"),
                    false,
                    Rarity.LEGENDARY
            );
        }

        public static Key createDebugKey() {
            return new Key(
                    "Llave de Debug",
                    "Permite acceso a áreas de depuración secretas.",
                    KeyType.SKELETON,
                    Set.of("Console Debug", "Modo Desarrollador"),
                    true,
                    Rarity.RARE
            );
        }
    }

    /**
     * Enumeración para tipos de llaves.
     */
    public enum KeyType {
        STANDARD("Estándar", "🔑", 10),
        MASTER("Maestra", "👑", 50),
        MAGICAL("Mágica", "✨", 25),
        ANCIENT("Antigua", "🏛️", 30),
        SKELETON("Ganzúa", "🔧", 15);

        private final String displayName;
        private final String icon;
        private final int basePower;

        KeyType(String displayName, String icon, int basePower) {
            this.displayName = displayName;
            this.icon = icon;
            this.basePower = basePower;
        }

        public String getDisplayName() { return displayName; }
        public String getIcon() { return icon; }
        public int getBasePower() { return basePower; }
    }
}
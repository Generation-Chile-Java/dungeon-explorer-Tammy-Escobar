package models.items;

import models.player.Player;

/**
 * Treasure.java
 * Representa tesoros que pueden curar al jugador.
 * Extiende BaseGameObject y especializa el comportamiento para curación.
 */
public class Treasure extends BaseGameObject {

    // Propiedades específicas del tesoro
    private final int healingPower;
    private final boolean canOverheal;
    private final TreasureEffect additionalEffect;

    /**
     * Constructor principal para crear un tesoro.
     *
     * @param name nombre del tesoro
     * @param description descripción del tesoro
     * @param healingPower cantidad de vida que restaura
     * @param rarity rareza del tesoro
     * @param canOverheal si puede curar por encima del máximo
     * @param additionalEffect efecto adicional del tesoro
     */
    public Treasure(String name, String description, int healingPower,
                    Rarity rarity, boolean canOverheal, TreasureEffect additionalEffect) {
        super(name, description, healingPower, ObjectType.TREASURE, rarity, true, 1);
        this.healingPower = Math.max(1, healingPower);
        this.canOverheal = canOverheal;
        this.additionalEffect = additionalEffect != null ? additionalEffect : TreasureEffect.NONE;
    }

    /**
     * Constructor simplificado para tesoros básicos.
     */
    public Treasure(String name, String description, int healingPower) {
        this(name, description, healingPower, Rarity.COMMON, false, TreasureEffect.NONE);
    }

    /**
     * Constructor para tesoros con rareza específica.
     */
    public Treasure(String name, String description, int healingPower, Rarity rarity) {
        this(name, description, healingPower, rarity, false, TreasureEffect.NONE);
    }

    // ============ IMPLEMENTACIÓN DE EFECTO ============

    @Override
    protected boolean executeEffect(Player player) {
        if (player == null || !player.isAlive()) {
            return false;
        }

        // Verificar si el jugador necesita curación
        if (!canOverheal && player.getStats().getHealth() >= player.getStats().getMaxHealth()) {
            System.out.println("💚 Ya tienes la vida al máximo.");
            return false;
        }

        // Aplicar curación
        int healedAmount = applyHealing(player);

        // Aplicar efecto adicional si existe
        applyAdditionalEffect(player);

        // Mostrar mensaje de éxito
        showHealingMessage(player, healedAmount);

        return true;
    }

    /**
     * Aplica la curación al jugador.
     *
     * @param player jugador a curar
     * @return cantidad realmente curada
     */
    private int applyHealing(Player player) {
        if (canOverheal) {
            // Curación que puede exceder el máximo
            player.getStats().increaseMaxHealth(healingPower / 2); // Bonus temporal
            player.heal(healingPower);
            return healingPower;
        } else {
            // Curación normal
            return player.getStats().heal(healingPower);
        }
    }

    /**
     * Aplica efectos adicionales del tesoro.
     */
    private void applyAdditionalEffect(Player player) {
        switch (additionalEffect) {
            case POWER_BOOST:
                player.getStats().increasePower(5);
                System.out.println("⚡ Tu poder ha aumentado!");
                break;

            case DEFENSE_BOOST:
                player.getStats().increaseDefense(3);
                System.out.println("🛡️ Tu defensa ha aumentado!");
                break;

            case EXPERIENCE_BONUS:
                player.getStats().addExperience(50);
                System.out.println("⭐ Has ganado experiencia adicional!");
                break;

            case FULL_RESTORE:
                player.getStats().fullHeal();
                System.out.println("✨ ¡Restauración completa!");
                break;

            case LUCK_BLESSING:
                // Efecto temporal (se podría implementar un sistema de buffs)
                System.out.println("🍀 Sientes una bendición de suerte!");
                break;

            case NONE:
            default:
                // Sin efecto adicional
                break;
        }
    }

    /**
     * Muestra el mensaje de curación personalizado según la rareza.
     */
    private void showHealingMessage(Player player, int healedAmount) {
        String message = switch (rarity) {
            case COMMON -> "🔧 Has usado " + name + " y recuperaste " + healedAmount + " puntos de vida.";
            case UNCOMMON -> "💎 El " + name + " brilla suavemente mientras te cura " + healedAmount + " puntos.";
            case RARE -> "✨ El poder del " + name + " te envuelve, curando " + healedAmount + " puntos de vida!";
            case EPIC -> "🌟 ¡El " + name + " libera una energía mágica que restaura " + healedAmount + " puntos!";
            case LEGENDARY -> "🏆 ¡El legendario " + name + " desata su poder ancestral, curando " + healedAmount + " puntos de vida!";
        };

        System.out.println(message);
    }

    // ============ MÉTODOS ESPECÍFICOS DE TESORO ============

    /**
     * Obtiene el poder de curación del tesoro.
     */
    public int getHealingPower() {
        return healingPower;
    }

    /**
     * Verifica si puede curar por encima del máximo.
     */
    public boolean canOverheal() {
        return canOverheal;
    }

    /**
     * Obtiene el efecto adicional del tesoro.
     */
    public TreasureEffect getAdditionalEffect() {
        return additionalEffect;
    }

    /**
     * Calcula la efectividad del tesoro para un jugador específico.
     */
    public double calculateEffectiveness(Player player) {
        if (player == null || !player.isAlive()) {
            return 0.0;
        }

        int currentHealth = player.getStats().getHealth();
        int maxHealth = player.getStats().getMaxHealth();

        if (!canOverheal && currentHealth >= maxHealth) {
            return 0.0; // Sin utilidad si ya está al máximo
        }

        int potentialHealing = Math.min(healingPower, maxHealth - currentHealth);
        return (double) potentialHealing / healingPower;
    }

    // ============ MÉTODOS SOBRESCRITOS ============

    @Override
    protected void onSuccessfulUse(Player player) {
        super.onSuccessfulUse(player);

        // Incrementar estadística de tesoros usados
        player.getStats().incrementTreasuresFound();

        // Mensaje especial para tesoros legendarios
        if (rarity == Rarity.LEGENDARY) {
            System.out.println("🎊 ¡Has usado un tesoro legendario! Este momento quedará en la historia.");
        }
    }

    @Override
    protected boolean canBeUsed() {
        // Los tesoros siempre se pueden intentar usar
        return super.canBeUsed();
    }

    @Override
    public BaseGameObject clone() {
        return new Treasure(name, description, healingPower, rarity, canOverheal, additionalEffect);
    }

    @Override
    public String getDetailedInfo() {
        StringBuilder info = new StringBuilder(super.getDetailedInfo());
        info.append("\n🏥 Curación: ").append(healingPower).append(" HP");

        if (canOverheal) {
            info.append("\n💫 Puede curar por encima del máximo");
        }

        if (additionalEffect != TreasureEffect.NONE) {
            info.append("\n🌟 Efecto especial: ").append(additionalEffect.getDescription());
        }

        return info.toString();
    }

    // ============ FACTORY METHODS ============

    /**
     * Crea tesoros predefinidos para diferentes herramientas de desarrollo.
     */
    public static class TreasureFactory {

        public static Treasure createGitTreasure() {
            return new Treasure(
                    "Git - Control de Versiones",
                    "La herramienta fundamental para el control de versiones. Te permite volver atrás cuando todo se rompe.",
                    25,
                    Rarity.UNCOMMON,
                    false,
                    TreasureEffect.EXPERIENCE_BONUS
            );
        }

        public static Treasure createIntelliJTreasure() {
            return new Treasure(
                    "IntelliJ IDEA",
                    "El IDE que hace que programar sea un placer. Autocompletado mágico incluido.",
                    30,
                    Rarity.RARE,
                    false,
                    TreasureEffect.POWER_BOOST
            );
        }

        public static Treasure createMavenTreasure() {
            return new Treasure(
                    "Maven",
                    "Gestión de dependencias sin dolor de cabeza. Las librerías vienen solas.",
                    20,
                    Rarity.COMMON,
                    false,
                    TreasureEffect.NONE
            );
        }

        public static Treasure createDockerTreasure() {
            return new Treasure(
                    "Docker",
                    "Contenedores que funcionan en cualquier lugar. 'Pero en mi máquina funciona' ya no es excusa.",
                    35,
                    Rarity.EPIC,
                    false,
                    TreasureEffect.DEFENSE_BOOST
            );
        }

        public static Treasure createStackOverflowTreasure() {
            return new Treasure(
                    "Stack Overflow",
                    "La fuente de toda sabiduría. Alguien ya tuvo tu problema antes.",
                    50,
                    Rarity.LEGENDARY,
                    true,
                    TreasureEffect.FULL_RESTORE
            );
        }

        public static Treasure createCoffeeTreasure() {
            return new Treasure(
                    "Café Premium",
                    "El combustible de los desarrolladores. Sin esto, nada funciona.",
                    15,
                    Rarity.COMMON,
                    false,
                    TreasureEffect.LUCK_BLESSING
            );
        }
    }

    /**
     * Enumeración para efectos adicionales de los tesoros.
     */
    public enum TreasureEffect {
        NONE("Sin efecto especial"),
        POWER_BOOST("Aumenta el poder"),
        DEFENSE_BOOST("Aumenta la defensa"),
        EXPERIENCE_BONUS("Otorga experiencia adicional"),
        FULL_RESTORE("Restauración completa"),
        LUCK_BLESSING("Bendición de suerte");

        private final String description;

        TreasureEffect(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
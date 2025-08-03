package models.player;

/**
 * PlayerStats.java
 * Maneja las estadísticas del jugador de forma encapsulada.
 * Aplica el principio de responsabilidad única.
 */
public class PlayerStats {

    // Estadísticas de vida
    private int health;
    private int maxHealth;

    // Estadísticas de combate
    private int power;
    private int defense;

    // Estadísticas de experiencia
    private int experience;
    private int experienceToNext;

    // Estadísticas de juego
    private int roomsExplored;
    private int enemiesDefeated;
    private int treasuresFound;

    /**
     * Constructor que inicializa las estadísticas base.
     *
     * @param initialHealth vida inicial del jugador
     */
    public PlayerStats(int initialHealth) {
        this.maxHealth = Math.max(1, initialHealth);
        this.health = this.maxHealth;
        this.power = 10;
        this.defense = 5;
        this.experience = 0;
        this.experienceToNext = 100;
        this.roomsExplored = 0;
        this.enemiesDefeated = 0;
        this.treasuresFound = 0;
    }

    // ============ GETTERS ============

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getPower() {
        return power;
    }

    public int getDefense() {
        return defense;
    }

    public int getExperience() {
        return experience;
    }

    public int getExperienceToNext() {
        return experienceToNext;
    }

    public int getRoomsExplored() {
        return roomsExplored;
    }

    public int getEnemiesDefeated() {
        return enemiesDefeated;
    }

    public int getTreasuresFound() {
        return treasuresFound;
    }

    // ============ MÉTODOS DE VIDA ============

    /**
     * Reduce la vida del jugador.
     *
     * @param damage cantidad de daño
     */
    public void reduceHealth(int damage) {
        if (damage > 0) {
            health = Math.max(0, health - damage);
        }
    }

    /**
     * Cura al jugador y retorna la cantidad realmente curada.
     *
     * @param amount cantidad de curación
     * @return cantidad realmente curada
     */
    public int heal(int amount) {
        if (amount <= 0) return 0;

        int oldHealth = health;
        health = Math.min(maxHealth, health + amount);
        return health - oldHealth;
    }

    /**
     * Verifica si el jugador está en estado crítico.
     *
     * @return true si la vida está por debajo del 25%
     */
    public boolean isCritical() {
        return health <= (maxHealth * 0.25);
    }

    /**
     * Obtiene el porcentaje de vida actual.
     *
     * @return porcentaje de vida (0-100)
     */
    public double getHealthPercentage() {
        return maxHealth > 0 ? (double) health / maxHealth * 100 : 0;
    }

    // ============ MÉTODOS DE ESTADÍSTICAS ============

    /**
     * Aumenta la vida máxima del jugador.
     *
     * @param increase cantidad a aumentar
     */
    public void increaseMaxHealth(int increase) {
        if (increase > 0) {
            maxHealth += increase;
            health += increase; // También aumenta la vida actual
        }
    }

    /**
     * Aumenta el poder del jugador.
     *
     * @param increase cantidad a aumentar
     */
    public void increasePower(int increase) {
        if (increase > 0) {
            power += increase;
        }
    }

    /**
     * Aumenta la defensa del jugador.
     *
     * @param increase cantidad a aumentar
     */
    public void increaseDefense(int increase) {
        if (increase > 0) {
            defense += increase;
        }
    }

    // ============ MÉTODOS DE EXPERIENCIA ============

    /**
     * Añade experiencia al jugador.
     *
     * @param exp cantidad de experiencia
     * @return true si subió de nivel
     */
    public boolean addExperience(int exp) {
        if (exp <= 0) return false;

        experience += exp;

        if (experience >= experienceToNext) {
            return levelUp();
        }

        return false;
    }

    /**
     * Maneja la subida de nivel.
     *
     * @return true si subió de nivel
     */
    private boolean levelUp() {
        experience -= experienceToNext;
        experienceToNext = (int) (experienceToNext * 1.5); // Incremento progresivo

        // Bonus por subir de nivel
        increaseMaxHealth(20);
        increasePower(5);
        increaseDefense(3);

        return true;
    }

    // ============ MÉTODOS DE ESTADÍSTICAS DE JUEGO ============

    /**
     * Incrementa el contador de salas exploradas.
     */
    public void incrementRoomsExplored() {
        roomsExplored++;
        addExperience(10); // Experiencia por explorar
    }

    /**
     * Incrementa el contador de enemigos derrotados.
     */
    public void incrementEnemiesDefeated() {
        enemiesDefeated++;
        addExperience(25); // Experiencia por combate
    }

    /**
     * Incrementa el contador de tesoros encontrados.
     */
    public void incrementTreasuresFound() {
        treasuresFound++;
        addExperience(15); // Experiencia por tesoro
    }

    // ============ MÉTODOS DE UTILIDAD ============

    /**
     * Restaura completamente la vida del jugador.
     */
    public void fullHeal() {
        health = maxHealth;
    }

    /**
     * Resetea las estadísticas a valores base (para nuevo nivel).
     */
    public void resetForNewLevel() {
        fullHeal();
        // Mantiene power, defense y experiencia
    }

    /**
     * Obtiene un resumen de las estadísticas.
     *
     * @return string con el resumen
     */
    public String getSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("💪 Poder: ").append(power).append("\n");
        sb.append("🛡️ Defensa: ").append(defense).append("\n");
        sb.append("⭐ Experiencia: ").append(experience).append("/").append(experienceToNext).append("\n");
        sb.append("🏠 Salas exploradas: ").append(roomsExplored).append("\n");
        sb.append("⚔️ Enemigos derrotados: ").append(enemiesDefeated).append("\n");
        sb.append("💎 Tesoros encontrados: ").append(treasuresFound);
        return sb.toString();
    }

    /**
     * Clona las estadísticas para backup.
     *
     * @return copia de las estadísticas
     */
    public PlayerStats clone() {
        PlayerStats copy = new PlayerStats(this.maxHealth);
        copy.health = this.health;
        copy.power = this.power;
        copy.defense = this.defense;
        copy.experience = this.experience;
        copy.experienceToNext = this.experienceToNext;
        copy.roomsExplored = this.roomsExplored;
        copy.enemiesDefeated = this.enemiesDefeated;
        copy.treasuresFound = this.treasuresFound;
        return copy;
    }
}
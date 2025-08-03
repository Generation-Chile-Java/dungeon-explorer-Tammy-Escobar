package models.enemies;

import models.player.Player;
import utils.GameConstants;
import java.util.*;

/**
 * BugEnemy.java - VERSIÓN FINAL LIMPIA
 * Representa un enemigo tipo "bug" en el juego.
 * Los enemigos desafían al jugador con preguntas de programación.
 *
 * CORRECCIONES APLICADAS:
 * - Fixed combat state management
 * - Improved question attempt tracking
 * - Added proper combat termination conditions
 * - Enhanced debug capabilities
 * - Removed misplaced EnemyRoomFactory
 */
public class BugEnemy {

    // Propiedades básicas del enemigo
    private final String id;
    private final String name;
    private final String description;
    private final BugType bugType;

    // Estadísticas de combate
    private int health;
    private final int maxHealth;
    private final int damage;
    private final int defense;

    // Sistema de preguntas
    private final List<Question> questions;
    private final Set<String> usedQuestions;
    private final Random random;

    // Estado del enemigo
    private EnemyState state;
    private boolean defeated;
    private int questionAttempts;
    private final int maxQuestionAttempts;

    // ✅ Variables de control mejoradas
    private int totalCombatTurns;
    private boolean inActiveCombat;
    private long lastQuestionTime;

    /**
     * Constructor principal para crear un enemigo bug.
     */
    public BugEnemy(String name, String description, BugType bugType,
                    int health, int damage, int defense, List<Question> questions) {
        this.id = generateId();
        this.name = validateString(name, "Bug Desconocido");
        this.description = validateString(description, "Un bug misterioso acecha en las sombras.");
        this.bugType = bugType != null ? bugType : BugType.SYNTAX_ERROR;

        this.maxHealth = Math.max(1, health);
        this.health = this.maxHealth;
        this.damage = Math.max(1, damage);
        this.defense = Math.max(0, defense);

        this.questions = questions != null ? new ArrayList<>(questions) : new ArrayList<>();
        this.usedQuestions = new HashSet<>();
        this.random = new Random();

        this.state = EnemyState.IDLE;
        this.defeated = false;
        this.questionAttempts = 0;
        this.maxQuestionAttempts = 5;

        this.totalCombatTurns = 0;
        this.inActiveCombat = false;
        this.lastQuestionTime = 0;
    }

    /**
     * Constructor simplificado para enemigos básicos.
     */
    public BugEnemy(String name, BugType bugType, Question question) {
        this(name,
                "Un " + bugType.getDisplayName() + " que desafía tu conocimiento de programación.",
                bugType,
                bugType.getBaseHealth(),
                bugType.getBaseDamage(),
                bugType.getBaseDefense(),
                List.of(question));
    }

    /**
     * Genera un ID único para el enemigo.
     */
    private String generateId() {
        return "ENEMY_" + System.nanoTime() + "_" + (int)(Math.random() * 1000);
    }

    /**
     * Valida que una cadena no sea nula o vacía.
     */
    private String validateString(String input, String defaultValue) {
        return (input != null && !input.trim().isEmpty()) ? input.trim() : defaultValue;
    }

    // ============ GETTERS ============

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public BugType getBugType() { return bugType; }
    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public int getDamage() { return damage; }
    public int getDefense() { return defense; }
    public EnemyState getState() { return state; }
    public boolean isDefeated() { return defeated; }
    public int getQuestionAttempts() { return questionAttempts; }
    public int getMaxQuestionAttempts() { return maxQuestionAttempts; }
    public int getTotalCombatTurns() { return totalCombatTurns; }
    public boolean isInActiveCombat() { return inActiveCombat; }

    // ============ MÉTODOS DE COMBATE ============

    /**
     * Inicia el combate con el jugador.
     */
    public CombatResult startCombat(Player player) {
        if (defeated) {
            return new CombatResult(false, "Este " + name + " ya ha sido derrotado.", CombatResult.CombatType.ALREADY_DEFEATED);
        }

        if (inActiveCombat) {
            System.out.println("⚠️ Combate ya en progreso con " + name);
            return new CombatResult(true, "Combate ya activo", CombatResult.CombatType.COMBAT_STARTED);
        }

        state = EnemyState.IN_COMBAT;
        inActiveCombat = true;
        questionAttempts = 0;
        totalCombatTurns = 0;
        usedQuestions.clear();

        System.out.println("⚔️ ¡Un " + name + " salvaje apareció!");
        System.out.println("🐛 " + description);
        System.out.println("🧠 " + bugType.getCombatMessage());

        return new CombatResult(true, "Combate iniciado con " + name, CombatResult.CombatType.COMBAT_STARTED);
    }

    /**
     * Presenta una pregunta al jugador.
     */
    public Question getCurrentQuestion() {
        if (questions.isEmpty()) {
            return createDefaultQuestion();
        }

        lastQuestionTime = System.currentTimeMillis();

        List<Question> availableQuestions = questions.stream()
                .filter(q -> !usedQuestions.contains(q.getId()))
                .toList();

        if (availableQuestions.isEmpty()) {
            if (questions.size() > 1 && questionAttempts < maxQuestionAttempts) {
                usedQuestions.clear();
                availableQuestions = new ArrayList<>(questions);
                System.out.println("🔄 Todas las preguntas usadas, expandiendo pool...");
            } else {
                return questions.get(0);
            }
        }

        Question selectedQuestion = availableQuestions.get(random.nextInt(availableQuestions.size()));
        usedQuestions.add(selectedQuestion.getId());

        return selectedQuestion;
    }

    /**
     * Procesa la respuesta del jugador.
     */
    public CombatResult processAnswer(String playerAnswer, Question question, Player player) {
        if (!inActiveCombat) {
            return new CombatResult(false, "No hay combate activo", CombatResult.CombatType.ALREADY_DEFEATED);
        }

        if (question == null) {
            System.out.println("❌ Error: Pregunta nula recibida");
            return new CombatResult(false, "Error en pregunta", CombatResult.CombatType.ATTEMPTS_EXHAUSTED);
        }

        questionAttempts++;
        totalCombatTurns++;

        System.out.println("🎯 Intento " + questionAttempts + "/" + maxQuestionAttempts);

        if (question.isCorrectAnswer(playerAnswer)) {
            return handleCorrectAnswer(player);
        } else {
            return handleIncorrectAnswer(question, player);
        }
    }

    /**
     * Maneja respuesta correcta del jugador.
     */
    private CombatResult handleCorrectAnswer(Player player) {
        int damageToEnemy = calculateDamageToEnemy(player);
        takeDamage(damageToEnemy);

        System.out.println("✅ ¡Correcto! Tu conocimiento daña al " + name + " (" + damageToEnemy + " puntos)");

        if (health <= 0) {
            return handleEnemyDefeat(player);
        } else {
            System.out.println("🩸 " + name + " - Vida: " + health + "/" + maxHealth);

            if (questionAttempts >= maxQuestionAttempts) {
                System.out.println("⏰ Se alcanzó el límite de preguntas para este combate");
                return new CombatResult(true, "Límite de preguntas alcanzado", CombatResult.CombatType.ATTEMPTS_EXHAUSTED);
            }

            return new CombatResult(true, "Daño infligido al enemigo", CombatResult.CombatType.DAMAGE_DEALT);
        }
    }

    /**
     * Maneja la derrota del enemigo.
     */
    private CombatResult handleEnemyDefeat(Player player) {
        defeated = true;
        state = EnemyState.DEFEATED;
        inActiveCombat = false;

        int expReward = bugType.getExperienceReward();
        player.getStats().addExperience(expReward);

        System.out.println("💀 ¡Has derrotado al " + name + "!");
        System.out.println("⭐ Ganaste " + expReward + " puntos de experiencia!");

        return new CombatResult(true, name + " derrotado", CombatResult.CombatType.ENEMY_DEFEATED);
    }

    /**
     * Maneja respuesta incorrecta del jugador.
     */
    private CombatResult handleIncorrectAnswer(Question question, Player player) {
        System.out.println("❌ Incorrecto. La respuesta era: " + question.getCorrectAnswer());
        System.out.println("💡 " + question.getExplanation());

        int damageToPlayer = calculateDamageToPlayer();
        player.takeDamage(damageToPlayer);

        System.out.println("🐛 ¡El " + name + " contraataca con confusión! (-" + damageToPlayer + " HP)");

        if (!player.isAlive()) {
            state = EnemyState.VICTORIOUS;
            inActiveCombat = false;
            return new CombatResult(false, "El jugador ha sido derrotado", CombatResult.CombatType.PLAYER_DEFEATED);
        }

        if (questionAttempts >= maxQuestionAttempts) {
            state = EnemyState.IDLE;
            inActiveCombat = false;
            System.out.println("⏰ Se agotaron los intentos de combate con " + name);
            return new CombatResult(false, "Se agotaron los intentos de combate", CombatResult.CombatType.ATTEMPTS_EXHAUSTED);
        }

        return new CombatResult(false, "Respuesta incorrecta", CombatResult.CombatType.INCORRECT_ANSWER);
    }

    /**
     * Calcula el daño que recibe el enemigo.
     */
    private int calculateDamageToEnemy(Player player) {
        int baseDamage = player.getStats().getPower();
        int actualDamage = Math.max(1, baseDamage - defense);
        double typeBonus = 1.0 + (bugType.getDifficultyMultiplier() * 0.1);
        return (int) (actualDamage * typeBonus);
    }

    /**
     * Calcula el daño que recibe el jugador.
     */
    private int calculateDamageToPlayer() {
        return Math.max(1, damage);
    }

    /**
     * Aplica daño al enemigo.
     */
    public void takeDamage(int damage) {
        if (damage > 0) {
            health = Math.max(0, health - damage);
        }
    }

    /**
     * Crea preguntas por defecto con más variedad.
     */
    private Question createDefaultQuestion() {
        Question[] defaultQuestions = {
                new Question("default1", "¿Cuál es el principio más importante en programación?",
                        new String[]{"Copiar y pegar", "Clean Code", "Programar rápido", "No documentar"},
                        "Clean Code", "El código limpio es fundamental para el mantenimiento y escalabilidad del software."),

                new Question("default2", "¿Qué significa POO?",
                        new String[]{"Programación Orientada a Objetos", "Programación Original Optimizada", "Proceso Operativo Organizado", "Proyecto Oficial Ordenado"},
                        "Programación Orientada a Objetos", "POO es un paradigma de programación que usa objetos y clases."),

                new Question("default3", "¿Cuál es la estructura de datos más básica?",
                        new String[]{"Array", "LinkedList", "HashMap", "TreeSet"},
                        "Array", "El array es la estructura de datos más fundamental en programación.")
        };

        return defaultQuestions[random.nextInt(defaultQuestions.length)];
    }

    /**
     * Reinicia el estado del enemigo para un nuevo combate.
     */
    public void reset() {
        health = maxHealth;
        state = EnemyState.IDLE;
        defeated = false;
        questionAttempts = 0;
        totalCombatTurns = 0;
        inActiveCombat = false;
        usedQuestions.clear();
        lastQuestionTime = 0;

        System.out.println("🔄 " + name + " ha sido reiniciado para un nuevo encuentro");
    }

    /**
     * Fuerza el fin del combate (para situaciones de emergencia).
     */
    public void forceEndCombat() {
        inActiveCombat = false;
        state = EnemyState.IDLE;
        System.out.println("🛑 Combate con " + name + " terminado forzadamente");
    }

    /**
     * Verifica si el enemigo puede continuar en combate.
     */
    public boolean canContinueCombat() {
        return !defeated && inActiveCombat && questionAttempts < maxQuestionAttempts && health > 0;
    }

    /**
     * Obtiene información detallada del enemigo.
     */
    public String getDetailedInfo() {
        StringBuilder info = new StringBuilder();
        info.append("🐛 ").append(name).append(" (").append(bugType.getDisplayName()).append(")\n");
        info.append("📝 ").append(description).append("\n");
        info.append("❤️ Vida: ").append(health).append("/").append(maxHealth).append("\n");
        info.append("⚔️ Daño: ").append(damage).append("\n");
        info.append("🛡️ Defensa: ").append(defense).append("\n");
        info.append("❓ Preguntas disponibles: ").append(questions.size()).append("\n");
        info.append("🎯 Estado: ").append(state.getDescription()).append("\n");
        info.append("🔄 Intentos: ").append(questionAttempts).append("/").append(maxQuestionAttempts).append("\n");
        info.append("⚡ En combate: ").append(inActiveCombat ? "Sí" : "No").append("\n");
        info.append("💀 Derrotado: ").append(defeated ? "Sí" : "No");

        return info.toString();
    }

    /**
     * Obtiene estadísticas de combate.
     */
    public String getCombatStats() {
        return String.format("Combate: Turnos=%d, Intentos=%d/%d, Activo=%s, Vida=%d/%d",
                totalCombatTurns, questionAttempts, maxQuestionAttempts,
                inActiveCombat, health, maxHealth);
    }

    @Override
    public String toString() {
        return String.format("%s[%s] - %s (Vida: %d/%d, Intentos: %d/%d)",
                name, bugType.getDisplayName(),
                state.getDescription(), health, maxHealth, questionAttempts, maxQuestionAttempts);
    }

    // ============ CLASES AUXILIARES ============

    /**
     * Representa una pregunta de combate con soporte para A,B,C,D.
     */
    public static class Question {
        private final String id;
        private final String question;
        private final String[] options;
        private final String correctAnswer;
        private final String explanation;

        public Question(String id, String question, String[] options, String correctAnswer, String explanation) {
            this.id = id != null ? id : UUID.randomUUID().toString();
            this.question = question;
            this.options = options != null ? options.clone() : new String[0];
            this.correctAnswer = correctAnswer;
            this.explanation = explanation;
        }

        public String getId() { return id; }
        public String getQuestion() { return question; }
        public String[] getOptions() { return options.clone(); }
        public String getCorrectAnswer() { return correctAnswer; }
        public String getExplanation() { return explanation; }

        /**
         * Verifica respuesta con soporte para A,B,C,D y texto completo.
         */
        public boolean isCorrectAnswer(String answer) {
            if (answer == null || correctAnswer == null) {
                return false;
            }

            String cleanAnswer = answer.trim();

            // Soporte para respuestas por letra (A, B, C, D)
            if (cleanAnswer.length() == 1 && options.length > 0) {
                char answerChar = cleanAnswer.toUpperCase().charAt(0);
                int answerIndex = answerChar - 'A';

                if (answerIndex >= 0 && answerIndex < options.length) {
                    String selectedOption = options[answerIndex];
                    return correctAnswer.equalsIgnoreCase(selectedOption);
                }
            }

            // Soporte para respuesta completa
            return correctAnswer.equalsIgnoreCase(cleanAnswer);
        }

        public void displayQuestion() {
            System.out.println("\n❓ " + question);
            if (options.length > 0) {
                System.out.println("Opciones:");
                for (int i = 0; i < options.length; i++) {
                    System.out.println("   " + (char)('A' + i) + ") " + options[i]);
                }
            }
        }
    }

    /**
     * Resultado de una acción de combate.
     */
    public static class CombatResult {
        private final boolean success;
        private final String message;
        private final CombatType type;

        public CombatResult(boolean success, String message, CombatType type) {
            this.success = success;
            this.message = message;
            this.type = type;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public CombatType getType() { return type; }

        @Override
        public String toString() {
            return String.format("CombatResult[%s: %s (%s)]",
                    success ? "SUCCESS" : "FAILURE", message, type);
        }

        public enum CombatType {
            COMBAT_STARTED,
            CORRECT_ANSWER,
            INCORRECT_ANSWER,
            DAMAGE_DEALT,
            ENEMY_DEFEATED,
            PLAYER_DEFEATED,
            ATTEMPTS_EXHAUSTED,
            ALREADY_DEFEATED
        }
    }

    // ============ ENUMERACIONES ============

    /**
     * Tipos de bugs enemigos.
     */
    public enum BugType {
        SYNTAX_ERROR("Error de Sintaxis", "🔤", 30, 15, 2, 25, 1.0,
                "¡Prepárate para enfrentar errores de sintaxis!"),

        NULL_POINTER("NullPointer", "🚫", 40, 20, 3, 35, 1.2,
                "¡Cuidado con las referencias nulas!"),

        LOGIC_ERROR("Error Lógico", "🧠", 50, 25, 4, 45, 1.4,
                "¡Tu lógica será puesta a prueba!"),

        RUNTIME_ERROR("Error de Ejecución", "⚡", 60, 30, 5, 55, 1.6,
                "¡Errores que aparecen en tiempo de ejecución!"),

        MEMORY_LEAK("Memory Leak", "💾", 70, 35, 6, 65, 1.8,
                "¡La memoria se escapa sin control!"),

        INFINITE_LOOP("Bucle Infinito", "🔄", 80, 40, 7, 75, 2.0,
                "¡Atrapado en un ciclo sin fin!");

        private final String displayName;
        private final String icon;
        private final int baseHealth;
        private final int baseDamage;
        private final int baseDefense;
        private final int experienceReward;
        private final double difficultyMultiplier;
        private final String combatMessage;

        BugType(String displayName, String icon, int baseHealth, int baseDamage,
                int baseDefense, int experienceReward, double difficultyMultiplier,
                String combatMessage) {
            this.displayName = displayName;
            this.icon = icon;
            this.baseHealth = baseHealth;
            this.baseDamage = baseDamage;
            this.baseDefense = baseDefense;
            this.experienceReward = experienceReward;
            this.difficultyMultiplier = difficultyMultiplier;
            this.combatMessage = combatMessage;
        }

        public String getDisplayName() { return displayName; }
        public String getIcon() { return icon; }
        public int getBaseHealth() { return baseHealth; }
        public int getBaseDamage() { return baseDamage; }
        public int getBaseDefense() { return baseDefense; }
        public int getExperienceReward() { return experienceReward; }
        public double getDifficultyMultiplier() { return difficultyMultiplier; }
        public String getCombatMessage() { return combatMessage; }
    }

    /**
     * Estados posibles del enemigo.
     */
    public enum EnemyState {
        IDLE("En espera"),
        IN_COMBAT("En combate"),
        DEFEATED("Derrotado"),
        VICTORIOUS("Victorioso"),
        FLEEING("Huyendo");

        private final String description;

        EnemyState(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
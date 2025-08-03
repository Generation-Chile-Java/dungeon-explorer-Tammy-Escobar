package rooms;

import interfaces.Interactable.InteractionResult;
import interfaces.Interactable.InteractionType;
import models.player.Player;
import models.enemies.BugEnemy;
import models.enemies.BugEnemy.Question;
import models.enemies.BugEnemy.CombatResult;
import models.player.Player.PlayerLevel;
import utils.GameDisplay;
import java.util.*;

/**
 * EnemyRoom.java
 * Representa una sala que contiene un enemigo que el jugador debe derrotar.
 * Maneja la lógica de combate basada en preguntas de programación.
 */
public class EnemyRoom extends BaseRoom {

    // Enemigo principal de la sala
    private final BugEnemy enemy;

    // Estado del combate
    private boolean combatActive;
    private boolean enemyDefeated;
    private final boolean respawnEnemy;

    // Control de combate
    private Scanner scanner;
    private int combatTurns;
    private final int maxCombatTurns;

    // Metadatos del combate
    private long combatStartTime;
    private Question currentQuestion;

    /**
     * Constructor principal para crear una sala con enemigo.
     *
     * @param name nombre de la sala
     * @param description descripción de la sala
     * @param enemy enemigo que habita la sala
     * @param respawnEnemy si el enemigo reaparece después de ser derrotado
     */
    public EnemyRoom(String name, String description, BugEnemy enemy, boolean respawnEnemy) {
        super(name, description, RoomType.ENEMY);

        this.enemy = enemy != null ? enemy : createDefaultEnemy();
        this.combatActive = false;
        this.enemyDefeated = false;
        this.respawnEnemy = respawnEnemy;
        this.scanner = new Scanner(System.in);
        this.combatTurns = 0;
        this.maxCombatTurns = 10;
        this.combatStartTime = 0;
        this.currentQuestion = null;

        // Añadir tags apropiados
        addTag("combat");
        addTag("enemy");
        addTag(enemy.getBugType().name().toLowerCase());

        // Configurar metadata
        setMetadata("enemy_type", enemy.getBugType());
        setMetadata("difficulty", calculateDifficulty());
    }

    /**
     * Constructor simplificado para sala con enemigo básico.
     */
    public EnemyRoom(String name, String description, BugEnemy enemy) {
        this(name, description, enemy, false);
    }

    /**
     * Crea un enemigo por defecto si no se proporciona uno.
     */
    private BugEnemy createDefaultEnemy() {
        Question defaultQuestion = new Question(
                "default",
                "¿Cuál es el principio fundamental de la programación orientada a objetos?",
                new String[]{"Herencia", "Encapsulación", "Polimorfismo", "Todas las anteriores"},
                "Todas las anteriores",
                "POO se basa en cuatro principios: encapsulación, herencia, polimorfismo y abstracción."
        );

        return new BugEnemy("Bug Genérico", BugEnemy.BugType.SYNTAX_ERROR, defaultQuestion);
    }

    /**
     * Calcula la dificultad de la sala basada en el enemigo.
     */
    private int calculateDifficulty() {
        return switch (enemy.getBugType()) {
            case SYNTAX_ERROR -> 1;
            case NULL_POINTER -> 2;
            case LOGIC_ERROR -> 3;
            case RUNTIME_ERROR -> 4;
            case MEMORY_LEAK -> 5;
            case INFINITE_LOOP -> 6;
        };
    }

    // ============ IMPLEMENTACIÓN DE INTERACCIÓN ============

    @Override
    protected InteractionResult executeRoomInteraction(Player player) {
        // Si el enemigo ya fue derrotado y no reaparece
        if (enemyDefeated && !respawnEnemy) {
            return handleDefeatedEnemyRoom(player);
        }

        // Si hay combate activo, continuar
        if (combatActive) {
            return continueCombat(player);
        }

        // Iniciar nuevo combate
        return startCombat(player);
    }

    /**
     * Maneja la interacción cuando el enemigo ya fue derrotado.
     */
    private InteractionResult handleDefeatedEnemyRoom(Player player) {
        System.out.println("💀 Los restos del " + enemy.getName() + " yacen derrotados aquí.");
        System.out.println("🏆 Ya has demostrado tu superioridad sobre este bug.");

        // Pequeña recompensa por revisitar
        player.getStats().addExperience(5);

        return InteractionResult.success(
                "Has revisado el área donde derrotaste al " + enemy.getName(),
                InteractionType.DIALOGUE
        );
    }

    /**
     * Inicia un nuevo combate con el enemigo.
     */
    private InteractionResult startCombat(Player player) {
        // Reiniciar enemigo si reaparece
        if (respawnEnemy && enemyDefeated) {
            enemy.reset();
            enemyDefeated = false;
        }

        // Mostrar información del enemigo
        showEnemyEncounter();

        // Iniciar combate
        CombatResult result = enemy.startCombat(player);

        if (result.isSuccess()) {
            combatActive = true;
            combatStartTime = System.currentTimeMillis();
            combatTurns = 0;

            return continueCombat(player);
        } else {
            return InteractionResult.failure("No se pudo iniciar el combate: " + result.getMessage());
        }
    }

    /**
     * Muestra el encuentro con el enemigo.
     */
    private void showEnemyEncounter() {
        GameDisplay.printSeparator("¡ENCUENTRO CON ENEMIGO!");
        System.out.println("🐛 " + enemy.getName() + " bloquea tu camino!");
        System.out.println("📝 " + enemy.getDescription());
        System.out.println("❤️ Vida del enemigo: " + enemy.getHealth() + "/" + enemy.getMaxHealth());
        System.out.println("💡 PISTA: Usa 'F' para luchar o muévete a otra dirección para huir");
        GameDisplay.printSeparator();
    }

    /**
     * Continúa o maneja el combate activo.
     */
    private InteractionResult continueCombat(Player player) {
        // Verificar límites de combate
        if (combatTurns >= maxCombatTurns) {
            return handleCombatTimeout(player);
        }

        if (!enemy.canContinueCombat()) {
            return handleCombatEnd(player);
        }

        // Obtener y mostrar pregunta
        currentQuestion = enemy.getCurrentQuestion();
        if (currentQuestion == null) {
            return handleCombatError(player);
        }

        showCombatQuestion();

        // Obtener respuesta del jugador
        String playerAnswer = getPlayerAnswer();

        // Procesar respuesta
        return processCombatAnswer(playerAnswer, player);
    }

    /**
     * Muestra la pregunta de combate.
     */
    private void showCombatQuestion() {
        combatTurns++;
        System.out.println("\n🎯 TURNO " + combatTurns + "/" + maxCombatTurns);
        System.out.println("⚔️ El " + enemy.getName() + " te desafía:");

        currentQuestion.displayQuestion();
        System.out.println("\n💡 Puedes responder con la letra (A, B, C, D) o escribir la respuesta completa.");
    }

    /**
     * Obtiene la respuesta del jugador.
     */
    private String getPlayerAnswer() {
        System.out.print("\n💭 Tu respuesta: ");
        String answer = scanner.nextLine().trim();

        // Log para debugging
        if (hasMetadata("debug_mode")) {
            System.out.println("🔧 Debug: Respuesta recibida: '" + answer + "'");
        }

        return answer;
    }

    /**
     * Procesa la respuesta del jugador en combate.
     */
    private InteractionResult processCombatAnswer(String answer, Player player) {
        CombatResult result = enemy.processAnswer(answer, currentQuestion, player);

        // Mostrar resultado
        System.out.println("\n" + result.getMessage());

        switch (result.getType()) {
            case ENEMY_DEFEATED:
                return handleEnemyDefeated(player);

            case PLAYER_DEFEATED:
                return handlePlayerDefeated(player);

            case ATTEMPTS_EXHAUSTED:
                return handleAttemptsExhausted(player);

            case DAMAGE_DEALT:
            case INCORRECT_ANSWER:
                return InteractionResult.success(
                        "El combate continúa. Prepárate para la siguiente pregunta.",
                        InteractionType.COMBAT
                );

            default:
                return InteractionResult.success(result.getMessage(), InteractionType.COMBAT);
        }
    }

    /**
     * Maneja la derrota del enemigo.
     */
    private InteractionResult handleEnemyDefeated(Player player) {
        enemyDefeated = true;
        combatActive = false;

        System.out.println("🎉 ¡Has derrotado al " + enemy.getName() + "!");
        System.out.println("🧠 Tu conocimiento de programación ha prevalecido sobre el bug.");

        // Recompensas
        int experienceReward = enemy.getBugType().getExperienceReward();
        player.getStats().addExperience(experienceReward);
        player.getStats().incrementEnemiesDefeated();

        // Curación pequeña por victoria
        player.heal(10);

        // Cambiar tags
        addTag("cleared");
        removeTag("dangerous");

        return InteractionResult.success(
                "¡Victoria! Has derrotado al " + enemy.getName() + " y ganado " + experienceReward + " experiencia.",
                InteractionType.COMBAT
        );
    }

    /**
     * Maneja la derrota del jugador.
     */
    private InteractionResult handlePlayerDefeated(Player player) {
        combatActive = false;

        System.out.println("💀 Has sido derrotado por " + enemy.getName() + "...");
        System.out.println("🔄 Pero un buen desarrollador aprende de sus errores.");

        return InteractionResult.failure("Has sido derrotado en combate.");
    }

    /**
     * Maneja el agotamiento de intentos de combate.
     */
    private InteractionResult handleAttemptsExhausted(Player player) {
        combatActive = false;

        System.out.println("⏰ Se han agotado los intentos de combate.");
        System.out.println("🏃 El " + enemy.getName() + " se retira por ahora, pero volverá...");

        // El enemigo escapa pero el jugador no muere
        enemy.reset();

        return InteractionResult.success(
                "El combate ha terminado sin un vencedor claro.",
                InteractionType.COMBAT
        );
    }

    /**
     * Maneja el timeout del combate.
     */
    private InteractionResult handleCombatTimeout(Player player) {
        combatActive = false;

        System.out.println("⏰ ¡El combate ha durado demasiado!");
        System.out.println("🏃 Decides retirarte estratégicamente...");

        // Aplicar daño por timeout
        player.takeDamage(enemy.getDamage() / 2);

        return InteractionResult.failure("El combate terminó por tiempo agotado.");
    }

    /**
     * Maneja el final natural del combate.
     */
    private InteractionResult handleCombatEnd(Player player) {
        combatActive = false;

        if (enemy.isDefeated()) {
            return handleEnemyDefeated(player);
        } else if (!player.isAlive()) {
            return handlePlayerDefeated(player);
        } else {
            // Combate terminó sin conclusión clara
            System.out.println("⚔️ El combate ha llegado a su fin sin un vencedor claro.");
            System.out.println("🤝 Ambos combatientes se retiran para reagruparse.");

            return InteractionResult.success(
                    "El combate terminó en empate.",
                    InteractionType.COMBAT
            );
        }
    }

    /**
     * Maneja errores durante el combate.
     */
    private InteractionResult handleCombatError(Player player) {
        combatActive = false;

        System.out.println("❌ Ha ocurrido un error en el combate.");
        System.out.println("🔧 El sistema se está recuperando...");

        return InteractionResult.failure("Error en el sistema de combate.");
    }

    // ============ MÉTODOS ESPECÍFICOS DE SALA DE ENEMIGO ============

    /**
     * Verifica si hay combate activo.
     */
    public boolean isCombatActive() {
        return combatActive;
    }

    /**
     * Verifica si el enemigo fue derrotado.
     */
    public boolean isEnemyDefeated() {
        return enemyDefeated;
    }

    /**
     * Obtiene el enemigo de la sala.
     */
    public BugEnemy getEnemy() {
        return enemy;
    }

    /**
     * Verifica si la sala es peligrosa.
     */
    public boolean isDangerous() {
        return !enemyDefeated || (respawnEnemy && !combatActive);
    }

    /**
     * Fuerza el fin del combate (para emergencias).
     */
    public void endCombat() {
        if (combatActive) {
            combatActive = false;
            enemy.forceEndCombat();
            System.out.println("🛑 Combate terminado forzadamente.");
        }
    }

    /**
     * Reinicia el enemigo de la sala.
     */
    public void respawnEnemy() {
        if (respawnEnemy || !enemyDefeated) {
            enemy.reset();
            enemyDefeated = false;
            combatActive = false;
            combatTurns = 0;

            removeTag("cleared");
            addTag("dangerous");

            System.out.println("🔄 " + enemy.getName() + " ha reaparecido en esta sala.");
        }
    }

    /**
     * Obtiene estadísticas del combate.
     */
    public String getCombatStatistics() {
        StringBuilder stats = new StringBuilder();
        stats.append("📊 Estadísticas de combate:\n");
        stats.append("   🐛 Enemigo: ").append(enemy.getName()).append("\n");
        stats.append("   ⚔️ Combate activo: ").append(combatActive ? "Sí" : "No").append("\n");
        stats.append("   💀 Enemigo derrotado: ").append(enemyDefeated ? "Sí" : "No").append("\n");
        stats.append("   🔄 Puede reaparecer: ").append(respawnEnemy ? "Sí" : "No").append("\n");
        stats.append("   🎯 Turnos de combate: ").append(combatTurns).append("/").append(maxCombatTurns).append("\n");

        if (combatActive && combatStartTime > 0) {
            long duration = (System.currentTimeMillis() - combatStartTime) / 1000;
            stats.append("   ⏱️ Duración: ").append(duration).append(" segundos");
        }

        return stats.toString();
    }

    // ============ MÉTODOS SOBRESCRITOS ============

    @Override
    protected void onFirstVisit(Player player) {
        super.onFirstVisit(player);

        if (!enemyDefeated) {
            System.out.println("⚠️ Sientes una presencia hostil en esta sala...");
            System.out.println("🔍 Un enemigo acecha en las sombras, listo para desafiarte.");
        }
    }

    @Override
    protected void onSubsequentVisit(Player player) {
        super.onSubsequentVisit(player);

        if (enemyDefeated && !respawnEnemy) {
            System.out.println("🏆 Esta sala ya ha sido limpiada de bugs.");
        } else if (enemyDefeated && respawnEnemy) {
            System.out.println("🔄 El enemigo podría haber reaparecido...");
        } else {
            System.out.println("⚔️ El enemigo sigue esperando un desafío.");
        }
    }

    @Override
    public String getInteractionPrompt() {
        if (combatActive) {
            return "Combate en progreso con " + enemy.getName();
        } else if (enemyDefeated && !respawnEnemy) {
            return "Presiona F para examinar " + getName() + " (enemigo derrotado)";
        } else {
            return "Presiona F para enfrentar a " + enemy.getName() + " en " + getName();
        }
    }

    @Override
    public boolean canInteract(Player player) {
        // Verificar que el jugador esté vivo y pueda luchar
        return super.canInteract(player) && player.isAlive();
    }

    @Override
    protected void showCannotInteractMessage(Player player) {
        if (!player.isAlive()) {
            System.out.println("💀 No puedes enfrentar enemigos en tu estado actual.");
        } else {
            super.showCannotInteractMessage(player);
        }
    }

    @Override
    public String getDetailedInfo() {
        StringBuilder info = new StringBuilder(super.getDetailedInfo());
        info.append("\n🐛 Enemigo: ").append(enemy.getName());
        info.append("\n💀 Estado: ").append(enemyDefeated ? "Derrotado" : "Activo");
        info.append("\n⚔️ Combate: ").append(combatActive ? "En progreso" : "Inactivo");
        info.append("\n🔄 Reaparece: ").append(respawnEnemy ? "Sí" : "No");
        info.append("\n🎯 Dificultad: ").append(getMetadata("difficulty"));

        return info.toString();
    }

    // ============ MÉTODOS DE UTILIDAD ============

    /**
     * Remueve un tag de la sala.
     */
    private void removeTag(String tag) {
        getTags().remove(tag.toLowerCase());
    }

    // ============ FACTORY METHODS ============

    /**
     * Factory para crear salas de enemigos predefinidas.
     */
    public static class EnemyRoomFactory {

        /**
         * Crea una sala de enemigo para nivel Trainee.
         */
        public static EnemyRoom createTraineeBugRoom() {
            Question question = new Question(
                    "trainee1",
                    "¿Qué significa JVM?",
                    new String[]{"Java Virtual Machine", "Java Version Manager", "Java Visual Mode", "Java Variable Method"},
                    "Java Virtual Machine",
                    "JVM (Java Virtual Machine) es el entorno de ejecución donde se ejecuta el bytecode de Java."
            );

            BugEnemy enemy = new BugEnemy("SyntaxBug Novato", BugEnemy.BugType.SYNTAX_ERROR, question);

            return new EnemyRoom(
                    "Cueva del Bug Novato",
                    "Una pequeña cueva donde habita un bug principiante, perfecto para entrenar.",
                    enemy,
                    false
            );
        }

        /**
         * Crea una sala de enemigo para nivel Junior.
         */
        public static EnemyRoom createJuniorBugRoom() {
            Question question = new Question(
                    "junior1",
                    "¿Cuál de estos es un principio SOLID?",
                    new String[]{"Single Responsibility", "Open/Closed", "Liskov Substitution", "Todos los anteriores"},
                    "Todos los anteriores",
                    "SOLID incluye: Single Responsibility, Open/Closed, Liskov Substitution, Interface Segregation y Dependency Inversion."
            );

            BugEnemy enemy = new BugEnemy(
                    "LogicBug Intermedio",
                    "Un bug más astuto que desafía tu lógica de programación.",
                    BugEnemy.BugType.LOGIC_ERROR,
                    50, 25, 5,
                    List.of(question)
            );

            return new EnemyRoom(
                    "Laboratorio del Error Lógico",
                    "Un laboratorio donde los errores lógicos toman forma y vida.",
                    enemy,
                    false
            );
        }

        /**
         * Crea una sala de enemigo para nivel Senior.
         */
        public static EnemyRoom createSeniorBugRoom() {
            List<Question> questions = Arrays.asList(
                    new Question(
                            "senior1",
                            "¿Qué patrón de diseño permite crear objetos sin especificar sus clases exactas?",
                            new String[]{"Factory", "Singleton", "Observer", "Strategy"},
                            "Factory",
                            "El patrón Factory Method permite crear objetos sin especificar su clase exacta."
                    ),
                    new Question(
                            "senior2",
                            "¿Cuál es la complejidad temporal del algoritmo QuickSort en el caso promedio?",
                            new String[]{"O(n)", "O(n log n)", "O(n²)", "O(log n)"},
                            "O(n log n)",
                            "QuickSort tiene complejidad O(n log n) en el caso promedio."
                    )
            );

            BugEnemy enemy = new BugEnemy(
                    "RuntimeError Avanzado",
                    "Un poderoso bug que domina los errores de ejecución más complejos.",
                    BugEnemy.BugType.RUNTIME_ERROR,
                    80, 35, 8,
                    questions
            );

            return new EnemyRoom(
                    "Fortaleza del Error de Ejecución",
                    "Una fortaleza donde los errores más peligrosos aguardan a los desarrolladores senior.",
                    enemy,
                    false
            );
        }

        /**
         * Crea una sala con el boss final.
         */
        public static EnemyRoom createBossRoom() {
            List<Question> bossQuestions = Arrays.asList(
                    new Question(
                            "boss1",
                            "¿Cuál es el objetivo principal de la arquitectura hexagonal?",
                            new String[]{"Separar lógica de negocio de detalles técnicos", "Usar 6 capas", "Optimizar rendimiento", "Reducir código"},
                            "Separar lógica de negocio de detalles técnicos",
                            "La arquitectura hexagonal busca separar la lógica de negocio de los detalles de implementación."
                    ),
                    new Question(
                            "boss2",
                            "¿Qué significa el principio DRY en programación?",
                            new String[]{"Don't Repeat Yourself", "Do Review Yearly", "Debug Right Yesterday", "Deploy Regularly Yet"},
                            "Don't Repeat Yourself",
                            "DRY (Don't Repeat Yourself) promueve evitar la duplicación de código."
                    ),
                    new Question(
                            "boss3",
                            "¿Cuál es la diferencia principal entre == y .equals() en Java?",
                            new String[]{"== compara referencias, .equals() compara contenido", "No hay diferencia", "== es más rápido", "== compara contenido"},
                            "== compara referencias, .equals() compara contenido",
                            "== compara referencias de objetos, mientras .equals() compara el contenido según su implementación."
                    )
            );

            BugEnemy bossEnemy = new BugEnemy(
                    "El Bug Ancestral",
                    "El bug más poderoso y antiguo del reino del código. Maestro de todos los tipos de errores.",
                    BugEnemy.BugType.INFINITE_LOOP,
                    120, 45, 10,
                    bossQuestions
            );

            EnemyRoom bossRoom = new EnemyRoom(
                    "Sanctum del Bug Ancestral",
                    "El sanctum final donde reside el bug más poderoso. Solo los verdaderos maestros del código pueden derrotarlo.",
                    bossEnemy,
                    false
            );

            // Configurar como sala de boss
            bossRoom.addTag("boss");
            bossRoom.addTag("final");
            bossRoom.setMetadata("is_boss_room", true);
            bossRoom.setMetadata("difficulty", 10);

            return bossRoom;
        }

        /**
         * Crea una sala de enemigo aleatoria según el nivel.
         */
        public static EnemyRoom createRandomBugRoom(PlayerLevel level) {
            return switch (level) {
                case TRAINEE -> createTraineeBugRoom();
                case JUNIOR -> createJuniorBugRoom();
                case SENIOR -> createSeniorBugRoom();
            };
        }

        /**
         * Crea una sala de entrenamiento con enemigo débil.
         */
        public static EnemyRoom createTrainingRoom() {
            Question easyQuestion = new Question(
                    "training1",
                    "¿Cuál es el tipo de dato para números enteros en Java?",
                    new String[]{"int", "string", "float", "char"},
                    "int",
                    "int es el tipo de dato primitivo para números enteros en Java."
            );

            BugEnemy trainingEnemy = new BugEnemy(
                    "Bug de Entrenamiento",
                    "Un bug muy débil diseñado para entrenar nuevos desarrolladores.",
                    BugEnemy.BugType.SYNTAX_ERROR,
                    20, 10, 1,
                    List.of(easyQuestion)
            );

            return new EnemyRoom(
                    "Dojo del Desarrollador",
                    "Un espacio seguro para entrenar tus habilidades contra bugs menores.",
                    trainingEnemy,
                    true // Reaparece para entrenar múltiples veces
            );
        }
    }
}
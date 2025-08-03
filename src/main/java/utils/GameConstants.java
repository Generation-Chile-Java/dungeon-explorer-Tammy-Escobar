package utils;

/**
 * GameConstants.java
 * Define todas las constantes del juego en un lugar centralizado.
 * Aplica el principio DRY y facilita el mantenimiento.
 */
public final class GameConstants {

    // ============ PREVENIR INSTANCIACIÓN ============
    private GameConstants() {
        throw new UnsupportedOperationException("Esta es una clase de utilidades y no debe ser instanciada");
    }

    // ============ CONFIGURACIÓN DEL JUGADOR ============

    /** Vida inicial del jugador */
    public static final int INITIAL_HEALTH = 100;

    /** Capacidad por defecto del inventario */
    public static final int DEFAULT_INVENTORY_CAPACITY = 10;

    /** Poder inicial del jugador */
    public static final int INITIAL_POWER = 10;

    /** Defensa inicial del jugador */
    public static final int INITIAL_DEFENSE = 5;

    /** Experiencia inicial */
    public static final int INITIAL_EXPERIENCE = 0;

    /** Experiencia necesaria para el primer nivel */
    public static final int INITIAL_EXPERIENCE_TO_NEXT = 100;

    // ============ CONFIGURACIÓN DE COMBATE ============

    /** Daño mínimo que siempre se aplica */
    public static final int MINIMUM_DAMAGE = 1;

    /** Multiplicador de daño crítico */
    public static final double CRITICAL_DAMAGE_MULTIPLIER = 1.5;

    /** Probabilidad base de crítico (%) */
    public static final int BASE_CRITICAL_CHANCE = 10;

    /** Experiencia por derrotar enemigo */
    public static final int EXPERIENCE_PER_ENEMY = 25;

    /** Experiencia por explorar sala */
    public static final int EXPERIENCE_PER_ROOM = 10;

    /** Experiencia por encontrar tesoro */
    public static final int EXPERIENCE_PER_TREASURE = 15;

    // ============ CONFIGURACIÓN DE MAPAS ============

    /** Tamaño del mapa Trainee - Ancho */
    public static final int TRAINEE_MAP_WIDTH = 3;

    /** Tamaño del mapa Trainee - Alto */
    public static final int TRAINEE_MAP_HEIGHT = 3;

    /** Tamaño del mapa Junior - Ancho */
    public static final int JUNIOR_MAP_WIDTH = 4;

    /** Tamaño del mapa Junior - Alto */
    public static final int JUNIOR_MAP_HEIGHT = 3;

    /** Tamaño del mapa Senior - Ancho */
    public static final int SENIOR_MAP_WIDTH = 5;

    /** Tamaño del mapa Senior - Alto */
    public static final int SENIOR_MAP_HEIGHT = 4;

    /** Posición inicial X del jugador */
    public static final int INITIAL_PLAYER_X = 1;

    /** Posición inicial Y del jugador */
    public static final int INITIAL_PLAYER_Y = 1;

    // ============ CONFIGURACIÓN DE OBJETOS ============

    /** Valor base de tesoros comunes */
    public static final int COMMON_TREASURE_VALUE = 20;

    /** Valor base de tesoros poco comunes */
    public static final int UNCOMMON_TREASURE_VALUE = 30;

    /** Valor base de tesoros raros */
    public static final int RARE_TREASURE_VALUE = 40;

    /** Valor base de tesoros épicos */
    public static final int EPIC_TREASURE_VALUE = 60;

    /** Valor base de tesoros legendarios */
    public static final int LEGENDARY_TREASURE_VALUE = 100;

    /** Usos por defecto para objetos reutilizables */
    public static final int DEFAULT_REUSABLE_USES = -1; // Infinito

    /** Usos por defecto para objetos consumibles */
    public static final int DEFAULT_CONSUMABLE_USES = 1;

    // ============ CONFIGURACIÓN DE ENEMIGOS ============

    /** Vida base de enemigos Trainee */
    public static final int TRAINEE_ENEMY_HEALTH = 30;

    /** Vida base de enemigos Junior */
    public static final int JUNIOR_ENEMY_HEALTH = 50;

    /** Vida base de enemigos Senior */
    public static final int SENIOR_ENEMY_HEALTH = 80;

    /** Daño base de enemigos Trainee */
    public static final int TRAINEE_ENEMY_DAMAGE = 15;

    /** Daño base de enemigos Junior */
    public static final int JUNIOR_ENEMY_DAMAGE = 25;

    /** Daño base de enemigos Senior */
    public static final int SENIOR_ENEMY_DAMAGE = 35;

    /** Tiempo límite para responder preguntas (segundos) */
    public static final int QUESTION_TIME_LIMIT = 30;

    // ============ CONFIGURACIÓN DE NIVELES ============

    /** Nombre del nivel Trainee */
    public static final String TRAINEE_LEVEL_NAME = "Desarrollador Trainee";

    /** Nombre del nivel Junior */
    public static final String JUNIOR_LEVEL_NAME = "Desarrollador Junior";

    /** Nombre del nivel Senior */
    public static final String SENIOR_LEVEL_NAME = "Desarrollador Senior";

    /** Multiplicador de experiencia por nivel */
    public static final double LEVEL_EXPERIENCE_MULTIPLIER = 1.5;

    /** Bonus de vida por subir de nivel */
    public static final int LEVEL_UP_HEALTH_BONUS = 20;

    /** Bonus de poder por subir de nivel */
    public static final int LEVEL_UP_POWER_BONUS = 5;

    /** Bonus de defensa por subir de nivel */
    public static final int LEVEL_UP_DEFENSE_BONUS = 3;

    // ============ CONFIGURACIÓN DE UI ============

    /** Ancho de la línea de separación */
    public static final int SEPARATOR_WIDTH = 50;

    /** Caracteres para la línea de separación */
    public static final String SEPARATOR_CHAR = "═";

    /** Tiempo de pausa entre mensajes (ms) */
    public static final int MESSAGE_DELAY = 1000;

    /** Número máximo de líneas en pantalla */
    public static final int MAX_SCREEN_LINES = 25;

    // ============ COMANDOS DEL JUEGO ============

    /** Comando para moverse hacia arriba */
    public static final String COMMAND_MOVE_UP = "w";

    /** Comando para moverse hacia abajo */
    public static final String COMMAND_MOVE_DOWN = "s";

    /** Comando para moverse hacia la izquierda */
    public static final String COMMAND_MOVE_LEFT = "a";

    /** Comando para moverse hacia la derecha */
    public static final String COMMAND_MOVE_RIGHT = "d";

    /** Comando para interactuar */
    public static final String COMMAND_INTERACT = "f";

    /** Comando para ver inventario */
    public static final String COMMAND_INVENTORY = "v";

    /** Comando para usar objeto */
    public static final String COMMAND_USE_ITEM = "x";

    /** Comando para ver estado */
    public static final String COMMAND_STATUS = "status";

    /** Comando para mostrar ayuda */
    public static final String COMMAND_HELP = "h";

    /** Comando para salir del juego */
    public static final String COMMAND_QUIT = "q";

    /** Comando para guardar juego */
    public static final String COMMAND_SAVE = "save";

    /** Comando para cargar juego */
    public static final String COMMAND_LOAD = "load";

    // ============ MENSAJES DEL SISTEMA ============

    /** Mensaje de bienvenida - CORREGIDO */
    public static final String WELCOME_MESSAGE = """
        ═══════════════════════════════════════════════════════════
                         🏰 DUNGEON EXPLORER 🏰                   
        ═══════════════════════════════════════════════════════════
        
        En el reino del código existe una leyenda sobre un poderoso
        bug ancestral que perturba la armonía de los programas.
        Solo encontrando la llave del tesoro podrás restaurar la
        paz y convertirte en un verdadero desarrollador Senior.
        
        Comenzarás como Trainee y deberás superar desafíos para
        avanzar a Junior y finalmente a Senior.
        
        ═══════════════════════════════════════════════════════════
        """;

    /** Mensaje de Game Over */
    public static final String GAME_OVER_MESSAGE = """
        💀 ¡Game Over! Has sido derrotado por los bugs...
        🔄 ¡Pero no te rindas! Un buen desarrollador aprende de sus errores.
        """;

    /** Mensaje de victoria */
    public static final String VICTORY_MESSAGE = """
        🎉 ¡FELICITACIONES! 🎉
        🏆 Has completado tu transformación de Trainee a Senior Developer!
        🌟 Has encontrado la llave del tesoro y restaurado la paz en el reino del código!
        👨‍🏫 Ahora eres un verdadero maestro del desarrollo de software.
        """;

    /** Mensaje de comando inválido */
    public static final String INVALID_COMMAND_MESSAGE = "❓ Comando no reconocido. Usa 'h' para ver la ayuda.";

    /** Mensaje de inventario lleno */
    public static final String INVENTORY_FULL_MESSAGE = "🎒 Tu inventario está lleno. No puedes recoger más objetos.";

    /** Mensaje de inventario vacío */
    public static final String INVENTORY_EMPTY_MESSAGE = "🎒 Tu inventario está vacío.";

    // ============ PISTAS DE NAVEGACIÓN ============

    /** Pistas para el mapa Trainee */
    public static final String[] TRAINEE_MAP_HINTS = {
            "💡 PISTA: El mapa Trainee es 3x3. Explora todas las direcciones desde la sala principal.",
            "🗺️ MAPA: Norte y Sur desde sala principal tienen enemigos. Este tiene tesoros.",
            "🔍 EXPLORACIÓN: Busca la llave secreta en las habitaciones con tesoros.",
            "⚔️ COMBATE: Responde correctamente las preguntas para derrotar a los bugs.",
            "🏠 HOGAR: Siempre puedes volver a la sala principal (1,1) para descansar."
    };

    /** Pistas generales de navegación */
    public static final String[] NAVIGATION_HINTS = {
            "🧭 Usa W,A,S,D para moverte (Norte, Oeste, Sur, Este)",
            "🔍 Usa 'F' para interactuar con salas, enemigos y tesoros",
            "📊 Usa 'STATUS' para ver tu posición actual y estadísticas",
            "💡 Si te pierdes, usa 'H' para ver todos los comandos disponibles",
            "🗺️ Explora sistemáticamente: visita cada dirección desde cada sala"
    };

    // ============ NOMBRES DE ARCHIVOS ============

    /** Archivo de configuración principal */
    public static final String CONFIG_FILE = "game_config.properties";

    /** Archivo de preguntas Trainee */
    public static final String TRAINEE_QUESTIONS_FILE = "trainee_questions.json";

    /** Archivo de preguntas Junior */
    public static final String JUNIOR_QUESTIONS_FILE = "junior_questions.json";

    /** Archivo de preguntas Senior */
    public static final String SENIOR_QUESTIONS_FILE = "senior_questions.json";

    /** Archivo de mapa Trainee */
    public static final String TRAINEE_MAP_FILE = "trainee_map.json";

    /** Archivo de mapa Junior */
    public static final String JUNIOR_MAP_FILE = "junior_map.json";

    /** Archivo de mapa Senior */
    public static final String SENIOR_MAP_FILE = "senior_map.json";

    /** Directorio de guardado */
    public static final String SAVE_DIRECTORY = "saves/";

    /** Extensión de archivos de guardado */
    public static final String SAVE_FILE_EXTENSION = ".sav";

    // ============ CONFIGURACIÓN DE RENDIMIENTO ============

    /** Tiempo máximo de espera para operaciones (ms) */
    public static final int MAX_OPERATION_TIMEOUT = 5000;

    /** Número máximo de reintentos */
    public static final int MAX_RETRY_ATTEMPTS = 3;

    /** Tamaño del pool de threads */
    public static final int THREAD_POOL_SIZE = 4;

    /** Tiempo de vida del cache (ms) */
    public static final long CACHE_LIFETIME = 300000; // 5 minutos

    // ============ CONFIGURACIÓN DE DEBUG ============

    /** Habilitar modo debug */
    public static final boolean DEBUG_MODE = false;

    /** Habilitar logs detallados */
    public static final boolean VERBOSE_LOGGING = false;

    /** Habilitar comandos de desarrollador */
    public static final boolean DEVELOPER_COMMANDS = false;

    /** Nivel de log por defecto */
    public static final String DEFAULT_LOG_LEVEL = "INFO";

    // ============ CONFIGURACIÓN DE BALANCEO ============

    /** Probabilidad de encontrar tesoro raro (%) */
    public static final int RARE_TREASURE_CHANCE = 15;

    /** Probabilidad de encontrar tesoro épico (%) */
    public static final int EPIC_TREASURE_CHANCE = 5;

    /** Probabilidad de encontrar tesoro legendario (%) */
    public static final int LEGENDARY_TREASURE_CHANCE = 1;

    /** Multiplicador de dificultad por nivel */
    public static final double DIFFICULTY_MULTIPLIER = 1.3;

    /** Tiempo base de regeneración de vida (segundos) */
    public static final int HEALTH_REGEN_TIME = 60;

    /** Cantidad de vida regenerada por turno */
    public static final int HEALTH_REGEN_AMOUNT = 5;

    // ============ CONFIGURACIÓN DE EFECTOS ============

    /** Duración de efectos temporales (turnos) */
    public static final int TEMPORARY_EFFECT_DURATION = 5;

    /** Multiplicador de efectos de bendición */
    public static final double BLESSING_EFFECT_MULTIPLIER = 1.2;

    /** Duración de maldiciones (turnos) */
    public static final int CURSE_DURATION = 3;

    /** Reducción de maldiciones (%) */
    public static final int CURSE_REDUCTION_PERCENT = 20;

    // ============ CONFIGURACIÓN DE AUDIO (FUTURO) ============

    /** Volumen por defecto (0.0 - 1.0) */
    public static final float DEFAULT_VOLUME = 0.7f;

    /** Habilitar efectos de sonido */
    public static final boolean SOUND_EFFECTS_ENABLED = true;

    /** Habilitar música de fondo */
    public static final boolean BACKGROUND_MUSIC_ENABLED = true;

    // ============ MÉTODOS DE UTILIDAD ============

    /**
     * Obtiene el separador visual estándar.
     *
     * @return línea de separación
     */
    public static String getSeparator() {
        return SEPARATOR_CHAR.repeat(SEPARATOR_WIDTH);
    }

    /**
     * Obtiene el separador con un título centrado.
     *
     * @param title título a centrar
     * @return línea de separación con título
     */
    public static String getSeparatorWithTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            return getSeparator();
        }

        String cleanTitle = " " + title.trim() + " ";
        int titleLength = cleanTitle.length();

        if (titleLength >= SEPARATOR_WIDTH) {
            return cleanTitle;
        }

        int padding = (SEPARATOR_WIDTH - titleLength) / 2;
        String leftSide = SEPARATOR_CHAR.repeat(padding);
        String rightSide = SEPARATOR_CHAR.repeat(SEPARATOR_WIDTH - padding - titleLength);

        return leftSide + cleanTitle + rightSide;
    }

    /**
     * Obtiene una pista aleatoria de navegación.
     *
     * @return pista de navegación
     */
    public static String getRandomNavigationHint() {
        return NAVIGATION_HINTS[(int) (Math.random() * NAVIGATION_HINTS.length)];
    }

    /**
     * Obtiene una pista específica del mapa Trainee.
     *
     * @param index índice de la pista
     * @return pista del mapa
     */
    public static String getTraineeMapHint(int index) {
        if (index >= 0 && index < TRAINEE_MAP_HINTS.length) {
            return TRAINEE_MAP_HINTS[index];
        }
        return getRandomNavigationHint();
    }

    /**
     * Calcula el valor de un tesoro basado en su rareza.
     *
     * @param rarity rareza del tesoro
     * @return valor calculado
     */
    public static int getTreasureValueByRarity(String rarity) {
        return switch (rarity.toUpperCase()) {
            case "COMMON" -> COMMON_TREASURE_VALUE;
            case "UNCOMMON" -> UNCOMMON_TREASURE_VALUE;
            case "RARE" -> RARE_TREASURE_VALUE;
            case "EPIC" -> EPIC_TREASURE_VALUE;
            case "LEGENDARY" -> LEGENDARY_TREASURE_VALUE;
            default -> COMMON_TREASURE_VALUE;
        };
    }

    /**
     * Calcula la vida de un enemigo basada en el nivel.
     *
     * @param level nivel del juego
     * @return vida del enemigo
     */
    public static int getEnemyHealthByLevel(String level) {
        return switch (level.toUpperCase()) {
            case "TRAINEE" -> TRAINEE_ENEMY_HEALTH;
            case "JUNIOR" -> JUNIOR_ENEMY_HEALTH;
            case "SENIOR" -> SENIOR_ENEMY_HEALTH;
            default -> TRAINEE_ENEMY_HEALTH;
        };
    }

    /**
     * Calcula el daño de un enemigo basado en el nivel.
     *
     * @param level nivel del juego
     * @return daño del enemigo
     */
    public static int getEnemyDamageByLevel(String level) {
        return switch (level.toUpperCase()) {
            case "TRAINEE" -> TRAINEE_ENEMY_DAMAGE;
            case "JUNIOR" -> JUNIOR_ENEMY_DAMAGE;
            case "SENIOR" -> SENIOR_ENEMY_DAMAGE;
            default -> TRAINEE_ENEMY_DAMAGE;
        };
    }

    /**
     * Verifica si un comando es válido.
     *
     * @param command comando a verificar
     * @return true si es válido
     */
    public static boolean isValidCommand(String command) {
        if (command == null || command.trim().isEmpty()) {
            return false;
        }

        String cmd = command.trim().toLowerCase();
        return cmd.equals(COMMAND_MOVE_UP) ||
                cmd.equals(COMMAND_MOVE_DOWN) ||
                cmd.equals(COMMAND_MOVE_LEFT) ||
                cmd.equals(COMMAND_MOVE_RIGHT) ||
                cmd.equals(COMMAND_INTERACT) ||
                cmd.equals(COMMAND_INVENTORY) ||
                cmd.equals(COMMAND_USE_ITEM) ||
                cmd.equals(COMMAND_STATUS) ||
                cmd.equals(COMMAND_HELP) ||
                cmd.equals(COMMAND_QUIT) ||
                cmd.equals(COMMAND_SAVE) ||
                cmd.equals(COMMAND_LOAD);
    }

    /**
     * Obtiene la descripción de un comando.
     *
     * @param command comando
     * @return descripción del comando
     */
    public static String getCommandDescription(String command) {
        return switch (command.toLowerCase()) {
            case COMMAND_MOVE_UP -> "Mover hacia arriba";
            case COMMAND_MOVE_DOWN -> "Mover hacia abajo";
            case COMMAND_MOVE_LEFT -> "Mover hacia la izquierda";
            case COMMAND_MOVE_RIGHT -> "Mover hacia la derecha";
            case COMMAND_INTERACT -> "Interactuar con la sala";
            case COMMAND_INVENTORY -> "Ver inventario";
            case COMMAND_USE_ITEM -> "Usar objeto del inventario";
            case COMMAND_STATUS -> "Ver estado del jugador";
            case COMMAND_HELP -> "Mostrar ayuda";
            case COMMAND_QUIT -> "Salir del juego";
            case COMMAND_SAVE -> "Guardar partida";
            case COMMAND_LOAD -> "Cargar partida";
            default -> "Comando desconocido";
        };
    }
}
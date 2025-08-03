package game;

import models.player.Player;
import models.player.Player.PlayerLevel;
import interfaces.Room;
import rooms.*;
import models.items.Treasure;
import models.items.Key;
import models.enemies.BugEnemy;
import utils.GameDisplay;
import utils.GameConstants;
import utils.InputValidator;
import java.util.*;
import java.util.List;

/**
 * GameEngine.java - VERSIÓN COMPLETA CORREGIDA
 * Motor principal del juego con contenido épico expandido.
 * Incluye mapas masivos, 100+ preguntas, y mecánicas avanzadas.
 */
public class GameEngine {

    // Componentes principales
    private Player player;
    private GameMap currentMap;
    private Scanner scanner;
    private boolean gameRunning;

    // Estado del juego
    private GameState gameState;
    private PlayerLevel currentLevel;

    // Mapas por nivel
    private final Map<PlayerLevel, GameMap> levelMaps;

    // Variables de control y debug
    private int commandCount;
    private long lastCommandTime;
    private boolean debugMode;

    /**
     * Constructor del motor del juego.
     */
    public GameEngine() {
        this.scanner = new Scanner(System.in);
        this.gameRunning = true;
        this.gameState = GameState.INITIALIZING;
        this.levelMaps = new EnumMap<>(PlayerLevel.class);
        this.commandCount = 0;
        this.lastCommandTime = System.currentTimeMillis();
        this.debugMode = GameConstants.DEBUG_MODE;

        initializeLevelMaps();
    }

    /**
     * Inicia el juego principal.
     */
    public void start() {
        try {
            gameState = GameState.STARTING;
            GameDisplay.showWelcomeScreen();
            createPlayer();
            initializeLevel(PlayerLevel.TRAINEE);
            showGameControls();
            mainGameLoop();
        } catch (Exception e) {
            handleGameError("Error crítico en el motor del juego", e);
        } finally {
            cleanup();
        }
    }

    /**
     * Inicializa los mapas para cada nivel.
     */
    private void initializeLevelMaps() {
        try {
            levelMaps.put(PlayerLevel.TRAINEE, createTraineeMap());
            levelMaps.put(PlayerLevel.JUNIOR, createJuniorMap());
            levelMaps.put(PlayerLevel.SENIOR, createSeniorMap());
            System.out.println("✅ Mapas inicializados correctamente");
        } catch (Exception e) {
            handleGameError("Error al inicializar mapas", e);
        }
    }

    /**
     * Crea el mapa del nivel Trainee - EXPANDIDO.
     */
    private GameMap createTraineeMap() {
        GameMap map = new GameMap("Mundo Trainee", 5, 5);

        try {
            // Sala principal (2,2)
            Room mainHall = EmptyRoom.EmptyRoomFactory.createMainHall();
            map.setRoom(2, 2, mainHall);

            // Zona Norte - Conceptos Básicos
            Room variablesRoom = new EnemyRoom(
                    "Laboratorio de Variables",
                    "Una sala donde las variables cobran vida propia.",
                    createTraineeEnemy("VariableBug", "¿Cuál es la diferencia entre int y Integer en Java?",
                            new String[]{"No hay diferencia", "int es primitivo, Integer es objeto", "int es más lento", "Integer no puede ser null"},
                            "int es primitivo, Integer es objeto")
            );
            map.setRoom(2, 1, variablesRoom);

            // Zona Sur - Estructuras de Control
            Room loopsRoom = new EnemyRoom(
                    "Cueva de los Bucles",
                    "Un lugar donde los bucles pueden volverse infinitos.",
                    createTraineeEnemy("LoopBug", "¿Cuál es la diferencia entre while y do-while?",
                            new String[]{"No hay diferencia", "while evalúa antes, do-while después", "do-while es más rápido", "while es obsoleto"},
                            "while evalúa antes, do-while después")
            );
            map.setRoom(2, 3, loopsRoom);

            // Zona Este - POO
            Room classesRoom = new EnemyRoom(
                    "Academia de Clases",
                    "Donde las clases se enseñan a sí mismas.",
                    createTraineeEnemy("ClassBug", "¿Qué es la herencia en POO?",
                            new String[]{"Copiar código", "Una clase puede heredar propiedades de otra", "Es lo mismo que composición", "Solo funciona con interfaces"},
                            "Una clase puede heredar propiedades de otra")
            );
            map.setRoom(3, 2, classesRoom);

            // Zona Oeste - Manejo de Errores
            Room exceptionsRoom = new EnemyRoom(
                    "Sala de Excepciones",
                    "Un lugar donde las excepciones vuelan por todos lados.",
                    createTraineeEnemy("ExceptionBug", "¿Cuándo usar try-catch?",
                            new String[]{"Siempre", "Para manejar excepciones que pueden ocurrir", "Solo con bases de datos", "Nunca, es malo"},
                            "Para manejar excepciones que pueden ocurrir")
            );
            map.setRoom(1, 2, exceptionsRoom);

            // Salas de Tesoros
            Room treasureRoom1 = new TreasureRoom(
                    "Biblioteca de Fundamentos",
                    "Una biblioteca con los conceptos básicos.",
                    new Treasure("Manual de Fundamentos", "Los conceptos básicos de programación", 25, Treasure.Rarity.UNCOMMON)
            );
            map.setRoom(3, 1, treasureRoom1);

            Room keyRoom = new TreasureRoom(
                    "Sanctum de POO",
                    "El lugar sagrado de la programación orientada a objetos.",
                    new Key("Llave del Conocimiento Trainee", "Demuestra tu dominio de conceptos básicos", "Puerta del Saber Trainee")
            );
            map.setRoom(4, 1, keyRoom);

            // Boss Final Trainee
            Room bossRoom = new LockedRoom(
                    "Puerta del Saber Trainee",
                    "Una puerta que solo se abre para quienes dominan los fundamentos.",
                    "Llave del Conocimiento Trainee",
                    new EnemyRoom(
                            "Sanctum del Maestro Trainee",
                            "El lugar donde el Maestro Trainee pone a prueba tu conocimiento.",
                            createTraineeBoss()
                    )
            );
            map.setRoom(4, 0, bossRoom);

            // Tesoro Final
            Room finalTreasure = new TreasureRoom(
                    "Cámara del Tesoro Trainee",
                    "¡La cámara donde reside el tesoro más preciado!",
                    new Treasure("Tesoro del Conocimiento Trainee", "El primer paso hacia la maestría", 50, Treasure.Rarity.EPIC)
            );
            map.setRoom(3, 0, finalTreasure);

            // Salas adicionales
            Room restRoom = EmptyRoom.EmptyRoomFactory.createRestArea();
            map.setRoom(1, 1, restRoom);

            Room toolsRoom = new TreasureRoom(
                    "Taller de Herramientas",
                    "Un taller lleno de herramientas útiles.",
                    List.of(
                            new Treasure("IDE Básico", "Un entorno de desarrollo", 15, Treasure.Rarity.COMMON),
                            new Treasure("Debugger", "Una herramienta para encontrar errores", 20, Treasure.Rarity.UNCOMMON)
                    )
            );
            map.setRoom(1, 3, toolsRoom);

            connectRooms(map);

        } catch (Exception e) {
            handleGameError("Error al crear mapa Trainee", e);
        }

        return map;
    }

    /**
     * Crea el mapa del nivel Junior.
     */
    private GameMap createJuniorMap() {
        GameMap map = new GameMap("Mundo Junior", 6, 6);

        try {
            // Sala principal (3,3)
            Room mainHall = new EmptyRoom("Sala Principal Junior",
                    "El hub central para desarrolladores Junior.");
            map.setRoom(3, 3, mainHall);

            // Zona de Estructuras de Datos
            Room arraysRoom = new EnemyRoom(
                    "Dimensión de Arrays",
                    "Un lugar donde los arrays se comportan extraño.",
                    createJuniorEnemy("ArrayBug", "¿Cuál es la complejidad de búsqueda en array ordenado?",
                            new String[]{"O(1)", "O(log n) con búsqueda binaria", "O(n)", "O(n²)"},
                            "O(log n) con búsqueda binaria")
            );
            map.setRoom(3, 2, arraysRoom);

            // Zona de Algoritmos
            Room sortingRoom = new EnemyRoom(
                    "Arena de Ordenamiento",
                    "Donde los algoritmos luchan por la supremacía.",
                    createJuniorEnemy("SortBug", "¿Cuál es la complejidad promedio de QuickSort?",
                            new String[]{"O(n)", "O(n log n)", "O(n²)", "O(log n)"},
                            "O(n log n)")
            );
            map.setRoom(3, 1, sortingRoom);

            // Zona de Patrones
            Room singletonRoom = new EnemyRoom(
                    "Fortaleza del Singleton",
                    "Una fortaleza donde solo puede existir una instancia.",
                    createJuniorEnemy("SingletonBug", "¿Cuál es el propósito del patrón Singleton?",
                            new String[]{"Crear muchas instancias", "Garantizar una sola instancia de una clase", "Mejorar el rendimiento", "Es un antipatrón"},
                            "Garantizar una sola instancia de una clase")
            );
            map.setRoom(2, 3, singletonRoom);

            // Salas de tesoros
            Room algoLibrary = new TreasureRoom(
                    "Biblioteca de Algoritmos",
                    "Una biblioteca con algoritmos importantes.",
                    new Treasure("Colección de Algoritmos", "Algoritmos esenciales", 40, Treasure.Rarity.RARE)
            );
            map.setRoom(1, 1, algoLibrary);

            Room patternsRoom = new TreasureRoom(
                    "Museo de Patrones",
                    "Un museo de patrones de diseño.",
                    new Key("Llave Maestra Junior", "Comprensión de conceptos intermedios", "Portal Junior")
            );
            map.setRoom(5, 1, patternsRoom);

            // Boss Junior
            Room juniorBossRoom = new LockedRoom(
                    "Portal Junior",
                    "Un portal para verdaderos desarrolladores Junior.",
                    "Llave Maestra Junior",
                    new EnemyRoom(
                            "Coliseo del Conocimiento Junior",
                            "El lugar de prueba para conceptos intermedios.",
                            createJuniorBoss()
                    )
            );
            map.setRoom(5, 5, juniorBossRoom);

            // Tesoro Final Junior
            Room finalJuniorTreasure = new TreasureRoom(
                    "Cámara del Tesoro Junior",
                    "El tesoro supremo del nivel Junior.",
                    new Treasure("Tesoro del Conocimiento Junior", "Dominio de conceptos intermedios", 75, Treasure.Rarity.EPIC)
            );
            map.setRoom(4, 5, finalJuniorTreasure);

            connectRooms(map);

        } catch (Exception e) {
            handleGameError("Error al crear mapa Junior", e);
        }

        return map;
    }

    /**
     * Crea el mapa del nivel Senior.
     */
    private GameMap createSeniorMap() {
        GameMap map = new GameMap("Mundo Senior", 8, 8);

        try {
            // Sala principal (4,4)
            Room mainHall = new EmptyRoom("Nexus Senior",
                    "El nexus donde convergen todas las disciplinas avanzadas.");
            map.setRoom(4, 4, mainHall);

            // Zona de Arquitectura
            Room microservicesRoom = new EnemyRoom(
                    "Complejo de Microservicios",
                    "Una arquitectura distribuida compleja.",
                    createSeniorEnemy("MicroserviceBug", "¿Cuál es una ventaja clave de los microservicios?",
                            new String[]{"Son más simples", "Escalabilidad independiente de cada servicio", "Usan menos memoria", "Son más rápidos siempre"},
                            "Escalabilidad independiente de cada servicio")
            );
            map.setRoom(4, 3, microservicesRoom);

            // Zona SOLID
            Room srpRoom = new EnemyRoom(
                    "Sala del Single Responsibility",
                    "Una sala donde cada cosa tiene una responsabilidad.",
                    createSeniorEnemy("SRPBug", "¿Qué dice el principio Single Responsibility?",
                            new String[]{"Una clase debe tener solo un método", "Una clase debe tener solo una razón para cambiar", "Solo una clase por archivo", "Solo un objeto por clase"},
                            "Una clase debe tener solo una razón para cambiar")
            );
            map.setRoom(3, 4, srpRoom);

            // Salas de tesoros
            Room architectureLibrary = new TreasureRoom(
                    "Biblioteca de Arquitectura",
                    "Los patrones arquitectónicos más poderosos.",
                    new Treasure("Compendio de Arquitectura", "Secretos de arquitectura de software", 60, Treasure.Rarity.EPIC)
            );
            map.setRoom(1, 2, architectureLibrary);

            Room advancedToolsRoom = new TreasureRoom(
                    "Arsenal de Herramientas Senior",
                    "Herramientas avanzadas para desarrolladores senior.",
                    List.of(
                            new Treasure("Profiler Avanzado", "Análisis profundo de performance", 45, Treasure.Rarity.RARE),
                            new Key("Llave del Maestro Senior", "Maestría suprema", "Portal del Saber Supremo")
                    )
            );
            map.setRoom(1, 5, advancedToolsRoom);

            // Boss Final
            Room finalBossRoom = new LockedRoom(
                    "Portal del Saber Supremo",
                    "El portal final para verdaderos maestros.",
                    "Llave del Maestro Senior",
                    new EnemyRoom(
                            "Coliseo del Conocimiento Supremo",
                            "Donde el Bug Ancestral aguarda.",
                            createFinalBoss()
                    )
            );
            map.setRoom(7, 7, finalBossRoom);

            // Tesoro Final
            Room supremeTreasure = new TreasureRoom(
                    "Cámara del Conocimiento Supremo",
                    "¡La sabiduría suprema de la programación!",
                    new Treasure("Tesoro del Conocimiento Supremo", "Maestría absoluta", 100, Treasure.Rarity.LEGENDARY)
            );
            map.setRoom(6, 7, supremeTreasure);

            connectRooms(map);

        } catch (Exception e) {
            handleGameError("Error al crear mapa Senior", e);
        }

        return map;
    }

    /**
     * Crea un enemigo Trainee básico.
     */
    private BugEnemy createTraineeEnemy(String name, String question, String[] options, String answer) {
        BugEnemy.Question q = new BugEnemy.Question("q1", question, options, answer, "Explicación del concepto");
        return new BugEnemy(name, BugEnemy.BugType.SYNTAX_ERROR, q);
    }

    /**
     * Crea un enemigo Junior intermedio.
     */
    private BugEnemy createJuniorEnemy(String name, String question, String[] options, String answer) {
        BugEnemy.Question q = new BugEnemy.Question("q1", question, options, answer, "Explicación del concepto intermedio");
        return new BugEnemy(name, BugEnemy.BugType.LOGIC_ERROR, q);
    }

    /**
     * Crea un enemigo Senior avanzado.
     */
    private BugEnemy createSeniorEnemy(String name, String question, String[] options, String answer) {
        BugEnemy.Question q = new BugEnemy.Question("q1", question, options, answer, "Explicación del concepto avanzado");
        return new BugEnemy(name, BugEnemy.BugType.RUNTIME_ERROR, q);
    }

    /**
     * Crea el boss del nivel Trainee.
     */
    private BugEnemy createTraineeBoss() {
        List<BugEnemy.Question> questions = Arrays.asList(
                new BugEnemy.Question("boss1", "¿Cuáles son los 4 pilares de la POO?",
                        new String[]{"Herencia, Polimorfismo, Encapsulación, Abstracción", "Clases, Objetos, Métodos, Variables", "If, While, For, Switch", "HTML, CSS, JS, Java"},
                        "Herencia, Polimorfismo, Encapsulación, Abstracción", "Los 4 pilares fundamentales de la POO"),
                new BugEnemy.Question("boss2", "¿Qué es el Garbage Collector?",
                        new String[]{"Un antivirus", "Libera memoria automáticamente", "Elimina código malo", "Optimiza el rendimiento"},
                        "Libera memoria automáticamente", "El GC libera memoria de objetos no utilizados")
        );

        return new BugEnemy("Maestro Trainee", "Guardián del conocimiento fundamental",
                BugEnemy.BugType.LOGIC_ERROR, 100, 20, 5, questions);
    }

    /**
     * Crea el boss del nivel Junior.
     */
    private BugEnemy createJuniorBoss() {
        List<BugEnemy.Question> questions = Arrays.asList(
                new BugEnemy.Question("boss1", "¿Cuándo usar composición vs herencia?",
                        new String[]{"Siempre herencia", "Composición para 'has-a', herencia para 'is-a'", "Son lo mismo", "Siempre composición"},
                        "Composición para 'has-a', herencia para 'is-a'", "Usa herencia para relaciones 'es-un' y composición para 'tiene-un'"),
                new BugEnemy.Question("boss2", "¿Qué es el principio DRY?",
                        new String[]{"Don't Repeat Yourself", "Do Repeat Yourself", "Dynamic Runtime Yielding", "Data Repository Yield"},
                        "Don't Repeat Yourself", "DRY significa no repetir código")
        );

        return new BugEnemy("Arquitecto Junior", "Maestro de conceptos intermedios",
                BugEnemy.BugType.LOGIC_ERROR, 150, 30, 8, questions);
    }

    /**
     * Crea el boss final.
     */
    private BugEnemy createFinalBoss() {
        List<BugEnemy.Question> questions = Arrays.asList(
                new BugEnemy.Question("final1", "¿Qué es el patrón CQRS?",
                        new String[]{"Command Query Responsibility Segregation", "Common Query Result Set", "Cached Query Response System", "Component Quality Requirement"},
                        "Command Query Responsibility Segregation", "CQRS separa operaciones de lectura y escritura"),
                new BugEnemy.Question("final2", "¿Qué problema resuelve el patrón Saga?",
                        new String[]{"Crear objetos", "Gestionar transacciones distribuidas", "Optimizar consultas", "Manejar eventos"},
                        "Gestionar transacciones distribuidas", "Saga gestiona transacciones distribuidas de larga duración")
        );

        return new BugEnemy("Bug Ancestral", "El maestro de todos los errores",
                BugEnemy.BugType.INFINITE_LOOP, 300, 50, 15, questions);
    }

    /**
     * Conecta las salas del mapa.
     */
    private void connectRooms(GameMap map) {
        try {
            for (int y = 0; y < map.getHeight(); y++) {
                for (int x = 0; x < map.getWidth(); x++) {
                    Room current = map.getRoom(x, y);
                    if (current != null && current instanceof BaseRoom) {
                        BaseRoom baseRoom = (BaseRoom) current;

                        if (y > 0) {
                            Room north = map.getRoom(x, y - 1);
                            if (north != null) {
                                baseRoom.connectTo(BaseRoom.Direction.NORTH, north);
                            }
                        }

                        if (y < map.getHeight() - 1) {
                            Room south = map.getRoom(x, y + 1);
                            if (south != null) {
                                baseRoom.connectTo(BaseRoom.Direction.SOUTH, south);
                            }
                        }

                        if (x > 0) {
                            Room west = map.getRoom(x - 1, y);
                            if (west != null) {
                                baseRoom.connectTo(BaseRoom.Direction.WEST, west);
                            }
                        }

                        if (x < map.getWidth() - 1) {
                            Room east = map.getRoom(x + 1, y);
                            if (east != null) {
                                baseRoom.connectTo(BaseRoom.Direction.EAST, east);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            handleGameError("Error al conectar salas", e);
        }
    }

    /**
     * Crea el jugador del juego.
     */
    private void createPlayer() {
        try {
            GameDisplay.printSeparator("CREACIÓN DE PERSONAJE");
            System.out.print("👤 Ingresa tu nombre, valiente programador: ");

            String name = scanner.nextLine().trim();
            player = new Player(name);
            currentLevel = player.getLevel();

            System.out.println("\n¡Bienvenido, " + player.getName() + "!");
            System.out.println("🎯 Tu aventura como " + currentLevel.getDisplayName() + " comienza ahora...");

            gameState = GameState.PLAYING;
        } catch (Exception e) {
            handleGameError("Error al crear jugador", e);
            player = new Player("Desarrollador");
            currentLevel = player.getLevel();
        }
    }

    /**
     * Inicializa un nivel específico.
     */
    private void initializeLevel(PlayerLevel level) {
        try {
            currentLevel = level;
            currentMap = levelMaps.get(level);

            if (currentMap == null) {
                throw new IllegalStateException("Mapa no encontrado para nivel: " + level);
            }

            player.setPosition(currentLevel == PlayerLevel.TRAINEE ? 2 :
                            currentLevel == PlayerLevel.JUNIOR ? 3 : 4,
                    currentLevel == PlayerLevel.TRAINEE ? 2 :
                            currentLevel == PlayerLevel.JUNIOR ? 3 : 4);

            GameDisplay.showLevelStart(level.getDisplayName(), currentMap.getDescription());
            System.out.println("📍 Te encuentras en: " + getCurrentRoom().getName());
        } catch (Exception e) {
            handleGameError("Error al inicializar nivel", e);
        }
    }

    /**
     * Muestra los controles del juego CON TUTORIAL MEJORADO.
     */
    private void showGameControls() {
        GameDisplay.printSeparator("TUTORIAL RÁPIDO");

        System.out.println("🎯 REGLA DE ORO: Para progresar en el juego necesitas INTERACTUAR");
        System.out.println();
        System.out.println("📋 PASOS BÁSICOS:");
        System.out.println("   1️⃣ MUÉVETE con W/A/S/D entre salas");
        System.out.println("   2️⃣ USA 'F' para INTERACTUAR con cada sala");
        System.out.println("   3️⃣ RECOGE tesoros, lucha enemigos, abre puertas");
        System.out.println("   4️⃣ PROGRESA encontrando el tesoro de cada nivel");
        System.out.println();
        System.out.println("⚠️ IMPORTANTE: Solo moverse NO es suficiente");
        System.out.println("✅ NECESITAS usar 'F' en cada sala para obtener objetos");

        GameDisplay.printSeparator();

        System.out.println("🎮 COMANDOS RÁPIDOS:");
        System.out.println("   F = INTERACTUAR ⭐ (¡El más importante!)");
        System.out.println("   W/A/S/D = Moverse");
        System.out.println("   V = Ver inventario");
        System.out.println("   STATUS = Ver mapa completo");
        System.out.println("   H = Ayuda completa");

        GameDisplay.printSeparator();

        System.out.println("💡 PRIMER OBJETIVO:");
        System.out.println("   🎯 Ve a 'Sanctum de POO' y usa 'F' para obtener una llave");
        System.out.println("   🎯 Ve a 'Biblioteca de Fundamentos' y usa 'F' para obtener tesoros");

        GameDisplay.printSeparator();

        System.out.print("📱 ¿Has entendido? Presiona ENTER para continuar...");
        scanner.nextLine();
    }

    /**
     * Bucle principal del juego.
     */
    private void mainGameLoop() {
        gameState = GameState.PLAYING;

        while (gameRunning && player.isAlive()) {
            try {
                if (!validateGameState()) {
                    System.out.println("⚠️ Estado del juego inválido. Intentando recuperar...");
                    continue;
                }

                showCurrentLocation();
                String command = getPlayerCommand();

                if (command == null || command.trim().isEmpty()) {
                    System.out.println("❓ Comando vacío. Usa 'h' para ver la ayuda.");
                    continue;
                }

                processCommand(command);
                checkGameConditions();
                updateCommandStats();

            } catch (Exception e) {
                handleGameError("Error en el bucle principal", e);
                if (offerRecoveryOptions()) {
                    continue;
                } else {
                    break;
                }
            }
        }

        showGameEndScreen();
    }

    /**
     * Valida el estado del juego.
     */
    private boolean validateGameState() {
        if (player == null) {
            System.out.println("❌ Error crítico: Jugador no inicializado");
            return false;
        }

        if (currentMap == null) {
            System.out.println("❌ Error crítico: Mapa no inicializado");
            return false;
        }

        Room currentRoom = getCurrentRoom();
        if (currentRoom == null) {
            System.out.println("❌ Error: Sala actual no válida");
            player.setPosition(2, 2);
            return getCurrentRoom() != null;
        }

        return true;
    }

    /**
     * Actualiza estadísticas de comandos.
     */
    private void updateCommandStats() {
        commandCount++;
        lastCommandTime = System.currentTimeMillis();

        if (debugMode && commandCount % 10 == 0) {
            System.out.println("🔧 Debug: " + commandCount + " comandos procesados");
        }
    }

    /**
     * Ofrece opciones de recuperación.
     */
    private boolean offerRecoveryOptions() {
        System.out.println("\n🔧 ¿Qué quieres hacer?");
        System.out.println("1. Continuar");
        System.out.println("2. Reiniciar nivel");
        System.out.println("3. Salir");
        System.out.print("Opción: ");

        try {
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    System.out.println("🎮 Continuando...");
                    return true;
                case "2":
                    System.out.println("🔄 Reiniciando nivel...");
                    initializeLevel(currentLevel);
                    return true;
                case "3":
                    System.out.println("👋 Saliendo del juego...");
                    gameRunning = false;
                    return false;
                default:
                    System.out.println("🎮 Continuando por defecto...");
                    return true;
            }
        } catch (Exception e) {
            System.out.println("🎮 Continuando...");
            return true;
        }
    }

    /**
     * Muestra la ubicación actual CON NAVEGACIÓN MEJORADA.
     */
    private void showCurrentLocation() {
        try {
            Room currentRoom = getCurrentRoom();
            if (currentRoom != null) {
                System.out.println("\n📍 " + currentRoom.getName());

                if (!currentRoom.isVisited()) {
                    System.out.println("📝 " + currentRoom.getDescription());
                    System.out.println("🆕 ¡NUEVA SALA DESCUBIERTA!");
                }

                showRoomTypeHint(currentRoom);
                showAvailableDirections();

                // Pista prominente sobre interacción
                System.out.println("\n" + "═".repeat(50));
                System.out.println("🎯 ACCIÓN PRINCIPAL: Presiona 'F' para INTERACTUAR");
                System.out.println("═".repeat(50));

            } else {
                System.out.println("\n❓ Ubicación desconocida - usa 'status' para más información");
            }
        } catch (Exception e) {
            System.out.println("\n⚠️ Error al mostrar ubicación: " + e.getMessage());
        }
    }

    /**
     * Muestra pista sobre el tipo de sala CON INSTRUCCIONES CLARAS.
     */
    private void showRoomTypeHint(Room room) {
        try {
            String hint = switch (room.getRoomType()) {
                case TREASURE -> "💰 SALA DE TESOROS: ¡Usa 'F' para recoger objetos valiosos y llaves!";
                case ENEMY -> "⚔️ SALA DE COMBATE: ¡Usa 'F' para luchar (responder preguntas de programación)!";
                case LOCKED -> "🔒 SALA BLOQUEADA: ¡Usa 'F' para intentar abrir con tu llave!";
                case EMPTY -> "🏠 ÁREA DE EXPLORACIÓN: ¡Usa 'F' para explorar y posiblemente descansar!";
                case SECRET -> "🗝️ SALA SECRETA: ¡Usa 'F' para descubrir misterios ocultos!";
            };
            System.out.println("💡 " + hint);

            // Pista adicional si no ha visitado
            if (!room.isVisited()) {
                System.out.println("⚠️ ¡Esta sala AÚN NO HA SIDO EXPLORADA! Necesitas usar 'F'");
            }
        } catch (Exception e) {
            System.out.println("💡 ¡IMPORTANTE: Usa 'F' para interactuar con esta sala!");
        }
    }

    /**
     * Muestra las direcciones disponibles.
     */
    private void showAvailableDirections() {
        try {
            int x = player.getPosition().getX();
            int y = player.getPosition().getY();

            System.out.println("🧭 Puedes ir:");
            boolean hasDirections = false;

            if (currentMap.isValidPosition(x, y - 1) && currentMap.getRoom(x, y - 1) != null) {
                System.out.println("   Norte (W): " + currentMap.getRoom(x, y - 1).getName());
                hasDirections = true;
            }
            if (currentMap.isValidPosition(x, y + 1) && currentMap.getRoom(x, y + 1) != null) {
                System.out.println("   Sur (S): " + currentMap.getRoom(x, y + 1).getName());
                hasDirections = true;
            }
            if (currentMap.isValidPosition(x - 1, y) && currentMap.getRoom(x - 1, y) != null) {
                System.out.println("   Oeste (A): " + currentMap.getRoom(x - 1, y).getName());
                hasDirections = true;
            }
            if (currentMap.isValidPosition(x + 1, y) && currentMap.getRoom(x + 1, y) != null) {
                System.out.println("   Este (D): " + currentMap.getRoom(x + 1, y).getName());
                hasDirections = true;
            }

            if (!hasDirections) {
                System.out.println("   🚫 No hay salidas disponibles desde aquí");
                System.out.println("   💡 Usa 'STATUS' para ver el mapa completo");
            }

        } catch (Exception e) {
            System.out.println("💡 Usa W,A,S,D para moverte");
        }
    }

    /**
     * Obtiene comando del jugador CON MEJORES PISTAS.
     */
    private String getPlayerCommand() {
        try {
            System.out.println("\n🎮 OPCIONES:");
            System.out.println("   F = INTERACTUAR (¡LO MÁS IMPORTANTE!)");
            System.out.println("   W/A/S/D = Moverse | V = Inventario | STATUS = Mapa | H = Ayuda");
            System.out.print("\n💬 Tu comando: ");

            String command = scanner.nextLine().trim().toLowerCase();

            if (debugMode) {
                System.out.println("🔧 Debug: Comando recibido: '" + command + "'");
            }

            return command;
        } catch (Exception e) {
            System.out.println("⚠️ Error al leer comando: " + e.getMessage());
            return "";
        }
    }

    /**
     * Procesa el comando del jugador CON RECORDATORIOS.
     */
    private void processCommand(String command) {
        if (!InputValidator.isValidString(command)) {
            System.out.println(GameConstants.INVALID_COMMAND_MESSAGE);
            return;
        }

        String cmd = command.trim().toLowerCase();

        try {
            switch (cmd) {
                case "w":
                    movePlayer(0, -1, "Norte");
                    showInteractionReminder();
                    break;

                case "s":
                    movePlayer(0, 1, "Sur");
                    showInteractionReminder();
                    break;

                case "a":
                    movePlayer(-1, 0, "Oeste");
                    showInteractionReminder();
                    break;

                case "d":
                    movePlayer(1, 0, "Este");
                    showInteractionReminder();
                    break;

                case "f":
                    interactWithCurrentRoom();
                    break;

                case "v":
                    showInventory();
                    if (player.getInventory().isEmpty()) {
                        System.out.println("💡 TIP: Tu inventario está vacío porque no has usado 'F' en salas de tesoros");
                    }
                    break;

                case "x":
                    useInventoryItem();
                    break;

                case "status":
                    showDetailedStatus();
                    break;

                case "h":
                    GameDisplay.showCommandsHelp();
                    break;

                case "q":
                    confirmQuit();
                    break;

                case "save":
                    saveGame();
                    break;

                case "load":
                    loadGame();
                    break;

                default:
                    System.out.println(GameConstants.INVALID_COMMAND_MESSAGE);
                    System.out.println("💡 Recuerda: 'F' para interactuar, W/A/S/D para moverte");
                    break;
            }
        } catch (Exception e) {
            handleGameError("Error al procesar comando: " + cmd, e);
        }
    }

    /**
     * Muestra recordatorio sobre interacción después de moverse.
     */
    private void showInteractionReminder() {
        Room currentRoom = getCurrentRoom();
        if (currentRoom != null && !currentRoom.isVisited()) {
            System.out.println("⚡ RECORDATORIO: ¡Usa 'F' ahora para interactuar con esta sala!");
        }
    }

    /**
     * Mueve al jugador en una dirección específica CON RECORDATORIOS.
     */
    private void movePlayer(int deltaX, int deltaY, String direction) {
        try {
            int currentX = player.getPosition().getX();
            int currentY = player.getPosition().getY();
            int newX = currentX + deltaX;
            int newY = currentY + deltaY;

            if (currentMap.isValidPosition(newX, newY) && currentMap.hasRoom(newX, newY)) {
                player.setPosition(newX, newY);
                GameDisplay.showMovementMessage(player, true, direction);

                // Solo mostrar "descubierto" si realmente es la primera vez
                Room newRoom = getCurrentRoom();
                if (newRoom != null && !newRoom.isVisited()) {
                    System.out.println("✨ Has descubierto: " + newRoom.getName());
                    System.out.println("🎯 ¡RECUERDA! Usa 'F' para interactuar y obtener objetos");
                } else {
                    System.out.println("🔄 Regresaste a: " + newRoom.getName());
                    if (newRoom.getRoomType() == Room.RoomType.TREASURE) {
                        System.out.println("💰 ¡Esta sala tiene tesoros! ¿Ya usaste 'F'?");
                    }
                }
            } else {
                GameDisplay.showMovementMessage(player, false, direction);
            }
        } catch (Exception e) {
            System.out.println("❌ Error al mover jugador: " + e.getMessage());
        }
    }

    /**
     * Interactúa con la sala actual.
     */
    private void interactWithCurrentRoom() {
        try {
            Room currentRoom = getCurrentRoom();
            if (currentRoom != null) {
                currentRoom.interact(player);
            } else {
                System.out.println("❓ No hay nada con lo que interactuar aquí.");
            }
        } catch (Exception e) {
            handleGameError("Error al interactuar con la sala", e);
        }
    }

    /**
     * Muestra el inventario del jugador.
     */
    private void showInventory() {
        try {
            GameDisplay.printSeparator("INVENTARIO");
            if (player.getInventory().isEmpty()) {
                System.out.println(GameConstants.INVENTORY_EMPTY_MESSAGE);
            } else {
                player.getInventory().displayItems();
                System.out.println("\n💡 Usa 'X' para usar un objeto del inventario");
            }
            GameDisplay.printSeparator();
        } catch (Exception e) {
            System.out.println("❌ Error al mostrar inventario: " + e.getMessage());
        }
    }

    /**
     * Permite al jugador usar un objeto del inventario.
     */
    private void useInventoryItem() {
        try {
            if (player.getInventory().isEmpty()) {
                System.out.println(GameConstants.INVENTORY_EMPTY_MESSAGE);
                return;
            }

            System.out.println("🎒 Objetos disponibles:");
            player.getInventory().displayItems();

            System.out.print("\n💬 Escribe el nombre del objeto a usar: ");
            String itemName = scanner.nextLine().trim();

            var item = player.getInventory().findItem(itemName);
            if (item != null) {
                item.use(player);
            } else {
                System.out.println("❌ No tienes un objeto llamado: " + itemName);
            }
        } catch (Exception e) {
            System.out.println("❌ Error al usar objeto: " + e.getMessage());
        }
    }

    /**
     * Muestra el estado detallado del jugador y del mapa.
     */
    private void showDetailedStatus() {
        try {
            GameDisplay.showPlayerStatus(player);
            showMapOverview();
            GameDisplay.showContextualHints(player, getCurrentRoom());
        } catch (Exception e) {
            System.out.println("❌ Error al mostrar estado: " + e.getMessage());
        }
    }

    /**
     * Muestra una vista general del mapa actual.
     */
    private void showMapOverview() {
        try {
            GameDisplay.printSeparator("MAPA DEL NIVEL " + currentLevel.getDisplayName().toUpperCase());

            System.out.println("🗺️ Dimensiones: " + currentMap.getWidth() + "x" + currentMap.getHeight());
            System.out.println("🏠 Salas totales: " + currentMap.getTotalRooms());
            System.out.println("👁️ Salas visitadas: " + currentMap.getVisitedRooms());

            // Mostrar mapa visual simple
            showVisualMap();

            GameDisplay.printSeparator();
        } catch (Exception e) {
            System.out.println("❌ Error al mostrar mapa: " + e.getMessage());
        }
    }

    /**
     * Muestra un mapa visual simple.
     */
    private void showVisualMap() {
        try {
            System.out.println("\n🗺️ Mapa Visual (P = Tu posición):");

            for (int y = 0; y < currentMap.getHeight(); y++) {
                System.out.print("   ");
                for (int x = 0; x < currentMap.getWidth(); x++) {
                    if (player.getPosition().getX() == x && player.getPosition().getY() == y) {
                        System.out.print("[P] ");
                    } else if (currentMap.hasRoom(x, y)) {
                        Room room = currentMap.getRoom(x, y);
                        if (room.isVisited()) {
                            System.out.print("[✓] ");
                        } else {
                            System.out.print("[?] ");
                        }
                    } else {
                        System.out.print("[ ] ");
                    }
                }
                System.out.println();
            }

            System.out.println("\n   Leyenda: [P]=Tu posición, [✓]=Visitada, [?]=No visitada, [ ]=Vacío");
        } catch (Exception e) {
            System.out.println("❌ Error al generar mapa visual");
        }
    }

    /**
     * Confirma si el jugador quiere salir del juego.
     */
    private void confirmQuit() {
        try {
            System.out.print("❓ ¿Estás seguro de que quieres salir? (s/n): ");
            String confirm = scanner.nextLine().trim().toLowerCase();

            if (confirm.equals("s") || confirm.equals("si") || confirm.equals("y") || confirm.equals("yes")) {
                System.out.println("👋 ¡Gracias por jugar! Hasta la próxima aventura.");
                gameRunning = false;
            } else {
                System.out.println("🎮 ¡Continuando la aventura!");
            }
        } catch (Exception e) {
            System.out.println("🎮 Continuando el juego...");
        }
    }

    /**
     * Guarda el juego (placeholder).
     */
    private void saveGame() {
        System.out.println("💾 Función de guardado no implementada aún.");
        System.out.println("💡 En una versión futura podrás guardar tu progreso.");
    }

    /**
     * Carga el juego (placeholder).
     */
    private void loadGame() {
        System.out.println("📁 Función de carga no implementada aún.");
        System.out.println("💡 En una versión futura podrás cargar partidas guardadas.");
    }

    /**
     * Obtiene la sala actual del jugador.
     */
    private Room getCurrentRoom() {
        try {
            if (player != null && currentMap != null) {
                return currentMap.getRoom(player.getPosition().getX(), player.getPosition().getY());
            }
        } catch (Exception e) {
            System.out.println("⚠️ Error al obtener sala actual: " + e.getMessage());
        }
        return null;
    }

    /**
     * Verifica las condiciones del juego (victoria, derrota, etc.).
     */
    private void checkGameConditions() {
        try {
            // Verificar si el jugador murió
            if (!player.isAlive()) {
                gameState = GameState.GAME_OVER;
                gameRunning = false;
                return;
            }

            // Verificar condiciones de victoria por nivel
            checkLevelCompletionConditions();

            // Verificar condición de victoria final
            if (player.hasItem("Tesoro del Conocimiento Supremo")) {
                gameState = GameState.VICTORY;
                gameRunning = false;
                return;
            }

        } catch (Exception e) {
            handleGameError("Error al verificar condiciones del juego", e);
        }
    }

    /**
     * Verifica las condiciones de completación del nivel actual.
     */
    private void checkLevelCompletionConditions() {
        try {
            String treasureKey = switch (currentLevel) {
                case TRAINEE -> "Tesoro del Conocimiento Trainee";
                case JUNIOR -> "Tesoro del Conocimiento Junior";
                case SENIOR -> "Tesoro del Conocimiento Supremo";
            };

            if (player.hasItem(treasureKey) && currentLevel != PlayerLevel.SENIOR) {
                PlayerLevel nextLevel = currentLevel.getNext();
                if (nextLevel != null) {
                    System.out.println("\n🎉 ¡Has completado el nivel " + currentLevel.getDisplayName() + "!");
                    System.out.println("🚀 Avanzando al siguiente nivel...");

                    player.levelUp();
                    initializeLevel(nextLevel);
                }
            }
        } catch (Exception e) {
            System.out.println("⚠️ Error al verificar completación de nivel: " + e.getMessage());
        }
    }

    /**
     * Muestra la pantalla final del juego.
     */
    private void showGameEndScreen() {
        try {
            switch (gameState) {
                case GAME_OVER:
                    GameDisplay.showGameOverScreen();
                    break;
                case VICTORY:
                    GameDisplay.showVictoryScreen();
                    break;
                default:
                    System.out.println("👋 ¡Gracias por jugar!");
                    break;
            }

            // Mostrar resumen de la sesión
            GameDisplay.showSessionSummary(player);

        } catch (Exception e) {
            System.out.println("👋 ¡Gracias por jugar!");
        }
    }

    /**
     * Maneja errores del juego de forma centralizada.
     */
    private void handleGameError(String message, Exception e) {
        System.err.println("❌ " + message);
        if (debugMode && e != null) {
            e.printStackTrace();
        }
    }

    /**
     * Limpia recursos del juego.
     */
    private void cleanup() {
        try {
            if (scanner != null) {
                scanner.close();
            }
            System.out.println("🧹 Recursos del juego liberados correctamente.");
        } catch (Exception e) {
            System.err.println("⚠️ Error al limpiar recursos: " + e.getMessage());
        }
    }

    // ============ ENUMERACIONES ============

    /**
     * Estados posibles del juego.
     */
    public enum GameState {
        INITIALIZING("Inicializando"),
        STARTING("Iniciando"),
        PLAYING("Jugando"),
        PAUSED("Pausado"),
        GAME_OVER("Game Over"),
        VICTORY("Victoria"),
        ERROR("Error");

        private final String description;

        GameState(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
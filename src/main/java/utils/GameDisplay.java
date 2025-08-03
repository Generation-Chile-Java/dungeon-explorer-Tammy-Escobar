package utils;

import models.player.Player;
import interfaces.Room;

/**
 * GameDisplay.java - VERSIÓN MEJORADA
 * Maneja toda la presentación visual del juego con pistas y navegación mejorada.
 * Aplica el principio de responsabilidad única para la UI.
 */
public final class GameDisplay {

    // Prevenir instanciación
    private GameDisplay() {
        throw new UnsupportedOperationException("Esta es una clase de utilidades");
    }

    // ============ PANTALLAS PRINCIPALES ============

    /**
     * Muestra la pantalla de bienvenida del juego.
     */
    public static void showWelcomeScreen() {
        clearScreen();
        System.out.println(GameConstants.WELCOME_MESSAGE);
        pause(2000);
    }

    /**
     * Muestra la pantalla de Game Over.
     */
    public static void showGameOverScreen() {
        clearScreen();
        printSeparator("GAME OVER");
        System.out.println(GameConstants.GAME_OVER_MESSAGE);
        printSeparator();
    }

    /**
     * Muestra la pantalla de victoria.
     */
    public static void showVictoryScreen() {
        clearScreen();
        printSeparator("¡VICTORIA!");
        System.out.println(GameConstants.VICTORY_MESSAGE);
        printSeparator();
    }

    /**
     * Muestra la pantalla de comandos disponibles CON PISTAS MEJORADAS.
     */
    public static void showCommandsHelp() {
        printSeparator("COMANDOS DISPONIBLES");
        System.out.println("🕹️ MOVIMIENTO:");
        System.out.printf("   %-3s - %s%n", GameConstants.COMMAND_MOVE_UP.toUpperCase(), "Mover hacia arriba (Norte ⬆️)");
        System.out.printf("   %-3s - %s%n", GameConstants.COMMAND_MOVE_DOWN.toUpperCase(), "Mover hacia abajo (Sur ⬇️)");
        System.out.printf("   %-3s - %s%n", GameConstants.COMMAND_MOVE_LEFT.toUpperCase(), "Mover hacia la izquierda (Oeste ⬅️)");
        System.out.printf("   %-3s - %s%n", GameConstants.COMMAND_MOVE_RIGHT.toUpperCase(), "Mover hacia la derecha (Este ➡️)");

        System.out.println("\n🎮 ACCIONES:");
        System.out.printf("   %-3s - %s%n", GameConstants.COMMAND_INTERACT.toUpperCase(), "Interactuar (luchar, recoger, abrir)");
        System.out.printf("   %-3s - %s%n", GameConstants.COMMAND_INVENTORY.toUpperCase(), "Ver inventario y objetos");
        System.out.printf("   %-3s - %s%n", GameConstants.COMMAND_USE_ITEM.toUpperCase(), "Usar objeto del inventario");

        System.out.println("\n📊 INFORMACIÓN:");
        System.out.printf("   %-8s - %s%n", GameConstants.COMMAND_STATUS.toUpperCase(), "Ver estado y mapa completo");
        System.out.printf("   %-8s - %s%n", GameConstants.COMMAND_HELP.toUpperCase(), "Mostrar esta ayuda");

        System.out.println("\n💾 SISTEMA:");
        System.out.printf("   %-8s - %s%n", GameConstants.COMMAND_SAVE.toUpperCase(), "Guardar partida");
        System.out.printf("   %-8s - %s%n", GameConstants.COMMAND_LOAD.toUpperCase(), "Cargar partida");
        System.out.printf("   %-8s - %s%n", GameConstants.COMMAND_QUIT.toUpperCase(), "Salir del juego");

        // Pistas adicionales
        System.out.println("\n💡 PISTAS ÚTILES:");
        System.out.println("   🗺️ Usa STATUS para ver un mapa visual de tu ubicación");
        System.out.println("   🔍 Explora sistemáticamente: visita todas las direcciones");
        System.out.println("   ⚔️ En combate, puedes responder con letras (A,B,C,D) o texto");
        System.out.println("   🏠 La sala principal es tu punto seguro para descansar");

        printSeparator();
    }

    // ============ INFORMACIÓN DEL JUGADOR ============

    /**
     * Muestra el estado completo del jugador con diseño mejorado.
     */
    public static void showPlayerStatus(Player player) {
        if (player == null) {
            System.out.println("❌ Error: Información del jugador no disponible.");
            return;
        }

        printSeparator("ESTADO DEL JUGADOR");

        // Información básica
        System.out.printf("👤 %s %s (%s)%n",
                player.getLevel().getIcon(),
                player.getName(),
                player.getLevel().getDisplayName());

        // Barra de vida visual
        showHealthBar(player);

        // Estadísticas
        System.out.printf("⚔️  Poder: %d%n", player.getStats().getPower());
        System.out.printf("🛡️  Defensa: %d%n", player.getStats().getDefense());

        // Posición con contexto
        System.out.printf("📍 Posición: %s%n", player.getPosition());

        // Inventario resumido
        System.out.printf("🎒 Inventario: %d/%d objetos%n",
                player.getInventory().getSize(),
                player.getInventory().getCapacity());

        // Estadísticas de juego
        showGameStatistics(player);

        printSeparator();
    }

    /**
     * Muestra una barra de vida visual mejorada.
     */
    private static void showHealthBar(Player player) {
        int currentHealth = player.getStats().getHealth();
        int maxHealth = player.getStats().getMaxHealth();
        double percentage = (double) currentHealth / maxHealth;

        int barLength = 20;
        int filledBars = (int) (percentage * barLength);

        StringBuilder healthBar = new StringBuilder("❤️  Vida: [");

        // Parte llena de la barra
        for (int i = 0; i < filledBars; i++) {
            healthBar.append("█");
        }

        // Parte vacía de la barra
        for (int i = filledBars; i < barLength; i++) {
            healthBar.append("░");
        }

        healthBar.append(String.format("] %d/%d", currentHealth, maxHealth));

        // Color y consejo según el estado de vida
        if (percentage > 0.75) {
            System.out.println(healthBar + " 💚 (Excelente)");
        } else if (percentage > 0.50) {
            System.out.println(healthBar + " 💛 (Bien)");
        } else if (percentage > 0.25) {
            System.out.println(healthBar + " 🧡 (Cuidado - busca curación)");
        } else {
            System.out.println(healthBar + " ❤️ ¡CRÍTICO! (Encuentra tesoros curativos)");
        }
    }

    /**
     * Muestra estadísticas de juego del jugador.
     */
    private static void showGameStatistics(Player player) {
        System.out.println("\n📊 ESTADÍSTICAS:");
        System.out.printf("   🏠 Salas exploradas: %d%n", player.getStats().getRoomsExplored());
        System.out.printf("   ⚔️ Enemigos derrotados: %d%n", player.getStats().getEnemiesDefeated());
        System.out.printf("   💎 Tesoros encontrados: %d%n", player.getStats().getTreasuresFound());
        System.out.printf("   ⭐ Experiencia: %d/%d%n",
                player.getStats().getExperience(),
                player.getStats().getExperienceToNext());
    }

    // ============ INFORMACIÓN DE SALAS ============

    /**
     * Muestra información detallada de una sala.
     */
    public static void showRoomInfo(Room room, Player player) {
        if (room == null) {
            System.out.println("🤔 No hay información disponible sobre esta área.");
            return;
        }

        printSeparator(room.getName().toUpperCase());

        // Icono según el tipo de sala
        String icon = getRoomIcon(room.getRoomType());
        System.out.println(icon + " " + room.getDescription());

        // Estado de la sala
        if (room.isVisited()) {
            System.out.println("👁️ Ya has explorado esta sala anteriormente.");
        } else {
            System.out.println("✨ Esta es la primera vez que visitas este lugar.");
        }

        // Accesibilidad
        if (!room.isAccessible()) {
            System.out.println("🚫 Esta sala no está disponible en este momento.");
        }

        System.out.println();
    }

    /**
     * Obtiene el icono apropiado para cada tipo de sala.
     */
    private static String getRoomIcon(Room.RoomType roomType) {
        return switch (roomType) {
            case EMPTY -> "🏠";
            case TREASURE -> "💰";
            case ENEMY -> "⚔️";
            case LOCKED -> "🔒";
            case SECRET -> "🗝️";
        };
    }

    // ============ MENSAJES DE ACCIÓN MEJORADOS ============

    /**
     * Muestra mensaje de movimiento del jugador CON CONTEXTO.
     */
    public static void showMovementMessage(Player player, boolean success, String direction) {
        if (success) {
            System.out.printf("🚶 Te has movido hacia %s. Posición actual: %s%n",
                    direction, player.getPosition());
        } else {
            System.out.printf("🚫 No puedes moverte hacia %s desde aquí.%n", direction);
        }
    }

    /**
     * Muestra mensaje de interacción.
     */
    public static void showInteractionMessage(String message) {
        System.out.println("💬 " + message);
    }

    /**
     * Muestra mensaje de error.
     */
    public static void showErrorMessage(String error) {
        System.out.println("❌ " + error);
    }

    /**
     * Muestra mensaje de éxito.
     */
    public static void showSuccessMessage(String message) {
        System.out.println("✅ " + message);
    }

    /**
     * Muestra mensaje de advertencia.
     */
    public static void showWarningMessage(String warning) {
        System.out.println("⚠️ " + warning);
    }

    /**
     * Muestra mensaje informativo.
     */
    public static void showInfoMessage(String info) {
        System.out.println("ℹ️ " + info);
    }

    // ============ COMBAT Y EVENTOS ============

    /**
     * Muestra el inicio de un combate.
     */
    public static void showCombatStart(String enemyName) {
        printSeparator("¡COMBATE!");
        System.out.println("⚔️ ¡Un " + enemyName + " salvaje apareció!");
        System.out.println("🧠 Prepárate para demostrar tus conocimientos de programación.");
        System.out.println("💡 PISTA: Responde con A,B,C,D o escribe la respuesta completa");
        printSeparator();
    }

    /**
     * Muestra una pregunta de combate.
     */
    public static void showCombatQuestion(String question) {
        System.out.println("❓ PREGUNTA:");
        System.out.println("   " + question);
        System.out.print("💭 Tu respuesta: ");
    }

    /**
     * Muestra el resultado de una respuesta de combate.
     */
    public static void showCombatResult(boolean correct, String correctAnswer, String enemyName) {
        if (correct) {
            System.out.println("✅ ¡Correcto! Has derrotado al " + enemyName + "!");
            System.out.println("🎉 Tu conocimiento ha prevalecido sobre el bug.");
        } else {
            System.out.println("❌ Incorrecto. La respuesta correcta era: " + correctAnswer);
            System.out.println("🐛 El " + enemyName + " contraataca con su confusión!");
        }
    }

    // ============ TRANSICIONES DE NIVEL ============

    /**
     * Muestra el inicio de un nuevo nivel.
     */
    public static void showLevelStart(String levelName, String description) {
        clearScreen();
        printSeparator("NUEVO NIVEL");
        System.out.println("🎯 " + levelName.toUpperCase());
        System.out.println();
        System.out.println(description);
        System.out.println();
        System.out.println("💡 CONSEJO: Usa 'STATUS' para ver el mapa del nivel");
        printSeparator();
        pause(3000);
    }

    /**
     * Muestra la completación de un nivel.
     */
    public static void showLevelComplete(String levelName, String nextLevel) {
        printSeparator("¡NIVEL COMPLETADO!");
        System.out.println("🎉 ¡Has completado el nivel " + levelName + "!");
        if (nextLevel != null) {
            System.out.println("🚀 Avanzando a: " + nextLevel);
            System.out.println("💪 Tus habilidades han mejorado para enfrentar nuevos desafíos");
        } else {
            System.out.println("🏆 ¡Has completado todos los niveles!");
        }
        printSeparator();
        pause(3000);
    }

    // ============ UTILIDADES DE FORMATO ============

    /**
     * Imprime una línea separadora.
     */
    public static void printSeparator() {
        System.out.println(GameConstants.getSeparator());
    }

    /**
     * Imprime una línea separadora con título.
     */
    public static void printSeparator(String title) {
        System.out.println(GameConstants.getSeparatorWithTitle(title));
    }

    /**
     * Limpia la pantalla (simulado).
     */
    public static void clearScreen() {
        // En una aplicación real, esto podría limpiar la consola
        System.out.println("\n".repeat(3));
    }

    /**
     * Pausa la ejecución por un tiempo determinado.
     */
    public static void pause(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Solicita al usuario que presione Enter para continuar.
     */
    public static void pressEnterToContinue() {
        System.out.print("\n📱 Presiona Enter para continuar...");
        try {
            System.in.read();
        } catch (Exception e) {
            // Ignorar errores de entrada
        }
    }

    /**
     * Muestra un mensaje con animación de escritura.
     */
    public static void typewriterEffect(String message, long delayMs) {
        for (char c : message.toCharArray()) {
            System.out.print(c);
            try {
                Thread.sleep(delayMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println();
    }

    /**
     * Muestra un progreso visual con barra.
     */
    public static void showProgressBar(String task, int current, int total) {
        double percentage = (double) current / total;
        int barLength = 30;
        int filledBars = (int) (percentage * barLength);

        StringBuilder bar = new StringBuilder("[");
        for (int i = 0; i < filledBars; i++) {
            bar.append("█");
        }
        for (int i = filledBars; i < barLength; i++) {
            bar.append("░");
        }
        bar.append("]");

        System.out.printf("\r🔄 %s %s %d%%", task, bar, (int)(percentage * 100));

        if (current == total) {
            System.out.println(" ✅");
        }
    }

    /**
     * Formatea un número con separadores de miles.
     */
    public static String formatNumber(int number) {
        return String.format("%,d", number);
    }

    /**
     * Centra un texto en una línea de ancho específico.
     */
    public static String centerText(String text, int width) {
        if (text.length() >= width) {
            return text;
        }

        int padding = (width - text.length()) / 2;
        return " ".repeat(padding) + text + " ".repeat(width - padding - text.length());
    }

    // ============ NUEVOS MÉTODOS DE AYUDA ============

    /**
     * Muestra pistas contextuales según la situación del jugador.
     */
    public static void showContextualHints(Player player, Room currentRoom) {
        if (player == null || currentRoom == null) return;

        try {
            // Pistas basadas en la vida del jugador
            if (player.getStats().isCritical()) {
                System.out.println("💡 URGENTE: Tu vida está crítica. Busca tesoros curativos o descansa en sala principal");
            }

            // Pistas basadas en el inventario
            if (player.getInventory().isEmpty()) {
                System.out.println("💡 TIP: Tu inventario está vacío. Explora salas con tesoros (💰)");
            }

            // Pistas basadas en el nivel
            switch (player.getLevel()) {
                case TRAINEE -> {
                    System.out.println("🎓 TRAINEE: Enfócate en aprender los conceptos básicos de programación");
                    System.out.println("💡 Ve al ESTE desde la sala principal para encontrar tesoros");
                }
                case JUNIOR -> {
                    System.out.println("💼 JUNIOR: Las preguntas son más complejas ahora");
                    System.out.println("💡 No todos los tesoros son verdaderos, algunos son falsos");
                }
                case SENIOR -> {
                    System.out.println("👑 SENIOR: Prepárate para el desafío final");
                    System.out.println("💡 Busca al Boss Final para completar tu evolución");
                }
            }

        } catch (Exception e) {
            System.out.println("💡 Usa 'H' para ver comandos o 'STATUS' para información detallada");
        }
    }

    /**
     * Muestra una brújula visual para orientación.
     */
    public static void showCompass() {
        System.out.println("\n🧭 BRÚJULA:");
        System.out.println("    ⬆️ W");
        System.out.println("    ⬆️ Norte");
        System.out.println();
        System.out.println("A ⬅️ [🧑] ➡️ D");
        System.out.println("Oeste    Este");
        System.out.println();
        System.out.println("    ⬇️ Sur");
        System.out.println("    ⬇️ S");
    }

    /**
     * Muestra un mini tutorial rápido.
     */
    public static void showQuickTutorial() {
        printSeparator("TUTORIAL RÁPIDO");
        System.out.println("🎮 MOVIMIENTO BÁSICO:");
        System.out.println("   W = Norte ⬆️  |  S = Sur ⬇️");
        System.out.println("   A = Oeste ⬅️  |  D = Este ➡️");

        System.out.println("\n🎯 OBJETIVOS:");
        System.out.println("   💰 Encuentra tesoros con objetos valiosos");
        System.out.println("   ⚔️ Derrota enemigos respondiendo preguntas");
        System.out.println("   🗝️ Busca llaves para abrir salas secretas");

        System.out.println("\n💡 COMANDOS ESENCIALES:");
        System.out.println("   F = Interactuar | STATUS = Ver mapa | H = Ayuda");

        printSeparator();
    }

    /**
     * Muestra información específica sobre combates.
     */
    public static void showCombatTips() {
        printSeparator("CONSEJOS DE COMBATE");
        System.out.println("⚔️ DURANTE EL COMBATE:");
        System.out.println("   📝 Lee las preguntas cuidadosamente");
        System.out.println("   🅰️ Puedes responder con letras (A, B, C, D)");
        System.out.println("   ✍️ O escribir la respuesta completa");
        System.out.println("   🧠 Las preguntas son sobre programación");

        System.out.println("\n💡 ESTRATEGIAS:");
        System.out.println("   ❤️ Mantén tu vida alta antes de combatir");
        System.out.println("   🏃 Puedes huir moviéndote a otra sala");
        System.out.println("   📚 Conocimiento = Poder en este mundo");

        printSeparator();
    }

    /**
     * Muestra el estado del juego de forma compacta.
     */
    public static void showCompactStatus(Player player, String roomName) {
        System.out.printf("❤️%d/%d 📍%s 🎒%d/%d | ",
                player.getStats().getHealth(),
                player.getStats().getMaxHealth(),
                roomName,
                player.getInventory().getSize(),
                player.getInventory().getCapacity()
        );

        // Estado de salud con color
        double healthPercent = (double) player.getStats().getHealth() / player.getStats().getMaxHealth();
        if (healthPercent > 0.75) {
            System.out.print("💚");
        } else if (healthPercent > 0.5) {
            System.out.print("💛");
        } else if (healthPercent > 0.25) {
            System.out.print("🧡");
        } else {
            System.out.print("❤️");
        }

        System.out.println();
    }

    /**
     * Muestra mensajes de motivación según el progreso.
     */
    public static void showMotivationalMessage(Player player) {
        int enemiesDefeated = player.getStats().getEnemiesDefeated();
        int treasuresFound = player.getStats().getTreasuresFound();

        if (enemiesDefeated == 0 && treasuresFound == 0) {
            System.out.println("🌟 ¡Acabas de comenzar tu aventura! Explora y descubre");
        } else if (enemiesDefeated >= 1) {
            System.out.println("⚔️ ¡Excelente! Has demostrado tus habilidades en combate");
        } else if (treasuresFound >= 1) {
            System.out.println("💰 ¡Bien hecho! Has encontrado objetos valiosos");
        }

        if (enemiesDefeated >= 3) {
            System.out.println("🏆 ¡Eres un verdadero cazador de bugs!");
        }

        if (treasuresFound >= 3) {
            System.out.println("💎 ¡Tu colección de tesoros es impresionante!");
        }
    }

    /**
     * Muestra advertencias importantes según el estado.
     */
    public static void showImportantWarnings(Player player) {
        // Advertencia de vida crítica
        if (player.getStats().isCritical()) {
            System.out.println("🚨 ¡ALERTA! Tu vida está en estado crítico");
            System.out.println("💊 Busca tesoros curativos o ve a la sala principal para descansar");
        }

        // Advertencia de inventario lleno
        if (player.getInventory().isFull()) {
            System.out.println("🎒 ¡Inventario lleno! No podrás recoger más objetos");
            System.out.println("💡 Usa objetos con 'X' para hacer espacio");
        }

        // Pista de progreso
        String objective = switch (player.getLevel()) {
            case TRAINEE -> "Tesoro del Conocimiento Trainee";
            case JUNIOR -> "Tesoro del Conocimiento Junior";
            case SENIOR -> "Tesoro del Conocimiento Supremo";
        };

        if (!player.hasItem(objective)) {
            System.out.println("🎯 OBJETIVO: Encuentra el '" + objective + "'");
        }
    }

    /**
     * Muestra un resumen de la sesión de juego.
     */
    public static void showSessionSummary(Player player) {
        printSeparator("RESUMEN DE LA SESIÓN");

        System.out.println("👤 Jugador: " + player.getName());
        System.out.println("🎓 Nivel: " + player.getLevel().getDisplayName());
        System.out.println("📍 Posición final: " + player.getPosition());

        System.out.println("\n📊 LOGROS DE LA SESIÓN:");
        System.out.println("   🏠 Salas exploradas: " + player.getStats().getRoomsExplored());
        System.out.println("   ⚔️ Enemigos derrotados: " + player.getStats().getEnemiesDefeated());
        System.out.println("   💎 Tesoros encontrados: " + player.getStats().getTreasuresFound());
        System.out.println("   ⭐ Experiencia ganada: " + player.getStats().getExperience());

        if (player.getInventory().getSize() > 0) {
            System.out.println("\n🎒 INVENTARIO:");
            player.getInventory().displayItems();
        }

        // Mensaje de progreso
        switch (player.getLevel()) {
            case TRAINEE -> System.out.println("\n🎯 SIGUIENTE: Avanza a Junior Developer");
            case JUNIOR -> System.out.println("\n🎯 SIGUIENTE: Conviértete en Senior Developer");
            case SENIOR -> System.out.println("\n🏆 ¡Eres un Senior Developer! Busca el desafío final");
        }

        printSeparator();
    }
}
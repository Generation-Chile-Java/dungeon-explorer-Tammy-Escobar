/**
 * Main.java
 * Punto de entrada principal de la aplicación DungeonGame.
 */

import game.GameEngine;
import utils.GameDisplay;

/**
 * Clase principal que inicia la aplicación DungeonGame.
 * Sigue el patrón de responsabilidad única - solo inicia el juego.
 */
public class Main {

    /**
     * Método principal que ejecuta la aplicación.
     *
     * @param args argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        try {
            // Crear e iniciar el motor del juego completo
            GameEngine gameEngine = new GameEngine();
            gameEngine.start();

        } catch (Exception e) {
            System.err.println("Error fatal al iniciar el juego: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}

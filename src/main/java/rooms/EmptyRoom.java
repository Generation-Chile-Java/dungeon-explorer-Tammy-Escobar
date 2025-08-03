package rooms;

import interfaces.Interactable.InteractionResult;
import interfaces.Interactable.InteractionType;
import models.player.Player;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

/**
 * EmptyRoom.java
 * Representa una sala vacía que puede contener elementos ambientales.
 * Extiende BaseRoom y proporciona funcionalidad específica para salas vacías.
 */
public class EmptyRoom extends BaseRoom {

    // Elementos ambientales de la sala
    private final List<String> ambientElements;
    private final List<String> flavorTexts;
    private final boolean hasSecrets;
    private final Random random;

    // Estado específico de sala vacía
    private boolean secretRevealed;
    private int searchAttempts;
    private final int maxSearchAttempts;

    /**
     * Constructor principal para crear una sala vacía.
     *
     * @param name nombre de la sala
     * @param description descripción de la sala
     * @param hasSecrets si la sala tiene secretos ocultos
     */
    public EmptyRoom(String name, String description, boolean hasSecrets) {
        super(name, description, RoomType.EMPTY);
        this.hasSecrets = hasSecrets;
        this.secretRevealed = false;
        this.searchAttempts = 0;
        this.maxSearchAttempts = 3;
        this.random = new Random();
        this.ambientElements = new ArrayList<>();
        this.flavorTexts = new ArrayList<>();

        initializeAmbientElements();
        initializeFlavorTexts();

        // Añadir tags específicos
        addTag("explorable");
        if (hasSecrets) {
            addTag("mysterious");
        }
    }

    /**
     * Constructor simplificado para salas vacías básicas.
     */
    public EmptyRoom(String name, String description) {
        this(name, description, false);
    }

    /**
     * Inicializa elementos ambientales aleatorios.
     */
    private void initializeAmbientElements() {
        ambientElements.add("polvo flotando en rayos de luz");
        ambientElements.add("ecos distantes de actividad");
        ambientElements.add("sombras danzando en las paredes");
        ambientElements.add("un suave zumbido de servidores lejanos");
        ambientElements.add("el tenue brillo de monitores apagados");
        ambientElements.add("cables serpenteando por el suelo");
        ambientElements.add("libros de programación abandonados");
        ambientElements.add("tazas de café vacías y olvidadas");
        ambientElements.add("notas Post-it con código garabateado");
        ambientElements.add("el fantasma de commits pasados");
    }

    /**
     * Inicializa textos narrativos variados.
     */
    private void initializeFlavorTexts() {
        flavorTexts.add("El silencio aquí es diferente, como si los algoritmos susurraran secretos.");
        flavorTexts.add("Esta sala tiene el aroma característico de largos debugging sessions.");
        flavorTexts.add("Puedes sentir la presencia de código que una vez vivió aquí.");
        flavorTexts.add("Las paredes parecen guardar memorias de refactorizaciones épicas.");
        flavorTexts.add("Hay algo reconfortante en la simplicidad de este espacio.");
        flavorTexts.add("El aire vibra con la energía de posibilidades no exploradas.");
        flavorTexts.add("Este lugar te recuerda que a veces, menos es más.");
        flavorTexts.add("Sientes que algo importante sucedió aquí, pero no puedes recordar qué.");
    }

    // ============ IMPLEMENTACIÓN DE INTERACCIÓN ============

    @Override
    protected InteractionResult executeRoomInteraction(Player player) {
        // Mostrar descripción base
        showRoomDescription();

        // Añadir elementos ambientales
        showAmbientElements();

        // Verificar si hay secretos por descubrir
        if (hasSecrets && !secretRevealed) {
            return handleSecretSearch(player);
        }

        // Ofrecer descanso si el jugador está herido
        if (player.getStats().getHealth() < player.getStats().getMaxHealth()) {
            return offerRest(player);
        }

        // Experiencia por exploración
        player.getStats().addExperience(5);

        return InteractionResult.success(
                "Has explorado completamente esta área.",
                InteractionType.DIALOGUE
        );
    }

    /**
     * Muestra la descripción de la sala con elementos variables.
     */
    private void showRoomDescription() {
        System.out.println("🏚️ " + getDescription());

        // Añadir texto narrativo aleatorio
        if (!flavorTexts.isEmpty()) {
            String flavorText = flavorTexts.get(random.nextInt(flavorTexts.size()));
            System.out.println("💭 " + flavorText);
        }
    }

    /**
     * Muestra elementos ambientales aleatorios.
     */
    private void showAmbientElements() {
        if (!ambientElements.isEmpty() && random.nextBoolean()) {
            String element = ambientElements.get(random.nextInt(ambientElements.size()));
            System.out.println("👁️ Notas: " + element);
        }
    }

    /**
     * Maneja la búsqueda de secretos en la sala.
     */
    private InteractionResult handleSecretSearch(Player player) {
        searchAttempts++;

        System.out.println("🔍 Algo en esta sala llama tu atención...");
        System.out.println("¿Quieres buscar más detalladamente? (s/n)");

        // En una implementación real, esto vendría del input del usuario
        // Por ahora, simulamos una búsqueda automática
        boolean wantsToSearch = random.nextBoolean();

        if (wantsToSearch) {
            return performSecretSearch(player);
        } else {
            return InteractionResult.success(
                    "Decides que no vale la pena investigar más por ahora.",
                    InteractionType.DIALOGUE
            );
        }
    }

    /**
     * Realiza la búsqueda de secretos.
     */
    private InteractionResult performSecretSearch(Player player) {
        System.out.println("🔍 Buscas cuidadosamente por la sala...");

        // Probabilidad de encontrar el secreto aumenta con cada intento
        double successChance = 0.3 + (searchAttempts * 0.2);

        if (random.nextDouble() < successChance || searchAttempts >= maxSearchAttempts) {
            return revealSecret(player);
        } else {
            return InteractionResult.success(
                    "No encuentras nada especial esta vez. Tal vez con más perseverancia...",
                    InteractionType.DIALOGUE
            );
        }
    }

    /**
     * Revela el secreto oculto de la sala.
     */
    private InteractionResult revealSecret(Player player) {
        secretRevealed = true;

        String secretMessage = generateSecretMessage();
        System.out.println("✨ " + secretMessage);

        // Recompensa por encontrar el secreto
        int experienceReward = 25;
        player.getStats().addExperience(experienceReward);

        // Pequeña curación como recompensa
        player.heal(10);

        // Cambiar el tag de la sala
        addTag("secret_found");

        return InteractionResult.success(
                "¡Has descubierto un secreto oculto! Ganas " + experienceReward + " puntos de experiencia.",
                InteractionType.LOOT
        );
    }

    /**
     * Genera un mensaje de secreto aleatorio.
     */
    private String generateSecretMessage() {
        String[] secrets = {
                "¡Encuentras una nota con un tip de programación valioso!",
                "¡Descubres un fragmento de código elegante escrito en la pared!",
                "¡Hallas un mensaje motivacional de un desarrollador anterior!",
                "¡Encuentras una referencia a un patrón de diseño útil!",
                "¡Descubres una pista sobre las mejores prácticas de clean code!",
                "¡Hallas un easter egg dejado por el arquitecto del sistema!",
                "¡Encuentras un mensaje sobre la importancia de los tests unitarios!",
                "¡Descubres una reflexión sobre la evolución del desarrollo de software!"
        };

        return secrets[random.nextInt(secrets.length)];
    }

    /**
     * Ofrece la posibilidad de descansar al jugador.
     */
    private InteractionResult offerRest(Player player) {
        System.out.println("💤 Esta sala pacífica parece un buen lugar para descansar.");
        System.out.println("¿Quieres tomarte un momento para recuperarte? (s/n)");

        // Simulación de respuesta (en implementación real vendría del input)
        boolean wantsToRest = random.nextBoolean();

        if (wantsToRest) {
            return performRest(player);
        } else {
            return InteractionResult.success(
                    "Decides continuar tu aventura sin descansar.",
                    InteractionType.DIALOGUE
            );
        }
    }

    /**
     * Ejecuta el descanso del jugador.
     */
    private InteractionResult performRest(Player player) {
        System.out.println("😴 Te tomas un momento para descansar y reflexionar...");

        // Curación gradual
        int healAmount = Math.min(20, player.getStats().getMaxHealth() - player.getStats().getHealth());

        if (healAmount > 0) {
            player.heal(healAmount);
            return InteractionResult.success(
                    "Te sientes más descansado. Has recuperado " + healAmount + " puntos de vida.",
                    InteractionType.HEAL
            );
        } else {
            return InteractionResult.success(
                    "Ya te sientes completamente descansado.",
                    InteractionType.DIALOGUE
            );
        }
    }

    // ============ MÉTODOS ESPECÍFICOS DE SALA VACÍA ============

    /**
     * Verifica si la sala tiene secretos por descubrir.
     */
    public boolean hasUndiscoveredSecrets() {
        return hasSecrets && !secretRevealed;
    }

    /**
     * Obtiene el número de intentos de búsqueda realizados.
     */
    public int getSearchAttempts() {
        return searchAttempts;
    }

    /**
     * Verifica si el secreto ha sido revelado.
     */
    public boolean isSecretRevealed() {
        return secretRevealed;
    }

    /**
     * Añade un elemento ambiental personalizado.
     */
    public void addAmbientElement(String element) {
        if (element != null && !element.trim().isEmpty()) {
            ambientElements.add(element.trim());
        }
    }

    /**
     * Añade un texto narrativo personalizado.
     */
    public void addFlavorText(String text) {
        if (text != null && !text.trim().isEmpty()) {
            flavorTexts.add(text.trim());
        }
    }

    // ============ MÉTODOS SOBRESCRITOS ============

    @Override
    protected void onFirstVisit(Player player) {
        super.onFirstVisit(player);
        System.out.println("🚶 Entras cautelosamente en esta sala aparentemente vacía...");
    }

    @Override
    protected void onSubsequentVisit(Player player) {
        super.onSubsequentVisit(player);

        if (secretRevealed) {
            System.out.println("✨ El secreto que descubriste antes aún resuena en tu mente.");
        } else if (hasSecrets) {
            System.out.println("🤔 Todavía sientes que hay algo más que descubrir aquí...");
        }
    }

    @Override
    public String getInteractionPrompt() {
        if (hasUndiscoveredSecrets()) {
            return "Presiona F para explorar " + getName() + " (puede haber secretos)";
        } else {
            return super.getInteractionPrompt();
        }
    }

    @Override
    public String getDetailedInfo() {
        StringBuilder info = new StringBuilder(super.getDetailedInfo());
        info.append("\n🔍 Secretos: ").append(hasSecrets ? "Sí" : "No");

        if (hasSecrets) {
            info.append("\n🎯 Secreto revelado: ").append(secretRevealed ? "Sí" : "No");
            info.append("\n🔢 Intentos de búsqueda: ").append(searchAttempts);
        }

        info.append("\n🌍 Elementos ambientales: ").append(ambientElements.size());

        return info.toString();
    }

    // ============ FACTORY METHODS ============

    /**
     * Factory para crear salas vacías predefinidas.
     */
    public static class EmptyRoomFactory {

        public static EmptyRoom createMainHall() {
            EmptyRoom room = new EmptyRoom(
                    "Sala Principal",
                    "Te encuentras en la sala principal de la mazmorra. Hay pasillos que se extienden en diferentes direcciones.",
                    false
            );
            room.addAmbientElement("un mapa del área colgado en la pared");
            room.addFlavorText("Este es tu punto de partida. Desde aquí, el mundo del código se abre ante ti.");
            return room;
        }

        public static EmptyRoom createRestArea() {
            EmptyRoom room = new EmptyRoom(
                    "Área de Descanso",
                    "Una sala tranquila con cómodos asientos y una atmosfera relajante.",
                    false
            );
            room.addAmbientElement("cojines suaves dispuestos en círculo");
            room.addAmbientElement("plantas que purifican el aire");
            room.addFlavorText("Este lugar irradia paz y tranquilidad.");
            return room;
        }

        public static EmptyRoom createMysteriousHallway() {
            EmptyRoom room = new EmptyRoom(
                    "Pasillo Misterioso",
                    "Un largo pasillo que se extiende hacia la oscuridad. Algo especial se oculta aquí.",
                    true
            );
            room.addAmbientElement("extraños símbolos tallados en las paredes");
            room.addAmbientElement("una corriente de aire fresco que viene de ninguna parte");
            room.addFlavorText("Los pasillos como este guardan los secretos más profundos del código.");
            return room;
        }

        public static EmptyRoom createAbandonedOffice() {
            EmptyRoom room = new EmptyRoom(
                    "Oficina Abandonada",
                    "Una oficina que alguna vez estuvo llena de actividad, ahora silenciosa.",
                    true
            );
            room.addAmbientElement("sillas giratorias que aún se mueven ligeramente");
            room.addAmbientElement("monitores que parpadean ocasionalmente");
            room.addAmbientElement("un whiteboard con diagramas medio borrados");
            room.addFlavorText("Puedes imaginar a los desarrolladores que trabajaron aquí, debuggeando hasta altas horas.");
            return room;
        }

        public static EmptyRoom createServerRoom() {
            EmptyRoom room = new EmptyRoom(
                    "Sala de Servidores Silenciosa",
                    "Una sala donde los servidores solían zumbar constantemente. Ahora todo está quieto.",
                    false
            );
            room.addAmbientElement("racks de servidores apagados");
            room.addAmbientElement("cables organizados con precisión militar");
            room.addAmbientElement("luces LED que parpadean débilmente");
            room.addFlavorText("El corazón silencioso de la infraestructura digital.");
            return room;
        }

        public static EmptyRoom createLibraryCorner() {
            EmptyRoom room = new EmptyRoom(
                    "Rincón de la Biblioteca",
                    "Un acogedor rincón lleno de libros de programación y documentación técnica.",
                    true
            );
            room.addAmbientElement("estanterías repletas de manuales técnicos");
            room.addAmbientElement("un sillón de lectura muy usado");
            room.addAmbientElement("marcadores y notas entre las páginas");
            room.addFlavorText("El conocimiento acumulado de generaciones de programadores descansa aquí.");
            return room;
        }

        public static EmptyRoom createBreakRoom() {
            EmptyRoom room = new EmptyRoom(
                    "Sala de Descanso",
                    "La sala donde los desarrolladores toman sus merecidos breaks y recargan energías.",
                    false
            );
            room.addAmbientElement("una cafetera que aún huele a café recién hecho");
            room.addAmbientElement("revistas técnicas apiladas en una mesa");
            room.addAmbientElement("un sofá cómodo con cojines hundidos");
            room.addFlavorText("El lugar donde nacen las mejores ideas, entre café y conversaciones casuales.");
            return room;
        }

        public static EmptyRoom createTestingLab() {
            EmptyRoom room = new EmptyRoom(
                    "Laboratorio de Testing",
                    "Un espacio dedicado a las pruebas y la calidad del software.",
                    true
            );
            room.addAmbientElement("dispositivos de testing de diferentes épocas");
            room.addAmbientElement("cables de conexión cuidadosamente organizados");
            room.addAmbientElement("pantallas mostrando dashboards de métricas");
            room.addFlavorText("Aquí se forja la confianza en el código, test por test.");
            return room;
        }

        public static EmptyRoom createBrainstormingRoom() {
            EmptyRoom room = new EmptyRoom(
                    "Sala de Brainstorming",
                    "Una sala diseñada para la creatividad y la generación de ideas.",
                    false
            );
            room.addAmbientElement("whiteboards cubiertos de diagramas y esquemas");
            room.addAmbientElement("post-its de colores pegados por todas partes");
            room.addAmbientElement("sillas móviles dispuestas en círculo");
            room.addFlavorText("Las paredes han sido testigos de las mejores arquitecturas de software.");
            return room;
        }
    }

    // ============ MÉTODOS DE UTILIDAD ADICIONALES ============

    /**
     * Obtiene un elemento ambiental aleatorio.
     */
    public String getRandomAmbientElement() {
        if (ambientElements.isEmpty()) {
            return "silencio absoluto";
        }
        return ambientElements.get(random.nextInt(ambientElements.size()));
    }

    /**
     * Obtiene un texto narrativo aleatorio.
     */
    public String getRandomFlavorText() {
        if (flavorTexts.isEmpty()) {
            return "Un lugar tranquilo para reflexionar.";
        }
        return flavorTexts.get(random.nextInt(flavorTexts.size()));
    }

    /**
     * Establece un nuevo número máximo de intentos de búsqueda.
     */
    public void setMaxSearchAttempts(int maxAttempts) {
        // No se puede modificar el campo final, pero se puede documentar para futuras versiones
        System.out.println("⚠️ El número máximo de intentos está fijado en " + maxSearchAttempts);
    }

    /**
     * Reinicia el estado de búsqueda de secretos.
     */
    public void resetSecretSearch() {
        if (hasSecrets) {
            secretRevealed = false;
            searchAttempts = 0;
            tags.remove("secret_found");
            System.out.println("🔄 El estado de búsqueda de secretos ha sido reiniciado.");
        }
    }

    /**
     * Fuerza la revelación del secreto (para debugging).
     */
    public void forceRevealSecret(Player player) {
        if (hasSecrets && !secretRevealed) {
            revealSecret(player);
            System.out.println("🔧 Secreto revelado por comando de desarrollador.");
        }
    }

    /**
     * Obtiene estadísticas de la sala vacía.
     */
    public String getRoomStatistics() {
        StringBuilder stats = new StringBuilder();
        stats.append("📊 Estadísticas de ").append(getName()).append(":\n");
        stats.append("   🔍 Tiene secretos: ").append(hasSecrets ? "Sí" : "No").append("\n");
        stats.append("   ✨ Secreto revelado: ").append(secretRevealed ? "Sí" : "No").append("\n");
        stats.append("   🎯 Intentos de búsqueda: ").append(searchAttempts).append("/").append(maxSearchAttempts).append("\n");
        stats.append("   👁️ Visitas: ").append(getVisitCount()).append("\n");
        stats.append("   🌍 Elementos ambientales: ").append(ambientElements.size()).append("\n");
        stats.append("   📝 Textos narrativos: ").append(flavorTexts.size()).append("\n");
        stats.append("   🏷️ Tags: ").append(getTags().size());

        return stats.toString();
    }
}
package models.player;

import interfaces.GameObject;
import utils.GameConstants;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Inventory.java
 * Maneja el inventario del jugador con capacidad limitada y organización inteligente.
 * Aplica el patrón Repository para gestión de objetos.
 */
public class Inventory {

    private final List<GameObject> items;
    private final int capacity;

    /**
     * Constructor que inicializa el inventario con capacidad por defecto.
     */
    public Inventory() {
        this(GameConstants.DEFAULT_INVENTORY_CAPACITY);
    }

    /**
     * Constructor que permite especificar la capacidad.
     *
     * @param capacity capacidad máxima del inventario
     */
    public Inventory(int capacity) {
        this.capacity = Math.max(1, capacity);
        this.items = new ArrayList<>(this.capacity);
    }

    // ============ MÉTODOS DE CONSULTA ============

    /**
     * Obtiene el número de objetos en el inventario.
     *
     * @return cantidad de objetos
     */
    public int getSize() {
        return items.size();
    }

    /**
     * Obtiene la capacidad máxima del inventario.
     *
     * @return capacidad máxima
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Verifica si el inventario está lleno.
     *
     * @return true si está lleno
     */
    public boolean isFull() {
        return items.size() >= capacity;
    }

    /**
     * Verifica si el inventario está vacío.
     *
     * @return true si está vacío
     */
    public boolean isEmpty() {
        return items.isEmpty();
    }

    /**
     * Obtiene el espacio disponible.
     *
     * @return espacios libres
     */
    public int getAvailableSpace() {
        return capacity - items.size();
    }

    // ============ MÉTODOS DE GESTIÓN DE OBJETOS ============

    /**
     * Añade un objeto al inventario.
     *
     * @param item objeto a añadir
     * @return true si se añadió correctamente
     */
    public boolean addItem(GameObject item) {
        if (item == null) {
            return false;
        }

        if (isFull()) {
            System.out.println("🎒 Inventario lleno. No se puede añadir " + item.getName());
            return false;
        }

        items.add(item);
        return true;
    }

    /**
     * Remueve un objeto específico del inventario.
     *
     * @param item objeto a remover
     * @return true si se removió correctamente
     */
    public boolean removeItem(GameObject item) {
        return items.remove(item);
    }

    /**
     * Remueve un objeto por su nombre.
     *
     * @param itemName nombre del objeto
     * @return true si se removió correctamente
     */
    public boolean removeItem(String itemName) {
        return items.removeIf(item -> item.getName().equals(itemName));
    }

    /**
     * Remueve un objeto por su índice.
     *
     * @param index índice del objeto
     * @return objeto removido o null si el índice es inválido
     */
    public GameObject removeItem(int index) {
        if (index >= 0 && index < items.size()) {
            return items.remove(index);
        }
        return null;
    }

    // ============ MÉTODOS DE BÚSQUEDA ============

    /**
     * Verifica si el inventario contiene un objeto específico.
     *
     * @param itemName nombre del objeto
     * @return true si tiene el objeto
     */
    public boolean hasItem(String itemName) {
        return items.stream()
                .anyMatch(item -> item.getName().equals(itemName));
    }

    /**
     * Busca un objeto por su nombre.
     *
     * @param itemName nombre del objeto
     * @return objeto encontrado o null
     */
    public GameObject findItem(String itemName) {
        return items.stream()
                .filter(item -> item.getName().equals(itemName))
                .findFirst()
                .orElse(null);
    }

    /**
     * Obtiene un objeto por su índice.
     *
     * @param index índice del objeto
     * @return objeto en el índice o null si es inválido
     */
    public GameObject getItem(int index) {
        if (index >= 0 && index < items.size()) {
            return items.get(index);
        }
        return null;
    }

    /**
     * Obtiene todos los objetos del inventario.
     *
     * @return lista inmutable de objetos
     */
    public List<GameObject> getAllItems() {
        return Collections.unmodifiableList(items);
    }

    // ============ MÉTODOS DE FILTRADO ============

    /**
     * Filtra objetos por tipo.
     *
     * @param type tipo de objeto
     * @return lista de objetos del tipo especificado
     */
    public List<GameObject> getItemsByType(GameObject.ObjectType type) {
        return items.stream()
                .filter(item -> item.getObjectType() == type)
                .collect(Collectors.toList());
    }

    /**
     * Filtra objetos por rareza.
     *
     * @param rarity rareza del objeto
     * @return lista de objetos de la rareza especificada
     */
    public List<GameObject> getItemsByRarity(GameObject.Rarity rarity) {
        return items.stream()
                .filter(item -> item.getRarity() == rarity)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene objetos consumibles.
     *
     * @return lista de objetos consumibles
     */
    public List<GameObject> getConsumableItems() {
        return items.stream()
                .filter(GameObject::isConsumable)
                .collect(Collectors.toList());
    }

    // ============ MÉTODOS DE ORGANIZACIÓN ============

    /**
     * Ordena el inventario por nombre.
     */
    public void sortByName() {
        items.sort(Comparator.comparing(GameObject::getName));
    }

    /**
     * Ordena el inventario por tipo.
     */
    public void sortByType() {
        items.sort(Comparator.comparing(GameObject::getObjectType));
    }

    /**
     * Ordena el inventario por rareza (de mayor a menor).
     */
    public void sortByRarity() {
        items.sort((a, b) -> b.getRarity().compareTo(a.getRarity()));
    }

    /**
     * Ordena el inventario por valor (de mayor a menor).
     */
    public void sortByValue() {
        items.sort((a, b) -> Integer.compare(b.getValue(), a.getValue()));
    }

    // ============ MÉTODOS DE UTILIDAD ============

    /**
     * Limpia completamente el inventario.
     */
    public void clear() {
        items.clear();
    }

    /**
     * Obtiene estadísticas del inventario.
     *
     * @return mapa con estadísticas por tipo
     */
    public Map<GameObject.ObjectType, Integer> getStatistics() {
        return items.stream()
                .collect(Collectors.groupingBy(
                        GameObject::getObjectType,
                        Collectors.summingInt(item -> 1)
                ));
    }

    /**
     * Calcula el valor total del inventario.
     *
     * @return valor total de todos los objetos
     */
    public int getTotalValue() {
        return items.stream()
                .mapToInt(GameObject::getValue)
                .sum();
    }

    // ============ MÉTODOS DE VISUALIZACIÓN ============

    /**
     * Muestra el contenido del inventario en consola.
     */
    public void displayItems() {
        if (isEmpty()) {
            System.out.println("   (inventario vacío)");
            return;
        }

        System.out.println("📦 Contenido del inventario:");
        for (int i = 0; i < items.size(); i++) {
            GameObject item = items.get(i);
            String rarity = item.getRarity().getSymbol();
            String type = item.getObjectType().getDisplayName();
            System.out.printf("   %d. %s %s [%s] - %s%n",
                    i + 1, rarity, item.getName(), type, item.getDescription());
        }
    }

    /**
     * Muestra el inventario organizado por categorías.
     */
    public void displayByCategory() {
        if (isEmpty()) {
            System.out.println("   (inventario vacío)");
            return;
        }

        Map<GameObject.ObjectType, List<GameObject>> categorized = items.stream()
                .collect(Collectors.groupingBy(GameObject::getObjectType));

        categorized.forEach((type, itemList) -> {
            System.out.println("📂 " + type.getDisplayName() + ":");
            itemList.forEach(item ->
                    System.out.println("   • " + item.getRarity().getSymbol() + " " + item.getName())
            );
        });
    }

    /**
     * Obtiene una representación compacta del inventario.
     *
     * @return string con resumen del inventario
     */
    public String getCompactView() {
        if (isEmpty()) {
            return "Vacío";
        }

        Map<GameObject.ObjectType, Integer> stats = getStatistics();
        return stats.entrySet().stream()
                .map(entry -> entry.getValue() + " " + entry.getKey().getDisplayName())
                .collect(Collectors.joining(", "));
    }

    @Override
    public String toString() {
        return String.format("Inventario[%d/%d]: %s", getSize(), getCapacity(), getCompactView());
    }
}
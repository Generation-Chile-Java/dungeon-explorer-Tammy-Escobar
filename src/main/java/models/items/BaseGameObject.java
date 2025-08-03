package models.items;

import interfaces.GameObject;
import models.player.Player;

/**
 * BaseGameObject.java
 * Clase abstracta base para todos los objetos del juego.
 * Implementa el patrón Template Method y aplica el principio DRY.
 */
public abstract class BaseGameObject implements GameObject {

    // Propiedades básicas del objeto
    protected final String name;
    protected final String description;
    protected final int value;
    protected final ObjectType objectType;
    protected final Rarity rarity;
    protected final boolean consumable;

    // Metadatos del objeto
    protected final String id;
    protected boolean isUsed;
    protected int usageCount;
    protected final int maxUsages;

    /**
     * Constructor principal para crear un objeto del juego.
     *
     * @param name nombre del objeto
     * @param description descripción del objeto
     * @param value valor numérico del objeto
     * @param objectType tipo del objeto
     * @param rarity rareza del objeto
     * @param consumable si el objeto es consumible
     * @param maxUsages número máximo de usos (-1 para infinito)
     */
    protected BaseGameObject(String name, String description, int value,
                             ObjectType objectType, Rarity rarity,
                             boolean consumable, int maxUsages) {
        this.name = validateString(name, "Objeto Sin Nombre");
        this.description = validateString(description, "Sin descripción");
        this.value = Math.max(0, value);
        this.objectType = objectType != null ? objectType : ObjectType.TREASURE;
        this.rarity = rarity != null ? rarity : Rarity.COMMON;
        this.consumable = consumable;
        this.maxUsages = maxUsages;
        this.id = generateId();
        this.isUsed = false;
        this.usageCount = 0;
    }

    /**
     * Constructor simplificado para objetos básicos.
     */
    protected BaseGameObject(String name, String description, int value,
                             ObjectType objectType, Rarity rarity) {
        this(name, description, value, objectType, rarity, true, 1);
    }

    /**
     * Valida que una cadena no sea nula o vacía.
     */
    private String validateString(String input, String defaultValue) {
        return (input != null && !input.trim().isEmpty()) ? input.trim() : defaultValue;
    }

    /**
     * Genera un ID único para el objeto.
     */
    private String generateId() {
        return objectType.name() + "_" + name.replaceAll("\\s+", "_") + "_" + System.nanoTime();
    }

    // ============ IMPLEMENTACIÓN DE INTERFACE ============

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public ObjectType getObjectType() {
        return objectType;
    }

    @Override
    public Rarity getRarity() {
        return rarity;
    }

    @Override
    public boolean isConsumable() {
        return consumable;
    }

    // ============ MÉTODOS TEMPLATE ============

    /**
     * Implementación del patrón Template Method para usar objetos.
     * Define el flujo general, las subclases implementan los detalles.
     */
    @Override
    public final void use(Player player) {
        if (!canBeUsed()) {
            showCannotUseMessage();
            return;
        }

        // Ejecutar el efecto específico del objeto
        boolean success = executeEffect(player);

        if (success) {
            onSuccessfulUse(player);
            incrementUsageCount();

            if (shouldBeConsumed()) {
                markAsUsed();
                onObjectConsumed(player);
            }
        } else {
            onFailedUse(player);
        }
    }

    // ============ MÉTODOS ABSTRACTOS ============

    /**
     * Ejecuta el efecto específico del objeto.
     * Debe ser implementado por cada subclase.
     *
     * @param player jugador que usa el objeto
     * @return true si el efecto se aplicó correctamente
     */
    protected abstract boolean executeEffect(Player player);

    // ============ MÉTODOS HOOK (OPCIONALES) ============

    /**
     * Se ejecuta cuando el objeto se usa exitosamente.
     * Las subclases pueden sobrescribir para efectos adicionales.
     */
    protected void onSuccessfulUse(Player player) {
        System.out.println("✅ " + name + " usado correctamente.");
    }

    /**
     * Se ejecuta cuando falla el uso del objeto.
     */
    protected void onFailedUse(Player player) {
        System.out.println("❌ No se pudo usar " + name + ".");
    }

    /**
     * Se ejecuta cuando el objeto es consumido.
     */
    protected void onObjectConsumed(Player player) {
        System.out.println("🗑️ " + name + " ha sido consumido.");
    }

    // ============ MÉTODOS DE VALIDACIÓN ============

    /**
     * Verifica si el objeto puede ser usado.
     */
    protected boolean canBeUsed() {
        if (isUsed && consumable) {
            return false;
        }

        if (maxUsages > 0 && usageCount >= maxUsages) {
            return false;
        }

        return true;
    }

    /**
     * Verifica si el objeto debe ser consumido después del uso.
     */
    protected boolean shouldBeConsumed() {
        if (!consumable) {
            return false;
        }

        if (maxUsages > 0) {
            return usageCount >= maxUsages;
        }

        return true; // Por defecto, los consumibles se consumen
    }

    /**
     * Muestra mensaje cuando no se puede usar el objeto.
     */
    protected void showCannotUseMessage() {
        if (isUsed) {
            System.out.println("🚫 " + name + " ya ha sido usado.");
        } else if (maxUsages > 0 && usageCount >= maxUsages) {
            System.out.println("🚫 " + name + " ha alcanzado su límite de usos.");
        } else {
            System.out.println("🚫 No se puede usar " + name + " en este momento.");
        }
    }

    // ============ MÉTODOS DE ESTADO ============

    /**
     * Incrementa el contador de usos.
     */
    private void incrementUsageCount() {
        usageCount++;
    }

    /**
     * Marca el objeto como usado.
     */
    private void markAsUsed() {
        isUsed = true;
    }

    /**
     * Obtiene el ID único del objeto.
     */
    public String getId() {
        return id;
    }

    /**
     * Verifica si el objeto ha sido usado.
     */
    public boolean isUsed() {
        return isUsed;
    }

    /**
     * Obtiene el número de veces que se ha usado.
     */
    public int getUsageCount() {
        return usageCount;
    }

    /**
     * Obtiene el número máximo de usos.
     */
    public int getMaxUsages() {
        return maxUsages;
    }

    /**
     * Obtiene los usos restantes.
     */
    public int getRemainingUses() {
        if (maxUsages < 0) {
            return Integer.MAX_VALUE; // Usos infinitos
        }
        return Math.max(0, maxUsages - usageCount);
    }

    // ============ MÉTODOS DE UTILIDAD ============

    /**
     * Resetea el estado del objeto.
     */
    public void reset() {
        isUsed = false;
        usageCount = 0;
    }

    /**
     * Crea una copia del objeto.
     */
    public abstract BaseGameObject clone();

    /**
     * Obtiene información detallada del objeto.
     */
    public String getDetailedInfo() {
        StringBuilder info = new StringBuilder();
        info.append("🏷️ ").append(name).append("\n");
        info.append("📝 ").append(description).append("\n");
        info.append("🏆 Tipo: ").append(objectType.getDisplayName()).append("\n");
        info.append("✨ Rareza: ").append(rarity.getSymbol()).append(" ").append(rarity.getName()).append("\n");
        info.append("💎 Valor: ").append(value).append("\n");
        info.append("🔄 Consumible: ").append(consumable ? "Sí" : "No").append("\n");

        if (maxUsages > 0) {
            info.append("📊 Usos: ").append(usageCount).append("/").append(maxUsages);
        } else if (maxUsages == 0) {
            info.append("📊 Un solo uso");
        } else {
            info.append("📊 Usos ilimitados");
        }

        return info.toString();
    }

    @Override
    public String toString() {
        return String.format("%s %s [%s]",
                rarity.getSymbol(),
                name,
                objectType.getDisplayName());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        BaseGameObject that = (BaseGameObject) obj;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
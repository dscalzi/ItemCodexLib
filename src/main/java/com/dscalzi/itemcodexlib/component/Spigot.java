/*
 * ItemCodexLib
 * Copyright (C) 2018 Daniel D. Scalzi
 * See LICENSE for license information.
 */
package com.dscalzi.itemcodexlib.component;

import org.bukkit.Material;

/**
 * Represents a Spigot object in the JSON file.
 * 
 * <p>Stores meta data about an item's implementation
 * in the Spigot/Bukkit API.
 * 
 * @since 1.0.0
 */
public class Spigot  {
    
    // These MUST match the fields declared below.
    public static final String KEY_MATERIAL = "material";
    public static final String KEY_POTION_DATA = "potionData";
    
    private Material material;
    private PotionAbstract potionData;
    
    /**
     * Create a new Spigot object.
     * 
     * @param material The Material represented by this item.
     * 
     * @since 1.0.0
     */
    public Spigot(Material material) {
        this(material, null);
    }
    
    /**
     * Create a new Spigot object.
     * 
     * @param material The Material represented by this item.
     * @param potionData The potion data for this object or null.
     * 
     * @since 1.0.0
     */
    public Spigot(Material material, PotionAbstract potionData) {
        this.material = material;
        this.potionData = potionData;
    }
    
    /**
     * Get the Material of this item.
     * 
     * @return The Material of this item.
     * 
     * @since 1.0.0
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * Set the Material of this item.
     * 
     * @param material The new Material.
     * 
     * @since 1.0.0
     */
    public void setMaterial(Material material) {
        this.material = material;
    }
    
    /**
     * Check if this item has PotionData.
     * 
     * @return True if this item has potion data.
     * 
     * @since 1.0.0
     */
    public boolean hasPotionData() {
        return this.potionData != null;
    }
    
    /**
     * Get the PotionData for this item. This returns
     * null if the item does not have PotionData.
     * 
     * <p>See {@link #hasPotionData()}.
     * 
     * @return The PotionData for this item or null.
     * 
     * @since 1.0.0
     */
    public PotionAbstract getPotionData() {
        return potionData;
    }

    /**
     * Set the PotionData for this item.
     * 
     * @param pData The new PotionData.
     * 
     * @since 1.0.0
     */
    public void setPotionData(PotionAbstract pData) {
        this.potionData = pData;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((material == null) ? 0 : material.hashCode());
        result = prime * result + ((potionData == null) ? 0 : potionData.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Spigot other = (Spigot) obj;
        if (material != other.material)
            return false;
        if (potionData == null) {
            if (other.potionData != null)
                return false;
        } else if (!potionData.equals(other.potionData))
            return false;
        return true;
    }
    
}

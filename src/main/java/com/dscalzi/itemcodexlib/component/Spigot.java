/*
 * ItemCodexLib
 * Copyright (C) 2018 Daniel D. Scalzi
 * See LICENSE for license information.
 */
package com.dscalzi.itemcodexlib.component;

import org.bukkit.Material;
import org.bukkit.potion.PotionData;

public class Spigot  {
    
    // These MUST match the declared fields in org.bukkit.potion.PotionData
    public static final String KEY_POTION_TYPE = "type";
    public static final String KEY_POTION_EXTENDED = "extended";
    public static final String KEY_POTION_UPGRADED = "upgraded";
    
    // These MUST match the fields declared below.
    public static final String KEY_MATERIAL = "material";
    public static final String KEY_POTION_DATA = "potionData";
    
    private Material material;
    private PotionData potionData;
    
    public Spigot(Material material) {
        this(material, null);
    }
    
    public Spigot(Material material, PotionData potionData) {
        this.material = material;
        this.potionData = potionData;
    }
    
    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }
    
    public boolean hasPotionData() {
        return this.potionData != null;
    }
    
    public PotionData getPotionData() {
        return potionData;
    }

    public void setPotionData(PotionData pData) {
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

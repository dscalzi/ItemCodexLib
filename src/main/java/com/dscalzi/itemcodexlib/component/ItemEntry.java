/*
 * ItemCodexLib
 * Copyright (C) 2018 Daniel D. Scalzi
 * See LICENSE for license information.
 */
package com.dscalzi.itemcodexlib.component;

import java.util.List;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;

public class ItemEntry {

    // These MUST match the fields declared below.
    public static final String KEY_SPIGOT = "spigot";
    public static final String KEY_LEGACY = "legacy";
    public static final String KEY_ALIASES = "aliases";
    
    private Spigot spigot;
    private Legacy legacy;
    private List<String> aliases;
    
    private transient ItemStack itemStack;
    
    public ItemEntry(Spigot spigot, Legacy legacy, List<String> aliases) {
        
        this.spigot = spigot;
        this.legacy = legacy;
        this.aliases = aliases;
        
    }

    public Spigot getSpigot() {
        return spigot;
    }

    public void setSpigot(Spigot spigot) {
        this.spigot = spigot;
    }

    public boolean hasLegacy() {
        return legacy != null;
    }
    
    public Legacy getLegacy() {
        return legacy;
    }

    public void setLegacy(Legacy legacy) {
        this.legacy = legacy;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }
    
    public ItemStack getItemStack() {
        if(this.itemStack == null) {
            this.itemStack = new ItemStack(this.getSpigot().getMaterial());
            if(this.getSpigot().hasPotionData()) {
                PotionMeta meta = ((PotionMeta) this.itemStack.getItemMeta());
                meta.setBasePotionData(this.getSpigot().getPotionData());
                this.itemStack.setItemMeta(meta);
            }
        }
        return this.itemStack;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((aliases == null) ? 0 : aliases.hashCode());
        result = prime * result + ((legacy == null) ? 0 : legacy.hashCode());
        result = prime * result + ((spigot == null) ? 0 : spigot.hashCode());
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
        ItemEntry other = (ItemEntry) obj;
        if (aliases == null) {
            if (other.aliases != null)
                return false;
        } else if (!aliases.equals(other.aliases))
            return false;
        if (legacy == null) {
            if (other.legacy != null)
                return false;
        } else if (!legacy.equals(other.legacy))
            return false;
        if (spigot == null) {
            if (other.spigot != null)
                return false;
        } else if (!spigot.equals(other.spigot))
            return false;
        return true;
    }
    
}

/*
 * ItemCodexLib
 * Copyright (C) 2018 Daniel D. Scalzi
 * See LICENSE for license information.
 */
package com.dscalzi.itemcodexlib.component;

import java.util.List;

import org.bukkit.inventory.ItemStack;
import com.dscalzi.itemcodexlib.component.adapter.IItemStackAdapter;

/**
 * Represents an ItemEntry in the JSON file.
 * 
 * @since 1.0.0
 */
public class ItemEntry {

    // These MUST match the fields declared below.
    public static final String KEY_SPIGOT = "spigot";
    public static final String KEY_LEGACY = "legacy";
    public static final String KEY_ALIASES = "aliases";
    
    private Spigot spigot;
    private Legacy legacy;
    private List<String> aliases;
    
    private transient ItemStack itemStack;
    private transient IItemStackAdapter adapter;
    
    /**
     * Create a new ItemEntry.
     * 
     * @param spigot This item's corresponding Spigot object.
     * @param legacy This item's corresponding Legacy object or null.
     * @param aliases A list of aliases for this item.
     * @param adapter The IStackAdapter to handle ItemStack creation.
     * 
     * @since 1.0.0
     */
    public ItemEntry(Spigot spigot, Legacy legacy, List<String> aliases, IItemStackAdapter adapter) {
        this.spigot = spigot;
        this.legacy = legacy;
        this.aliases = aliases;
        this.adapter = adapter;
    }

    /**
     * Get this ItemEntry's Spigot object.
     * 
     * @return The Spigot object for this item entry.
     * 
     * @since 1.0.0
     */
    public Spigot getSpigot() {
        return spigot;
    }

    /**
     * Set this ItemEntry's Spigot Object.
     * 
     * @param spigot The new Spigot object.
     * 
     * @since 1.0.0
     */
    public void setSpigot(Spigot spigot) {
        this.spigot = spigot;
    }

    /**
     * Check if this ItemEntry has a legacy component.
     * This will be false for all items added post 1.13.
     * 
     * @return True if this object has a legacy component.
     * 
     * @since 1.0.0
     */
    public boolean hasLegacy() {
        return legacy != null;
    }
    
    /**
     * Get this ItemEntry's Legacy object. This will be null
     * if the item was added post 1.13.
     * 
     * <p>See {@link #hasLegacy()}
     * 
     * @return The Legacy object for this ItemEntry or null.
     * 
     * @since 1.0.0
     */
    public Legacy getLegacy() {
        return legacy;
    }

    /**
     * Set this ItemEntry's Legacy object.
     * 
     * @param legacy The new Legacy object.
     * 
     * @since 1.0.0
     */
    public void setLegacy(Legacy legacy) {
        this.legacy = legacy;
    }

    /**
     * Get this ItemEntry's alias list.
     * 
     * @return The alias list for this ItemEntry.
     * 
     * @since 1.0.0
     */
    public List<String> getAliases() {
        return aliases;
    }

    /**
     * Set this ItemEntry's alias list.
     * 
     * @param aliases The new alias list.
     * 
     * @since 1.0.0
     */
    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }
    
    /**
     * Get the ItemStack represented by this ItemEntry.
     * 
     * @return The ItemStack represented by this ItemEntry.
     * 
     * @since 1.0.0
     */
    public ItemStack getItemStack() {
        if(this.itemStack == null) {
            this.itemStack = this.adapter.resolveItemStack(this);
        }
        return this.itemStack.clone();
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

/*
 * ItemCodexLib
 * Copyright (C) 2018 Daniel D. Scalzi
 * See LICENSE for license information.
 */
package com.dscalzi.itemcodexlib.component;

import java.util.List;

/**
 * Represents the root object in the JSON file.
 * 
 * @since 1.0.0
 */
public class ItemList {

    // These MUST match the fields declared below.
    public static final String KEY_VERSION = "version";
    public static final String KEY_ITEMS = "items";
    
    private String version;
    private List<ItemEntry> items;
    
    /**
     * Create a new ItemEntry.
     * 
     * @param version The corresponding MineCraft version.
     * @param entries A list of ItemEntries.
     * 
     * @since 1.0.0
     */
    public ItemList(String version, List<ItemEntry> entries) {
        this.version = version;
        this.items = entries;
    }

    /**
     * Get the MineCraft version this ItemList targets.
     * 
     * @return The MineCraft version this ItemList targets.
     * 
     * @since 1.0.0
     */
    public String getVersion() {
        return version;
    }

    /**
     * Set the MineCraft version this ItemList targets.
     * 
     * @param version The new MineCraft version.
     * 
     * @since 1.0.0
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Get a list of ItemEntry objects declared by this ItemList.
     * 
     * @return A list of ItemEntry objects.
     * 
     * @since 1.0.0
     */
    public List<ItemEntry> getItems() {
        return items;
    }

    /**
     * Set the list of ItemEntry objects declared by this ItemList.
     * 
     * @param entries The new list of ItemEntry objects.
     * 
     * @since 1.0.0
     */
    public void setItems(List<ItemEntry> entries) {
        this.items = entries;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((items == null) ? 0 : items.hashCode());
        result = prime * result + ((version == null) ? 0 : version.hashCode());
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
        ItemList other = (ItemList) obj;
        if (items == null) {
            if (other.items != null)
                return false;
        } else if (!items.equals(other.items))
            return false;
        if (version == null) {
            if (other.version != null)
                return false;
        } else if (!version.equals(other.version))
            return false;
        return true;
    }
    
}

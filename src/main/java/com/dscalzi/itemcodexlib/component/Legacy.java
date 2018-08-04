/*
 * ItemCodexLib
 * Copyright (C) 2018 Daniel D. Scalzi
 * See LICENSE for license information.
 */
package com.dscalzi.itemcodexlib.component;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a Legacy object in the JSON file.
 * 
 * <p>Stores the legacy data values of an item.
 * 
 * @since 1.0.0
 */
public class Legacy {

    // These MUST match the fields declared below.
    public static final String KEY_LEGACY_ID = "id";
    public static final String KEY_LEGACY_DATA = "data";
    
    public static final Pattern LEGACY_REGEX_1 = Pattern.compile("^(\\d+):(\\d+)$");
    public static final Pattern LEGACY_REGEX_2 = Pattern.compile("^(\\d+)$");
    
    private short id;
    private short data;
    
    /**
     * Create a new Legacy object.
     * 
     * @param id The item's Id value.
     * @param data The item's data value.
     * 
     * @since 1.0.0
     */
    public Legacy(short id, short data) {
        this.id = id;
        this.data = data;
    }

    /**
     * Get the Id for this Legacy object.
     * 
     * @return The Id for this Legacy object.
     * 
     * @since 1.0.0
     */
    public short getId() {
        return id;
    }

    /**
     * Set the Id for this Legacy object.
     * 
     * @param id The new Id.
     * 
     * @since 1.0.0
     */
    public void setId(short id) {
        this.id = id;
    }

    /**
     * Get the data value for this Legacy object.
     * 
     * @return The data value for this Legacy object.
     * 
     * @since 1.0.0
     */
    public short getData() {
        return data;
    }

    /**
     * Set the data value for this Legacy object.
     * 
     * @param data The new data value.
     * 
     * @since 1.0.0
     */
    public void setData(short data) {
        this.data = data;
    }
    
    /**
     * Parse a legacy string into a Legacy object. This takes
     * the form of '{id}:{data}' or '{id}', where id and data
     * are shorts.
     * 
     * <p>Ex. '1:0' or '1' resolves to a Legacy object which
     * represents stone.
     * 
     * @param s The legacy string to parse.
     * @return An optional which resolves to the Legacy object
     * represented by the provided String.
     * 
     * @since 1.0.0
     */
    public static Optional<Legacy> parseLegacy(String s){
        try {
            Matcher m1 = LEGACY_REGEX_1.matcher(s);
            if(m1.matches()) {
                short id = Short.parseShort(m1.group(1));
                short data = Short.parseShort(m1.group(2));
                return Optional.of(new Legacy(id, data));
            }
            Matcher m2 = LEGACY_REGEX_2.matcher(s);
            if(m2.matches()) {
                return Optional.of(new Legacy(Short.parseShort(m2.group()), (short)0));
            }
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
        return Optional.empty();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + data;
        result = prime * result + id;
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
        Legacy other = (Legacy) obj;
        if (data != other.data)
            return false;
        if (id != other.id)
            return false;
        return true;
    }
    
}

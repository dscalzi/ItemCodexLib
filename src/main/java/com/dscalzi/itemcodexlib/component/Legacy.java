/*
 * ItemCodexLib
 * Copyright (C) 2018 Daniel D. Scalzi
 * See LICENSE for license information.
 */
package com.dscalzi.itemcodexlib.component;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Legacy {

    // These MUST match the fields declared below.
    public static final String KEY_LEGACY_ID = "id";
    public static final String KEY_LEGACY_DATA = "data";
    
    public static final Pattern LEGACY_REGEX_1 = Pattern.compile("^(\\d+):(\\d+)$");
    public static final Pattern LEGACY_REGEX_2 = Pattern.compile("^(\\d+)$");
    
    private short id;
    private short data;
    
    public Legacy(short id, short data) {
     
        this.id = id;
        this.data = data;
        
    }

    public short getId() {
        return id;
    }

    public void setId(short id) {
        this.id = id;
    }

    public short getData() {
        return data;
    }

    public void setData(short data) {
        this.data = data;
    }
    
    public static Optional<Legacy> parseLegacy(String s){
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

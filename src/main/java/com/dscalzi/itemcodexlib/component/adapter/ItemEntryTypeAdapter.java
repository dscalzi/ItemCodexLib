/*
 * ItemCodexLib
 * Copyright (C) 2018 Daniel D. Scalzi
 * See LICENSE for license information.
 */
package com.dscalzi.itemcodexlib.component.adapter;

import java.lang.reflect.Type;
import com.dscalzi.itemcodexlib.component.ItemEntry;
import com.dscalzi.itemcodexlib.component.Legacy;
import com.dscalzi.itemcodexlib.component.Spigot;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class ItemEntryTypeAdapter implements JsonSerializer<ItemEntry>{

    @Override
    public JsonElement serialize(ItemEntry src, Type typeOfSrc, JsonSerializationContext context) {
        
        // Serialize Spigot
        final Spigot s = src.getSpigot();
        final JsonObject spigotObject = new JsonObject();
        spigotObject.addProperty(Spigot.KEY_MATERIAL, s.getMaterial().name());
        if(src.getSpigot().hasPotionData()) {
            JsonObject potionData = new JsonObject();
            potionData.addProperty(Spigot.KEY_POTION_TYPE, s.getPotionData().getType().name());
            potionData.addProperty(Spigot.KEY_POTION_EXTENDED, s.getPotionData().isExtended());
            potionData.addProperty(Spigot.KEY_POTION_UPGRADED, s.getPotionData().isExtended());
            
            spigotObject.add(Spigot.KEY_POTION_DATA, potionData);
        }
        
        // Serialize 
        final JsonObject legacyObject = new JsonObject();
        legacyObject.addProperty(Legacy.KEY_LEGACY_ID, src.getLegacy().getId());
        legacyObject.addProperty(Legacy.KEY_LEGACY_DATA, src.getLegacy().getData());
        
        Gson g = new Gson();
        
        JsonObject itemEntry = new JsonObject();
        itemEntry.add(ItemEntry.KEY_SPIGOT, spigotObject);
        itemEntry.add(ItemEntry.KEY_LEGACY, legacyObject);
        itemEntry.addProperty(ItemEntry.KEY_ALIASES, g.toJson(src.getAliases()));
        
        
        return itemEntry;
    }

}

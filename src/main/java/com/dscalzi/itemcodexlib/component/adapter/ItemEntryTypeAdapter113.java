/*
 * ItemCodexLib
 * Copyright (C) 2018 Daniel D. Scalzi
 * See LICENSE for license information.
 */
package com.dscalzi.itemcodexlib.component.adapter;

import java.lang.reflect.Type;
import java.util.List;
import java.util.logging.Logger;

import com.dscalzi.itemcodexlib.component.ItemEntry;
import com.dscalzi.itemcodexlib.component.Legacy;
import com.dscalzi.itemcodexlib.component.PotionAbstract;
import com.dscalzi.itemcodexlib.component.Spigot;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

public class ItemEntryTypeAdapter113 implements JsonSerializer<ItemEntry>, JsonDeserializer<ItemEntry>{

    private Logger logger;
    private IItemStackAdapter adapter;
    
    public ItemEntryTypeAdapter113(Logger logger) {
        this.logger = logger;
        this.adapter = new ItemStackAdapter113();
    }
    
    @Override
    public JsonElement serialize(ItemEntry src, Type typeOfSrc, JsonSerializationContext context) {
        
        // Serialize Spigot
        final Spigot s = src.getSpigot();
        final JsonObject spigotObject = new JsonObject();
        spigotObject.addProperty(Spigot.KEY_MATERIAL, s.getMaterial().name());
        if(src.getSpigot().hasPotionData()) {
            JsonObject potionData = new JsonObject();
            potionData.addProperty(PotionAbstract.KEY_POTION_TYPE, s.getPotionData().getType());
            potionData.addProperty(PotionAbstract.KEY_POTION_EXTENDED, s.getPotionData().isExtended());
            potionData.addProperty(PotionAbstract.KEY_POTION_UPGRADED, s.getPotionData().isExtended());
            
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

    @Override
    public ItemEntry deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        
        Gson g = new Gson();
        JsonObject ob = json.getAsJsonObject();
        
        Legacy legacy = null;
        if(ob.has(ItemEntry.KEY_LEGACY)) {
            legacy = g.fromJson(ob.get(ItemEntry.KEY_LEGACY), Legacy.class);
        }
        List<String> aliases = g.fromJson(ob.get(ItemEntry.KEY_ALIASES), new TypeToken<List<String>>() {}.getType());
        
        Spigot spigot = g.fromJson(ob.get(ItemEntry.KEY_SPIGOT), Spigot.class);
        if(spigot.getMaterial() == null) {
            logger.warning("Error while parsing: Entry has unknown material type " + ob.get(ItemEntry.KEY_SPIGOT).getAsJsonObject().get(Spigot.KEY_MATERIAL) + ".");
            
            return null;
        }
        
        return new ItemEntry(spigot, legacy, aliases, adapter);
    }

}

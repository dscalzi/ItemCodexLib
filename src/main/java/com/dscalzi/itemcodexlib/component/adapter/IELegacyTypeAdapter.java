/*
 * ItemCodexLib
 * Copyright (C) 2018 Daniel D. Scalzi
 * See LICENSE for license information.
 */
package com.dscalzi.itemcodexlib.component.adapter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.dscalzi.itemcodexlib.component.ItemEntry;
import com.dscalzi.itemcodexlib.component.Legacy;
import com.dscalzi.itemcodexlib.component.Spigot;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

public class IELegacyTypeAdapter implements JsonDeserializer<ItemEntry>{
    
    private Logger logger;
    private Constructor constructor;
    
    public IELegacyTypeAdapter(Logger logger) {
        this.logger = logger;
        try {
            constructor = ItemStack.class.getConstructor(int.class, int.class, short.class, Byte.class);
        } catch (NoSuchMethodException e) {
            // Should not happen on 1.12.2 and below.
            e.printStackTrace();
        } catch (SecurityException e) {
            // Should be public
            e.printStackTrace();
        }
    }

    @Override
    public ItemEntry deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        
        if(constructor == null) {
            return null;
        }
        
        Gson g = new Gson();
        JsonObject ob = json.getAsJsonObject();
        
        Legacy legacy = null;
        if(ob.has(ItemEntry.KEY_LEGACY)) {
            legacy = g.fromJson(ob.get(ItemEntry.KEY_LEGACY), Legacy.class);
            
            ItemStack target;
            try {
                target = (ItemStack)constructor.newInstance(legacy.getId(), 1, legacy.getData(), (byte)legacy.getData());
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException e) {
                // Should not happen on 1.12.2 and below.
                e.printStackTrace();
                return null;
            }
            
            List<String> aliases = g.fromJson(ob.get(ItemEntry.KEY_ALIASES), new TypeToken<List<String>>() {}.getType());
            
            Spigot spigot = g.fromJson(ob.get(ItemEntry.KEY_SPIGOT), Spigot.class);
            spigot.setMaterial(target.getType());
            if(spigot.getMaterial() == null || spigot.getMaterial() == Material.AIR) {
                logger.warning("Error while parsing: Entry has unknown type " + legacy.getId() + ":" + legacy.getData() + ".");
                return null;
            }
            
            return new ItemEntry(spigot, legacy, aliases);
        } else {
            return null;
        }
    }

}

/*
 * ItemCodexLib
 * Copyright (C) 2018 Daniel D. Scalzi
 * See LICENSE for license information.
 */
package com.dscalzi.itemcodexlib.component.adapter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.dscalzi.itemcodexlib.VersionUtil;
import com.dscalzi.itemcodexlib.component.ItemEntry;
import com.dscalzi.itemcodexlib.component.Legacy;
import com.dscalzi.itemcodexlib.component.Spigot;
import com.dscalzi.itemcodexlib.util.ReflectionUtil;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

public class ItemEntryTypeAdapter18 implements JsonDeserializer<ItemEntry>{
    
    private Logger logger;
    private Constructor<ItemStack> constructor;
    private IItemStackAdapter adapter;
    private boolean is19up;
    
    @SuppressWarnings("unchecked")
    public ItemEntryTypeAdapter18(Logger logger) {
        this.logger = logger;
        this.constructor = (Constructor<ItemStack>)ReflectionUtil.getConstructor(ItemStack.class, int.class, int.class, short.class, Byte.class);
        this.is19up = VersionUtil.compare(VersionUtil.getVersion(), "1.9") >= 0;
        if(this.is19up) {
            this.adapter = new ItemStackAdapter19();
        } else {
            this.adapter = new ItemStackAdapter18();
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
            if(!is19up) {
                // Use the old splash potion id.
                if(legacy.getId() == 438) {
                    legacy.setId((short)373);
                }
            }
            
            ItemStack target;
            try {
                target = constructor.newInstance(legacy.getId(), 1, legacy.getData(), (byte)legacy.getData());
            } catch (Throwable t) {
                // Should not happen on 1.12.2 and below.
                t.printStackTrace();
                return null;
            }
            
            List<String> aliases = g.fromJson(ob.get(ItemEntry.KEY_ALIASES), new TypeToken<List<String>>() {}.getType());
            
            Spigot spigot = g.fromJson(ob.get(ItemEntry.KEY_SPIGOT), Spigot.class);
            spigot.setMaterial(target.getType());
            if(spigot.getMaterial() == null || spigot.getMaterial() == Material.AIR) {
                logger.warning("Error while parsing: Entry has unknown type " + legacy.getId() + ":" + legacy.getData() + ".");
                return null;
            }
            
            return new ItemEntry(spigot, legacy, aliases, adapter);
        } else {
            return null;
        }
    }

}

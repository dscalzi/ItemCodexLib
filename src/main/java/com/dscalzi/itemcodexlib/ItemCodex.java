/*
 * ItemCodexLib
 * Copyright (C) 2018 Daniel D. Scalzi
 * See LICENSE for license information.
 */
package com.dscalzi.itemcodexlib;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import com.dscalzi.itemcodexlib.component.ItemEntry;
import com.dscalzi.itemcodexlib.component.ItemList;
import com.dscalzi.itemcodexlib.component.Legacy;
import com.dscalzi.itemcodexlib.component.PotionAbstract;
import com.dscalzi.itemcodexlib.component.adapter.ItemEntryTypeAdapter18;
import com.dscalzi.itemcodexlib.component.adapter.ItemStackAdapter113;
import com.dscalzi.itemcodexlib.component.adapter.ItemStackAdapter18;
import com.dscalzi.itemcodexlib.component.adapter.ItemStackAdapter19;
import com.dscalzi.itemcodexlib.component.adapter.IItemStackAdapter;
import com.dscalzi.itemcodexlib.component.adapter.ILegacyItemStackAdapter;
import com.dscalzi.itemcodexlib.component.adapter.ItemEntryTypeAdapter113;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

/**
 * Utility class to load and interact with the item codex.
 * 
 * @since 1.0.0
 */
public class ItemCodex {

    public static final String VERSION = "1.0.0-pre.6";
    public static final String CODEX_FILE_NAME = "items.json";
    
    private ItemList loadedList;
    private Map<Legacy, ItemEntry> legacyMap;
    private Map<String, ItemEntry> aliasMap;
    private File localFile;
    private Logger logger;
    
    private boolean legacy = false;
    private IItemStackAdapter adapter;
    
    /**
     * Initialize a new ItemCodex instance.
     * 
     * @param plugin The plugin which is initializing this ItemCodex.
     * 
     * @since 1.0.0
     */
    public ItemCodex(JavaPlugin plugin) {
        
        this.legacyMap = new HashMap<Legacy, ItemEntry>();
        this.aliasMap = new HashMap<String, ItemEntry>();
        this.localFile = new File(plugin.getDataFolder() + File.separator + CODEX_FILE_NAME);
        this.logger = plugin.getLogger();
        
        if(VersionUtil.compare(VersionUtil.getVersion(), "1.13") >= 0) {
            this.adapter = new ItemStackAdapter113();
        } else if(VersionUtil.compare(VersionUtil.getVersion(), "1.9") >= 0) {
            this.adapter = new ItemStackAdapter19();
        } else {
            this.adapter = new ItemStackAdapter18();
        }
        
        logger.info("Loading ItemCodex..");
        this.initialize();
        this.initMaps();
        logger.info("ItemCodex loaded!");
    }
    
    private void initialize() {
        
        if(!this.localFile.exists()) {
            updateLocalFile();
        }
        
        ItemList il = loadJSONFile();
        if(VersionUtil.gt(VERSION, il.getRevision())) {
            logger.info("Updating " + CODEX_FILE_NAME + ": v" + il.getRevision() + " -> v" + VERSION);
            updateLocalFile();
            this.loadedList = loadJSONFile();
        } else {
            this.loadedList = il;
        }
        
        cleanJSONFile();
        
    }
    
    private void updateLocalFile() {
        try(InputStream is = ItemCodex.class.getResourceAsStream("/" + CODEX_FILE_NAME)) {
            if(!localFile.getParentFile().exists()) {
                localFile.getParentFile().mkdirs();
            }
            Files.copy(is, localFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            logger.severe("Failed to save " + CODEX_FILE_NAME + " locally.");
            e.printStackTrace();
        }
    }
    
    private ItemList loadJSONFile() {
        Gson g = null;
        
        if(this.adapter instanceof ItemStackAdapter113){
            logger.info("Using 1.13 Item Adapter.");
            g = new GsonBuilder().registerTypeAdapter(ItemEntry.class, new ItemEntryTypeAdapter113(this.logger)).create();
        } else {
            logger.info("Using Legacy Item Adapter.");
            this.legacy = true;
            g = new GsonBuilder().registerTypeAdapter(ItemEntry.class, new ItemEntryTypeAdapter18(this.logger)).create();
        }
        
        try(Reader r = new FileReader(localFile);
            JsonReader jr = new JsonReader(r)){
            
            return g.fromJson(jr, ItemList.class);
            
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private void cleanJSONFile() {
        this.loadedList.getItems().removeIf(e -> e == null);
    }
    
    private void initMaps() {
        
        for(final ItemEntry e : loadedList.getItems()) {
            
            if(!this.legacy && !e.getSpigot().hasPotionData()) {
                aliasMap.put(e.getSpigot().getMaterial().name().toLowerCase(), e);
            }
            for(final String s : e.getAliases()) {
                aliasMap.put(s, e);
            }
            
            if(e.hasLegacy()) {
               legacyMap.put(e.getLegacy(), e);
            }
        }
        
    }
    
    /**
     * Search for an ItemEntry by alias or legacy string. See
     * {@link Legacy#parseLegacy(String)} for information on
     * the format of a legacy string.
     * 
     * @param keyword The alias or legacy string to search with.
     * @return An optional which resolves to the ItemEntry corresponding
     * to the provided keyword.
     * 
     * @since 1.0.0
     */
    public Optional<ItemEntry> getItem(String keyword){
        Optional<Legacy> lOpt = Legacy.parseLegacy(keyword);
        if(lOpt.isPresent()) {
            return getItemByLegacy(lOpt.get());
        }
        return getItemByAlias(keyword.toLowerCase());
    }
    
    /**
     * Search for an ItemEntry by Legacy object. This method will not
     * return items added after MineCraft version 1.13. It is provided
     * simply for compatibility purposes.
     * 
     * See {@link #getItem(String)}.
     * 
     * @param legacy The legacy object to search with.
     * @return An optional which resolves to the ItemEntry corresponding
     * to the provided legacy object.
     */
    public Optional<ItemEntry> getItemByLegacy(Legacy legacy) {
        return Optional.ofNullable(legacyMap.get(legacy));
    }
    
    /**
     * Search for an ItemEntry by alias.
     * 
     * @param alias The alias to search with.
     * @return An optional which resolves to the ItemEntry corresponding
     * to the provided alias.
     * 
     * @since 1.0.0
     */
    public Optional<ItemEntry> getItemByAlias(String alias) {
        return Optional.ofNullable(aliasMap.get(alias));
    }
    
    /**
     * Search for the ItemEntry matching the provided ItemStack.
     * 
     * @param item The ItemStack to search with.
     * @return An optional which resolves to the ItemEntry corresponding
     * to the provided ItemStack.
     * 
     * @since 1.0.0
     */
    public Optional<ItemEntry> getItemByItemStack(ItemStack item){
        
        if(this.adapter instanceof ILegacyItemStackAdapter) {
            ILegacyItemStackAdapter lAdapter = (ILegacyItemStackAdapter)adapter;
            Legacy l = lAdapter.getLegacyFromItemStack(item);
            if(this.adapter.isPotion(item)) {
                if(adapter instanceof ItemStackAdapter19) {
                    PotionAbstract potionData = adapter.abstractPotionData(item);
                    for(ItemEntry e : loadedList.getItems()) {
                        if(e.getSpigot().getMaterial() == item.getType()) {
                            if(e.getSpigot().getPotionData().equals(potionData)) {
                                return Optional.of(e);
                            }
                        }
                    }
                }
            }
            // 1.8 is pure id:data
            return getItemByLegacy(l);
        } else {
            if(this.adapter.isPotion(item)) {
                PotionAbstract potionData = adapter.abstractPotionData(item);
                for(ItemEntry e : loadedList.getItems()) {
                    if(e.getSpigot().getMaterial().equals(item.getType())) {
                        if(e.getSpigot().hasPotionData() && e.getSpigot().getPotionData().equals(potionData)) {
                            return Optional.of(e);
                        }
                    }
                }
            } else {
                for(ItemEntry e : loadedList.getItems()) {
                    if(e.getSpigot().getMaterial().equals(item.getType())) {
                        return Optional.of(e);
                    }
                }
            }
        }
        
        return Optional.empty();
        
    }
    
    /**
     * Get the full loaded ItemList. 
     * 
     * @return The full ItemList object loaded from the json file.
     * 
     * @since 1.0.0
     */
    public ItemList getLoadedList() {
        return this.loadedList;
    }
    
}

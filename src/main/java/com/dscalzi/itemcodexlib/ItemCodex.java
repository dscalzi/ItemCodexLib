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

import org.bukkit.plugin.java.JavaPlugin;
import com.dscalzi.itemcodexlib.component.ItemEntry;
import com.dscalzi.itemcodexlib.component.ItemList;
import com.dscalzi.itemcodexlib.component.Legacy;
import com.dscalzi.itemcodexlib.component.adapter.ItemEntryTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

/**
 * Utility class to load and interact with the item codex.
 * 
 * @since 1.0.0
 */
public class ItemCodex {

    public static final String VERSION = "1.0.0";
    public static final String CODEX_FILE_NAME = "items.json";
    
    private ItemList loadedList;
    private Map<Legacy, ItemEntry> legacyMap;
    private Map<String, ItemEntry> aliasMap;
    private File localFile;
    private Logger logger;
    
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
        Gson g = new GsonBuilder().registerTypeAdapter(ItemEntry.class, new ItemEntryTypeAdapter(this.logger)).create();
        
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
            
            aliasMap.put(e.getSpigot().getMaterial().name().toLowerCase(), e);
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

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
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

public class ItemCodex {

    public static final String CODEX_FILE_NAME = "items.json";
    
    private ItemList loadedList;
    private Map<Legacy, ItemEntry> legacyMap;
    private Map<String, ItemEntry> aliasMap;
    private File localFile;
    private Logger logger;
    
    public ItemCodex(JavaPlugin plugin) {
        
        this.legacyMap = new HashMap<Legacy, ItemEntry>();
        this.aliasMap = new HashMap<String, ItemEntry>();
        this.localFile = new File(plugin.getDataFolder() + File.separator + CODEX_FILE_NAME);
        this.logger = plugin.getLogger();
        
        this.initialize();
        this.initMaps();
    }
    
    private void initialize() {
        
        if(!this.localFile.exists()) {
            try(InputStream is = ItemCodex.class.getResourceAsStream("/" + CODEX_FILE_NAME)) {
                Files.copy(is, localFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                logger.severe("Failed to save " + CODEX_FILE_NAME + " locally.");
                e.printStackTrace();
            }
        }
        
        Gson g = new Gson();
        
        try(Reader r = new FileReader(localFile);
            JsonReader jr = new JsonReader(r)){
            
            ItemList il = g.fromJson(jr, ItemList.class);
            this.loadedList = il;
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
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
    
    public Optional<ItemEntry> getItem(String keyword){
        Optional<Legacy> lOpt = Legacy.parseLegacy(keyword);
        if(lOpt.isPresent()) {
            return getItemByLegacy(lOpt.get());
        }
        return getItemByAlias(keyword.toLowerCase());
    }
    
    public Optional<ItemEntry> getItemByLegacy(Legacy l) {
        return Optional.ofNullable(legacyMap.get(l));
    }
    
    public Optional<ItemEntry> getItemByAlias(String s) {
        return Optional.ofNullable(aliasMap.get(s));
    }
    
    public ItemList getLoadedList() {
        return this.loadedList;
    }
    
}

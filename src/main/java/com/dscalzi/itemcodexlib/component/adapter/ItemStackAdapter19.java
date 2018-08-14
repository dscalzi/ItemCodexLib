/*
 * ItemCodexLib
 * Copyright (C) 2018 Daniel D. Scalzi
 * See LICENSE for license information.
 */
package com.dscalzi.itemcodexlib.component.adapter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

import com.dscalzi.itemcodexlib.component.ItemEntry;
import com.dscalzi.itemcodexlib.component.Legacy;
import com.dscalzi.itemcodexlib.component.PotionAbstract;
import com.dscalzi.itemcodexlib.util.ReflectionUtil;

public class ItemStackAdapter19 implements IItemStackAdapter {
    
    @Override
    public ItemStack resolveItemStack(ItemEntry origin) {
        final Legacy l = origin.getLegacy();
        
        @SuppressWarnings("unchecked")
        Constructor<ItemStack> c = (Constructor<ItemStack>)ReflectionUtil.getConstructor(ItemStack.class, int.class, int.class, short.class);
        ItemStack is;
        try {
            is = c.newInstance(l.getId(), 1, origin.getSpigot().hasPotionData() ? 0 : l.getData());
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
        
        if(origin.getSpigot().hasPotionData()) {
            final PotionAbstract pa = origin.getSpigot().getPotionData();
            Class<?> potionDataClass = ReflectionUtil.getBukkitClass("potion.PotionData");
            Constructor<?> potionDataConstructor = ReflectionUtil.getConstructor(potionDataClass, PotionType.class, boolean.class, boolean.class);
            Method setBasePotionData = ReflectionUtil.getMethod(PotionMeta.class, "setBasePotionData", potionDataClass);
            PotionMeta meta = ((PotionMeta) is.getItemMeta());
            try {
                Object potionData;
                potionData = potionDataConstructor.newInstance(PotionType.valueOf(pa.getType()), pa.isExtended(), pa.isUpgraded());
                //PotionData pd = new PotionData(PotionType.valueOf(pa.getType()), pa.isExtended(), pa.isUpgraded());
                setBasePotionData.invoke(meta, potionData);
                //meta.setBasePotionData(pd);
                is.setItemMeta(meta);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        return is;
    }
    
}

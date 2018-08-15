/*
 * ItemCodexLib
 * Copyright (C) 2018 Daniel D. Scalzi
 * See LICENSE for license information.
 */
package com.dscalzi.itemcodexlib.component.adapter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;

import com.dscalzi.itemcodexlib.component.ItemEntry;
import com.dscalzi.itemcodexlib.component.Legacy;
import com.dscalzi.itemcodexlib.component.PotionAbstract;
import com.dscalzi.itemcodexlib.util.ReflectionUtil;

public class ItemStackAdapter18 implements IItemStackAdapter, ILegacyItemStackAdapter {
    
    @Override
    public ItemStack resolveItemStack(ItemEntry origin) {
        final Legacy l = origin.getLegacy();
        
        @SuppressWarnings("unchecked")
        Constructor<ItemStack> c = (Constructor<ItemStack>)ReflectionUtil.getConstructor(ItemStack.class, int.class, int.class, short.class);
        try {
            return c.newInstance(l.getId(), 1, l.getData());
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
            
    }

    @Override
    public boolean isPotion(ItemStack i) {
        return i.getType() == Material.POTION;
    }

    @Override
    public PotionAbstract abstractPotionData(ItemStack i) {
        Class<?> potionClass = ReflectionUtil.getBukkitClass("potion.Potion");
        Method fromItemStack = ReflectionUtil.getMethod(potionClass, "fromItemStack", ItemStack.class);
        Method getType = ReflectionUtil.getMethod(potionClass, "getType");
        Method hasExtendedDuration = ReflectionUtil.getMethod(potionClass, "hasExtendedDuration");
        Method getLevel = ReflectionUtil.getMethod(potionClass, "getLevel");
        try {
            Object potion = fromItemStack.invoke(null, i);
            int level = (int)getLevel.invoke(potion);
            return new PotionAbstract(((PotionType)getType.invoke(potion)).name(), (boolean)hasExtendedDuration.invoke(potion), level == 2);
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }

    @Override
    public Legacy getLegacyFromItemStack(ItemStack i) {
        Method getTypeId = ReflectionUtil.getMethod(ItemStack.class, "getTypeId");
        try {
            return new Legacy((int)getTypeId.invoke(i), i.getDurability());
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }
    
}

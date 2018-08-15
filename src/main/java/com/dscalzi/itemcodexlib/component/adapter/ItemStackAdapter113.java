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
import com.dscalzi.itemcodexlib.component.PotionAbstract;
import com.dscalzi.itemcodexlib.util.ReflectionUtil;

public class ItemStackAdapter113 implements IItemStackAdapter {

    @Override
    public ItemStack resolveItemStack(ItemEntry origin) {
        ItemStack is = new ItemStack(origin.getSpigot().getMaterial());
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

    @Override
    public boolean isPotion(ItemStack i) {
        return i.hasItemMeta() && i.getItemMeta() instanceof PotionMeta;
    }

    @Override
    public PotionAbstract abstractPotionData(ItemStack i) {
        if(this.isPotion(i)) {
            Class<?> potionDataClass = ReflectionUtil.getBukkitClass("potion.PotionData");
            Method getBasePotionData = ReflectionUtil.getMethod(PotionMeta.class, "getBasePotionData");
            Method getType = ReflectionUtil.getMethod(potionDataClass, "getType");
            Method isExtended = ReflectionUtil.getMethod(potionDataClass, "isExtended");
            Method isUpgraded = ReflectionUtil.getMethod(potionDataClass, "isUpgraded");
            try {
                Object originData = getBasePotionData.invoke((PotionMeta)i.getItemMeta());
                return new PotionAbstract(((PotionType)getType.invoke(originData)).name(), (boolean)isExtended.invoke(originData), (boolean)isUpgraded.invoke(originData));
            } catch (Throwable t) {
                t.printStackTrace();
                return null;
            }
        }
        return null;
    }

}

/*
 * ItemCodexLib
 * Copyright (C) 2018 Daniel D. Scalzi
 * See LICENSE for license information.
 */
package com.dscalzi.itemcodexlib.component.adapter;

import org.bukkit.inventory.ItemStack;

import com.dscalzi.itemcodexlib.component.ItemEntry;
import com.dscalzi.itemcodexlib.component.PotionAbstract;

public interface IItemStackAdapter {

    public ItemStack resolveItemStack(ItemEntry origin);
    
    public boolean isPotion(ItemStack i);
    
    public PotionAbstract abstractPotionData(ItemStack i);
    
}

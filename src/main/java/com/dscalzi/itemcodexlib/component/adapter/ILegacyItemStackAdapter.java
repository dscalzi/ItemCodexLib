/*
 * ItemCodexLib
 * Copyright (C) 2018 Daniel D. Scalzi
 * See LICENSE for license information.
 */
package com.dscalzi.itemcodexlib.component.adapter;

import org.bukkit.inventory.ItemStack;

import com.dscalzi.itemcodexlib.component.Legacy;

public interface ILegacyItemStackAdapter {

    public Legacy getLegacyFromItemStack(ItemStack i);
    
}

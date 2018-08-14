/*
 * ItemCodexLib
 * Copyright (C) 2018 Daniel D. Scalzi
 * See LICENSE for license information.
 */
package com.dscalzi.itemcodexlib.component.adapter;

import java.lang.reflect.Constructor;
import org.bukkit.inventory.ItemStack;
import com.dscalzi.itemcodexlib.component.ItemEntry;
import com.dscalzi.itemcodexlib.component.Legacy;
import com.dscalzi.itemcodexlib.util.ReflectionUtil;

public class ItemStackAdapter18 implements IItemStackAdapter {
    
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
    
}

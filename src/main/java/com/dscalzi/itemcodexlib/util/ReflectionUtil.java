/*
 * ItemCodexLib
 * Copyright (C) 2018 Daniel D. Scalzi
 * See LICENSE for license information.
 */
package com.dscalzi.itemcodexlib.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public final class ReflectionUtil {

    // Caches

    private final static Map<String, Class<?>> bukkitClasses;

    private final static Map<Class<?>, Map<String, Method>> cachedMethods;
    private final static Map<Class<?>, Map<Class<?>[], Constructor<?>>> cachedConstructors;

    static {
        bukkitClasses = new HashMap<String, Class<?>>();
        cachedMethods = new HashMap<Class<?>, Map<String, Method>>();
        cachedConstructors = new HashMap<Class<?>, Map<Class<?>[], Constructor<?>>>();
    }

    public static Class<?> getBukkitClass(String localPackage) {

        if (bukkitClasses.containsKey(localPackage))
            return bukkitClasses.get(localPackage);

        String declaration = "org.bukkit." + localPackage;
        Class<?> clazz;

        try {
            clazz = Class.forName(declaration);
        } catch (Throwable e) {
            e.printStackTrace();
            return bukkitClasses.put(localPackage, null);
        }

        bukkitClasses.put(localPackage, clazz);
        return clazz;
    }

    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... params) {
        if (!cachedMethods.containsKey(clazz))
            cachedMethods.put(clazz, new HashMap<String, Method>());

        Map<String, Method> methods = cachedMethods.get(clazz);

        if (methods.containsKey(methodName))
            return methods.get(methodName);

        try {
            Method method = clazz.getMethod(methodName, params);
            methods.put(methodName, method);
            cachedMethods.put(clazz, methods);
            return method;
        } catch (Throwable e) {
            e.printStackTrace();
            methods.put(methodName, null);
            cachedMethods.put(clazz, methods);
            return null;
        }
    }
    
    public static Constructor<?> getConstructor(Class<?> clazz, Class<?>... params) {
        if (!cachedConstructors.containsKey(clazz))
            cachedConstructors.put(clazz, new HashMap<Class<?>[], Constructor<?>>());
        
        Map<Class<?>[], Constructor<?>> constructors = cachedConstructors.get(clazz);
        
        if (constructors.containsKey(params))
            return constructors.get(params);
        
        try {
            Constructor<?> constructor = clazz.getConstructor(params);
            constructors.put(params, constructor);
            cachedConstructors.put(clazz, constructors);
            return constructor;
        } catch (Throwable e) {
            e.printStackTrace();
            constructors.put(params, null);
            cachedConstructors.put(clazz, constructors);
            return null;
        }
        
    }

}

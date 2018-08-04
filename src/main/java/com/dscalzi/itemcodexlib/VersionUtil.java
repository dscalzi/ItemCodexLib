/*
 * ItemCodexLib
 * Copyright (C) 2018 Daniel D. Scalzi
 * See LICENSE for license information.
 */
package com.dscalzi.itemcodexlib;

import java.util.regex.Pattern;

import org.bukkit.Bukkit;

/**
 * Utility class to assist with MineCraft version semantics.
 * 
 * Supports {major}.{minor}.{revision}, where the revision is optional.
 * Does not support pre-release components.
 */
public class VersionUtil {

    private static final Pattern VERSION_REGEX = Pattern.compile("\\(MC: (.*)\\)");
    
    private static String version;
    
    /**
     * Get the MineCraft version for this implementation.
     * 
     * @return The MineCraft version of the server.
     * 
     * @since 1.0.0
     */
    public static String getVersion() {
        if(version == null) {
            version = VERSION_REGEX.matcher(Bukkit.getVersion()).group();
        }
        return version;
    }
    
    /**
     * Return if the first version is greater than the second;
     * 
     * @param first The first version string.
     * @param second The second version string.
     * @return True if the first version is greater than the second.
     * 
     * @since 1.0.0
     */
    public static boolean gt(String first, String second) {
        return compare(first, second) == 1;
    }
    
    /**
     * Return if the first version is less than the second;
     * 
     * @param first The first version string.
     * @param second The second version string.
     * @return True if the first version is less than the second.
     * 
     * @since 1.0.0
     */
    public static boolean lt(String first, String second) {
        return compare(first, second) == -1;
    }
    
    /**
     * Return if the first version is equal to the second;
     * 
     * @param first The first version string.
     * @param second The second version string.
     * @return True if the first version is equal to the second.
     * 
     * @since 1.0.0
     */
    public static boolean eq(String first, String second) {
        return compare(first, second) == 0;
    }

    /**
     * See {@link java.util.Comparator#compare(Object, Object)}.
     * 
     * @param first The first version string.
     * @param second The second version string.
     * @return A negative integer if first &lt; second, 0 if equal,
     * and a positive integer if first &gt; second.
     * 
     * @since 1.0.0
     */
    public static int compare(String first, String second) {
        String[] fPts = first.split(".");
        String[] sPts = second.split(".");
        
        // Compare Major
        int fMaj = Integer.parseInt(fPts[0]), sMaj = Integer.parseInt(sPts[0]);
        if(fMaj == sMaj) {
            // Compare Minor
            int fMin = Integer.parseInt(fPts[1]), sMin = Integer.parseInt(sPts[1]);
            if(fMin == sMin) {
                // Compare Revision.
                // Mojang only has a revision if its value is > 0.
                if(fPts.length > 2) {
                    int fRev = Integer.parseInt(fPts[2]);
                    if(sPts.length > 2) {
                        return fRev - Integer.parseInt(sPts[2]);
                    } else {
                        return 1;
                    }
                } else if(sPts.length > 2) {
                    return -1;
                } else {
                    return 0;
                }
            } else {
                return fMin - sMin;
            }
        } else {
            return fMaj - sMaj;
        }
    }
    
}

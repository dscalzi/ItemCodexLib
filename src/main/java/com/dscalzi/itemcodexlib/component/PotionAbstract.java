package com.dscalzi.itemcodexlib.component;

/**
 * Represents a Abstract potion data stored in the JSON file.
 * 
 * @since 1.0.0
 */
public class PotionAbstract {

    public static final String KEY_POTION_TYPE = "type";
    public static final String KEY_POTION_EXTENDED = "extended";
    public static final String KEY_POTION_UPGRADED = "upgraded";
    
    // These MUST match the fields declared below.
    private String type;
    private boolean extended;
    private boolean upgraded;
    
    /**
     * Create a new PotionAbstract. Both extended and upgraded
     * cannot be true at the same time.
     * 
     * @param type The PotionType string.
     * @param extended Whether or not the potion is extended.
     * @param upgraded Whether or not the potion is upgraded.
     * 
     * @since 1.0.0
     */
    public PotionAbstract(String type, boolean extended, boolean upgraded) {
        this.type = type;
        this.extended = extended;
        this.upgraded = upgraded;
    }

    /**
     * Get the PotionType.
     * 
     * @return The PotionType string.
     * 
     * @since 1.0.0
     */
    public String getType() {
        return type;
    }

    /**
     * Set the PotionType.
     * 
     * @param type The new PotionType string.
     * 
     * @since 1.0.0
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Check if the potion is extended.
     * 
     * @return Whether or not the potion is extended.
     * 
     * @since 1.0.0
     */
    public boolean isExtended() {
        return extended;
    }

    /**
     * Change if the potion is extended.
     * 
     * @param extended The new extended value.
     * 
     * @since 1.0.0
     */
    public void setExtended(boolean extended) {
        this.extended = extended;
    }

    /**
     * Check if the potion is upgraded.
     * 
     * @return Whether or not the potion is upgraded.
     * 
     * @since 1.0.0
     */
    public boolean isUpgraded() {
        return upgraded;
    }

    /**
     * Change if the potion is upraded.
     * 
     * @param upgraded The new upgraded value.
     * 
     * @since 1.0.0
     */
    public void setUpgraded(boolean upgraded) {
        this.upgraded = upgraded;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (extended ? 1231 : 1237);
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + (upgraded ? 1231 : 1237);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PotionAbstract other = (PotionAbstract) obj;
        if (extended != other.extended)
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        if (upgraded != other.upgraded)
            return false;
        return true;
    }
    
}

/**
 * enum representing different types of garment
 */
public enum GarmentType {
    T_SHIRT, HOODIE,SELECT_TYPE;
    /**
     * @return prettified representation of garment constants
     */
    public String toString() {
        return switch (this) {
            case HOODIE -> "Hoodie";
            case T_SHIRT -> "T-shirt";
            case SELECT_TYPE -> "Select garment type";
        };
    }
}

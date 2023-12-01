/**
 * enum representing different garment material types
 */
public enum Material {
    COTTON, WOOL_BLEND, POLYESTER,NA;
    /**
     * @return prettified representation of material constants
     */
    public String toString(){
        return switch (this){
            case COTTON -> "Cotton";
            case POLYESTER -> "Polyester";
            case WOOL_BLEND -> "Wool blend";
            default -> "NA";
        };
    }
}

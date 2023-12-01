/**
 * enum representing the different garment sizes
 */
public enum Size {
    XS,S,M,L,XL,XXL,XXXL,XXXXL;
    /**
     * @return prettified representation of sizes constants
     */
    public String toString(){
        return switch (this) {
            case XS -> "Extra small";
            case S -> "Small";
            case M -> "Medium";
            case L -> "Large";
            case XL -> "XL";
            case XXL -> "2XL";
            case XXXL -> "3XL";
            case XXXXL -> "4XL";
//            default -> "NA";
        };
    }
}

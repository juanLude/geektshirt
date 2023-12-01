/**
 * enum representing the different types of sleeves
 */
public enum SleeveType {
    LONG,SHORT,SLEEVELESS,BAT_WING,PUFFED,NA;
    /**
     * @return prettified representation of sleeves constants
     */
    public String toString(){
        return switch (this){
            case SHORT -> "Short";
            case LONG -> "Long";
            case PUFFED -> "Puffed";
            case BAT_WING -> "Bat-wing";
            case SLEEVELESS -> "Sleeveless";
            case NA -> "Any sleeve will do";
        };
    }
}

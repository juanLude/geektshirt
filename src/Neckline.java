/**
 * enum representing t-shirt neckline styles
 */
public enum Neckline {
    CREW, V, SCOOP, HIGH,NA;
    /**
     * @return prettified representation of neckline constants
     */
    public String toString(){
        return switch (this){
            case V -> "V - neck";
            case CREW -> "Crew neck";
            case HIGH -> "High neck";
            case SCOOP -> "Scoop neck";
            case NA -> "Any neck will do";
        };
    }

}

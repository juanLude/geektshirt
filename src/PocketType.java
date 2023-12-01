/**
 * enum representing the hoodie pocket types
 */
public enum PocketType {
    KANGAROO,PATCH,ZIPPER,SLASH,FAUX,NA;
    /**
     * @return prettified representation of pocket type constants
     */
    public String toString(){
        return switch (this){
            case KANGAROO -> "Kangaroo";
            case FAUX -> "Faux";
            case PATCH -> "Patch";
            case SLASH -> "Slash";
            case ZIPPER -> "Zipper";
            case NA -> "Any pocket will do";
        };
    }
}



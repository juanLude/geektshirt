/**
 * enum representing different hoodie styles
 */
public enum HoodieStyle {
    PULLOVER,ZIP_UP,OVER_SIZED,ATHLETIC,NA;
    /**
     * @return prettified representation of hoodie style constants
     */
    public String toString(){
        return switch (this){
            case OVER_SIZED -> "Over-sized";
            case PULLOVER -> "Pull-over";
            case ZIP_UP -> "Zip-up";
            case ATHLETIC -> "Athletic";
            case NA -> "Any style will do";
        };
    }
}


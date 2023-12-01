public enum Brand {
    TOMMY_BUGFINDER, YODAHOODS, LACODER,MASTER_TEE,CODEBREAKER,YODATS,MASTER_HOOD,NA;

    public String toString(){
        return switch (this){
            case TOMMY_BUGFINDER -> "Tommy Bugfinder";
            case YODAHOODS -> "YodaHoods";
            case LACODER -> "Lacoder";
            case MASTER_TEE -> "Master Tee";
            case CODEBREAKER -> "CodeBreaker";
            case YODATS -> "YodaTs";
            case MASTER_HOOD -> "Master Hood";
            case NA -> "Any brand will do";
        };
    }
}

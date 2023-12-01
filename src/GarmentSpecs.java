import java.util.*;
/**
 * class from which GarmentSpecs objects can be created
 */
public class GarmentSpecs {
    private final double minPrice;
    private final double maxPrice;
    private final Map<Filter,Object> filterMap;
    /**
     * constructor to create a GarmentSpecs object
     * @param filterMap a Map containing the criteria used to compare GarmentSpecs
     * @param minPrice the t-shirt's desired minimum price in dollars
     * @param maxPrice the t-shirt's desired maximum price in
     *
     */
    public GarmentSpecs(Map<Filter,Object> filterMap, double minPrice, double maxPrice) {
        this.minPrice=minPrice;
        this.maxPrice=maxPrice;
        this.filterMap=new LinkedHashMap<>(filterMap);
    }
    /**
     * This constructor is used to create the 'dream' aspects of real garments
     * @param filterMap a Map containing the criteria used to compare GarmentSpecs
     */
    public GarmentSpecs(Map<Filter,Object> filterMap) {
        this.filterMap=new LinkedHashMap<>(filterMap);
        minPrice = -1;
        maxPrice = -1;
    }
    // getters
    /**
     * @return the garment's desired minimum price
     */
    public double getMinPrice() {
        return minPrice;
    }
    /**
     * @return the garment's desired maximum price
     */
    public double getMaxPrice() {
        return maxPrice;
    }
    /**
     * access all the key-value pairs
     *
     * @return the entire map of filter keys and values
     */
    public Map<Filter, Object> getAllFilters() {
        return new HashMap<>(filterMap);
    }
    /**
     * @param key a Filter (enum) constant representing a search criteria
     * @return a value stored in the map at a given key
     */
    public Object getFilter(Filter key){return getAllFilters().get(key);}
    /**
     * method to return a description of generic Garment features
     *
     * @return a formatted String description of the garment's dream features
     */
    public String getGarmentSpecInfo(){
        StringBuilder description = new StringBuilder();
        for(Filter key: filterMap.keySet()) description.append("\n").append(key).append(": ").append(getFilter(key));

        return description.toString();
    }
    /**
     * method to get a boolean expression based on meeting all the user's requirements
     *
     * @param garmentSpecs a GarmentSpecs object representing a user's preferred garment
     * @return a boolean expression based on whether user's requirements are met
     */
    public boolean matches(GarmentSpecs garmentSpecs){
        for(Filter key : garmentSpecs.getAllFilters().keySet()) {
            if(this.getAllFilters().containsKey(key)){
                if(getFilter(key) instanceof Collection<?> && garmentSpecs.getFilter(key) instanceof Collection<?>){
                    Set<Object> intersect = new HashSet<>((Collection<?>) garmentSpecs.getFilter(key));
                    intersect.retainAll((Collection<?>) getFilter(key));
                    if(intersect.size()==0) return false;
                }
                else if(garmentSpecs.getFilter(key) instanceof Collection<?>){
                    Set<Object> items = new HashSet<>((Collection<?>) garmentSpecs.getFilter(key));
                    if(!items.contains(this.getFilter(key))) return false;
                }
                else if(!getFilter(key).equals(garmentSpecs.getFilter(key))) return false;
            }

        }
        return true;
    }


}

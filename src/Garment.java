import java.text.DecimalFormat;

public class Garment {
    private final String name;
    private final long productCode;
    private final double price;
    private final String description;
    private final GarmentSpecs garmentSpecs;

    public Garment(String name,long productCode, double price, String description, GarmentSpecs garmentSpecs) {
        this.name=name;
        this.productCode = productCode;
        this.price = price;
        this.description = description;
        this.garmentSpecs=garmentSpecs;
    }

    public String getName(){
        return name;
    }
    public long getProductCode() {
        return productCode;
    }
    public double getPrice() {
        return price;
    }
    public String getDescription(){
        return description;
    }

    public GarmentSpecs getGarmentSpecs() {
        return garmentSpecs;
    }

    public String getGarmentInformation(){
        DecimalFormat df = new DecimalFormat("0.00");
        return "Item name: "+this.getName()+"\nCaption: "+this.getDescription() +"\nProduct code: "
                +this.getProductCode()+this.getGarmentSpecs().getGarmentSpecInfo()+"\nPrice: $"
                +df.format(this.getPrice())+"\n";
    }
}

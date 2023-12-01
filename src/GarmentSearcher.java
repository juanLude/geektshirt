//import javax.swing.*;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.util.*;
//
//public class GarmentSearcher {
//
//    private static final String filePath = "./inventory.txt";
//    private static Inventory allGarments;
//    private static final String appName = "Garment Geek";
//
//    public static void main(String[] args) {
//        allGarments = loadInventory(filePath);
//        GarmentSpecs usersDesiredGarment = getFilters();
//        processSearchResults(usersDesiredGarment);
//        System.exit(0);
//    }
//
//    public static GarmentSpecs getFilters(){
//        Map<Filter,Object> filterMap = new HashMap<>();
//        GarmentType garmentType = (GarmentType) JOptionPane.showInputDialog(null,"What kind of garment are you looking for?",appName, JOptionPane.QUESTION_MESSAGE,null,GarmentType.values(),GarmentType.HOODIE);
//        if(garmentType==null)System.exit(0);
//        filterMap.put(Filter.GARMENT_TYPE,garmentType);
//
//        Material material = (Material) JOptionPane.showInputDialog(null,"Please select your preferred material:",appName, JOptionPane.QUESTION_MESSAGE,null,Material.values(),Material.COTTON);
//        if(material==null)System.exit(0);
//        if(!material.equals(Material.NA)) filterMap.put(Filter.MATERIAL,material);
//
//        Size size = (Size) JOptionPane.showInputDialog(null,"Please select your preferred size (XS - 4XL):",appName, JOptionPane.QUESTION_MESSAGE,null,Size.values(),Size.M);
//        if(size==null)System.exit(0);
//        filterMap.put(Filter.SIZE,size);
//
//        String brand = (String) JOptionPane.showInputDialog(null,"Please select your preferred brand:",appName, JOptionPane.QUESTION_MESSAGE,null,allGarments.getAllBrands().toArray(), allGarments.getAllBrands().toArray()[1]);
//        if(brand==null)System.exit(0);
//        if(!brand.equals("NA")) filterMap.put(Filter.BRAND,brand);
//
//        if(garmentType.equals(GarmentType.T_SHIRT)) {
//            Neckline neckline = (Neckline) JOptionPane.showInputDialog(null, "Please select your preferred neckline style:", appName, JOptionPane.QUESTION_MESSAGE, null, Neckline.values(), Neckline.CREW);
//            if (neckline == null) System.exit(0);
//
//            SleeveType sleeveType = (SleeveType) JOptionPane.showInputDialog(null, "Please select your preferred sleeve type:", appName, JOptionPane.QUESTION_MESSAGE, null, SleeveType.values(), SleeveType.SHORT);
//            if (sleeveType == null) System.exit(0);
//
//            if(!neckline.equals(Neckline.NA)) filterMap.put(Filter.NECKLINE,neckline);
//            if(!sleeveType.equals(SleeveType.NA)) filterMap.put(Filter.SLEEVE_TYPE,sleeveType);
//        }
//        else {
//            HoodieStyle hoodieStyle = (HoodieStyle) JOptionPane.showInputDialog(null, "Please select your preferred hoodie style:", appName, JOptionPane.QUESTION_MESSAGE, null, HoodieStyle.values(), HoodieStyle.PULLOVER);
//            if (hoodieStyle == null) System.exit(0);
//
//            PocketType pocketType = (PocketType) JOptionPane.showInputDialog(null, "Please select your preferred pocket type:", appName, JOptionPane.QUESTION_MESSAGE, null, PocketType.values(), PocketType.KANGAROO);
//            if (pocketType == null) System.exit(0);
//
//            if(!hoodieStyle.equals(HoodieStyle.NA)) filterMap.put(Filter.HOODIE_STYLE,hoodieStyle);
//            if(!pocketType.equals(PocketType.NA)) filterMap.put(Filter.POCKET_TYPE,pocketType);
//        }
//
//        int minPrice=-1,maxPrice = -1;
//        while(minPrice<0) {
//            String userInput = JOptionPane.showInputDialog(null, "Please enter the lowest price", appName, JOptionPane.QUESTION_MESSAGE);
//            if(userInput==null)System.exit(0);
//            try {
//                minPrice = Integer.parseInt(userInput);
//                if(minPrice<0) JOptionPane.showMessageDialog(null,"Price must be >= 0.",appName, JOptionPane.ERROR_MESSAGE);
//            }
//            catch (NumberFormatException e){
//                JOptionPane.showMessageDialog(null,"Invalid input. Please try again.", appName, JOptionPane.ERROR_MESSAGE);
//            }
//        }
//        while(maxPrice<minPrice) {
//            String userInput = JOptionPane.showInputDialog(null, "Please enter the highest price", appName, JOptionPane.QUESTION_MESSAGE);
//            if(userInput==null)System.exit(0);
//            try {
//                maxPrice = Integer.parseInt(userInput);
//                if(maxPrice<minPrice) JOptionPane.showMessageDialog(null,"Price must be >= "+minPrice,appName, JOptionPane.ERROR_MESSAGE);
//            }
//            catch (NumberFormatException e){
//                JOptionPane.showMessageDialog(null,"Invalid input. Please try again.", appName, JOptionPane.ERROR_MESSAGE);
//            }
//        }
//        return new GarmentSpecs(filterMap,minPrice,maxPrice);
//    }
//
//    public static void processSearchResults(GarmentSpecs dreamGarment){
//        List<Garment> matchingGarments = allGarments.findMatch(dreamGarment);
//        if(matchingGarments.size()>0) {
//            Map<String, Garment> options = new HashMap<>();
//            StringBuilder infoToShow = new StringBuilder("Matches found!! The following garments meet your criteria: \n");
//            for (Garment matchingGarment : matchingGarments) {
//                infoToShow.append(matchingGarment.getGarmentInformation());
//                options.put(matchingGarment.getName(), matchingGarment);
//            }
//            String choice = (String) JOptionPane.showInputDialog(null, infoToShow + "\n\nPlease select which t-shirt you'd like to order:", appName, JOptionPane.INFORMATION_MESSAGE, null, options.keySet().toArray(), "");
//            if(choice==null) System.exit(0);
//            Garment chosenGarment = options.get(choice);
//            submitOrder(getUserContactInfo(),chosenGarment, (Size) dreamGarment.getFilter(Filter.SIZE));
//            JOptionPane.showMessageDialog(null,"Thank you! Your order has been submitted. "+
//                    "One of our friendly staff will be in touch shortly.",appName, JOptionPane.INFORMATION_MESSAGE);
//        }
//        else{
//            JOptionPane.showMessageDialog(null,"Unfortunately none of our garments meet your criteria :("+
//                    "\n\tTo exit, click OK.",appName, JOptionPane.INFORMATION_MESSAGE);
//        }
//    }
//
//    public static Geek getUserContactInfo(){
//        String name = JOptionPane.showInputDialog(null,"Please enter your full name.",appName, JOptionPane.QUESTION_MESSAGE);
//        if(name==null) System.exit(0);
//        long phoneNumber=0;
//        while(phoneNumber==0) {
//            try {
//                String userInput = JOptionPane.showInputDialog(null, "Please enter your phone number.", appName, JOptionPane.QUESTION_MESSAGE);
//                if(userInput==null) System.exit(0);
//                phoneNumber = Long.parseLong(userInput);
//            } catch (NumberFormatException e) {
//                phoneNumber = Long.parseLong(JOptionPane.showInputDialog(null, "Invalid entry. Please enter your phone number.", appName, JOptionPane.ERROR_MESSAGE));
//            }
//        }
//        return new Geek(name,phoneNumber);
//    }
//
//    public static void submitOrder(Geek geek, Garment Garment, Size size) {
//        String filePath = geek.getName().replace(" ","_")+"_"+Garment.getProductCode()+".txt";
//        Path path = Path.of(filePath);
//        String lineToWrite = "Order details:\n\t" +
//                "Name: "+geek.getName()+
//                "\n\tPhone number: 0"+geek.getPhoneNumber()+
//                "\n\tItem: "+Garment.getName()+" ("+Garment.getProductCode()+")" +
//                "\n\tSize: "+size;
//        try {
//            Files.writeString(path, lineToWrite);
//        }catch (IOException io){
//            System.out.println("Order could not be placed. \nError message: "+io.getMessage());
//            System.exit(0);
//        }
//    }
//
//    public static Inventory loadInventory(String filePath) {
//        Inventory allGarments = new Inventory();
//        Path path = Path.of(filePath);
//        List<String> fileContents = null;
//        try {
//            fileContents = Files.readAllLines(path);
//        }catch (IOException io){
//            System.out.println("File could not be found");
//            System.exit(0);
//        }
//        for(int i=1;i<fileContents.size();i++){
//            String[] info = fileContents.get(i).split("\\[");
//            String[] singularInfo = info[0].split(",");
//            String sizesRaw = info[1].replace("]","");
//            String description = info[2].replace("]","");
//
//            GarmentType garmentType = null;
//            try {
//                garmentType = GarmentType.valueOf(singularInfo[0].replace("-","_").toUpperCase()); //error catching
//            }catch (IllegalArgumentException e){
//                System.out.println("Error in file. type data could not be parsed for garment on line "+(i+1)+". Terminating. \nError message: "+e.getMessage());
//                System.exit(0);
//            }
//            String name = singularInfo[1];
//
//            long productCode = 0;
//            try{
//                productCode = Long.parseLong(singularInfo[2]);
//            }catch (NumberFormatException n) {
//                System.out.println("Error in file. Product code could not be parsed for garment on line "+(i+1)+". Terminating. \nError message: "+n.getMessage());
//                System.exit(0);
//            }
//
//            double price = 0;
//            try{
//                price = Double.parseDouble(singularInfo[3]);
//            }catch (NumberFormatException n){
//                System.out.println("Error in file. Price could not be parsed for garment on line "+(i+1)+". Terminating. \nError message: "+n.getMessage());
//                System.exit(0);
//            }
//
//            String brand = singularInfo[4];
//
//            Material material = null;
//            try{
//                material = Material.valueOf(singularInfo[5].toUpperCase().replace(" ","_"));
//            }catch (IllegalArgumentException e){
//                System.out.println("Error in file. Material data could not be parsed for garment on line "+(i+1)+". Terminating. \nError message: "+e.getMessage());
//                System.exit(0);
//            }
//            Neckline neckline = null;
//            try{
//                neckline = Neckline.valueOf(singularInfo[6].toUpperCase());
//            }catch (IllegalArgumentException e){
//                System.out.println("Error in file. Neckline data could not be parsed for t-shirt on line "+(i+1)+". Terminating. \nError message: "+e.getMessage());
//                System.exit(0);
//            }
//            SleeveType sleeveType = null;
//            try{
//                sleeveType = SleeveType.valueOf(singularInfo[7].toUpperCase().replace(" ","_"));
//            }catch (IllegalArgumentException e){
//                System.out.println("Error in file. Sleeve type data could not be parsed for t-shirt on line "+(i+1)+". Terminating. \nError message: "+e.getMessage());
//                System.exit(0);
//            }
//            PocketType pocketType = null;
//            try{
//                pocketType = PocketType.valueOf(singularInfo[8].toUpperCase());
//            }catch (IllegalArgumentException e){
//                System.out.println("Error in file. Pocket type data could not be parsed for hoodie on line "+(i+1)+". Terminating. \nError message: "+e.getMessage());
//                System.exit(0);
//            }
//            HoodieStyle hoodieStyle = null;
//            try{
//                hoodieStyle = HoodieStyle.valueOf(singularInfo[9].toUpperCase().replace(" ","_"));
//            }catch (IllegalArgumentException e){
//                System.out.println("Error in file. Style data could not be parsed for hoodie on line "+(i+1)+". Terminating. \nError message: "+e.getMessage());
//                System.exit(0);
//            }
//
//            Set<Size> sizes = new HashSet<>();
//            for(String s: sizesRaw.split(",")){
//                Size size = Size.S;
//                try {
//                    size = Size.valueOf(s);
//                }catch (IllegalArgumentException e){
//                    System.out.println("Error in file. Size data could not be parsed for t-shirt on line "+(i+1)+". Terminating. \nError message: "+e.getMessage());
//                    System.exit(0);
//                }
//                sizes.add(size);
//            }
//
//            Map<Filter,Object> filterMap = new LinkedHashMap<>();
//            filterMap.put(Filter.GARMENT_TYPE,garmentType);
//            filterMap.put(Filter.BRAND,brand);
//            filterMap.put(Filter.MATERIAL,material);
//            filterMap.put(Filter.SIZE,sizes);
//            if(!neckline.equals(Neckline.NA)) filterMap.put(Filter.NECKLINE,neckline);
//            if(!sleeveType.equals(SleeveType.NA)) filterMap.put(Filter.SLEEVE_TYPE,sleeveType);
//            if(!hoodieStyle.equals(HoodieStyle.NA)) filterMap.put(Filter.HOODIE_STYLE,hoodieStyle);
//            if(!pocketType.equals(PocketType.NA)) filterMap.put(Filter.POCKET_TYPE,pocketType);
//
//            GarmentSpecs dreamGarment = new GarmentSpecs(filterMap);
//
//            Garment Garment = new Garment(name,productCode,price,description,dreamGarment);
//            allGarments.addGarment(Garment);
//        }
//        return allGarments;
//    }
//}

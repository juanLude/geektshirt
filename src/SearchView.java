import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.List;

public class SearchView {
    private static CriteriaEntry criteriaEntry; //object used for inputting user info/filters
    private static final String iconFilePath = "./icon.png";
    private static ImageIcon icon = new ImageIcon(iconFilePath);
    private static GarmentType garmentType;
    private static final String appName = "Garment Finder";
    private static JFrame mainWindow = null; //main container
    private static JPanel searchView = null;
    private static final String allGarmentsFilePath = "./inventory.txt";
    private static Inventory allGarments = null;

    private static JPanel userInformationView = null;

    /**
     * main method used to allow the user to search in the garment database, and place a purchase order request.
     * Also,it initialises and creates main JFrame.
     *
     * @param args none required
     */
    public static void main(String[] args) {
        allGarments = loadInventory(allGarmentsFilePath);
        mainWindow = new JFrame(appName);
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        icon = new ImageIcon(iconFilePath);
        mainWindow.setIconImage(icon.getImage());
        mainWindow.setMinimumSize(new Dimension(500, 450));
        searchView = enterCriteria();
        mainWindow.setContentPane(searchView);
        mainWindow.pack();
        mainWindow.setVisible(true);
    }

    /**
     * method used to extract user-entered data to create a dreamGarment object which will be used to search the database of real garments for a match
     *
     * @param criteriaEntry an instance of the CriteriaEntry class (used to generate JPanels for user to enter/select filters)
     */
    public static void conductSearch(CriteriaEntry criteriaEntry) {
        //this map will store the user's selections
        Map<Filter, Object> criteria = new HashMap<>();
        garmentType = criteriaEntry.getSelectedGarmentType();

        //if the user hasn't selected a type, remind them
        if (garmentType.equals(GarmentType.SELECT_TYPE)) {
            JOptionPane.showMessageDialog(mainWindow, "You MUST select a garment type.\n", "Invalid search", JOptionPane.INFORMATION_MESSAGE, null);
            return;
        }
        criteria.put(Filter.GARMENT_TYPE, garmentType);

        Set<Brand> chosenBrands = criteriaEntry.getSelectedBrands();
        if (chosenBrands != null) criteria.put(Filter.BRAND, chosenBrands);

        Material material = criteriaEntry.getSelectedMaterial();
        if (!material.equals(Material.NA)) criteria.put(Filter.MATERIAL, material);

        Set<Size> chosenSizes = criteriaEntry.getSelectedSizes();
        if (chosenSizes == null) {
            JOptionPane.showMessageDialog(mainWindow, "You MUST select a size.\n", "Invalid search", JOptionPane.INFORMATION_MESSAGE, null);
            return;
        }
        if (!chosenSizes.isEmpty()) criteria.put(Filter.SIZE, criteriaEntry.getSelectedSizes());

        HoodieStyle hoodieStyle = criteriaEntry.getHoodieStyle();
        if (!hoodieStyle.equals(HoodieStyle.NA)) criteria.put(Filter.HOODIE_STYLE, hoodieStyle);

        PocketType pocketType = criteriaEntry.getPocketType();
        if (!pocketType.equals(PocketType.NA)) criteria.put(Filter.POCKET_TYPE, pocketType);

        SleeveType sleeveType = criteriaEntry.getSleeveType();
        if (!sleeveType.equals(SleeveType.NA)) criteria.put(Filter.SLEEVE_TYPE, sleeveType);

        Neckline neckline = criteriaEntry.getNeckline();
        if (!neckline.equals(Neckline.NA)) criteria.put(Filter.NECKLINE, neckline);

        double minPrice = criteriaEntry.getMinPrice();
        double maxPrice = criteriaEntry.getMaxPrice();

        //add the user's choices to the map
        criteria.put(Filter.SIZE, chosenSizes);
        //create a dreamGarment object, initialising it with the map and the min-max price values
        GarmentSpecs dreamGarment = new GarmentSpecs(criteria, minPrice, maxPrice);

        //use the AllPets findMatch method to find all compatible garments
        List<Garment> potentialMatches = allGarments.findMatch(dreamGarment);

        //call the show results method, passing in compatible garments
        showResults(potentialMatches);
    }

    /**
     * a method that brings together in a JPanel (view 2) the results, and user-selection options
     *
     * @param compatibleGarments an arraylist of garment objects that match the user's selection criteria
     */
    public static void showResults(List<Garment> compatibleGarments) {
        //if there are compatible garments
        if (compatibleGarments.size() > 0) {
            Map<String, Garment> options = new LinkedHashMap<>();
            options.put("Select garment", null);
            //create a panel to contain the descriptions of the compatible garments
            JPanel garmentDescriptions = new JPanel();
            garmentDescriptions.setBorder(BorderFactory.createTitledBorder("Matches found!! The following garments meet your criteria: "));
            garmentDescriptions.setLayout(new BoxLayout(garmentDescriptions, BoxLayout.Y_AXIS));
            garmentDescriptions.add(Box.createRigidArea(new Dimension(0, 10)));

            //provide text area for each garment
            for (Garment compatibleGarment : compatibleGarments) {
                //add the garment description to the panel
                garmentDescriptions.add(describeIndividualGarment(compatibleGarment));
                options.put(compatibleGarment.getName() + " (" + compatibleGarment.getProductCode() + ")", compatibleGarment);
            }
            JScrollPane verticalScrollBar = new JScrollPane(garmentDescriptions);
            verticalScrollBar.setPreferredSize(new Dimension(300, 450));
            verticalScrollBar.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

            SwingUtilities.invokeLater(() -> verticalScrollBar.getViewport().setViewPosition(new Point(0, 0)));
            //next, initialise the combo box with the garments
            JComboBox<String> optionsCombo = new JComboBox<>(options.keySet().toArray(new String[0]));
            //let the user select a garment
            ActionListener actionListener = e -> {
                mainWindow.getContentPane().removeAll();
                checkUserGarmentSelection(options, optionsCombo);

            };
            optionsCombo.addActionListener(actionListener);

            //give the user the option to return to view 1 (to search again)
            JButton goBackToSearch = new JButton("Back to search");
            goBackToSearch.addActionListener(e -> {
                mainWindow.setContentPane(searchView);
                mainWindow.revalidate();
            });

            //this panel is for the dropdown list - it contains a border, with instructional title, the dropdown list and rigid areas for padding
            JPanel selectionPanel = new JPanel();
            selectionPanel.setLayout(new BoxLayout(selectionPanel, BoxLayout.LINE_AXIS));
            selectionPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            selectionPanel.add(Box.createRigidArea(new Dimension(10, 0)));
            selectionPanel.add(optionsCombo);
            selectionPanel.add(Box.createRigidArea(new Dimension(10, 0)));
            selectionPanel.add(goBackToSearch);
            selectionPanel.add(Box.createRigidArea(new Dimension(10, 0)));
            selectionPanel.add(Box.createRigidArea(new Dimension(10, 0)));
            selectionPanel.add(Box.createRigidArea(new Dimension(0, 20)));

            //this is the overall panel (view 2) used to display the matching garments and the dropdown list
            JPanel results = new JPanel();
            results.setLayout(new BorderLayout());
            //add padding to the top of the panel
            results.add(Box.createRigidArea(new Dimension(0, 10)), BorderLayout.NORTH);
            results.add(verticalScrollBar, BorderLayout.CENTER);
            results.add(selectionPanel, BorderLayout.SOUTH);
            //set main window to the results panel (view 2)
            mainWindow.setContentPane(results);
            mainWindow.revalidate();
        } else {
            //popup window letting user know there are no matches
            JOptionPane.showMessageDialog(searchView, "Unfortunately none of our garments meet your criteria.\n", "No Matching Garments", JOptionPane.INFORMATION_MESSAGE, null);
        }
    }

    /**
     * this method checks whether the user has selected or not a garment
     *
     * @param options a Map that links garment names to garment objects
     */
    public static void checkUserGarmentSelection(Map<String, Garment> options, JComboBox<String> optionsCombo) {
        String garmentName = (String) optionsCombo.getSelectedItem();
        if (options.get(garmentName) != null) {
            Garment chosenGarment = options.get(garmentName);
            //this will switch the contents of the main frame to the user contact field with chosen garment description
            placeOrder(chosenGarment);
        }
    }

    /**
     * method to describe a single garment
     *
     * @param garment the garment to describe
     * @return a JTextArea
     */
    public static JTextArea describeIndividualGarment(Garment garment) {
        //create a text area to contain the garment description
        JTextArea garmentDescription = new JTextArea(garment.getGarmentInformation());
        garmentDescription.setEditable(false);
        garmentDescription.setLineWrap(true);
        garmentDescription.setWrapStyleWord(true);
        return garmentDescription;
    }

    /**
     * this method allows the user to place an order by entering their details into a contact form
     * It writes the garment and person's details to a file once the user clicks "submit"
     */

    public static void placeOrder(Garment chosenGarment) {
        //instruct the user to fill out the form
        JLabel garmentMessage = new JLabel("To place an order for " + chosenGarment.getName() + " fill in the form below");
        garmentMessage.setAlignmentX(0);
        JScrollPane jScrollPane = new JScrollPane(describeIndividualGarment(chosenGarment));
        jScrollPane.getViewport().setPreferredSize(new Dimension(300, 150));
        jScrollPane.setAlignmentX(0);

        JPanel garmentDescriptionPanel = new JPanel();
        garmentDescriptionPanel.setAlignmentX(0);
        garmentDescriptionPanel.add(garmentMessage);
        garmentDescriptionPanel.add(jScrollPane);

        //use the contactForm method to get a panel containing components that allow the user to input info
        JPanel userInputPanel = criteriaEntry.contactForm();
        userInputPanel.setAlignmentX(0);


        //create a button, which when clicked, writes the user's request to a file
        JButton submit = new JButton("Submit");

        ActionListener actionListener = e -> {
            String lineToWrite = "Name: " + criteriaEntry.getName() + " \nEmail: " + criteriaEntry.getEmail() + "\nPhone number: "
                    + criteriaEntry.getPhoneNumber() + "\n\nMessage: " + criteriaEntry.getMessage() +
                    "\n\n" + criteriaEntry.getName() + " wishes to adopt " + chosenGarment.getName() + " (" + chosenGarment.getProductCode() + ")";
            writeMessageToFile(lineToWrite);
        };
        submit.addActionListener(actionListener);

        //add the description panel, contact form panel and button to a new frame, and assign it to view 3
        JPanel mainFramePanel = new JPanel();
        mainFramePanel.setLayout(new BorderLayout());
        mainFramePanel.add(garmentDescriptionPanel, BorderLayout.NORTH);
        mainFramePanel.add(userInputPanel, BorderLayout.CENTER);
        mainFramePanel.add(Box.createRigidArea(new Dimension(20, 0)), BorderLayout.WEST);
        mainFramePanel.add(Box.createRigidArea(new Dimension(20, 0)), BorderLayout.EAST);
        mainFramePanel.add(submit, BorderLayout.SOUTH);

        userInformationView = mainFramePanel;
        mainWindow.setContentPane(userInformationView);
        mainWindow.revalidate();
    }

    /**
     * @param lineToWrite the String to be written to the file
     *                    a method to write a user's message or order request to a file
     */
    public static void writeMessageToFile(String lineToWrite) {
        String filePath = criteriaEntry.getName().replace(" ", "_") + "_query.txt";
        Path path = Path.of(filePath);
        try {
            Files.writeString(path, lineToWrite);
            JOptionPane.showMessageDialog(mainWindow, "Thank you for your message. \nOne of our friendly staff will be in touch shortly. \nClose this dialog to terminate."
                    , "Message Sent", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        } catch (IOException io) {
            JOptionPane.showMessageDialog(mainWindow, "Error: Message could not be sent! Please try again!"
                    , null, JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * this method creates a panel to be used as the only container in the main frame while users enter search criteria
     * It instantiates the CriteriaEntry class to get user input panel and input values
     *
     * @return a JPanel representing the search view (data entry and submit button)
     */
    public static JPanel enterCriteria() {
        //create a local JPanel to contain all the elements of the search form
        JPanel searchWindow = new JPanel();
        searchWindow.setLayout(new BorderLayout());

        criteriaEntry = new CriteriaEntry(garmentType, allGarments.highestPrice());
        //add the panel of criteria panels to the main search window panel
        searchWindow.add(criteriaEntry.generateSearchView(), BorderLayout.CENTER);

        //add a button at the bottom of the search panel, when users click it, the conduct search method will be called
        JButton submitInfo = new JButton("Submit");
        ActionListener actionListener = e -> conductSearch(criteriaEntry);
        submitInfo.addActionListener(actionListener);
        //add the button to the bottom-most part of the search panel
        searchWindow.add(submitInfo, BorderLayout.SOUTH);
        return searchWindow;
    }

    /**
     * method to load all garment data from file
     *
     * @param filePath a file path to the txt file containing all the garment data
     * @return an allGarments object
     */

    public static Inventory loadInventory(String filePath) {
        Inventory allGarments = new Inventory();
        Path path = Path.of(filePath);
        List<String> fileContents = null;
        try {
            fileContents = Files.readAllLines(path);
        } catch (IOException io) {
            System.out.println("File could not be found");
            System.exit(0);
        }
        for (int i = 1; i < fileContents.size(); i++) {
            String[] info = fileContents.get(i).split("\\[");
            String[] singularInfo = info[0].split(",");
            String sizesRaw = info[1].replace("]", "");
            String description = info[2].replace("]", "");

            GarmentType garmentType = null;
            try {
                garmentType = GarmentType.valueOf(singularInfo[0].replace("-", "_").toUpperCase()); //error catching
            } catch (IllegalArgumentException e) {
                System.out.println("Error in file. type data could not be parsed for garment on line " + (i + 1) + ". Terminating. \nError message: " + e.getMessage());
                System.exit(0);
            }
            String name = singularInfo[1];

            long productCode = 0;
            try {
                productCode = Long.parseLong(singularInfo[2]);
            } catch (NumberFormatException n) {
                System.out.println("Error in file. Product code could not be parsed for garment on line " + (i + 1) + ". Terminating. \nError message: " + n.getMessage());
                System.exit(0);
            }

            double price = 0;
            try {
                price = Double.parseDouble(singularInfo[3]);
            } catch (NumberFormatException n) {
                System.out.println("Error in file. Price could not be parsed for garment on line " + (i + 1) + ". Terminating. \nError message: " + n.getMessage());
                System.exit(0);
            }

            Set<Brand> brands = new HashSet<>();
            for (String b : singularInfo[4].toUpperCase().replace(" ", "_").split(",")) {
                Brand brand = null;
                try {
                    brand = Brand.valueOf(b);
                } catch (IllegalArgumentException e) {
                    System.out.println("Error in file. Brand data could not be parsed for garment on line " + (i + 1) + ". Terminating. \nError message: " + e.getMessage());
                    System.exit(0);
                }
                brands.add(brand);
            }

            Material material = null;
            try {
                material = Material.valueOf(singularInfo[5].toUpperCase().replace(" ", "_"));
            } catch (IllegalArgumentException e) {
                System.out.println("Error in file. Material data could not be parsed for garment on line " + (i + 1) + ". Terminating. \nError message: " + e.getMessage());
                System.exit(0);
            }
            Neckline neckline = null;
            try {
                neckline = Neckline.valueOf(singularInfo[6].toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Error in file. Neckline data could not be parsed for t-shirt on line " + (i + 1) + ". Terminating. \nError message: " + e.getMessage());
                System.exit(0);
            }
            SleeveType sleeveType = null;
            try {
                sleeveType = SleeveType.valueOf(singularInfo[7].toUpperCase().replace(" ", "_"));
            } catch (IllegalArgumentException e) {
                System.out.println("Error in file. Sleeve type data could not be parsed for t-shirt on line " + (i + 1) + ". Terminating. \nError message: " + e.getMessage());
                System.exit(0);
            }
            PocketType pocketType = null;
            try {
                pocketType = PocketType.valueOf(singularInfo[8].toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Error in file. Pocket type data could not be parsed for hoodie on line " + (i + 1) + ". Terminating. \nError message: " + e.getMessage());
                System.exit(0);
            }
            HoodieStyle hoodieStyle = null;
            try {
                hoodieStyle = HoodieStyle.valueOf(singularInfo[9].toUpperCase().replace(" ", "_"));
            } catch (IllegalArgumentException e) {
                System.out.println("Error in file. Style data could not be parsed for hoodie on line " + (i + 1) + ". Terminating. \nError message: " + e.getMessage());
                System.exit(0);
            }

            Set<Size> sizes = new HashSet<>();
            for (String s : sizesRaw.split(",")) {
                Size size = Size.S;
                try {
                    size = Size.valueOf(s);
                } catch (IllegalArgumentException e) {
                    System.out.println("Error in file. Size data could not be parsed for garment on line " + (i + 1) + ". Terminating. \nError message: " + e.getMessage());
                    System.exit(0);
                }
                sizes.add(size);
            }

            Map<Filter, Object> filterMap = new LinkedHashMap<>();
            filterMap.put(Filter.GARMENT_TYPE, garmentType);
            filterMap.put(Filter.BRAND, brands);
            filterMap.put(Filter.MATERIAL, material);
            filterMap.put(Filter.SIZE, sizes);
            if (!neckline.equals(Neckline.NA)) filterMap.put(Filter.NECKLINE, neckline);
            if (!sleeveType.equals(SleeveType.NA)) filterMap.put(Filter.SLEEVE_TYPE, sleeveType);
            if (!hoodieStyle.equals(HoodieStyle.NA)) filterMap.put(Filter.HOODIE_STYLE, hoodieStyle);
            if (!pocketType.equals(PocketType.NA)) filterMap.put(Filter.POCKET_TYPE, pocketType);

            GarmentSpecs dreamGarment = new GarmentSpecs(filterMap);

            Garment Garment = new Garment(name, productCode, price, description, dreamGarment);
            allGarments.addGarment(Garment);
        }
        return allGarments;
    }
}

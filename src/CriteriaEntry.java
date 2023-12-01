import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.util.*;

public class CriteriaEntry {
    //fields
    private HoodieStyle hoodieStyle;
    private PocketType pocketType;
    private SleeveType sleeveType;
    private Neckline neckline;
    private JPanel typeOfDreamGarmentSpecificCriteriaPanel;
    private final CardLayout cardLayout = new CardLayout();
    private final String IMAGE_PANEL = "garment images";
    private final String HOODIE_PANEL = "Hoodie";
    private final String TSHIRT_PANEL = "T-shirt";
    private double minPrice = 0;
    private double maxPrice = 0;
    // private final JLabel feedback = new JLabel("");
    private final JLabel feedbackMin = new JLabel(" ");
    private final JLabel feedbackMax = new JLabel(" ");
    private Set<Brand> selectedBrands;
    private Set<Size> selectedSizes;
    private Material selectedMaterial;
    private GarmentType selectedGarmentType;
    private JTextField name;
    private JTextField email;
    private JTextField phoneNumber;
    private JTextArea message;
    private final double highestPrice;

    /**
     * @param selectedGarmentType the type of garment selected by the user
     * @param highestPrice        the highest price of all the garments in the database
     */
    public CriteriaEntry(GarmentType selectedGarmentType, double highestPrice) {
        this.selectedGarmentType = selectedGarmentType;
        this.highestPrice = highestPrice;
    }

    //brings all the JPanels below together

    /**
     * generates the search few for garment type, brand, size, material and price range with three different
     * card layouts: image panel, hoodie panel and t-shirt panel
     *
     * @return the described JPanel
     */
    public JPanel generateSearchView() {
        JPanel criteria = new JPanel();
        //set the layout to BoxLayout - Y axis, so that all the components are vertically stacked
        criteria.setLayout(new BoxLayout(criteria, BoxLayout.Y_AXIS));
        criteria.add(getUserInputGarmentType());
        criteria.add(getUserInputBrand());
        criteria.add(getUserInputSizes());
        criteria.add(getUserInputMaterial());
        criteria.add(getUserInputPriceRange());
        typeOfDreamGarmentSpecificCriteriaPanel = new JPanel();
        typeOfDreamGarmentSpecificCriteriaPanel.setAlignmentX(0);
        typeOfDreamGarmentSpecificCriteriaPanel.setLayout(cardLayout);
        typeOfDreamGarmentSpecificCriteriaPanel.add(this.generateImagePanel(), IMAGE_PANEL);
        typeOfDreamGarmentSpecificCriteriaPanel.add(this.userInputHoodie(), HOODIE_PANEL);
        typeOfDreamGarmentSpecificCriteriaPanel.add(this.userInputTShirt(), TSHIRT_PANEL);
        criteria.add(typeOfDreamGarmentSpecificCriteriaPanel);
        return criteria;
    }

    /**
     * generates a JPanel containing a dropdown lists for garment type
     *
     * @return a JPanel that allows users to select a garment type
     */
    public JPanel getUserInputGarmentType() {

        JComboBox<GarmentType> typeOfGarmentJComboBox = new JComboBox<>(GarmentType.values());
        typeOfGarmentJComboBox.requestFocusInWindow();
        typeOfGarmentJComboBox.setAlignmentX(0);
        typeOfGarmentJComboBox.setSelectedItem(GarmentType.SELECT_TYPE);
        selectedGarmentType = (GarmentType) typeOfGarmentJComboBox.getSelectedItem();
        typeOfGarmentJComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                ifTypeSelected(typeOfGarmentJComboBox);
                selectedGarmentType = (GarmentType) typeOfGarmentJComboBox.getSelectedItem();
            }
        });
        //request that the user select their preferred garment
        JPanel garmentTypeSelectionPanel = new JPanel();
        garmentTypeSelectionPanel.setLayout(new BoxLayout(garmentTypeSelectionPanel, BoxLayout.Y_AXIS));
        garmentTypeSelectionPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        garmentTypeSelectionPanel.setAlignmentX(0);
        garmentTypeSelectionPanel.add(typeOfGarmentJComboBox);
        garmentTypeSelectionPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        return garmentTypeSelectionPanel;
    }

    /**
     * generates a JPanel containing a JList listing all the available brands
     *
     * @return a JPanel that allows users to select one or more brands
     */
    public JPanel getUserInputBrand() {

        JList<Brand> selectBrands = new JList<>(Brand.values());
        //enable multi-selection
        selectBrands.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        //create a scroll pane
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(selectBrands);
        selectBrands.setLayoutOrientation(JList.VERTICAL);
        scrollPane.setPreferredSize(new Dimension(250, 60));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        //set the position of the scroll bar to the top of the scrollable area
        SwingUtilities.invokeLater(() -> scrollPane.getViewport().setViewPosition(new Point(0, 0)));

        ListSelectionListener listSelectionListener = e -> {
            selectedBrands = new HashSet<>(selectBrands.getSelectedValuesList());
        };
        selectBrands.addListSelectionListener(listSelectionListener);
        //add the dropdown list, and an instructional JLabel to a panel and return it
        JPanel brandsPanel = new JPanel();
        brandsPanel.setLayout(new BoxLayout(brandsPanel, BoxLayout.Y_AXIS));
        brandsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        JLabel instruction = new JLabel("Please select your preferred brands");
        instruction.setAlignmentX(0);
        brandsPanel.add(instruction);
        JLabel clarification = new JLabel("(To multi-select, hold Ctrl)");
        clarification.setAlignmentX(0);
        clarification.setFont(new Font("", Font.ITALIC, 12));
        brandsPanel.add(clarification);
        scrollPane.setAlignmentX(0);
        brandsPanel.add(scrollPane);
        brandsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        return brandsPanel;
    }

    /**
     * generates a JPanel containing a JList listing all the available sizes
     *
     * @return a JPanel that allows users to select one or more sizes
     */
    public JPanel getUserInputSizes() {
        //Create a JList of all the sizes
        JList<Size> selectSizes = new JList<>(Size.values());

        selectSizes.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(selectSizes);
        selectSizes.setLayoutOrientation(JList.VERTICAL);
        scrollPane.setPreferredSize(new Dimension(250, 60));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        SwingUtilities.invokeLater(() -> scrollPane.getViewport().setViewPosition(new Point(0, 0)));

        //update the size field if the user selects a new item
        ListSelectionListener listSelectionListener = e -> {
            selectedSizes = new HashSet<>(selectSizes.getSelectedValuesList());
        };
        selectSizes.addListSelectionListener(listSelectionListener);
        //add the dropdown list, and an instructional JLabel to a panel and return it
        JPanel sizesPanel = new JPanel();
        sizesPanel.setLayout(new BoxLayout(sizesPanel, BoxLayout.Y_AXIS));
        sizesPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        JLabel instruction = new JLabel("Please select your preferred sizes");
        instruction.setAlignmentX(0);
        sizesPanel.add(instruction);
        JLabel clarification = new JLabel("(To multi-select, hold Ctrl)");
        clarification.setAlignmentX(0);
        clarification.setFont(new Font("", Font.ITALIC, 12));
        sizesPanel.add(clarification);
        scrollPane.setAlignmentX(0);
        sizesPanel.add(scrollPane);
        sizesPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        return sizesPanel;
    }

    /**
     * generates a JPanel containing a radio button listing all the available materials
     *
     * @return a JPanel that allows users to select one or no material
     */
    public JPanel getUserInputMaterial() {
        ButtonGroup materialButtonGroup = new ButtonGroup();
        JRadioButton cotton = new JRadioButton(Material.COTTON.toString());
        JRadioButton polyester = new JRadioButton(Material.POLYESTER.toString());
        JRadioButton wool = new JRadioButton(Material.WOOL_BLEND.toString());

        selectedMaterial = Material.NA; //material is initialised to NA by default
        materialButtonGroup.add(cotton);
        materialButtonGroup.add(polyester);
        materialButtonGroup.add(wool);

        cotton.setActionCommand(Material.COTTON.name());
        wool.setActionCommand(Material.WOOL_BLEND.name());
        polyester.setActionCommand(Material.POLYESTER.name());


        //add an action listener to each button
        ActionListener actionListener = e -> {
            selectedMaterial = Material.valueOf(materialButtonGroup.getSelection().getActionCommand().toUpperCase());
        };
        cotton.addActionListener(actionListener);
        wool.addActionListener(actionListener);
        polyester.addActionListener(actionListener);

        //create and return a new JPanel
        JPanel materialPanel = new JPanel();
        materialPanel.setAlignmentX(0);
        materialPanel.setBorder(BorderFactory.createTitledBorder("Which material do you prefer?"));
        materialPanel.add(cotton);
        materialPanel.add(wool);
        materialPanel.add(polyester);

        return materialPanel;
    }

    /**
     * generates a JPanel containing a radio button listing all the available materials
     *
     * @return a JPanel that allows users to select one or no material
     */
    public JPanel getUserInputPriceRange() {
        //labels for the text boxes
        JLabel minLabel = new JLabel("Min. price");
        JLabel maxLabel = new JLabel("Max. price");
        //create text boxes...
        JTextField min = new JTextField(4);
        JTextField max = new JTextField(4);
        //set default values for the age range text boxes (editable)
        min.setText(String.valueOf(0));
        max.setText(String.valueOf(highestPrice));
        maxPrice = highestPrice;

        //set the font and color of the feedback messages
        feedbackMin.setFont(new Font("", Font.ITALIC, 12));
        feedbackMin.setForeground(Color.RED);
        feedbackMax.setFont(new Font("", Font.ITALIC, 12));
        feedbackMax.setForeground(Color.RED);

        //add the document listeners
        min.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                //if the check min method returns false, request user addresses invalid input
                if (!checkMin(min)) min.requestFocus();
                checkMax(max); //after min has been updated, check max is still valid
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                //removing and inserting should be subjected to the same checks
                if (!checkMin(min)) min.requestFocus();
                checkMax(max);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            } //NA
        });
        max.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (!checkMax(max)) max.requestFocusInWindow();
                checkMin(min);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (!checkMax(max)) max.requestFocusInWindow();
                checkMin(min);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });

        //add the text fields and labels to a panel
        JPanel ageRangePanel = new JPanel();
        ageRangePanel.add(minLabel);
        ageRangePanel.add(min);
        ageRangePanel.add(maxLabel);
        ageRangePanel.add(max);

        JPanel finalPanel = new JPanel();
        finalPanel.setBorder(BorderFactory.createTitledBorder("Select desired price range"));
        finalPanel.setLayout(new BoxLayout(finalPanel, BoxLayout.Y_AXIS));
        finalPanel.setAlignmentX(0);
        finalPanel.add(ageRangePanel);
        feedbackMin.setAlignmentX(0);
        feedbackMax.setAlignmentX(0);
        finalPanel.add(feedbackMin);
        finalPanel.add(feedbackMax);

        return finalPanel;
    }

    /**
     * validates user input for min price
     *
     * @param minEntry the JTextField used to enter min price
     * @return true if valid price, false if invalid
     */
    private boolean checkMin(JTextField minEntry) {
        feedbackMin.setText("");
        try {
            double tempMin = Double.parseDouble(minEntry.getText());
            if (tempMin < 0 || tempMin > maxPrice) {
                feedbackMin.setText("Min price must be >= " + 0 + " and <= " + maxPrice + ". Defaulting to " + minPrice + " - " + maxPrice + ".");
                minEntry.selectAll();
                return false;
            } else {
                minPrice = tempMin;
                feedbackMin.setText("");
                return true;
            }
        } catch (NumberFormatException n) {
            feedbackMin.setText("Please enter a valid number for min price. Defaulting to " + minPrice + " - " + maxPrice + ".");
            minEntry.selectAll();
            return false;
        }
    }

    /**
     * validates user input for max price
     *
     * @param maxEntry the JTextField used to enter max price
     * @return true if valid price, false if invalid
     */
    private boolean checkMax(JTextField maxEntry) {
        feedbackMax.setText("");
        try {
            double tempMax = Double.parseDouble(maxEntry.getText());
            if (tempMax < minPrice) {
                feedbackMax.setText("Max price must be >= min price. Defaulting to " + minPrice + " - " + maxPrice + ".");
                maxEntry.selectAll();
                return false;
            } else {
                maxPrice = tempMax;
                feedbackMax.setText("");
                return true;
            }
        } catch (NumberFormatException n) {
            feedbackMax.setText("Please enter a valid number for max price. Defaulting to " + minPrice + " - " + maxPrice + ".");
            maxEntry.selectAll();
            return false;
        }
    }

    /**
     * generates a JPanel containing three images
     *
     * @return a JPanel with images
     */
    public JPanel generateImagePanel() {
        //load the 3 images as JLabels
        JLabel breaking = new JLabel(new ImageIcon("./breakingBad_.jpg"));
        JLabel keepCalm = new JLabel(new ImageIcon("./keepCalm_.jpg"));
        JLabel west = new JLabel(new ImageIcon("./westworld_.jpg"));
        //create a new container panel, add the 3 images to it and return the panel
        JPanel imagePanel = new JPanel();
        imagePanel.setAlignmentX(0);
        imagePanel.add(breaking);
        imagePanel.add(keepCalm);
        imagePanel.add(west);
        return imagePanel;
    }

    /**
     * a method to generate a JPanel containing a name, email, phone num and message fields
     *
     * @return a JPanel
     */
    public JPanel contactForm() {
        //create labels and text fields for users to enter contact info and message
        JLabel enterName = new JLabel("Full name");
        name = new JTextField(12);

        JLabel enterEmail = new JLabel("Email address");
        email = new JTextField(12);
        JLabel enterPhoneNumber = new JLabel("Phone number");
        phoneNumber = new JTextField(12);
        JLabel enterMessage = new JLabel("Type your query below");
        message = new JTextArea(6, 12);

        JScrollPane jScrollPane = new JScrollPane(message);
        jScrollPane.getViewport().setPreferredSize(new Dimension(250, 100));

        //create a new panel, add padding and user entry boxes/messages to the panel
        JPanel userInputPanel = new JPanel();
        userInputPanel.setLayout(new BoxLayout(userInputPanel, BoxLayout.Y_AXIS));
        userInputPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        userInputPanel.setAlignmentX(0);
        enterName.setAlignmentX(0);
        name.setAlignmentX(0);
        userInputPanel.add(enterName);
        userInputPanel.add(name);
        userInputPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        enterEmail.setAlignmentX(0);
        email.setAlignmentX(0);
        userInputPanel.add(enterEmail);
        userInputPanel.add(email);
        userInputPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        enterPhoneNumber.setAlignmentX(0);
        phoneNumber.setAlignmentX(0);
        userInputPanel.add(enterPhoneNumber);
        userInputPanel.add(phoneNumber);
        userInputPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        enterMessage.setAlignmentX(0);
        message.setAlignmentX(0);
        userInputPanel.add(enterMessage);
        jScrollPane.setAlignmentX(0);

        userInputPanel.add(jScrollPane);
        userInputPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        return userInputPanel;
    }

    /**
     * method called if user selects type of garment - used to display the appropriate search criteria panel
     *
     * @param typeOfGarmentJComboBox the dropdown list from which user has selected a type of garment
     */
    public void ifTypeSelected(JComboBox<GarmentType> typeOfGarmentJComboBox) {

        selectedGarmentType = (GarmentType) typeOfGarmentJComboBox.getSelectedItem();
        assert selectedGarmentType != null;
        //show the appropriate JPanel
        if (selectedGarmentType.equals(GarmentType.SELECT_TYPE))
            cardLayout.show(typeOfDreamGarmentSpecificCriteriaPanel, IMAGE_PANEL);
        else if (selectedGarmentType.equals(GarmentType.HOODIE))
            cardLayout.show(typeOfDreamGarmentSpecificCriteriaPanel, HOODIE_PANEL);
        else if (selectedGarmentType.equals(GarmentType.T_SHIRT))
            cardLayout.show(typeOfDreamGarmentSpecificCriteriaPanel, TSHIRT_PANEL);

    }

    /**
     * @return a JPanel containing all search parameters for hoodie type
     */
    public JPanel userInputHoodie() {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
        jPanel.setAlignmentX(0);
        jPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        JLabel instruction = new JLabel("Please select your preferred hoodie style (optional)");
        instruction.setAlignmentX(0);
        jPanel.add(instruction);
        JPanel hoodieStyle = userInputHoodieStyle();
        hoodieStyle.setAlignmentX(0);
        jPanel.add(hoodieStyle);
        jPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        JLabel pocketInstruction = new JLabel("Please select your preferred pocket type (optional)");
        pocketInstruction.setAlignmentX(0);
        jPanel.add(pocketInstruction);
        JPanel pocketType = userInputHoodiePocketType();
        pocketType.setAlignmentX(0);
        jPanel.add(pocketType);
        jPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        return jPanel;
    }

    /**
     * @return a JPanel containing all search parameters for t-shirt type
     */
    public JPanel userInputTShirt() {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
        jPanel.setAlignmentX(0);
        jPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        JLabel instruction = new JLabel("Please select your preferred neckline style (optional)");
        instruction.setAlignmentX(0);
        jPanel.add(instruction);
        JPanel neckline = userInputNeckline();
        neckline.setAlignmentX(0);
        jPanel.add(neckline);
        jPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        JLabel sleeveInstruction = new JLabel("Please select your preferred sleeve style (optional)");
        sleeveInstruction.setAlignmentX(0);
        jPanel.add(sleeveInstruction);
        JPanel sleeveType = userInputSleeveType();
        sleeveType.setAlignmentX(0);
        jPanel.add(sleeveType);
        jPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        return jPanel;
    }

    /**
     * allows user selection of one hoodie style
     *
     * @return a JPanel containing a dropdown list
     */
    public JPanel userInputHoodieStyle() {
        //a dropdown list of all religion options
        JComboBox<HoodieStyle> jComboBox = new JComboBox<>(HoodieStyle.values());
        jComboBox.setAlignmentX(0);
        jComboBox.setMaximumSize(new Dimension(2500, 40));

        jComboBox.setSelectedItem(HoodieStyle.NA);
        //initialise the field
        hoodieStyle = (HoodieStyle) jComboBox.getSelectedItem();

        jComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                hoodieStyle = (HoodieStyle) jComboBox.getSelectedItem();
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(0);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(jComboBox);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        return panel;
    }

    /**
     * allows user selection of one hoodie pocket type
     *
     * @return a JPanel containing a dropdown list
     */
    public JPanel userInputHoodiePocketType() {
        //a dropdown list of all the pocket types
        JComboBox<PocketType> jComboBox = new JComboBox<>(PocketType.values());
        jComboBox.setAlignmentX(0);
        jComboBox.setMaximumSize(new Dimension(2500, 40));
        //setting pocket type to NA
        jComboBox.setSelectedItem(PocketType.NA);
        //initialise the field
        pocketType = (PocketType) jComboBox.getSelectedItem();

        jComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                pocketType = (PocketType) jComboBox.getSelectedItem();
            }
        });
        //create and return JPanel
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(0);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(jComboBox);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        return panel;
    }

    /**
     * allows user selection of one t-shirt sleeve type
     *
     * @return a JPanel containing a dropdown list
     */
    public JPanel userInputSleeveType() {
        //a dropdown list of all the sleeve options
        JComboBox<SleeveType> jComboBox = new JComboBox<>(SleeveType.values());
        jComboBox.setAlignmentX(0);
        jComboBox.setMaximumSize(new Dimension(2500, 40));
        //set sleeve type preference to NA
        jComboBox.setSelectedItem(SleeveType.NA);
        //initialise the field
        sleeveType = (SleeveType) jComboBox.getSelectedItem();

        jComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                sleeveType = (SleeveType) jComboBox.getSelectedItem();
            }
        });
        //create and return JPanel
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(0);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(jComboBox);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        return panel;
    }

    /**
     * allows user selection of one t-shirt neckline type
     *
     * @return a JPanel containing a dropdown list
     */
    public JPanel userInputNeckline() {
        //a dropdown list of all neckline options
        JComboBox<Neckline> jComboBox = new JComboBox<>(Neckline.values());
        jComboBox.setAlignmentX(0);
        jComboBox.setMaximumSize(new Dimension(2500, 40));
        //setting neckline type preference to NA
        jComboBox.setSelectedItem(Neckline.NA);
        //initialise the field
        neckline = (Neckline) jComboBox.getSelectedItem();

        jComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                neckline = (Neckline) jComboBox.getSelectedItem();
            }
        });
        //create and return JPanel
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(0);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(jComboBox);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        return panel;
    }
    //getters - used to access values the user has entered when an object of this class is created

    /**
     * @return the user's desired min price
     */
    public double getMinPrice() {
        return minPrice;
    }

    /**
     * @return the user's desired max price
     */
    public double getMaxPrice() {
        return maxPrice;
    }

    /**
     * @return the user's type of garment selection
     */
    public GarmentType getSelectedGarmentType() {
        return selectedGarmentType;
    }

    /**
     * @return the user's brand selections
     */
    public Set<Brand> getSelectedBrands() {
        return selectedBrands;
    }

    /**
     * @return the user's type of material selection
     */
    public Material getSelectedMaterial() {
        return selectedMaterial;
    }

    /**
     * @return the user's preferred sizes
     */
    public Set<Size> getSelectedSizes() {
        return selectedSizes;
    }

    /**
     * @return the user's type of hoodie selection
     */
    public HoodieStyle getHoodieStyle() {
        return hoodieStyle;
    }

    /**
     * @return the user's type of pocket selection
     */
    public PocketType getPocketType() {
        return pocketType;
    }

    /**
     * @return the user's type of sleeve selection
     */
    public SleeveType getSleeveType() {
        return sleeveType;
    }

    /**
     * @return the user's type of neckline selection
     */
    public Neckline getNeckline() {
        return neckline;
    }

    /**
     * @return the user's name from the JText field
     */
    public String getName() {
        return name.getText();
    }

    /**
     * @return the user's email from the JText field
     */
    public String getEmail() {
        return email.getText();
    }

    /**
     * @return the user's phone number from the JText field
     */
    public String getPhoneNumber() {
        return phoneNumber.getText();
    }

    /**
     * @return the user's message from the JText field
     */
    public String getMessage() {
        return message.getText();
    }

}

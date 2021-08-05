package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


/**
 * This class contains the functionality for adding products to the inventory.
 * <p>
 * The user can enter data values in the text fields form (except the product ID), and select parts to associate with the product.
 * After saving the product, the user is automatically redirected to the Main form.
 * Canceling or exiting this form redirects users to the Main form.
 * AddProduct.fxml contains the markup for this screen.
 */
public class AddProduct implements Initializable {

    private static final ObservableList<Part> partsInvSearch = FXCollections.observableArrayList();
    private static final ObservableList<Part> productsInv = FXCollections.observableArrayList();
    private static final ObservableList<Part> partsObservableList = FXCollections.observableArrayList();
    @FXML
    public Button searchPartsBtn;
    @FXML
    public TextField searchAssociatedPartsTxt;
    Stage stage;
    Parent scene;
    Inventory inventory;
    @FXML
    private Label addProductIdLbl;
    @FXML
    private Label addProductNameLbl;
    @FXML
    private TextField productIdTxt;
    @FXML
    private TextField productNameTxt;
    @FXML
    private TextField stockTxt;
    @FXML
    private TextField productPriceTxt;
    @FXML
    private TextField productMaxTxt;
    @FXML
    private TextField productMachineIdTxt;
    @FXML
    private TextField productMinTxt;
    @FXML
    private TableView<Part> allPartTbl;
    @FXML
    private TableColumn<Part, Integer> partIdCol;
    @FXML
    private TableColumn<Part, String> partNameCol;
    @FXML
    private TableColumn<Part, Integer> stockCol;
    @FXML
    private TableColumn<Part, Double> ppuCol;
    @FXML
    private Button saveProductBtn;
    @FXML
    private Button cancelAddProductBtn;
    @FXML
    private Button removeAssocPartBtn;
    @FXML
    private Button addPartBtn;
    @FXML
    private TableView<Part> associatedPartsTbl;
    @FXML
    private TableColumn<Part, Integer> associatedPartIdCol;
    @FXML
    private TableColumn<Part, String> associatedPartNameCol;
    @FXML
    private TableColumn<Part, Integer> associatedStock;
    @FXML
    private TableColumn<Part, Double> associatedPpuCol;
    @FXML
    private TextField searchPartsTxt;
    private int id;


    /**
     * Populates parts tableview.
     */
    @FXML
    public void generateParts() {
        partsObservableList.setAll( Inventory.getAllParts() );
        partIdCol.setCellValueFactory( new PropertyValueFactory<>( "id" ) );
        partNameCol.setCellValueFactory( new PropertyValueFactory<>( "name" ) );
        stockCol.setCellValueFactory( new PropertyValueFactory<>( "stock" ) );
        ppuCol.setCellValueFactory( new PropertyValueFactory<>( "price" ) );
        allPartTbl.setItems( partsObservableList );

    }

    /**
     * Searches for parts by ID or name (partial or full name) using the text field, and displays matching results in the all parts TableView.
     * <p>
     * If the part is not found, the application displays an error message in a dialog box. If the search field is empty, the table is repopulated with all available parts.
     *
     * @param mouseEvent User clicks on the search button.
     */
    @FXML
    public void onMouseClickSearchPart(MouseEvent mouseEvent) {

        String name = searchPartsTxt.getText();
        try {
            int id = Integer.parseInt( name );
            Part tempPart = Inventory.lookupPart( id );
            partsInvSearch.setAll( tempPart );
            allPartTbl.setItems( partsInvSearch );
        } catch (NumberFormatException numberFormatException) {
            partsInvSearch.setAll( Inventory.lookupPart( name ) );
            allPartTbl.setItems( partsInvSearch );
        }
        if (name.equals( "" )) {
            allPartTbl.setItems( Inventory.getAllParts() );
        }
        if (partsInvSearch.isEmpty()) {

            allPartTbl.setItems( Inventory.getAllParts() );
            handleProductErrorDialog( "noResults" );
        }
        allPartTbl.refresh();

    }


    /**
     * Canceling this form redirects users to the Main form.
     * <p>
     * Prompts the user to confirm cancellation if product information wasn't saved.
     *
     * @param mouseEvent confirmation to return to main screen.
     * @throws IOException Resources are unavailable.
     */
    @FXML
    public void onClickedCancelAddProduct(MouseEvent mouseEvent) throws IOException {
        Alert alert = new Alert( Alert.AlertType.CONFIRMATION );
        alert.setContentText( "Product will not be save. Do you still want to continue?" );
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            backToMain( mouseEvent );
        } else {
            Stage stage = (Stage) ((Button) mouseEvent.getSource()).getScene().getWindow();
            Parent scene = FXMLLoader.load( getClass().getResource( "/view/AddProduct.fxml" ) );
            stage.setScene( new Scene( scene ) );
            stage.show();
        }

    }

    /**
     * Associates one or more parts with a product.
     * <p>
     * Copies selected part from the top table to the bottom table and adds the data to the product.
     *
     * @param event User clicks the Add button under the Parts TableView.
     */
    @FXML
    public void onClickAddAssociatedPart(MouseEvent event) {
        productsInv.add( allPartTbl.getSelectionModel().getSelectedItem() );
        associatedPartIdCol.setCellValueFactory( new PropertyValueFactory<>( "id" ) );
        associatedPartNameCol.setCellValueFactory( new PropertyValueFactory<>( "name" ) );
        associatedStock.setCellValueFactory( new PropertyValueFactory<>( "stock" ) );
        associatedPpuCol.setCellValueFactory( new PropertyValueFactory<>( "price" ) );
        associatedPartsTbl.setItems( productsInv );


        Part selectedPart = allPartTbl.getSelectionModel().getSelectedItem();
        if (selectedPart == null) {
            handleProductErrorDialog( "notSelected" );
        }
    }


    /**
     * Dissociates a part from a product.
     * <p>
     * Removes the selected part from the bottom table.
     * Displays an error message if no part is selected.
     *
     * @param event User clicks the remove associated parts button.
     */
    @FXML
    public void onClickedRemoveAssocPart(MouseEvent event) {

        Part selectedPart = associatedPartsTbl.getSelectionModel().getSelectedItem();
        if (selectedPart == null) {
            handleProductErrorDialog( "notSelected" );
        } else {
            productsInv.remove( selectedPart );
        }

    }

    /**
     * Saves the product's name, inventory level,price,maximum and minimum values,and list of associated parts.
     * <p>
     * Automatically redirects user to the Main form after save.
     *
     * @param event User clicks on the save button.
     */
    @FXML
    public void onClickedSaveProduct(MouseEvent event) {
        Product product;
        try {
            String name = productNameTxt.getText();
            int id = Integer.parseInt( productIdTxt.getText() );
            double price = Double.parseDouble( productPriceTxt.getText() );
            int stock = Integer.parseInt( stockTxt.getText() );
            int min = Integer.parseInt( productMinTxt.getText() );
            int max = Integer.parseInt( productMaxTxt.getText() );

            productValidation( name, price, stock, min, max );
            product = new Product( id, name, price, stock, min, max );

            for (Part sp : productsInv) {
                product.addAssociatedPart( sp );
            }
            Inventory.addProduct( product );
        } catch (NumberFormatException e) {
            handleProductErrorDialog( "default" );

        } finally {
            backToMain( event );
        }


    }


    /**
     * Implements input validation and calls method to handle error dialog.
     * <p>
     * Validation requirements:
     * Min must be less than Max.
     * Stock must be between Min and Max and greater than zero.
     * Part must have a name.
     * Price must be greater that zero.
     *
     * @param name  name of the product.
     * @param price price of the product.
     * @param stock number of product in stock.
     * @param min   min number of product allowed.
     * @param max   max number of product allowed.
     */
    public void productValidation(String name, double price, int stock, int min, int max) {

        if (!((min <= stock) && (stock <= max))) {
            handleProductErrorDialog( "inventory" );
        } else if (name.isEmpty() || !name.matches( "^[a-zA-Z]*$" )) {
            handleProductErrorDialog( "badName" );
        } else if (price <= 0) {
            handleProductErrorDialog( "price" );
        } else if (min > max) {
            handleProductErrorDialog( "range" );
        } else if (stock < 0) {
            handleProductErrorDialog( "inventory" );
        }

    }

    /**
     * Displays the dialog associated with the error type.
     * <p>
     * This method is used when:
     * No item has been selected.
     * Search returns no results.
     * Name does not meet the requirements.
     * Price does not meet the requirements.
     * Inventory requirements are not met.
     *
     * @param errorType String passed from validation method
     */
    public void handleProductErrorDialog(String errorType) {
        Alert alert = new Alert( Alert.AlertType.ERROR );
        switch (errorType) {
            case "notSelected":
                alert.setContentText( "Please select an item from the table" );
                alert.showAndWait();
                break;
            case "noResults":
                alert = new Alert( Alert.AlertType.INFORMATION );
                alert.setContentText( "No matching results" );
                alert.showAndWait();
                break;
            case "badName":
                alert.setContentText( "Please give the part a valid name" );
                alert.showAndWait();
                break;
            case "price":
                alert.setContentText( "Price must be greater than 0" );
                alert.showAndWait();
                break;
            case "inventory":
                alert.setContentText( "Inventory must be between the minimum and maximum values" );
                alert.showAndWait();
                break;
            case "range":
                alert.setContentText( "Inventory minimum cannot be greater than the inventory maximum." );
                alert.showAndWait();
                break;
            case "zeroInventory":
                alert.setContentText( "Inventory must be greater than 0." );
                alert.showAndWait();
                break;
            default:
                alert.setContentText( "Please enter a valid value for each text field" );
                alert.showAndWait();
        }
    }

    /**
     * Redirects user to the main screen after clicking Cancel or saving a product.
     *
     * @param mouseEvent User clicks on the Cancel button.
     */
    public void backToMain(MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = new FXMLLoader( getClass().getResource( "/view/MainScreen.fxml" ) );
            MainScreen controller = new MainScreen( inventory );
            loader.setController( controller );
            Parent root = loader.load();
            Scene scene = new Scene( root );
            Stage stage = (Stage) ((Button) mouseEvent.getSource()).getScene().getWindow();
            stage.setScene( scene );
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes the controller class.
     * <p>
     * Sets automatically generated number to inventory products that aren't editable by the user.
     *
     * @param url            location.
     * @param resourceBundle class resources.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Inventory.setProductId();
        int id = Inventory.getProductId();
        productIdTxt.setText( String.valueOf( id ) );

        generateParts();

    }
}
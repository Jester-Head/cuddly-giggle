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
 * This class contains the functionality for modifying existing products in the inventory.
 * <p>
 * The user can modify data values in the text fields sent from the Main form (except the product ID).
 * The user can also add or remove a product's associated parts.
 * After saving modifications to the product, the user is automatically redirected to the Main form.
 * Canceling or exiting this form redirects users to the Main form.
 * ModifyProduct.fxml contains the markup for this screen.
 */
public class ModifyProduct implements Initializable {

    private static final ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static final ObservableList<Part> partsInvSearch = FXCollections.observableArrayList();
    private final ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    @FXML
    public Button cancelAddProduct;
    @FXML
    public Button searchParts;
    @FXML
    public TableView<Part> associatedPartsTbl;
    Inventory inventory;
    @FXML
    private TextField modProductIdTxt;
    @FXML
    private TextField modProductNameTxt;
    @FXML
    private TextField modProductMaxTxt;
    @FXML
    private TextField modProductMinTxt;
    @FXML
    private TextField modProductPriceTxt;
    @FXML
    private TextField modProductStockTxt;
    @FXML

    private Button addAssociatedPartBtn;
    @FXML
    private Button savePartBtn;
    @FXML
    private Button cancelAddPartBtn;
    @FXML
    private Button removeAssocPartBtn;
    @FXML
    private TableView<Part> allPartsTbl;
    @FXML
    private TableColumn<Part, Integer> allPartsIdCol;
    @FXML
    private TableColumn<Part, String> allPartsNameCol;
    @FXML
    private TableColumn<Part, Integer> allPartsStockCol;
    @FXML
    private TableColumn<Part, Double> allPartsPPUCol;
    @FXML
    private TableColumn<Part, Integer> associatedPartsIdCol;
    @FXML
    private TableColumn<Part, String> associatedPartsNameCol;
    @FXML
    private TableColumn<Part, Integer> associatedPartsStockCol;
    @FXML
    private TableColumn<Part, Double> associatedPartsPPUCol;
    @FXML
    private TextField searchProductTextField;

    /**
     * Searches for parts by ID or partial or full name using the text field and displays matching results in the Parts TableView.
     *
     * @param mouseEvent the user clicks on the search button.
     */
    @FXML
    public void onClickedSearchParts(MouseEvent mouseEvent) {

        String name = searchProductTextField.getText();
        try {
            int id = Integer.parseInt( name );
            Part tempPart = Inventory.lookupPart( id );
            partsInvSearch.setAll( tempPart );
            allPartsTbl.setItems( partsInvSearch );
        } catch (NumberFormatException numberFormatException) {
            partsInvSearch.setAll( Inventory.lookupPart( name ) );
            allPartsTbl.setItems( partsInvSearch );
        }
        if (name.equals( "" )) {
            allPartsTbl.setItems( Inventory.getAllParts() );
        }
        if (partsInvSearch.isEmpty()) {
            handleSearchErrorDialog( "noResults" );
            allPartsTbl.setItems( Inventory.getAllParts() );
        }
        allPartsTbl.refresh();


    }


    /**
     * Adds selected parts to the product's associated parts list and displays the parts in a tableview.
     *
     * @param event the user clicks on the add button.
     */
    @FXML
    public void onClickAddAssociatedPart(MouseEvent event) {

        associatedParts.addAll( allPartsTbl.getSelectionModel().getSelectedItem() );
        associatedPartsIdCol.setCellValueFactory( new PropertyValueFactory<>( "id" ) ); //calls getId method of the object created
        associatedPartsNameCol.setCellValueFactory( new PropertyValueFactory<>( "name" ) );
        associatedPartsStockCol.setCellValueFactory( new PropertyValueFactory<>( "stock" ) );
        associatedPartsPPUCol.setCellValueFactory( new PropertyValueFactory<>( "price" ) );
        associatedPartsTbl.setItems( associatedParts );


        Part selectedPart = allPartsTbl.getSelectionModel().getSelectedItem();
        if (selectedPart == null) {
            handleSearchErrorDialog( "notSelected" );
        }

    }

    /**
     * Displays the dialog associated with the error type.
     *
     * @param type string passed from validation method
     */
    public void handleSearchErrorDialog(String type) {
        Alert alert = new Alert( Alert.AlertType.ERROR );
        switch (type) {
            case "notSelected":
                alert.setContentText( "Please select an item from the table" );
                alert.showAndWait();
                break;
            case "noResults":
                alert = new Alert( Alert.AlertType.INFORMATION );
                alert.setContentText( "No matching results" );
                alert.showAndWait();
        }
    }

    /**
     * This prompts the user to confirm cancellation and then returns them to the main screen.
     *
     * @param mouseEvent the user clicks on the ok button.
     * @throws IOException the resources are not available when accessed.
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
            Parent scene = FXMLLoader.load( getClass().getResource( "/view/ModifyProduct.fxml" ) );
            stage.setScene( new Scene( scene ) );
            stage.show();
        }

    }


    /**
     * Removes product's associate part from the table or displays error message if no part is selected.
     *
     * @param event the user clicks on the remove associated part button.
     */
    @FXML
    public void onClickedRemoveAssocPart(MouseEvent event) {
        Part selectedPart = associatedPartsTbl.getSelectionModel().getSelectedItem();
        if (selectedPart == null) {
            handleProductErrorDialog( "notSelected" );
        } else {
            associatedParts.remove( selectedPart );
        }


    }

    /**
     * Updates the product's details upon confirmation or displays an error message if input values are invalid.
     *
     * @param mouseEvent the user clicks on the save button.
     * @throws IOException product details do not match variable types.
     */
    @FXML
    public void onClickedSaveProduct(MouseEvent mouseEvent) throws IOException {
        try {

            int id = Integer.parseInt( modProductIdTxt.getText() );
            String name = modProductNameTxt.getText();
            int stock = Integer.parseInt( modProductStockTxt.getText() );
            double price = Double.parseDouble( modProductPriceTxt.getText() );
            int min = Integer.parseInt( modProductMinTxt.getText() );
            int max = Integer.parseInt( modProductMaxTxt.getText() );

            productValidation( name, price, stock, min, max );

            Product product = new Product( id, name, price, stock, min, max );

            Alert alert = new Alert( Alert.AlertType.CONFIRMATION );
            alert.setContentText( "Apply all changes?" );
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Inventory.updateProduct( id, product );

                for (Part sp : associatedParts) {
                    product.addAssociatedPart( sp );
                }
            }


        } catch (NumberFormatException e) {
            handleProductErrorDialog( "default" );
        } finally {
            backToMain( mouseEvent );

        }


    }


    /**
     * Redirects user to the main screen upon confirmation.
     *
     * @param mouseEvent user selects the ok button.
     * @throws IOException the resources are not available when accessed.
     */
    public void backToMain(MouseEvent mouseEvent) throws IOException {

        FXMLLoader loader = new FXMLLoader( getClass().getResource( "/view/MainScreen.fxml" ) );
        MainScreen controller = new MainScreen( inventory );
        loader.setController( controller );
        Parent root = loader.load();
        Scene scene = new Scene( root );
        Stage stage = (Stage) ((Button) mouseEvent.getSource()).getScene().getWindow();
        stage.setScene( scene );
        stage.show();

    }

    /**
     * Set text fields to product's details that have been passed from main screen.
     *
     * @param product class representation of an inventory product.
     */
    @FXML
    public void sendProducts(Product product) {

        modProductIdTxt.setText( String.valueOf( product.getId() ) );
        modProductMaxTxt.setText( String.valueOf( product.getMax() ) );
        modProductMinTxt.setText( String.valueOf( product.getMin() ) );
        modProductNameTxt.setText( product.getName() );
        modProductPriceTxt.setText( String.valueOf( product.getPrice() ) );
        modProductStockTxt.setText( String.valueOf( product.getStock() ) );
        associatedParts.addAll( product.getAllAssociatedParts() );

    }

    /**
     * Displays inventory parts.
     */
    public void displayAllParts() {
        allParts.setAll( Inventory.getAllParts() );
        allPartsIdCol.setCellValueFactory( new PropertyValueFactory<>( "id" ) );
        allPartsNameCol.setCellValueFactory( new PropertyValueFactory<>( "name" ) );
        allPartsStockCol.setCellValueFactory( new PropertyValueFactory<>( "stock" ) );
        allPartsPPUCol.setCellValueFactory( new PropertyValueFactory<>( "price" ) );
        allPartsTbl.setItems( allParts );

    }

    /**
     * Displays product's associated parts.
     */
    public void displayAssociatedParts() {

        associatedPartsIdCol.setCellValueFactory( new PropertyValueFactory<>( "id" ) );
        associatedPartsNameCol.setCellValueFactory( new PropertyValueFactory<>( "name" ) );
        associatedPartsStockCol.setCellValueFactory( new PropertyValueFactory<>( "stock" ) );
        associatedPartsPPUCol.setCellValueFactory( new PropertyValueFactory<>( "price" ) );
        associatedPartsTbl.setItems( associatedParts );

    }

    /**
     * Displays the dialog associated with the error type.
     *
     * @param errorType string passed from validation method
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
     * Implements input validation and calls method to handle error dialog.
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
        } else if (name.isEmpty()) {
            handleProductErrorDialog( "badName" );
        } else if (price <= 0) {
            handleProductErrorDialog( "price" );
        } else if (min > max) {
            handleProductErrorDialog( "range" );
        } else if (stock < 0) {
            handleProductErrorDialog( "inventory" );
        }

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        displayAllParts();
        displayAssociatedParts();


    }


}
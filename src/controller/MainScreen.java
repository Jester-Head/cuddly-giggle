package controller;

import javafx.application.Platform;
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
 * Main Screen Controller.
 */
public class MainScreen implements Initializable {

    private static final ObservableList<Part> partsInv = FXCollections.observableArrayList();
    private static final ObservableList<Product> productsInv = FXCollections.observableArrayList();
    private static final ObservableList<Part> partsInvSearch = FXCollections.observableArrayList();
    private static final ObservableList<Product> productInvSearch = FXCollections.observableArrayList();
    Inventory inventory;
    Stage stage;
    Parent scene;
    @FXML
    private Button addPartBtn;
    @FXML
    private Button modPartBtn;
    @FXML
    private Button deletePartBtn;
    @FXML
    private TableView<Part> partsTableView;
    @FXML
    private TableColumn<Part, Integer> partIdCol;
    @FXML
    private TableColumn<Part, String> partNameCol;
    @FXML
    private TableColumn<Part, Integer> partInvLvLCol;
    @FXML
    private TableColumn<Part, Double> partPPUCol;
    @FXML
    private TextField searchPartTxt;
    @FXML
    private Button searchPartMainsBtn;
    @FXML
    private Button addProductBtn;
    @FXML
    private Button modProductBtn;
    @FXML
    private Button deleteProductBtn;
    @FXML
    private Button searchProductsBtn;
    @FXML
    private TextField searchProductTxt;
    @FXML
    private TableView<Product> productsTableView;
    @FXML
    private TableColumn<Product, Integer> productIdCol;
    @FXML
    private TableColumn<Product, String> productNameCol;
    @FXML
    private TableColumn<Product, Integer> invProductCol;
    @FXML
    private TableColumn<Product, Double> productPPUCol;
    @FXML
    private Button exitBtn;

    /**
     * Main screen controller.
     *
     * @param inventory Instance of the inventory class.
     */
    public MainScreen(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        generateParts();
        generateProducts();
    }


    /**
     * Populates the parts tableview.
     */
    public void generateParts() {
        partsInv.setAll( Inventory.getAllParts() );

        partIdCol.setCellValueFactory( new PropertyValueFactory<>( "id" ) );
        partNameCol.setCellValueFactory( new PropertyValueFactory<>( "name" ) );
        partInvLvLCol.setCellValueFactory( new PropertyValueFactory<>( "stock" ) );
        partPPUCol.setCellValueFactory( new PropertyValueFactory<>( "price" ) );
        partsTableView.setItems( partsInv );

    }

    /**
     * Populates the products tableview.
     */
    public void generateProducts() {
        productsInv.setAll( Inventory.getAllProducts() );

        productIdCol.setCellValueFactory( new PropertyValueFactory<>( "id" ) );
        productNameCol.setCellValueFactory( new PropertyValueFactory<>( "name" ) );
        invProductCol.setCellValueFactory( new PropertyValueFactory<>( "stock" ) );
        productPPUCol.setCellValueFactory( new PropertyValueFactory<>( "price" ) );
        productsTableView.setItems( productsInv );

    }


    /**
     * Opens the Add Product form when the user clicks the Add button under the Products TableView.
     *
     * @param mouseEvent User clicks the Add button under the Products TableView.
     * @throws IOException the resources are not available.
     */
    @FXML
    public void onClickAddProduct(MouseEvent mouseEvent) throws IOException {
        stage = (Stage) ((Button) mouseEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load( getClass().getResource( "/view/AddProduct.fxml" ) );
        stage.setScene( new Scene( scene ) );
        stage.show();
    }


    /**
     * Opens the Modify Product form and displays the product details in corresponding fields.
     * <p>
     * Calls an error handler method if a product isn't selected.
     *
     * @param mouseEvent User clicks the Modify button under the Products TableView.
     */
    @FXML
    public void onClickedModifyProduct(MouseEvent mouseEvent) {

        try {

            /*
       The line below caused my program to display the error dialog whether or not an item was selected. This error is due to assigning the selected item to a new product instead of passing the existing object to the modify product controller.
       Simply removing this line solved the problem.
                Product product = productsTableView.getSelectionModel().getSelectedItem();

            */


            FXMLLoader loader = new FXMLLoader();
            loader.setLocation( getClass().getResource( "/view/ModifyProduct.fxml" ) );
            loader.load();

            ModifyProduct modifyProduct = loader.getController();
            modifyProduct.sendProducts( productsTableView.getSelectionModel().getSelectedItem() );

            stage = (Stage) ((Button) mouseEvent.getSource()).getScene().getWindow();
            scene = loader.getRoot();
            stage.setScene( (new Scene( scene )) );
            stage.show();

        } catch (Exception exception) {
            handleMainErrorDialog( "notSelected" );
        }


    }


    /**
     * Deletes the selected product or displays an error message in a dialog box if it can't be deleted.
     * <p>
     * This method will produce an error message if no product is selected or if a product has at least one associated part.
     *
     * @param mouseEvent the user clicks the delete button under the Products TableView
     */
    @FXML
    public void onClickedDeleteProduct(MouseEvent mouseEvent) {
    /*
    Below is another logical error. I fixed by deleting the unnecessary observable list I used to test for the product containing associated parts.

        Product selectedProduct = productsTableView.getSelectionModel().getSelectedItem();
        ObservableList<Part> testProductParts;
        testProductParts = selectedProduct.getAllAssociatedParts();

        if (selectedProduct == null) {
            handleMainErrorDialog( "notSelected" );
        } else {
            Alert alert = new Alert( Alert.AlertType.CONFIRMATION );
            alert.setContentText( "Are you sure you want to remove this item?" );
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                if (!testProductParts.isEmpty()) {
                    handleMainErrorDialog( "assocParts" );
                } else {
                    productsInv.remove( selectedProduct );
                }
            }
        }
    */
        Product product = productsTableView.getSelectionModel().getSelectedItem();

        if (product == null) {
            Alert alert = new Alert( Alert.AlertType.ERROR, "Please select a product to delete." );
            alert.showAndWait();
        } else {
            Alert alert = new Alert( Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this product?" );
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                if (!product.getAllAssociatedParts().isEmpty()) {
                    handleMainErrorDialog( "assocParts" );
                } else {
                    Inventory.deleteProduct( product );
                    productsTableView.setItems( Inventory.getAllProducts() );
                }
            }
        }
    }


    /**
     * Opens the Add Part form when the user clicks on the Add button under the Parts TableView.
     *
     * @param mouseEvent the user clicks on the Add button under the Parts TableView.
     * @throws IOException the resources are not available when accessed.
     */
    @FXML
    public void onClickAddPart(MouseEvent mouseEvent) throws IOException {

        stage = (Stage) ((Button) mouseEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load( getClass().getResource( "/view/AddPart.fxml" ) );
        stage.setScene( new Scene( scene ) );
        stage.show();
    }


    /**
     * Searches for parts by ID or name (partial or full name) using the text field, and displays matching results in the Parts TableView.
     * <p>
     * If the part is not found, the application displays an error message in a dialog box. If the search field is empty, the table is repopulated with all available parts.
     *
     * @param mouseEvent User clicks on the search button.
     */
    @FXML
    public void onClickedSearchParts(MouseEvent mouseEvent) {

        String name = searchPartTxt.getText();
        try {
            int id = Integer.parseInt( name );
            Part tempPart = Inventory.lookupPart( id );
            partsInvSearch.setAll( tempPart );
            partsTableView.setItems( partsInvSearch );
        } catch (NumberFormatException numberFormatException) {
            partsInvSearch.setAll( Inventory.lookupPart( name ) );
            partsTableView.setItems( partsInvSearch );
        }
        if (name.equals( "" )) {
            partsTableView.setItems( Inventory.getAllParts() );
        }
        if (partsInvSearch.isEmpty()) {
            handleMainErrorDialog( "noResults" );
            partsTableView.setItems( Inventory.getAllParts() );
        }
        partsTableView.refresh();
    }


    /**
     * Opens the Modify Part form and displays the part details in corresponding fields.
     * <p>
     * Calls an error handler method if a part isn't selected.
     *
     * @param mouseEvent User clicks the Modify button under the Parts TableView.
     */
    @FXML
    public void onClickedModifyPart(MouseEvent mouseEvent) {
        try {
            Part item = partsTableView.getSelectionModel().getSelectedItem();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation( getClass().getResource( "/view/ModifyPart.fxml" ) );
            loader.load();

            ModifyPart modifyPart = loader.getController();
            modifyPart.receivePart( item );

            stage = (Stage) ((Button) mouseEvent.getSource()).getScene().getWindow();
            scene = loader.getRoot();
            stage.setScene( (new Scene( scene )) );
            stage.show();
        } catch (Exception e) {
            handleMainErrorDialog( "notSelected" );

        }

    }

    /**
     * Deletes the selected part from the Parts TableView or calls the error handler method if part can't be deleted.
     *
     * @param mouseEvent User clicks the Delete button under the Parts TableView.
     */
    @FXML
    public void onClickedDeletePart(MouseEvent mouseEvent) {

        Part selectedPart = partsTableView.getSelectionModel().getSelectedItem();
        if (selectedPart == null) {
            handleMainErrorDialog( "notSelected" );
        } else {
            handleMainErrorDialog( "confirm" );
            partsInv.remove( selectedPart );
        }


    }

    /**
     * Closes App upon confirmation.
     *
     * @param mouseEvent User clicks on the Exit button.
     */
    @FXML
    public void onClickedExit(MouseEvent mouseEvent) {
        Alert alert = new Alert( Alert.AlertType.CONFIRMATION, "Do you want exit the program?", ButtonType.YES, ButtonType.NO );
        ButtonType result = alert.showAndWait().orElse( ButtonType.NO );
        if (ButtonType.YES.equals( result ))
            Platform.exit();
    }

    /**
     * Displays the dialog associated with the error type.
     * <p>
     * This method is used when:
     * No item has been selected.
     * The search returns no results.
     * Trying to remove a product with associated parts.
     * Confirmation is required.
     *
     * @param errorType String passed from the calling method.
     */
    public void handleMainErrorDialog(String errorType) {
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
            case "confirm":
                alert = new Alert( Alert.AlertType.CONFIRMATION );
                alert.setContentText( "Are you sure you want to remove this item?" );
                alert.showAndWait();
                break;
            case "assocParts":
                alert.setContentText( "Cannot delete product with associated parts" );
                alert.showAndWait();


        }
    }

    /**
     * Searches for products by ID or name (partial or full name) using the text field and displays matching results in the ProductsTableView.
     * <p>
     * If the product is not found, the application displays an error message in a dialog box. If the search field is empty, the table is repopulated with all available products.
     *
     * @param mouseEvent User clicks on the search button above the Products TableView.
     */
    @FXML
    public void onClickedSearchProducts(MouseEvent mouseEvent) {

        String name = searchProductTxt.getText();
        try {
            int id = Integer.parseInt( name );
            Product tempProduct = Inventory.lookupProduct( id );
            productInvSearch.setAll( tempProduct );
            productsTableView.setItems( productInvSearch );
        } catch (NumberFormatException numberFormatException) {

            productInvSearch.setAll( Inventory.lookupProduct( name ) );
            productsTableView.setItems( productInvSearch );
        }
        if (name.equals( "" )) {
            productsTableView.setItems( Inventory.getAllProducts() );
        }
        if (productInvSearch.isEmpty()) {
            handleMainErrorDialog( "noResults" );
            productsTableView.setItems( Inventory.getAllProducts() );
        }
        partsTableView.refresh();
    }

}
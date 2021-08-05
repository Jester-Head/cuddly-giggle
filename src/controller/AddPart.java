package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.OutSourced;
import model.Part;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This class contains the functionality for adding parts to the inventory.
 * <p>
 * The user can enter data values in the text fields.
 * After saving the part, the user is automatically redirected to the Main form.
 * Canceling or exiting this form redirects users to the Main form.
 * AddPart.fxml contains the markup for this screen.
 */
public class AddPart implements Initializable {
    Stage stage;
    Parent scene;
    @FXML
    private Label switchLbl;
    @FXML
    private TextField partIdTxt;
    @FXML
    private TextField partNameTxt;
    @FXML
    private TextField invTxt;
    @FXML
    private TextField partPriceTxt;
    @FXML
    private TextField maxPartTxt;
    @FXML
    private TextField minPartTxt;
    @FXML
    private TextField switchTxt;
    @FXML
    private Button savePartBtn;
    @FXML
    private Button cancelAddPartBtn;
    @FXML
    private RadioButton outSourcedRbtn;
    @FXML
    private ToggleGroup inOutTG;
    @FXML
    private RadioButton inHouseRbtn;
    private boolean isInHouse;
    private int id;
    private Inventory inventory;


    /**
     * Switch the bottom label to the correct value (Machine ID).
     *
     * @param event In house radio button is selected.
     */
    @FXML
    public void OnActionHandleInHouse(ActionEvent event) {
        isInHouse = true;
        switchLbl.setText( "Machine ID: " );
    }


    /**
     * Switch the bottom label to the correct value (Company Name).
     *
     * @param event Out source radio button is selected.
     */
    @FXML
    public void OnActionHandleOutsource(ActionEvent event) {
        isInHouse = false;
        switchLbl.setText( "Company Name: " );
    }


    /**
     * Canceling this form redirects users to the Main form.
     * <p>
     * Prompts the user to confirm cancellation if part information wasn't saved.
     *
     * @param mouseEvent confirmation to return to main screen.
     * @throws IOException the resources are unavailable.
     */
    @FXML
    public void onClickedCancelAddPart(MouseEvent mouseEvent) throws IOException {
        Alert alert = new Alert( Alert.AlertType.CONFIRMATION );
        alert.setContentText( "Part will not be save. Do you still want to continue?" );
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            backToMain( mouseEvent );
        } else {
            Stage stage = (Stage) ((Button) mouseEvent.getSource()).getScene().getWindow();
            Parent scene = FXMLLoader.load( getClass().getResource( "/view/AddPart.fxml" ) );
            stage.setScene( new Scene( scene ) );
            stage.show();
        }

    }


    /**
     * Saves the part's name, inventory level,price,maximum and minimum values,and company name or machine ID values into active text fields.
     * <p>
     * Automatically redirects user to the Main form after save.
     *
     * @param mouseEvent User clicks on the save button.
     * @throws IOException The data entered in the text fields are invalid.
     */
    @FXML
    public void onClickedSavePart(MouseEvent mouseEvent) throws IOException {

        try {
            inHouseRbtn.setToggleGroup( inOutTG );
            outSourcedRbtn.setToggleGroup( inOutTG );


            int id = Integer.parseInt( partIdTxt.getText() );
            String name = partNameTxt.getText();
            int stock = Integer.parseInt( invTxt.getText() );
            double price = Double.parseDouble( partPriceTxt.getText() );
            int min = Integer.parseInt( minPartTxt.getText() );
            int max = Integer.parseInt( maxPartTxt.getText() );
            Part part = null;

            if (inHouseRbtn.isSelected()) {
                int machineIdInt = Integer.parseInt( switchTxt.getText() );
                part = new InHouse( id, name, price, stock, min, max, machineIdInt );
            } else {
                String companyName = switchTxt.getText();
                part = new OutSourced( id, name, price, stock, min, max, companyName );
            }
            partValidation( name, price, stock, min, max );
            Inventory.addPart( part );
        } catch (NumberFormatException e) {
            handlePartErrorDialog( "machineId" );
        } finally {
            backToMain( mouseEvent );
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
     * @param name  name of the part.
     * @param price price of the part.
     * @param stock number of parts in stock.
     * @param min   min number of parts allowed.
     * @param max   max number of parts allowed.
     */
    public void partValidation(String name, double price, int stock, int min, int max) {

        if (!((min <= stock) && (stock <= max))) {
            handlePartErrorDialog( "inventory" );
        } else if (name.isEmpty()) {
            handlePartErrorDialog( "badName" );
        } else if (price <= 0) {
            handlePartErrorDialog( "price" );
        } else if (min > max) {
            handlePartErrorDialog( "range" );
        } else if (stock < 0) {
            handlePartErrorDialog( "inventory" );
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
     * Machine ID isn't an integer.
     * Trying to remove a product with associated parts.
     * Confirmation is required.
     *
     * @param errorType String passed from the calling method.
     */
    public void handlePartErrorDialog(String errorType) {
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
            case "machineId":
                alert.setContentText( "Machine ID should only contain numeric values" );
                alert.showAndWait();
                break;
            default:
                alert.setContentText( "Invalid input" );
                alert.showAndWait();
        }
    }


    /**
     * Redirects user to the main screen after clicking Cancel or saving a part.
     *
     * @param mouseEvent User clicks cancel button.
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
     * Initializes the controller class and assigns automatically generated number to inventory parts.
     *
     * @param url            location.
     * @param resourceBundle class resources.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Inventory.setPartId();
        id = Inventory.getPartId();
        partIdTxt.setText( String.valueOf( id ) );

    }
}
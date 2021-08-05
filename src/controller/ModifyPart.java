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
 * This class contains the functionality for modifying existing parts in the inventory.
 * <p>
 * The user can modify data values in the text fields sent from the Main form (except the part ID).
 * After saving modifications to the part, the user is automatically redirected to the Main form.
 * Canceling or exiting this form redirects users to the Main form.
 * ModifyPart.fxml contains the markup for this screen.
 */
public class ModifyPart implements Initializable {
    Stage stage;
    Parent scene;

    @FXML
    private Label modSwitchLbl;

    @FXML
    private TextField modPartIdTxt;

    @FXML
    private TextField modPartNameTxt;

    @FXML
    private TextField modStockTxt;

    @FXML
    private TextField modPriceTxt;

    @FXML
    private TextField modMaxPartTxt;

    @FXML
    private TextField modSwitchTxt;

    @FXML
    private TextField modMinPartTxt;

    @FXML
    private TextField modCompanyTxt;

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


    private Inventory inventory;

    /**
     * Switch the bottom label to the correct value (Machine ID).
     *
     * @param event In house radio button is selected.
     */
    @FXML
    public void OnActionHandleInHouse(ActionEvent event) {
        isInHouse = true;
        modSwitchLbl.setText( "Machine ID: " );
    }


    /**
     * Switch the bottom label to the correct value (Company Name).
     *
     * @param event Out source radio button is selected.
     */
    @FXML
    public void OnActionHandleOutsource(ActionEvent event) {
        isInHouse = false;
        modSwitchLbl.setText( "Company Name: " );
    }


    /**
     * Updates the part's name, inventory level,price,maximum and minimum values,and company name or machine ID values into active text fields.
     * <p>
     * Automatically redirects user to the Main form after save.
     *
     * @param mouseEvent User clicks on the save button.
     * @throws IOException The data entered in the text fields are invalid.
     */
    @FXML
    public void onClickedSavePart(MouseEvent mouseEvent) throws IOException {
        try {
            int id = Integer.parseInt( modPartIdTxt.getText() );
            String name = modPartNameTxt.getText();
            int stock = Integer.parseInt( modStockTxt.getText() );
            double price = Double.parseDouble( modPartIdTxt.getText() );
            int min = Integer.parseInt( modMinPartTxt.getText() );
            int max = Integer.parseInt( modMaxPartTxt.getText() );
            String machineId = modSwitchTxt.getText();
            Part part = null;
            if (isInHouse) {
                int machineIdInt = Integer.parseInt( machineId );
                part = new InHouse( id, name, price, stock, min, max, machineIdInt );
            } else {
                String companyName = modSwitchTxt.getText();
                part = new OutSourced( id, name, price, stock, min, max, companyName );

            }
            partValidation( name, price, stock, min, max );
            Alert alert = new Alert( Alert.AlertType.CONFIRMATION );
            alert.setContentText( "Apply all changes?" );
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Inventory.updatePart( id, part );
            }

        } catch (NumberFormatException e) {
            handlePartErrorDialog( "machineId" );
        } finally {
            backToMain( mouseEvent );
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
                alert.setContentText( "Please enter a valid value for each text field" );
                alert.showAndWait();
        }
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
            Parent scene = FXMLLoader.load( getClass().getResource( "/view/ModifyPart.fxml" ) );
            stage.setScene( new Scene( scene ) );
            stage.show();
        }

    }


    /**
     * Set text fields to part details passed from main screen.
     *
     * @param part representation an existing part.
     */
    @FXML
    public void receivePart(Part part) {

        modPartIdTxt.setText( String.valueOf( part.getId() ) );
        modMaxPartTxt.setText( String.valueOf( part.getMax() ) );
        modMinPartTxt.setText( String.valueOf( part.getMin() ) );
        modPartNameTxt.setText( part.getName() );
        modPriceTxt.setText( String.valueOf( part.getPrice() ) );
        modStockTxt.setText( String.valueOf( part.getStock() ) );

        if (part instanceof InHouse) {
            modSwitchTxt.setText( String.valueOf( ((InHouse) part).getMachineId() ) );
            inHouseRbtn.setSelected( true );
            modSwitchLbl.setText( "Machine ID: " );

        } else {
            outSourcedRbtn.setSelected( true );
            modSwitchLbl.setText( "Company Name: " );
            modSwitchTxt.setText( ((OutSourced) part).getCompanyName() );

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
        } else if (name.isEmpty() || !name.matches( "^[a-zA-Z]*$" )) {
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
     * Redirects user to the main screen after clicking Cancel or saving a part.
     *
     * @param mouseEvent User clicks cancel button.
     * @throws IOException resources not available.
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
     * Initializes controller class.
     *
     * @param url            location.
     * @param resourceBundle resources.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


}
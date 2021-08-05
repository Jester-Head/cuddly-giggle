package main;


/*
 * @author Sarah Berg
 * @date 12/19/2020
 * @version 3.0
 *
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.*;

/**
 * An application that manages the creation, modification, and deletion of inventory items.
 */
public class InventoryProgram extends Application {

    public static void main(String[] args) {


        launch( args );
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Inventory inventory = new Inventory();
        addData( inventory );

        FXMLLoader loader = new FXMLLoader( getClass().getResource( "/view/MainScreen.fxml" ) );
        controller.MainScreen controller = new controller.MainScreen( inventory );
        loader.setController( controller );

        Parent root = loader.load();
        primaryStage.setTitle( "Inventory Management System" );
        primaryStage.setScene( new Scene( root, 900, 600 ) );
        primaryStage.show();


    }

    /**
     * This method adds inventory items for testing purposes
     *
     * @param inventory instance of inventory class,
     */
    public void addData(Inventory inventory) {

        //In House
        Part ih = new InHouse( 100, "Unicorn", 10.0, 70, 60, 100, 1230 );
        Part ih1 = new InHouse( 101, "Chain mail", 11.0, 71, 61, 101, 1231 );
        Part ih2 = new InHouse( 102, "Hepatitis C", 12.0, 72, 62, 102, 1232 );
        Part ih3 = new InHouse( 103, "Fade leaf", 14.0, 74, 64, 104, 1234 );

        Inventory.addPart( ih );
        Inventory.addPart( ih1 );
        Inventory.addPart( ih2 );
        Inventory.addPart( ih3 );


        //Out Sourced
        Part os = new OutSourced( 104, "Coffee", 10.0, 70, 60, 100, "Beards and Pasta" );
        Part os1 = new OutSourced( 105, "Cookies", 11.0, 71, 61, 101, "Wine and Pastries" );
        Part os2 = new OutSourced( 106, "Witches", 12.0, 72, 62, 102, "Fatty Boo boo" );
        Part os3 = new OutSourced( 107, "Sun", 13.0, 73, 63, 103, "Boodis Street" );


        Inventory.addPart( os );
        Inventory.addPart( os1 );
        Inventory.addPart( os2 );
        Inventory.addPart( os3 );


        //Products
        Product product1 = new Product( 1, "Discord", 49.99, 50, 1, 25 );
        Product product2 = new Product( 2, "Excalibur", 9000.00, 2, 1, 5 );
        Product product3 = new Product( 3, "Pepsi", 3.49, 200, 1, 25 );
        Product product4 = new Product( 4, "Fuzzy Socks", 1000.00, 3, 1, 5 );
        Product product5 = new Product( 5, "Majestic horn", 750.00, 5, 1, 10 );
        Product product6 = new Product( 6, "Ring of Power", 1000.00, 3, 1, 5 );
        Product product7 = new Product( 7, "Herbs and Spices", 55.00, 8, 1, 60 );
        Product product8 = new Product( 8, "Game", 9999, 1, 0, 2 );


        Inventory.addProduct( product1 );
        Inventory.addProduct( product2 );
        Inventory.addProduct( product3 );
        Inventory.addProduct( product4 );
        Inventory.addProduct( product5 );
        Inventory.addProduct( product6 );
        Inventory.addProduct( product7 );
        Inventory.addProduct( product8 );


    }

/*
   A feature I'd add to the next version would highlight the text fields containing invalid input instead of clearing all text fields.
   I'd also expand on the company information for outsourcing by adding forms and a table consisting of all the associated companies and their details.
 */
}
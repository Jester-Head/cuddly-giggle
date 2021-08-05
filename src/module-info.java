module InventoryProgram {
    requires javafx.fxml;
    requires javafx.controls;
    opens main;
    opens model;
    opens controller;
    opens view;
}
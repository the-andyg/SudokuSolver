module com.example.sudokusolverapplication {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.application to javafx.fxml;

    exports com.application;
    exports com.controller;
    exports com.model;
}
module com.example.fastwriting {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.fastwriting to javafx.fxml;
    opens com.example.fastwriting.controller to javafx.fxml;
    opens com.example.fastwriting.model to javafx.fxml;

    exports com.example.fastwriting;
    exports com.example.fastwriting.model;
}
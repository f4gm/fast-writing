module com.example.fastwriting {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.fastwriting to javafx.fxml;
    exports com.example.fastwriting;
}
module com.example.mini_twitter {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.mini_twitter to javafx.fxml;
    exports com.example.mini_twitter;
}
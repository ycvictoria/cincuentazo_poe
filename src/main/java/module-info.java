module org.example.miniproyecto3 {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.miniproyecto3 to javafx.fxml;
    exports org.example.miniproyecto3;
}
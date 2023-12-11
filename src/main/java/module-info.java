module com.led {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    opens com.led to javafx.fxml;
    exports com.led;
}
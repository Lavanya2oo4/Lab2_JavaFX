module com.lab2_lavanya {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.almasb.fxgl.all;
    requires java.sql;

    opens com.lab2_lavanya to javafx.fxml;
    exports com.lab2_lavanya;
}
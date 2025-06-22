module bd.edu.seu.mealmanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;

    opens bd.edu.seu.mealmanagementsystem.controller to javafx.fxml;

    opens bd.edu.seu.mealmanagementsystem.Model to javafx.base;

    exports bd.edu.seu.mealmanagementsystem;
}
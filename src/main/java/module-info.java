module bd.edu.seu.mealmanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens bd.edu.seu.mealmanagementsystem to javafx.fxml;
    exports bd.edu.seu.mealmanagementsystem;

    opens bd.edu.seu.mealmanagementsystem.controller to javafx.fxml;
    exports bd.edu.seu.mealmanagementsystem.controller;
}
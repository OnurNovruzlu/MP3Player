module coftea.learn {
    requires javafx.fxml;
    requires java.sql;
    requires javafx.media;
    requires javafx.controls;
    requires javafx.web;


    opens coftea.mp3 to javafx.fxml;
    exports coftea.mp3;
}
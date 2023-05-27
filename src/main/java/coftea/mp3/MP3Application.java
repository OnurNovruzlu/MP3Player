package coftea.mp3;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MP3Application extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/mp3.fxml")));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setTitle("MP3 Player");
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(s->{
            Platform.exit();
            System.exit(0);
        });
    }

    public static void main(String[] args) {
        launch();
    }
}
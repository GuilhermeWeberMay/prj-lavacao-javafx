package br.edu.ifsc.fln;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("hello-view.fxml"));
<<<<<<< Updated upstream
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Lavação dos Guri!");
=======
        Scene scene = new Scene(fxmlLoader.load(), 636, 630);
        stage.setTitle("Hello!");
>>>>>>> Stashed changes
        stage.setScene(scene);
        stage.show();
    }
}

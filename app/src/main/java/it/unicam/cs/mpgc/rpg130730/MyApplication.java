package it.unicam.cs.mpgc.rpg130730;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MyApplication extends Application {
    private final String APPLICATION_TITLE = "TestProject";

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("fxml_example.fxml"));
        Scene scene = new Scene(root, 300, 275);

        stage.setTitle(APPLICATION_TITLE);
        stage.setScene(scene);
        stage.show();
    }
}

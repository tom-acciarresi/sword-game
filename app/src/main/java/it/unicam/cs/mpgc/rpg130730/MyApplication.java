package it.unicam.cs.mpgc.rpg130730;

import javafx.application.Application;
import javafx.stage.Stage;

public class MyApplication extends Application {
    private final String APPLICATION_TITLE = "TestProject";

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle(APPLICATION_TITLE);
        primaryStage.show();
    }
}

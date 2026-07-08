package it.unicam.cs.mpgc.rpg130730.ui;

import it.unicam.cs.mpgc.rpg130730.AssetLibrary;
import it.unicam.cs.mpgc.rpg130730.Launcher;
import it.unicam.cs.mpgc.rpg130730.util.Vector2;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class MainMenu extends StackPane {
    public static final Vector2 MAIN_MENU_SIZE = new Vector2(
            Launcher.LEVEL_SIZE.x(),
            Launcher.LEVEL_SIZE.y() + GUI.GUI_SIZE.y());

    public MainMenu() {
        createBackground();
        createElements();
    }

    private void createBackground() {
        setPrefWidth(MAIN_MENU_SIZE.x());
        setPrefHeight(MAIN_MENU_SIZE.y());
        setStyle("-fx-background-color: black");
    }

    private void createElements() {
        this.getChildren().addAll(
                createTitle(),
                createNewGameButton(),
                createContinueButton());
    }

    private Text createTitle() {
        Vector2 OFFSET_FROM_CENTER = new Vector2(0, -100);
        Text title = new Text(Launcher.APPLICATION_TITLE);
        title.setFont(AssetLibrary.TITLE_FONT);
        title.setFill(Color.WHITE);
        title.setTranslateX(OFFSET_FROM_CENTER.x());
        title.setTranslateY(OFFSET_FROM_CENTER.y());
        return title;
    }

    private Button createNewGameButton() {
        Vector2 OFFSET_FROM_CENTER = new Vector2(0, 20);
        Vector2 BUTTON_SIZE = new Vector2(400, 64);
        Button button = new Button("New Game");
        button.setFont(AssetLibrary.GUI_FONT);
        button.setTranslateX(OFFSET_FROM_CENTER.x());
        button.setTranslateY(OFFSET_FROM_CENTER.y());
        button.setPrefWidth(BUTTON_SIZE.x());
        button.setPrefHeight(BUTTON_SIZE.y());
        button.setOnMouseClicked(e -> {
            newGame();
        });
        return button;
    }

    private Button createContinueButton() {
        Vector2 OFFSET_FROM_CENTER = new Vector2(0, 80);
        Vector2 BUTTON_SIZE = new Vector2(400, 64);
        Button button = new Button("Continue Game");
        button.setFont(AssetLibrary.GUI_FONT);
        button.setTranslateX(OFFSET_FROM_CENTER.x());
        button.setTranslateY(OFFSET_FROM_CENTER.y());
        button.setPrefWidth(BUTTON_SIZE.x());
        button.setPrefHeight(BUTTON_SIZE.y());
        button.setOnMouseClicked(e -> {
            continueGame();
        });
        return button;
    }

    private void newGame() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'newGame'");
    }

    private void continueGame() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'continueGame'");
    }
}

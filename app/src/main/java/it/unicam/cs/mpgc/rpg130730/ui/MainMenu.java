package it.unicam.cs.mpgc.rpg130730.ui;

import it.unicam.cs.mpgc.rpg130730.AssetLibrary;
import it.unicam.cs.mpgc.rpg130730.KeyBind;
import it.unicam.cs.mpgc.rpg130730.Launcher;
import it.unicam.cs.mpgc.rpg130730.persistence.SaveData;
import it.unicam.cs.mpgc.rpg130730.persistence.SaveSystem;
import it.unicam.cs.mpgc.rpg130730.util.datatypes.Vector2;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class MainMenu extends StackPane {
    // #region constants
    public static final Vector2 MAIN_MENU_SIZE = new Vector2(
            Launcher.LEVEL_SIZE.x(),
            Launcher.LEVEL_SIZE.y() + UI.GUI_SIZE.y());
    // #endregion

    // #region constructors
    public MainMenu() {
        createBackground();
        createElements();
        setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyBind.QUIT.keyCode()) {
                Launcher.quitWithoutSaving();
            }
        });
    }
    // #endregion

    private void createBackground() {
        setPrefWidth(MAIN_MENU_SIZE.x());
        setPrefHeight(MAIN_MENU_SIZE.y());
        setStyle("-fx-background-color: black");
    }

    private void createElements() {
        this.getChildren().addAll(
                createTitle(),
                createNewGameButton(),
                createContinueButton(),
                createAttribution());
    }

    private void newGame() {
        Launcher.getSceneManager().newGame();
        Launcher.getSceneManager().getChildren().remove(this);
    }

    private void continueGame() {
        SaveData savedata = SaveSystem.load();
        if (savedata == null) {
            newGame();
            return;
        }

        Launcher.getSceneManager().continueGame(savedata);
        Launcher.getSceneManager().getChildren().remove(this);
    }

    // #region create elements
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
        Vector2 BUTTON_SIZE = new Vector2(64 * 6, 64);
        Button button = new Button("New Game");
        button.setFont(AssetLibrary.GUI_FONT);
        button.setStyle("-fx-background-color: lightgray");
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
        Vector2 OFFSET_FROM_CENTER = new Vector2(0, 100);
        Vector2 BUTTON_SIZE = new Vector2(64 * 6, 64);
        Button button = new Button("Continue Game");
        button.setFont(AssetLibrary.GUI_FONT);
        button.setStyle("-fx-background-color: lightgray");
        button.setTranslateX(OFFSET_FROM_CENTER.x());
        button.setTranslateY(OFFSET_FROM_CENTER.y());
        button.setPrefWidth(BUTTON_SIZE.x());
        button.setPrefHeight(BUTTON_SIZE.y());
        button.setOnMouseClicked(e -> {
            continueGame();
        });
        return button;
    }

    private Text createAttribution() {
        Vector2 OFFSET_FROM_CENTER = new Vector2(280, 320);
        Text selfAdvertising = new Text("A game by Tom");
        selfAdvertising.setFont(AssetLibrary.TEXT_FONT);
        selfAdvertising.setFill(Color.LIGHTGRAY);
        selfAdvertising.setTranslateX(OFFSET_FROM_CENTER.x());
        selfAdvertising.setTranslateY(OFFSET_FROM_CENTER.y());
        return selfAdvertising;
    }
    // #endregion

}

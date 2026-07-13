package it.unicam.cs.mpgc.rpg130730.ui;

import it.unicam.cs.mpgc.rpg130730.Launcher;
import it.unicam.cs.mpgc.rpg130730.entities.Player;
import it.unicam.cs.mpgc.rpg130730.util.datatypes.Vector2;
import javafx.scene.layout.Pane;

public class UI extends Pane {
    public static final Vector2 GUI_SIZE = new Vector2(
            Launcher.LEVEL_SIZE.x(),
            64);

    private static final Vector2 HP_BAR_POS = new Vector2(624, 16);

    // #region constructors
    public UI(Player player) {
        createBackground();
        HealthBar healthBar = new HealthBar(player);
        healthBar.setLayoutX(HP_BAR_POS.x());
        healthBar.setLayoutY(HP_BAR_POS.y());
        this.getChildren().add(healthBar);
    }
    // #endregion

    private void createBackground() {
        setPrefWidth(GUI_SIZE.x());
        setPrefHeight(GUI_SIZE.y());
        setStyle("-fx-background-color: black");
    }
}

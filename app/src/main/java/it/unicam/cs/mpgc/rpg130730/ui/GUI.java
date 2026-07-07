package it.unicam.cs.mpgc.rpg130730.ui;

import it.unicam.cs.mpgc.rpg130730.Launcher;
import it.unicam.cs.mpgc.rpg130730.util.Vector2;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class GUI extends Pane {
    public static final int GUI_HEIGHT = 64;

    private static final Vector2 HP_BAR_POS = new Vector2(624, 16);
    private static final Vector2 HP_BAR_SIZE = new Vector2(128, 32);

    private Rectangle blackSlider;

    public GUI() {
        setPrefWidth(Launcher.WINDOW_SIZE.x());
        setPrefHeight(GUI_HEIGHT);
        setStyle("-fx-background-color: black");

        Rectangle redBar = new Rectangle(HP_BAR_SIZE.x(), HP_BAR_SIZE.y(), Color.RED);
        redBar.setX(HP_BAR_POS.x());
        redBar.setY(HP_BAR_POS.y());

        blackSlider = new Rectangle(HP_BAR_SIZE.x(), HP_BAR_SIZE.y(), Color.BLACK);
        blackSlider.setX(HP_BAR_POS.x() - HP_BAR_SIZE.x());
        blackSlider.setY(HP_BAR_POS.y());

        Vector2 TEXT_POS = new Vector2(574, 44);
        Text hpText = new Text(TEXT_POS.x(), TEXT_POS.y(), "HP");
        int FONT_SIZE = 32;
        hpText.setFont(Font.font("Fira Sans", FontWeight.BOLD, FONT_SIZE));
        hpText.setFill(Color.WHITE);

        getChildren().addAll(redBar, blackSlider, hpText);
    }

    public void updateBar(double percentage) {
        blackSlider.setX(HP_BAR_POS.x() - HP_BAR_SIZE.x() + (1 - percentage) * HP_BAR_SIZE.x());
    }
}

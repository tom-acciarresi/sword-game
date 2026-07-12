package it.unicam.cs.mpgc.rpg130730.ui;

import it.unicam.cs.mpgc.rpg130730.AssetLibrary;
import it.unicam.cs.mpgc.rpg130730.Launcher;
import it.unicam.cs.mpgc.rpg130730.GameLoop.Updatable;
import it.unicam.cs.mpgc.rpg130730.entities.Player;
import it.unicam.cs.mpgc.rpg130730.util.datatypes.Vector2;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class GUI extends Pane {
    public static final Vector2 GUI_SIZE = new Vector2(
            Launcher.LEVEL_SIZE.x(),
            64);

    private static final Vector2 HP_BAR_POS = new Vector2(624, 16);

    public GUI(Player p) {
        createBackground();
        HealthBar healthBar = new HealthBar(p);
        healthBar.setLayoutX(HP_BAR_POS.x());
        healthBar.setLayoutY(HP_BAR_POS.y());
        this.getChildren().add(healthBar);
    }

    private void createBackground() {
        setPrefWidth(GUI_SIZE.x());
        setPrefHeight(GUI_SIZE.y());
        setStyle("-fx-background-color: black");
    }

    private class HealthBar extends Group implements Updatable {
        private static final Vector2 HP_BAR_SIZE = new Vector2(128, 32);
        private Rectangle healthBarSlider = createHealthBar();

        private Player player;
        private double healthPreviousFrame;

        private HealthBar(Player p) {
            subscribeToUpdates();
            player = p;
            healthPreviousFrame = p.getHealth();
        }

        private Rectangle createHealthBar() {
            Rectangle slidingRedBar = createSlidingRedBar();
            Rectangle blackBar = createBlackBar();
            Text hpText = createText();

            this.getChildren().addAll(slidingRedBar, blackBar, hpText);
            return slidingRedBar;
        }

        // Red bar that slides according to HP
        private Rectangle createSlidingRedBar() {
            Rectangle slidingRedBar = new Rectangle(HP_BAR_SIZE.x(), HP_BAR_SIZE.y(), Color.RED);
            slidingRedBar.setX(-HP_BAR_SIZE.x());
            // Fill to 100%
            slidingRedBar.setTranslateX(HP_BAR_SIZE.x());
            return slidingRedBar;
        }

        // Black bar that hides missing part of HP bar
        private Rectangle createBlackBar() {
            Rectangle blackBar = new Rectangle(HP_BAR_SIZE.x(), HP_BAR_SIZE.y(), Color.BLACK);
            blackBar.setX(-HP_BAR_SIZE.x());
            return blackBar;
        }

        // Text
        private Text createText() {
            Vector2 TEXT_POS = new Vector2(-50, 28);
            Text hpText = new Text(TEXT_POS.x(), TEXT_POS.y(), "HP");
            hpText.setFont(AssetLibrary.GUI_FONT);
            hpText.setFill(Color.WHITE);
            return hpText;
        }

        @Override
        public void update(double timeDelta) {
            double healthThisFrame = player.getHealth();
            if (healthPreviousFrame != healthThisFrame) {
                updateBar(healthThisFrame / Player.DEFAULT_PLAYER_HEALTH);
            }
            healthPreviousFrame = healthThisFrame;
        }

        private void updateBar(double percentage) {
            // Clamp between 0 and 1
            percentage = percentage > 1 ? 1 : (percentage < 0 ? 0 : percentage);

            healthBarSlider.setTranslateX(percentage * HP_BAR_SIZE.x());
        }
    }
}

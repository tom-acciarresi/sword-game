package it.unicam.cs.mpgc.rpg130730.ui;

import it.unicam.cs.mpgc.rpg130730.AssetLibrary;
import it.unicam.cs.mpgc.rpg130730.Launcher;
import it.unicam.cs.mpgc.rpg130730.GameLoop.Updatable;
import it.unicam.cs.mpgc.rpg130730.entities.Player;
import it.unicam.cs.mpgc.rpg130730.util.Vector2;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public final class GUI extends Pane {
    public static final Vector2 GUI_SIZE = new Vector2(
            Launcher.LEVEL_SIZE.x(),
            64);

    private HealthBar hb;

    public GUI(Player p) {
        createBackground();
        hb = new HealthBar(p);
    }

    private void createBackground() {
        setPrefWidth(GUI_SIZE.x());
        setPrefHeight(GUI_SIZE.y());
        setStyle("-fx-background-color: black");
    }

    public HealthBar getHealthBar() {
        return hb;
    }

    public class HealthBar implements Updatable {
        private static final Vector2 HP_BAR_POS = new Vector2(624, 16);
        private static final Vector2 HP_BAR_SIZE = new Vector2(128, 32);
        private Rectangle healthBarSlider = createHealthBar();

        private Player player;
        private double healthPreviousFrame;

        public HealthBar(Player p) {
            subscribeToUpdates();
            player = p;
            healthPreviousFrame = p.getHealth();
        }

        private Rectangle createHealthBar() {
            Rectangle slidingRedBar = createSlidingRedBar();
            Rectangle blackBar = createBlackBar();
            Text hpText = createText();

            getChildren().addAll(slidingRedBar, blackBar, hpText);
            return slidingRedBar;
        }

        // Red bar that slides according to HP
        private Rectangle createSlidingRedBar() {
            Rectangle slidingRedBar = new Rectangle(HP_BAR_SIZE.x(), HP_BAR_SIZE.y(), Color.RED);
            slidingRedBar.setX(HP_BAR_POS.x() - HP_BAR_SIZE.x());
            slidingRedBar.setY(HP_BAR_POS.y());
            slidingRedBar.setTranslateX(HP_BAR_SIZE.x());
            return slidingRedBar;
        }

        // Black bar that hides missing part of HP bar
        private Rectangle createBlackBar() {
            Rectangle blackBar = new Rectangle(HP_BAR_SIZE.x(), HP_BAR_SIZE.y(), Color.BLACK);
            blackBar.setX(HP_BAR_POS.x() - HP_BAR_SIZE.x());
            blackBar.setY(HP_BAR_POS.y());
            return blackBar;
        }

        // Text
        private Text createText() {
            Vector2 TEXT_POS = new Vector2(574, 44);
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

        public void updateBar(double percentage) {
            // Clamp between 0 and 1
            if (percentage < 0)
                percentage = 0;
            if (percentage > 1)
                percentage = 1;

            healthBarSlider.setTranslateX(percentage * HP_BAR_SIZE.x());
        }
    }
}

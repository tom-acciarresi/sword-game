package it.unicam.cs.mpgc.rpg130730.util.datatypes;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class Area2D extends Rectangle {
    private VoidConsumer onAreaExitedProperty = new VoidConsumer() {
    };
    private VoidConsumer onAreaEnteredProperty = new VoidConsumer() {
    };

    // #region Constructors
    Area2D() {
        super();
    }

    public Area2D(double width, double height) {
        super(width, height);
    }

    public Area2D(double width, double height, Paint fill) {
        super(width, height, fill);
    }

    public Area2D(double x, double y, double width, double height) {
        super(x, y, width, height);
    }
    // #endregion

    public void setOnAreaEntered(VoidConsumer func) {
        onAreaEnteredProperty = func;
    }

    public void setOnAreaExited(VoidConsumer func) {
        onAreaExitedProperty = func;
    }

    public void areaEntered() {
        onAreaEnteredProperty.run();
    }

    public void areaExited() {
        onAreaExitedProperty.run();
    }
}

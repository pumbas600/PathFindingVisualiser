package nz.pumbas.Utilities;

import javafx.scene.paint.Color;

public enum Tag {
    NONE(GlobalConstants.DEFAULT_NODE_COLOUR),
    START(GlobalConstants.START_NODE_COLOUR),
    END(GlobalConstants.END_NODE_COLOUR),
    BARRIER(GlobalConstants.BARRIER_COLOUR);

    public Color colour;
    Tag(Color color) {
        this.colour = color;
    }
}

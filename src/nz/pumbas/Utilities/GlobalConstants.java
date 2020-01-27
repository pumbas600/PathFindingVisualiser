package nz.pumbas.Utilities;

import javafx.scene.paint.Color;
import javafx.stage.Screen;

public class GlobalConstants {
    public static double SCREEN_WIDTH = Screen.getPrimary().getBounds().getWidth();
    public static double SCREEN_HEIGHT = Screen.getPrimary().getVisualBounds().getHeight();
    public static double TOP_BAR_HEIGHT = 40d;

    public static int WIDTH = 30;
    public static int HEIGHT = 20;
    public static int TILE_SIZE = 35;
    public static float MIN_TILE_SIZE = 10;
    public static boolean CAN_MOVE_DIAGONALLY = false;

    public static Color BARRIER_COLOUR = Color.BLACK;
    public static Color DEFAULT_NODE_COLOUR = Color.WHITE;
    public static Color END_NODE_COLOUR = Color.RED;
    public static Color START_NODE_COLOUR = Color.GREEN;
    public static Color PATH_COLOUR = Color.MEDIUMPURPLE;
    public static Color REVEALED_NODE_COLOUR = Color.BLUE;
}

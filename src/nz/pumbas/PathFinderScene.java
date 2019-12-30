package nz.pumbas;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import nz.pumbas.Utilities.Node;
import nz.pumbas.Utilities.Tag;
import nz.pumbas.Utilities.Vector;

import java.awt.image.BufferedImage;

public class PathFinderScene {

    public static int WIDTH = 30;
    public static int HEIGHT = 20;
    public static int TILE_SIZE = 35;

    private Scene pathFinderScene;
    private Scene menuScene;
    private Node[][] nodeGrid;

    public PathFinderScene(Scene menuScene, int width, int height, int tileSize) {
        this.menuScene = menuScene;
        WIDTH = width;
        HEIGHT = height;
        TILE_SIZE = tileSize;
    }

    public void initialisePathFinderScene() {
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(5));

        nodeGrid = new Node[WIDTH][HEIGHT];

        GridPane grid = new GridPane();
        setupGrid(grid, nodeGrid);
        grid.setGridLinesVisible(true);
        borderPane.setBottom(grid);

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(5));
        hBox.setSpacing(10d);
        hBox.setMinWidth(WIDTH * TILE_SIZE);
        hBox.setMinHeight(40d);
        borderPane.setTop(hBox);

        new InputManager(hBox, grid, nodeGrid);

        pathFinderScene = new Scene(borderPane);
    }

    public void initialiseImageGrid(BufferedImage image) {
        System.out.println("Initialising image");
        initialisePathFinderScene();

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                if (new java.awt.Color(image.getRGB(x, y), true).equals(java.awt.Color.BLACK)) {
                    nodeGrid[x][y].setColour(Color.GRAY);
                    nodeGrid[x][y].setTag(Tag.BARRIER);
                }
            }
        }

    }

    private void setupGrid(GridPane grid, Node[][] nodeGrid) {

        for (int x = 0; x < nodeGrid.length; x++) {
            for (int y = 0; y < nodeGrid[x].length; y++) {
                Node node = new Node(new Vector(x, y), Color.WHITE);
                nodeGrid[x][y] = node;
                grid.add(node.getRectangle(), x, y);
            }
        }
    }

    public Scene getPathFinderScene() {
        return pathFinderScene;
    }
}

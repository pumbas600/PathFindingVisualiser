package nz.pumbas.DifferentScenes;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import nz.pumbas.InputManager;
import nz.pumbas.MazeGeneration.MazeGenerator;
import nz.pumbas.Utilities.GlobalConstants;
import nz.pumbas.Utilities.Node;
import nz.pumbas.Utilities.Tag;
import nz.pumbas.Utilities.Vector;

import java.awt.image.BufferedImage;

public class PathFinderScene implements CustomScene {

    public static double PathFinderOptionBarSize = 40d;

    private Scene scene;
    private Node[][] nodeGrid;

    public PathFinderScene(int width, int height, int tileSize, boolean generateRandomMaze) {
        GlobalConstants.WIDTH = width;
        GlobalConstants.HEIGHT = height;
        double maxVerticalHeight = GlobalConstants.SCREEN_HEIGHT - GlobalConstants.TOP_BAR_HEIGHT - PathFinderOptionBarSize;
        double maxHorizonalWidth = GlobalConstants.SCREEN_WIDTH;
        GlobalConstants.TILE_SIZE = (int) Math.min(tileSize,
                Math.min(maxHorizonalWidth / GlobalConstants.WIDTH, maxVerticalHeight / GlobalConstants.HEIGHT));

        initialisePathFinderScene(generateRandomMaze);
        if (generateRandomMaze) {
            MazeGenerator.GenerateMaze(nodeGrid);
        }
    }

    public PathFinderScene(BufferedImage image) {
        GlobalConstants.WIDTH = image.getWidth();
        GlobalConstants.HEIGHT = image.getHeight();
        double maxVerticalHeight = GlobalConstants.SCREEN_HEIGHT - GlobalConstants.TOP_BAR_HEIGHT - PathFinderOptionBarSize;
        double maxHorizonalWidth = GlobalConstants.SCREEN_WIDTH;
        GlobalConstants.TILE_SIZE = (int) Math.min(maxHorizonalWidth / GlobalConstants.WIDTH, maxVerticalHeight / GlobalConstants.HEIGHT);

        initialiseImageGrid(image);
    }

    private void initialisePathFinderScene(boolean generateRandomMaze) {
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(5));

        GridPane grid = new GridPane();
        nodeGrid = setupGrid(grid, generateRandomMaze);
        grid.setGridLinesVisible(true);
        borderPane.setBottom(grid);

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(5));
        hBox.setSpacing(10d);
        hBox.setMinWidth(GlobalConstants.WIDTH * GlobalConstants.TILE_SIZE);
        hBox.setMinHeight(PathFinderOptionBarSize);
        borderPane.setTop(hBox);

        scene = new Scene(borderPane);
        new InputManager(hBox, grid, nodeGrid);
    }

    /*
    Loops through each pixel of the image and if that pixel if black, it sets the corresponding
    node on the nodeGrid to a barrier.
     */

    private void initialiseImageGrid(BufferedImage image) {
        //You cannot have both an imported image scene and a randomly generated maze.
        initialisePathFinderScene(false);

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                if (new java.awt.Color(image.getRGB(x, y), true).equals(java.awt.Color.BLACK)) {
                    nodeGrid[x][y].setTag(Tag.BARRIER);
                }
            }
        }
    }

    private Node[][] setupGrid(GridPane grid, boolean generateRandomMaze) {
        Node[][] nodeGrid = new Node[GlobalConstants.WIDTH][GlobalConstants.HEIGHT];
        for (int x = 0; x < nodeGrid.length; x++) {
            for (int y = 0; y < nodeGrid[x].length; y++) {
                Node node = new Node(new Vector(x, y));

                //The Maze Algorithm starts with a grid full of barriers, and works backwards to carve out the paths.
                if (generateRandomMaze) node.setTag(Tag.BARRIER);

                nodeGrid[x][y] = node;
                grid.add(node.getRectangle(), x, y);
            }
        }
        return nodeGrid;
    }

    @Override
    public Scene getScene() {
        return scene;
    }
}

package nz.pumbas;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.*;
import nz.pumbas.DifferentScenes.MenuScene;
import nz.pumbas.DifferentScenes.SceneController;
import nz.pumbas.PathFinders.AStarPathFinder;
import nz.pumbas.PathFinders.DijkstraPathFinding;
import nz.pumbas.PathFinders.PathFinder;
import nz.pumbas.Utilities.*;

public class InputManager {

    private Tag placeMode = Tag.START;
    private Node[][] nodeGrid;
    private Node endNode;
    private Node startNode;
    private boolean begunPathFinding = false;
    private PathFinder pathFinder;
    private GridPane grid;

    public ScrollBar speedScrollBar;
    public Label NodeComparisons;

    private static InputManager instance;

    public InputManager(BorderPane topBar, GridPane grid, Node[][] nodeGrid) {
        this.grid = grid;
        this.nodeGrid = nodeGrid;
        instance = this;

        speedScrollBar = new ScrollBar();
        speedScrollBar.setMin(1);
        speedScrollBar.setMax(60);
        speedScrollBar.setValue(15);

        Button startNodeButton = new Button("Start Tile");
        Button addBarrier = new Button("Add Barrier");
        Button endNodeButton = new Button("End Tile");
        Button beginSearch = new Button("Begin Search");

        ComboBox<String> pathFinderComboBox = new ComboBox<>();
        pathFinderComboBox.getItems().addAll("A*", "Dijkstra");
        pathFinderComboBox.setValue("A*");

        startNodeButton.setOnMouseClicked(event -> placeMode = Tag.START);
        addBarrier.setOnMouseClicked(event -> placeMode = Tag.BARRIER);
        endNodeButton.setOnMouseClicked(event -> placeMode = Tag.END);

        beginSearch.setOnMouseClicked(event -> {
            if (startNode == null || endNode == null || pathFinderComboBox.getValue() == null) return;
            pathFinder = determinePathFinder(pathFinderComboBox.getValue());
            if (pathFinder == null) return;

            startNodeButton.setDisable(true);
            addBarrier.setDisable(true);
            endNodeButton.setDisable(true);
            pathFinderComboBox.setDisable(true);
            beginSearch.setDisable(true);
            begunPathFinding = true;

            pathFinder.beginPathFinding(startNode, endNode);
        });


        MenuButton menuButton = new MenuButton("More");
        MenuItem backMenuItem = new MenuItem("Back");
        MenuItem resetMentItem = new MenuItem("Reset");
        MenuItem saveMenuItem = new MenuItem("Save");

        backMenuItem.setOnAction(event -> SceneController.changeSceneTo(new MenuScene()));
        resetMentItem.setOnAction(event -> {
            pathFinder.setStopped();
            pathFinder = null;
            for (int x = 0; x < GlobalConstants.WIDTH; x++) {
                for (int y = 0; y < GlobalConstants.HEIGHT; y++) {
                    Node node = nodeGrid[x][y];
                    if (node.getTag() != Tag.BARRIER) {
                        node.setTag(Tag.NONE);
                        Label label = node.getLabel();
                        if (label != null) label.setText("");
                        node.gCost = Double.POSITIVE_INFINITY;
                        node.hCost = 0;
                    }
                }
            }
            startNode = null;
            endNode = null;
            startNodeButton.setDisable(false);
            addBarrier.setDisable(false);
            endNodeButton.setDisable(false);
            pathFinderComboBox.setDisable(false);
            beginSearch.setDisable(false);
            begunPathFinding = false;
            NodeComparisons.setText("0");
            placeMode = Tag.START;
        });
        saveMenuItem.setOnAction(event -> IOManager.saveNodeGridAsImage(nodeGrid));

        menuButton.getItems().addAll(backMenuItem, new SeparatorMenuItem(), resetMentItem, saveMenuItem);

        NodeComparisons = new Label("0");

        HBox leftSide = new HBox();
        leftSide.setSpacing(8d);
        leftSide.setMinWidth(GlobalConstants.WIDTH * GlobalConstants.TILE_SIZE / 4d);
        leftSide.setAlignment(Pos.CENTER_LEFT);
        leftSide.getChildren().addAll(menuButton, UtilityFunctions.horizontalSpace(15), new Label("Speed:"), speedScrollBar);

        HBox middle = new HBox();
        middle.setSpacing(8d);
        middle.setMinWidth(GlobalConstants.WIDTH * GlobalConstants.TILE_SIZE / 3d);
        middle.setAlignment(Pos.CENTER);
        middle.getChildren().addAll(startNodeButton, addBarrier, endNodeButton);

        HBox rightSide = new HBox();
        rightSide.setSpacing(8d);
        rightSide.setMinWidth(GlobalConstants.WIDTH * GlobalConstants.TILE_SIZE / 4d);
        rightSide.setAlignment(Pos.CENTER_RIGHT);
        rightSide.getChildren().addAll(new Label("Algorithm"), pathFinderComboBox, beginSearch,
                UtilityFunctions.horizontalSpace(15), new Label("Node Comparisons:"), NodeComparisons);

        topBar.setLeft(leftSide);
        topBar.setCenter(middle);
        topBar.setRight(rightSide);

        grid.setOnMouseClicked(event -> {
            if (begunPathFinding) return;
            int x = (int)(event.getX() / GlobalConstants.TILE_SIZE);
            int y = (int)(event.getY() / GlobalConstants.TILE_SIZE);

            if (x < 0 || x >= GlobalConstants.WIDTH || y < 0 || y >= GlobalConstants.HEIGHT) return;

            switch (placeMode) {
                case BARRIER:
                    if (nodeGrid[x][y].getTag() == Tag.BARRIER) removeBarrier(x, y);
                    else addBarrier(x, y);
                    break;

                case START:
                    setStartNode(x, y);
                    break;

                case END:
                    setEndNode(x, y);
            }
        });
    }

    public static InputManager getInstance(){
        return instance;
    }

    private PathFinder determinePathFinder(String pathFinder) {
        switch (pathFinder) {
            case "A*":
                return new AStarPathFinder(grid, nodeGrid, speedScrollBar);
            case "Dijkstra":
                return new DijkstraPathFinding(grid, nodeGrid, speedScrollBar);
            default:
                return null;
        }
    }

    private void setEndNode(int x, int y) {
        if (endNode != null) {
            Node gridNode = nodeGrid[endNode.getX()][endNode.getY()];
            gridNode.setTag(Tag.NONE);
        }
        endNode = nodeGrid[x][y];
        endNode.setTag(Tag.END);
    }

    private void setStartNode(int x, int y) {
        if (startNode != null) {
            Node gridNode = nodeGrid[startNode.getX()][startNode.getY()];
            gridNode.setTag(Tag.NONE);
        }
        startNode = nodeGrid[x][y];
        startNode.setTag(Tag.START);
    }

    private void addBarrier(int x, int y) {
        Node gridNode = nodeGrid[x][y];
        if (gridNode != startNode && gridNode != endNode) {
            gridNode.setTag(Tag.BARRIER);
        }
    }

    private void removeBarrier(int x, int y) {
        Node gridNode = nodeGrid[x][y];
        gridNode.setTag(Tag.NONE);

    }

}

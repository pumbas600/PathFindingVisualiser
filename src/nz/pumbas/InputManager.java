package nz.pumbas;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import nz.pumbas.Utilities.Tag;
import nz.pumbas.Utilities.Node;

public class InputManager {

    private GridPane grid;
    private Tag placeMode = Tag.START;
    private PathFinder pathFinder;
    private Node[][] nodeGrid;
    private Node endNode;
    private Node startNode;

    public InputManager(HBox hbox, GridPane grid, Node[][] nodeGrid) {
        this.grid = grid;
        this.nodeGrid = nodeGrid;

        Button back = new Button("Back");
        back.setOnMouseClicked(event -> {
            Main.setMenuScene();
        });

        ScrollBar speedScrollBar = new ScrollBar();
        speedScrollBar.setMin(1);
        speedScrollBar.setMax(60);
        speedScrollBar.setValue(15);

        this.pathFinder = new PathFinder(grid, speedScrollBar);

        Button startNodeButton = new Button("Start Tile");
        Button addBarrier = new Button("Add Barrier");
        Button endNodeButton = new Button("End Tile");
        Button beginSearch = new Button("Begin Search");

        startNodeButton.setOnMouseClicked(event -> placeMode = Tag.START);
        addBarrier.setOnMouseClicked(event -> placeMode = Tag.BARRIER);
        endNodeButton.setOnMouseClicked(event -> placeMode = Tag.END);

        beginSearch.setOnMouseClicked(event -> {
            if (startNode == null || endNode == null) return;

            startNodeButton.setDisable(true);
            addBarrier.setDisable(true);
            endNodeButton.setDisable(true);
            pathFinder.beginPathFinding(nodeGrid, startNode, endNode);
            beginSearch.setDisable(true);
        });

        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().addAll(back, speedScrollBar, startNodeButton, addBarrier, endNodeButton, beginSearch);

        grid.setOnMouseClicked(event -> {
            int x = (int)(event.getX() / PathFinderScene.TILE_SIZE);
            int y = (int)(event.getY() / PathFinderScene.TILE_SIZE);

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

    private void setEndNode(int x, int y) {
        if (endNode != null) {
            Node gridNode = nodeGrid[endNode.getX()][endNode.getY()];
            gridNode.setColour(Color.WHITE);
        }
        endNode = nodeGrid[x][y];
        endNode.setColour(Color.RED);
        if (endNode.getTag() == Tag.BARRIER) endNode.setTag(Tag.NONE);
    }

    private void setStartNode(int x, int y) {
        if (startNode != null) {
            Node gridNode = nodeGrid[startNode.getX()][startNode.getY()];
            gridNode.setColour(Color.WHITE);
        }
        startNode = nodeGrid[x][y];
        startNode.setColour(Color.GREEN);
        if (startNode.getTag() == Tag.BARRIER) startNode.setTag(Tag.NONE);
    }

    private void addBarrier(int x, int y) {
        Node gridNode = nodeGrid[x][y];
        if (gridNode != startNode && gridNode != endNode) {
            gridNode.setTag(Tag.BARRIER);
            gridNode.setColour(Color.GRAY);
        }
    }

    private void removeBarrier(int x, int y) {
        Node gridNode = nodeGrid[x][y];
        gridNode.setColour(Color.WHITE);
        gridNode.setTag(Tag.NONE);

    }

}

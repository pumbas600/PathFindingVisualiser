package nz.pumbas;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import nz.pumbas.DifferentScenes.MenuScene;
import nz.pumbas.DifferentScenes.SceneController;
import nz.pumbas.Utilities.GlobalConstants;
import nz.pumbas.Utilities.Tag;
import nz.pumbas.Utilities.Node;

public class InputManager {

    private Tag placeMode = Tag.START;
    private PathFinder pathFinder;
    private Node[][] nodeGrid;
    private Node endNode;
    private Node startNode;
    private boolean begunPathFinding = false;

    public InputManager(HBox hbox, GridPane grid, Node[][] nodeGrid) {
        this.nodeGrid = nodeGrid;

        Button back = new Button("Back");
        back.setOnMouseClicked(event -> SceneController.changeSceneTo(new MenuScene()));

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
            begunPathFinding = true;
            pathFinder.beginPathFinding(nodeGrid, startNode, endNode);
            beginSearch.setDisable(true);
        });

        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().addAll(back, speedScrollBar, startNodeButton, addBarrier, endNodeButton, beginSearch);

        grid.setOnMouseClicked(event -> {
            if (begunPathFinding) return;
            int x = (int)(event.getX() / GlobalConstants.TILE_SIZE);
            int y = (int)(event.getY() / GlobalConstants.TILE_SIZE);

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

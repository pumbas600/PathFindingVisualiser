package nz.pumbas.PathFinders;

import javafx.animation.AnimationTimer;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.GridPane;
import nz.pumbas.InputManager;
import nz.pumbas.Utilities.GlobalConstants;
import nz.pumbas.Utilities.Node;
import nz.pumbas.Utilities.Tag;

import java.util.ArrayList;
import java.util.Collections;

public abstract class PathFinder {
    protected GridPane grid;
    protected ScrollBar speedScrollBar;
    protected Label checkedNodes;

    protected Node startNode;
    protected Node endNode;
    protected Node[][] nodeGrid;

    protected int FPS = 60;
    protected AnimationTimer mainLoop;
    protected int frame = 0;

    protected int checkedNodesCount = 0;

    public PathFinder(GridPane grid, Node[][] nodeGrid, ScrollBar speedScrollBar) {
        this.grid = grid;
        this.nodeGrid = nodeGrid;
        this.speedScrollBar = speedScrollBar;
        this.checkedNodes = InputManager.getInstance().NodeComparisons;
    }

    public abstract void beginPathFinding(Node startNode, Node endNode);
    abstract void pathFinding();

    protected void startMainLoop() {
        (mainLoop = new AnimationTimer()
        {
            @Override
            public void handle(long currentNanoTime)
            {
                frame++;
                if (frame % (FPS / (int) speedScrollBar.getValue()) == 0) {
                    checkedNodes.setText(String.valueOf(checkedNodesCount));
                    pathFinding();
                }
            }
        }).start();
    }

    public void setStopped() {
        mainLoop.stop();
    }

    protected ArrayList<Node> getNeighbours(Node node) {
        ArrayList<Node> neighbours = new ArrayList<>();
        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                if (x == 0 && y == 0) continue;

                //The absolute difference of x and y will always be 1 if they're directly connected.
                if (!GlobalConstants.CAN_MOVE_DIAGONALLY && (Math.abs(x - y) != 1)) continue;

                int xPos = x + node.getX();
                int yPos = y + node.getY();

                if (xPos < 0 || xPos >= GlobalConstants.WIDTH || yPos < 0 || yPos >= GlobalConstants.HEIGHT) continue;
                Node gridNode = nodeGrid[xPos][yPos];
                if (gridNode.getTag() != Tag.BARRIER)
                    neighbours.add(gridNode);
            }
        }

        return neighbours;
    }

    protected boolean isDiagonal(Node current, Node neighbour) {
        return current.getPos().getSquaredDist(neighbour.getPos()) != 1;
    }

    protected void retracePath() {
        Node currentNode = endNode.cameFrom;
        ArrayList<Node> path = new ArrayList<>();
        while (currentNode != startNode && currentNode != null) {
            path.add(currentNode);
            currentNode = currentNode.cameFrom;
        }

        Collections.reverse(path);
        for (Node node : path) {
            node.setColour(GlobalConstants.PATH_COLOUR);
        }
    }
}

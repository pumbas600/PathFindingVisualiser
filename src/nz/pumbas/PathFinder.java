package nz.pumbas;

import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import nz.pumbas.Utilities.HeapFiles.Heap;
import nz.pumbas.Utilities.Node;
import nz.pumbas.Utilities.Tag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class PathFinder {

    public boolean canMoveDiagonally = true;

    private Node startNode;
    private Node endNode;
    private Node[][] nodeGrid;
    private GridPane grid;

    //A* Variables:
    private Heap<Node> openSet = new Heap<>(Node.class, PathFinderScene.WIDTH * PathFinderScene.HEIGHT);
    private HashSet<Node> closedSet = new HashSet<>();

    private static Color evaluatedNodeColour = Color.DARKBLUE;
    private static Color revealedNodeColour = Color.BLUE;
    private static Color pathNodeColour = Color.BLACK;

    private AnimationTimer mainLoop;
    public static int SPEED = 20; //Nodes per second;
    private static int FPS = 60;
    private int frame = 0;
    private ScrollBar speedScrollBar;

    PathFinder(GridPane grid, ScrollBar speedScrollBar) {
        this.grid = grid;
        this.speedScrollBar = speedScrollBar;
    }


    public void beginPathFinding(Node[][] nodeGrid, Node startNode, Node endNode) {
        this.nodeGrid = nodeGrid;
        this.startNode = startNode;
        this.endNode = endNode;

        openSet.addItem(startNode);
        startNode.gCost = 0;
        startNode.setHCost(endNode);

        (mainLoop = new AnimationTimer()
        {
            @Override
            public void handle(long currentNanoTime)
            {
                frame++;
                if (frame % (FPS / (int) speedScrollBar.getValue()) == 0)
                    pathFinding();
            }
        }).start();
    }

    private void pathFinding() {
        if (!openSet.isEmpty()) {

            Node current = openSet.removeFirstItem();

            closedSet.add(current);

            if (current == endNode) {
                retracePath();
                mainLoop.stop(); //Stop this loop
                return;
            }
            if(current != startNode) current.setColour(evaluatedNodeColour);

            for (Node neighbour : getNeighbours(current)) {
                if (closedSet.contains(neighbour)) continue;
                double distanceCost = (canMoveDiagonally && isDiagonal(current, neighbour)) ? 1.4d : 1d;

                double newGCost = current.gCost + distanceCost;
                if (newGCost < neighbour.gCost) {
                    neighbour.cameFrom = current;
                    neighbour.gCost = newGCost;

                    if (!openSet.contains(neighbour)) {
                        neighbour.setHCost(endNode);
                        openSet.addItem(neighbour);
                        if (neighbour != endNode) neighbour.setColour(revealedNodeColour);
                    }
                    else {
                        openSet.updateItem(neighbour);
                    }
                    //Only create a new label if one doesn't exist - This prevents new fcosts being displayed over the top of the previous one.
                    if (neighbour.getLabel() == null) {
                        Label fCostLabel = new Label(String.valueOf(neighbour.fCost()));
                        fCostLabel.setStyle("-fx-text-fill: white; -fx-font-size: " + PathFinderScene.TILE_SIZE * 0.35);
                        fCostLabel.setAlignment(Pos.CENTER);
                        neighbour.setLabel(fCostLabel);
                        grid.add(fCostLabel, neighbour.getX(), neighbour.getY());
                    }
                    else {
                        neighbour.getLabel().setText(String.valueOf(neighbour.fCost()));
                    }
                }
            }
            return;
        }
        System.out.println("No path could be found!");
        mainLoop.stop();
    }

    private ArrayList<Node> getNeighbours(Node node) {
        ArrayList<Node> neighbours = new ArrayList<>();
        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                if (x == 0 && y == 0) continue;

                if (!canMoveDiagonally && (Math.abs(x - y) != 1)) continue;

                int xPos = x + node.getX();
                int yPos = y + node.getY();

                if (xPos < 0 || xPos >= PathFinderScene.WIDTH || yPos < 0 || yPos >= PathFinderScene.HEIGHT) continue;
                Node gridNode = nodeGrid[xPos][yPos];
                if (gridNode.getTag() != Tag.BARRIER)
                    neighbours.add(gridNode);
            }
        }

        return neighbours;
    }

    private boolean isDiagonal(Node current, Node neighbour) {
        return current.getPos().getSquaredDist(neighbour.getPos()) != 1;
    }

    private void retracePath() {
        Node currentNode = endNode.cameFrom;
        ArrayList<Node> path = new ArrayList<>();
        while (currentNode != startNode) {
            path.add(currentNode);
            currentNode = currentNode.cameFrom;
        }

        Collections.reverse(path);
        for (Node node : path) {
            node.setColour(pathNodeColour);
        }
    }
}

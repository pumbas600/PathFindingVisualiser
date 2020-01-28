package nz.pumbas.PathFinders;

import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import nz.pumbas.Utilities.GlobalConstants;
import nz.pumbas.Utilities.HeapFiles.Heap;
import nz.pumbas.Utilities.Node;
import nz.pumbas.Utilities.UtilityFunctions;

import java.util.HashSet;

public class AStarPathFinder extends PathFinder {

    //A* Variables:
    private Heap<Node> openSet = new Heap<>(Node.class, GlobalConstants.WIDTH * GlobalConstants.HEIGHT);
    private HashSet<Node> closedSet = new HashSet<>();

    public AStarPathFinder(GridPane grid, Node[][] nodeGrid, ScrollBar speedScrollBar) {
        super(grid, nodeGrid, speedScrollBar);
    }

    @Override
    public void beginPathFinding(Node startNode, Node endNode) {
        this.startNode = startNode;
        this.endNode = endNode;

        openSet.addItem(startNode);
        startNode.gCost = 0;
        startNode.setHCost(endNode);

        startMainLoop();
    }

    @Override
    void pathFinding() {
        if (!openSet.isEmpty()) {
            //Gets the node with the lowest fCost - If the fCost for two nodes is the same, it gets
            //the node closest to the end.
            Node current = openSet.removeFirstItem();
            closedSet.add(current);

            //If the current node is the end node, then the fastest path has been found.
            if (current == endNode) {
                retracePath();
                mainLoop.stop(); //Stop this loop
                return;
            }
            //Maps a colour of hue between 0 and 333 based on how close the current node is to the end node.
            if(current != startNode) current.setColour(Color.hsb(
                    UtilityFunctions.mapBetween(current.hCost, 0, startNode.getPos().getDist(endNode.getPos()), 0, 330),
                    0.7f, 0.8f));

            for (Node neighbour : getNeighbours(current)) {
                if (closedSet.contains(neighbour)) continue;
                checkedNodesCount++;
                //If the neighbour is diagonal to the current node, then the distance cost is 1.4, otherwise its 1
                double distanceCost = (GlobalConstants.CAN_MOVE_DIAGONALLY && isDiagonal(current, neighbour)) ? 1.4d : 1d;
                double newGCost = current.gCost + distanceCost;
                //If the new gCost is less than the old one, a faster route has been found to this node.
                if (newGCost < neighbour.gCost) {
                    neighbour.gCost = newGCost;
                    neighbour.cameFrom = current;

                    if (!openSet.contains(neighbour)) {
                        //Sets the distance from the neighbour to the end node.
                        neighbour.setHCost(endNode);
                        openSet.addItem(neighbour);
                        if (neighbour != endNode) neighbour.setColour(GlobalConstants.REVEALED_NODE_COLOUR);
                    }
                    else {
                        //Updates the neighbour in the openset so that the it's position reflects its new fCost
                        openSet.updateItem(neighbour);
                    }
                    //Only create a new label if one doesn't exist - This prevents new fcosts being displayed over the top of the previous one.
                    if (neighbour.getLabel() == null) {
                        Label fCostLabel = new Label(String.valueOf(neighbour.fCost()));
                        fCostLabel.setStyle("-fx-text-fill: white; -fx-font-size: " + GlobalConstants.TILE_SIZE * 0.35);
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
}

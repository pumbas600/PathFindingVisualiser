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
import nz.pumbas.Utilities.Tag;
import nz.pumbas.Utilities.UtilityFunctions;

public class DijkstraPathFinding extends PathFinder {

    private Heap<Node> openSet = new Heap<>(Node.class, GlobalConstants.WIDTH * GlobalConstants.HEIGHT);

    public DijkstraPathFinding(GridPane grid, Node[][] nodeGrid, ScrollBar speedScrollBar) {
        super(grid, nodeGrid, speedScrollBar);
    }

    @Override
    public void beginPathFinding(Node startNode, Node endNode) {
        this.startNode = startNode;
        this.endNode = endNode;
        startNode.gCost = 0;

        for (int x = 0; x < GlobalConstants.WIDTH; x++) {
            for (int y = 0; y < GlobalConstants.HEIGHT; y++) {
                Node node = nodeGrid[x][y];
                if (node.getTag() != Tag.BARRIER) openSet.addItem(node);
            }
        }

        startMainLoop();
    }
    /*
    Dijkstra's pathfinding algorithm checks every possible node as to be certain that this is
    the fastest route. As a result, many more nodes are checked than incompairson to A*.
    More information, including the pseudocode can be found here: https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm#Pseudocode
     */
    @Override
    void pathFinding() {
        if (!openSet.isEmpty()) {
            //Get the node with the smallest distance.
            Node current = openSet.removeFirstItem();

            //If the current node is the end node, then the fastest path has been found.
            if (current == endNode) {
                retracePath();
                mainLoop.stop(); //Stop this loop
                return;
            }

            //Maps a colour of hue between 0 and 333 based on how close the current node is to the end node.
            if(current != startNode) current.setColour(Color.hsb(
                    UtilityFunctions.mapBetween(current.getPos().getDist(endNode.getPos()),
                            0, startNode.getPos().getDist(endNode.getPos()), 0, 330), 0.7f, 0.8f));

            for (Node neighbour : getNeighbours(current)) {
                checkedNodesCount++;
                //If the neighbour is diagonal to the current node, then the distance cost is 1.4, otherwise its 1
                double distanceCost = (GlobalConstants.CAN_MOVE_DIAGONALLY && isDiagonal(current, neighbour)) ? 1.4d : 1d;
                double newDistance = current.gCost + distanceCost;
                //If an alternative path of shorter distance has been found, update the neighbour
                if (newDistance < neighbour.gCost) {
                    neighbour.gCost = newDistance;
                    neighbour.cameFrom = current;

                    //Updates the neighbour in the openset so that the it's position reflects its new distance
                    if (openSet.contains(neighbour)) openSet.updateItem(neighbour);
                    if (neighbour != endNode) neighbour.setColour(GlobalConstants.REVEALED_NODE_COLOUR);

                    if (neighbour.getLabel() == null) {
                        Label label = new Label(String.valueOf(neighbour.gCost));
                        label.setStyle("-fx-text-fill: white; -fx-font-size: " + GlobalConstants.TILE_SIZE * 0.35);
                        neighbour.setLabel(label);
                        grid.add(label, neighbour.getX(), neighbour.getY());
                    }
                    else {
                        neighbour.getLabel().setText(String.valueOf(neighbour.gCost));
                    }
                }
            }
            return;
        }
        mainLoop.stop();
    }
}

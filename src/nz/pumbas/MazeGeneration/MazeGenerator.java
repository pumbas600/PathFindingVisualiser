package nz.pumbas.MazeGeneration;

import nz.pumbas.Utilities.GlobalConstants;
import nz.pumbas.Utilities.Node;
import nz.pumbas.Utilities.Tag;

import java.util.ArrayList;
import java.util.Random;

public class MazeGenerator {

    /*
    Maze Generation based off 'Randomised Prim's algorithm'.
    More information on this algorithm, including the pseudocode can be found
    here: https://en.wikipedia.org/wiki/Maze_generation_algorithm#Randomized_Prim's_algorithm
     */

    private static Node[][] nodeGrid;

    public static void GenerateMaze(Node[][] nodeGrid) {
        MazeGenerator.nodeGrid = nodeGrid;
        Random random = new Random();

        //Any random starting point except for a point on the edge of the grid
        int startX = random.nextInt(GlobalConstants.WIDTH - 2) + 1;
        int startY = random.nextInt(GlobalConstants.HEIGHT - 2) + 1;

        Node node = nodeGrid[startX][startY];
        node.setTag(Tag.NONE);
        ArrayList<Node> walls = new ArrayList<>(getSurroundingWalls(node, GlobalConstants.CAN_MOVE_DIAGONALLY));

        while (!walls.isEmpty()) {
            node = walls.get(random.nextInt(walls.size()));
            ArrayList<Node> surroundingWalls = getSurroundingWalls(node, true);

            if (surroundingWalls.size() >= 6) {
                node.setTag(Tag.NONE);
                walls.addAll(GlobalConstants.CAN_MOVE_DIAGONALLY ? surroundingWalls : getSurroundingWalls(node, false));
            }
            walls.remove(node);
        }

    }

    private static ArrayList<Node> getSurroundingWalls(Node node, boolean includeDiagonal) {
        ArrayList<Node> walls = new ArrayList<>();
        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                if (x == 0 && y == 0) continue;

                //The absolute difference of x and y will always be 1 if they're directly connected.
                if (!includeDiagonal && (Math.abs(x - y) != 1)) continue;

                int xPos = x + node.getX();
                int yPos = y + node.getY();

                //Only adds walls within the bounds of the nodeGrid
                if (xPos < 0 || xPos >= GlobalConstants.WIDTH || yPos < 0 || yPos >= GlobalConstants.HEIGHT) continue;
                Node wall = nodeGrid[xPos][yPos];
                if (wall.getTag() != Tag.NONE)
                    walls.add(wall);
            }
        }
        return walls;
    }
}

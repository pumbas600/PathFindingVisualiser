package nz.pumbas.Utilities;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import nz.pumbas.Main;
import nz.pumbas.PathFinderScene;
import org.jetbrains.annotations.NotNull;

public class Node {

    private Tag tag = Tag.NONE;
    private Vector pos;
    private Rectangle rectangle;
    private Label label;

    public Node cameFrom;
    public double hCost;
    public double gCost = Double.POSITIVE_INFINITY;

    public Node(Vector pos, Color colour) {
        this.pos = pos;
        this.rectangle = new Rectangle();
        rectangle.setWidth(PathFinderScene.TILE_SIZE);
        rectangle.setHeight(PathFinderScene.TILE_SIZE);
        rectangle.setFill(colour);
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public float fCost(){
        return Math.round((gCost + hCost) * 10) / 10f;
    }

    public void setHCost(Node endNode) {
        hCost = pos.getDist(endNode.getPos());
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public int getX()
    {
        return pos.getX();
    }

    public int getY()
    {
        return pos.getY();
    }

    public Vector getPos()
    {
        return pos;
    }

    @NotNull
    public Rectangle getRectangle()
    {
        return rectangle;
    }

    public void setColour(Color colour) {
        rectangle.setFill(colour);
    }
}

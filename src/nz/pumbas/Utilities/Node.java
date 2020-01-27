package nz.pumbas.Utilities;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import nz.pumbas.Utilities.HeapFiles.HeapItem;
import org.jetbrains.annotations.NotNull;

public class Node implements HeapItem<Node> {

    private Tag tag = Tag.NONE;
    private Vector pos;
    private Rectangle rectangle;
    private Label label;

    public Node cameFrom;
    public double hCost;
    public double gCost = Double.POSITIVE_INFINITY;

    private int itemIndex;

    public Node(Vector pos) {
        this.pos = pos;
        this.rectangle = new Rectangle(GlobalConstants.TILE_SIZE, GlobalConstants.TILE_SIZE, tag.colour);
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
        rectangle.setFill(tag.colour);
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

    @Override
    public void setItemIndex(int index) {
        itemIndex = index;
    }

    @Override
    public int getItemIndex() {
        return itemIndex;
    }

    @Override
    public int compareTo(@NotNull Node node) {
        //Returns - compare as a lower number should be treated as having a higher priority, rather than a lower one.
        int compare = Float.compare(fCost(), node.fCost());
        if (compare == 0) {

            return -Double.compare(hCost, node.hCost);
        }
        return -compare;
    }
}

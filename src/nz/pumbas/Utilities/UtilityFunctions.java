package nz.pumbas.Utilities;

import javafx.scene.layout.HBox;

public class UtilityFunctions {
    public static double mapBetween(double value, double minVal, double maxVal, double minRes, double maxRes) {
        double valuePercent = (value - minVal) / (maxVal - minVal);
        return (maxRes - minRes) * valuePercent + minRes;
    }

    public static HBox horizontalSpace(double width){
        HBox space = new HBox();
        space.setMinWidth(width);
        return space;
    }
}

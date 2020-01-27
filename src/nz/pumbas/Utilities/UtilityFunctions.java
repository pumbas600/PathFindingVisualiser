package nz.pumbas.Utilities;

public class UtilityFunctions {
    public static double mapBetween(double value, double minVal, double maxVal, double minRes, double maxRes) {
        double valuePercent = (value - minVal) / (maxVal - minVal);
        return (maxRes - minRes) * valuePercent + minRes;
    }
}

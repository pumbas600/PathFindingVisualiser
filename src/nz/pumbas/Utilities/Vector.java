package nz.pumbas.Utilities;

import org.jetbrains.annotations.NotNull;

public class Vector {
    private int x;
    private int y;

    public Vector(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public double getSquaredDist(Vector vector) {
        return (Math.abs((x - vector.getX()) * (x - vector.getX()))
                + Math.abs((y - vector.getY()) * (y - vector.getY())));
    }

    public Double getDist(Vector vector) {
        return (Math.sqrt(getSquaredDist(vector)));
    }

    public boolean equals(@NotNull Vector vector)
    {
        return vector.getX() == x && vector.getY() == y;
    }
}

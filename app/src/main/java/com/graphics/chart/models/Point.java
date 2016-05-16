package com.graphics.chart.models;

public class Point implements Comparable<Point> {
    private float x;
    private float y;

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public String toString() {
        return x + " : " + y;
    }

    @Override
    public int compareTo(Point point) {
        return (this.getX() < point.getX() ? -1 :
                (this.getX() == point.getX() ? 0 : 1));
    }
}

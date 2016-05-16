package com.graphics.chart.models;

import java.util.ArrayList;

public class Response {

    private ArrayList<Point> points;

    public Response(ArrayList<Point> points) {
        this.points = points;
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    public String toString() {
        return points.toString();
    }

}

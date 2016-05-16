package com.graphics.chart.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.widget.RelativeLayout;

import com.graphics.chart.models.Point;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Chart {
    private RelativeLayout chartLayout;
    private GraphicalView chartView;

    public Chart(Context context, int id) {
        this.chartLayout = (RelativeLayout) ((Activity) context).findViewById(id);
    }

    public void removeAllViews() {
        chartLayout.removeAllViews();
    }

    public void build(ArrayList<Point> points) {
        chartLayout.removeAllViews();
        XYSeries series = new XYSeries("WEB API Points");
        Collections.sort(points, new PointComparator());
        for (Point point : points) {
            series.add(point.getX(), point.getY());
        }

        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(series);
        XYSeriesRenderer renderer = new XYSeriesRenderer();
        renderer.setLineWidth(2);
        renderer.setColor(Color.BLUE);
        renderer.setDisplayBoundingPoints(true);
        renderer.setPointStyle(PointStyle.CIRCLE);
        renderer.setPointStrokeWidth(3);
        XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
        mRenderer.addSeriesRenderer(renderer);
        mRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
        mRenderer.setShowLabels(true);
        mRenderer.setGridColor(Color.BLACK);
        mRenderer.setShowGrid(true);
        chartView = ChartFactory.getCubeLineChartView(chartLayout.getContext(), dataset, mRenderer, Float.valueOf("0.2"));
        chartLayout.addView(chartView);
    }

    public GraphicalView getChartView() {
        return chartView;
    }

    private class PointComparator implements Comparator<Point> {
        public int compare(Point p1, Point p2) {
            return p1.compareTo(p2);
        }
    }
}

package com.graphics.chart.adapters;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.graphics.chart.models.Point;
import com.graphics.chart.R;

import java.util.ArrayList;

public class PointsAdapter extends ArrayAdapter<Point> {
    private ArrayList<Point> points;
    private Context context;

    public PointsAdapter(Context context, ArrayList<Point> points) {
        super(context, R.layout.grid_item);
        this.points = points;
        this.context = context;
    }

    @Override
    public int getCount() {
        return points.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final View current;
        final ViewHolderItem viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolderItem();
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            current = inflater.inflate(R.layout.grid_item, parent, false);
            viewHolder.xItem = (TextView) current.findViewById(R.id.x_value);
            ViewGroup.LayoutParams paramsX = viewHolder.xItem.getLayoutParams();
            paramsX.width = ViewGroup.LayoutParams.MATCH_PARENT / 2;
            viewHolder.yItem = (TextView) current.findViewById(R.id.y_value);
            ViewGroup.LayoutParams paramsY = viewHolder.xItem.getLayoutParams();
            paramsY.width = ViewGroup.LayoutParams.MATCH_PARENT / 2;

            current.setTag(viewHolder);
        } else {
            current = convertView;
            viewHolder = (ViewHolderItem) current.getTag();
        }

        viewHolder.xItem.setText(String.valueOf("X= " + String.format("%.02f", points.get(position).getX())));
        viewHolder.yItem.setText(String.valueOf("Y= " + String.format("%.02f", points.get(position).getY())));
        return current;
    }

    private class ViewHolderItem {
        TextView xItem;
        TextView yItem;
    }
}
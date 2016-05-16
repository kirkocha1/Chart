package com.graphics.chart.views;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.graphics.chart.adapters.PointsAdapter;

public class PointsList extends ListView {

    public PointsList(Context contex) {
        super(contex);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
        setDynamicHeight();
    }

    private void setDynamicHeight() {
        PointsAdapter listViewAdapter = (PointsAdapter) this.getAdapter();
        if (listViewAdapter == null) {
            return;
        }

        int totalHeight = 0;
        int items = listViewAdapter.getCount();
        View listItem = listViewAdapter.getView(0, null, this);
        listItem.measure(0, 0);
        totalHeight = listItem.getMeasuredHeight();
        totalHeight *= (items + 1);

        ViewGroup.LayoutParams params = this.getLayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = totalHeight;
        this.setLayoutParams(params);
    }
}

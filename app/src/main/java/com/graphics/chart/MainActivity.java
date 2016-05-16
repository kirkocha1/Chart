package com.graphics.chart;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.graphics.chart.adapters.PointsAdapter;
import com.graphics.chart.models.ErrorMessage;
import com.graphics.chart.models.Point;
import com.graphics.chart.models.Response;
import com.graphics.chart.network.BaseNetworkTask;
import com.graphics.chart.network.Settings;
import com.graphics.chart.views.Chart;
import com.graphics.chart.views.PointsList;

import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //Constant
    private final String URL = Settings.URL;
    //Widgets
    private Button startButton;
    private EditText pointsInput;
    private PointsList grid;
    private AlertDialog errorDialog;
    private Chart chart;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewCompoentIntialization();
    }

    private void viewCompoentIntialization() {
        LinearLayout gridLayout = (LinearLayout) findViewById(R.id.grid_layout);
        grid = new PointsList(this);
        gridLayout.addView(grid, 0);
        pointsInput = (EditText) findViewById(R.id.point_input);
        startButton = (Button) findViewById(R.id.start_button);
        startButton.setOnClickListener(this);
        chart = new Chart(this, R.id.chart_layout);

    }

    private int getPointsCount() {
        int amount = 0;
        try{
            String inputValue = pointsInput.getText().toString();
            if (!inputValue.isEmpty()) {
                amount = Integer.parseInt(inputValue);
            }
        }catch (Exception ex){
            Log.e(Settings.APP_TAG, "wrong value");
        }

        return amount;
    }

    @Override
    public void onClick(View view) {
        onStartButton(view);
    }

    private void onStartButton(View view) {
        view.setClickable(false);
        int pointsCount = getPointsCount();
        String parameters = String.format("count=%d&version=%s", pointsCount, Settings.WEB_API_VERSION);
        GetPointsTask task = new GetPointsTask();
        task.post(URL, Settings.POST, parameters);

    }

    private class GetPointsTask extends BaseNetworkTask {

        @Override
        public void errorHandling(int responseCode) {
            showErrorMessage(new ErrorMessage("Http request was failed"), responseCode);
        }

        @Override
        public void onDataRecieved(String content) {
            try {
                if (content != null) {
                    JSONObject response = new JSONObject(content);
                    int statusCode = response.getInt("result");
                    if (statusCode == 0) {
                        ArrayList<Point> points = new Gson().fromJson(response.getString("response"), Response.class).getPoints();
                        PointsAdapter adapter = new PointsAdapter(grid.getContext(), points);
                        grid.setAdapter(adapter);
                        chart.build(points);
                    } else {
                        chart.removeAllViews();
                        grid.setAdapter(null);
                        ErrorMessage message = new Gson().fromJson(response.getString("response"), ErrorMessage.class);
                        showErrorMessage(message, statusCode);
                    }
                }
            } catch (Exception ex) {
                Log.e(Settings.APP_TAG, ex.toString());
            }
            startButton.setClickable(true);
        }
    }

    private void showErrorMessage(ErrorMessage message, int statusCode) {
        errorDialog = new AlertDialog.Builder(MainActivity.this).create();
        errorDialog.setTitle("ERROR");
        errorDialog.setMessage("Error code: " + String.valueOf(statusCode) + "\n" + message.getMessage());
        errorDialog.show();

    }
}

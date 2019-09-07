package com.pennapps.insulin;

import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.DataPoint;



public class MainActivity extends AppCompatActivity {
    protected GraphView graph;
    EditText carbsField;
    protected LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
    protected int cuando = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Insulin: " + getIntent().getStringExtra("email"));
//        setSupportActionBar(toolbar);

        graph = (GraphView) findViewById(R.id.graph);


        graph.addSeries(series);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(cuando);


        carbsField = (EditText) findViewById(R.id.carbs);
        carbsField.setImeActionLabel("Done", EditorInfo.IME_ACTION_DONE);
        carbsField.setOnEditorActionListener((view,key,event) -> {
            addPoint();
            return true;

        });

        series.setTitle("Sugar Consumed");
        series.setDrawBackground(true);
        series.setBackgroundColor(Color.argb(200, 215,235, 255));
        series.setColor(Color.rgb(28, 141, 216));
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10);
        series.setAnimated(true);
    }

    protected void addPoint() {
        try {
            final int carbs = Integer.parseInt(carbsField.getText().toString());
            DataPoint latest = new DataPoint(cuando++, carbs);
            graph.getViewport().setMaxX(cuando);
            series.appendData(latest, false, 10);



        } catch (NumberFormatException nfe) {}

    }

    protected void addPoint(final int carbs) {
            DataPoint latest = new DataPoint(cuando++, carbs);
            graph.getViewport().setMaxX(cuando);
            series.appendData(latest, false, 10);

    }

    public void sendRequest(View view) {
        ApiRequest req = new ApiRequest(this);
        req.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

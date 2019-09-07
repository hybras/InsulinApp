package com.pennapps.insulin;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


public class MainActivity extends AppCompatActivity {
    GraphView graph;
    EditText carbsField;
    LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
    int cuando = 0;

    final String request = "https://api.nutritionix.com/v1_1/search/taco?results=0%3A1&cal_min=0&cal_max=50000&fields=item_name%2Cbrand_name%2Citem_id%2Cbrand_id%2Cnf_total_carbohydrate&appId=1a226fac&appKey=89b309422edc2c2d3086983c18af602f";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        graph = (GraphView) findViewById(R.id.graph);
        carbsField = (EditText) findViewById(R.id.carbs);
        graph.addSeries(series);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(cuando);


        carbsField.setImeActionLabel("Done", EditorInfo.IME_ACTION_DONE);
        carbsField.setOnEditorActionListener((view,key,event) -> {
            addPoint(view);
            ApiRequest req = new ApiRequest(view);
            req.execute(request);

            return true;

        });
    }

    private void addPoint(View view) {
        try {
            final int carbs = Integer.parseInt(carbsField.getText().toString());
            DataPoint latest = new DataPoint(cuando++, carbs);
            graph.getViewport().setMaxX(cuando+1);
            series.appendData(latest, false, 10);


        } catch (NumberFormatException nfe) {
            Snackbar.make(view, "Please enter a number", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
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

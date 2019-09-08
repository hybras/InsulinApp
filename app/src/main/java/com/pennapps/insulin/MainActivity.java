package com.pennapps.insulin;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.DataPoint;





public class MainActivity extends AppCompatActivity {
    protected GraphView graph;
    EditText carbsField;
    protected LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
    protected int cuando = 0;


    Meal meal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Insulin: " + getIntent().getStringExtra("email"));
//        setSupportActionBar(toolbar);
        int TDD = getIntent().getIntExtra("TDD", 50);
        int targetBG = getIntent().getIntExtra("targetBG", 120);
        boolean insulinType = getIntent().getBooleanExtra("insulinType", false);

        meal = new Meal(insulinType, TDD, targetBG);

        graph = (GraphView) findViewById(R.id.graph);

        TextView targetLabel = findViewById(R.id.TDD);
        targetLabel.setText("Total Daily Dose: " + Integer.toString(TDD) + " grams");

        EditText currentCarbs = findViewById(R.id.currentCarbs);
        currentCarbs.setImeActionLabel("Done", EditorInfo.IME_ACTION_DONE);
        currentCarbs.setOnEditorActionListener((view,key,event) -> {
            String asString = currentCarbs.getText().toString();
            meal.setCurrentBG(Integer.parseInt(asString));
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if(imm.isAcceptingText()) { // verify if the soft keyboard is open
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
            Snackbar.make(view, "You need to take " + meal.insulin() + " grams of insulin", Snackbar.LENGTH_LONG).show();
            return true;
        });


        graph.addSeries(series);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(cuando);


        carbsField = (EditText) findViewById(R.id.carbs);
        carbsField.setImeActionLabel("Done", EditorInfo.IME_ACTION_DONE);
        carbsField.setOnEditorActionListener((view,key,event) -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if(imm.isAcceptingText()) { // verify if the soft keyboard is open
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
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
        Intent rec = new Intent(getApplicationContext(),RecImage.class);
        startActivity(rec);
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

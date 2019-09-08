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

        graph = findViewById(R.id.graph);

        TextView targetLabel = findViewById(R.id.info);
        String infoString = "Total Daily Dose: " + TDD + " grams"
                + ", Sensitivity: " + meal.calcCF()
                + ", CHO Ratio: " + meal.calcCHO();
        targetLabel.setText(infoString);

        EditText currentCarbs = findViewById(R.id.currentCarbs);
        currentCarbs.setImeActionLabel("Done", EditorInfo.IME_ACTION_DONE);
        currentCarbs.setOnEditorActionListener((view,key,event) -> {
            String asString = currentCarbs.getText().toString();
            meal.setCurrentBG(Integer.parseInt(asString));
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if(imm.isAcceptingText()) { // verify if the soft keyboard is open
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
            Snackbar.make(view, "You need to take " + meal.insulin() + " grams of insulin", Snackbar.LENGTH_INDEFINITE).show();
            TextView insulinLabel = findViewById(R.id.gramsInsulin);
            insulinLabel.setText("Take " + Integer.toString(meal.insulin()) + " grams of insulin");
            insulinLabel.setVisibility(View.VISIBLE);
            return true;
        });


        carbsField = findViewById(R.id.carbs);
        carbsField.setImeActionLabel("Done", EditorInfo.IME_ACTION_DONE);
        carbsField.setOnEditorActionListener((view,key,event) -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if(imm.isAcceptingText()) { // verify if the soft keyboard is open
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
            try {
                addPoint(Integer.parseInt(carbsField.getText().toString()));
            } catch (NumberFormatException nf) {}
            return true;

        });

        EditText foodText = findViewById(R.id.foodChoice);
        foodText.setImeActionLabel("Done", EditorInfo.IME_ACTION_DONE);
        foodText.setOnEditorActionListener((view,key,event) -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if(imm.isAcceptingText()) { // verify if the soft keyboard is open
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
            String foodString = foodText.getText().toString();
            ApiRequest food = new ApiRequest(
                    (i) -> {
                        meal.setMealCHO(i);
                        try {
                            meal.setCurrentBG(Integer.parseInt(currentCarbs.getText().toString()));
                        } catch (NumberFormatException nf) {
                            meal.setCurrentBG(50);
                        }
                        int insulin = meal.insulin();
                        TextView insulinLabel = findViewById(R.id.gramsInsulin);
                        insulinLabel.setText("Take " + Integer.toString(insulin) + " grams of insulin");
                        insulinLabel.setVisibility(View.VISIBLE);
                        Snackbar.make(getCurrentFocus(), "You need " + insulin + " grams insulin",Snackbar.LENGTH_LONG).show();
                    }
            );
            food.execute(foodString);
            return  true;
        });

        graph.addSeries(series);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(cuando);

        series.setTitle("Sugar Consumed");
        series.setDrawBackground(true);
        series.setBackgroundColor(Color.argb(200, 215,235, 255));
        series.setColor(Color.rgb(28, 141, 216));
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10);
        series.setAnimated(true);
    }

    protected void addPoint(int carbs) {
        DataPoint latest = new DataPoint(cuando++, carbs);
        graph.getViewport().setMaxX(cuando);
        series.appendData(latest, false, 10);

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

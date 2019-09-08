package com.pennapps.insulin;

import android.app.ActivityManager;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;


import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.IntConsumer;

import javax.net.ssl.HttpsURLConnection;

public class ApiRequest extends AsyncTask<String, Void, Integer> {

    private final String request = "https://api.nutritionix.com/v1_1/search/%s?results=0%3A1&cal_min=0&cal_max=50000&fields=item_name%2Cbrand_name%2Citem_id%2Cbrand_id%2Cnf_total_carbohydrate&appId=1a226fac&appKey=89b309422edc2c2d3086983c18af602f";



    private final IntConsumer eat;

    ApiRequest (IntConsumer eat) {
        this.eat = eat;
    }

    @Override
    protected Integer doInBackground(String... strings) {
        try {
            URL url = new URL(String.format(request, strings[0]));
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            StringBuilder stringBuilder = new StringBuilder(600);
            byte[] out = new byte[512];
            while (in.read(out)!=-1) {
                String str = new String(out);
                stringBuilder.append(str);
            }

            urlConnection.disconnect();
            try {
                JSONObject full = new JSONObject(stringBuilder.toString());
                return full.getJSONArray("hits").getJSONObject(0).getJSONObject("fields").getInt("nf_total_carbohydrate");
            } catch (JSONException js) {
                return 0;
            }

        } catch (MalformedURLException ml) {
            return 0;
        } catch (IOException io) {
            return 0;
        }
    }

    @Override
    protected void onPostExecute(Integer carbs) {
       eat.accept(carbs);
    }
}

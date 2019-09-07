package com.pennapps.insulin;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class ApiRequest extends AsyncTask<String, Void, String> {

    final View view;
    ApiRequest (View v) {
        this.view = v;
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            URL url = new URL(strings[0]);
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            StringBuilder stringBuilder = new StringBuilder(600);
            byte[] out = new byte[512];
            while (in.read(out)!=-1) {
                String str = new String(out);
                stringBuilder.append(str);
            }

            urlConnection.disconnect();

            return stringBuilder.toString();

        }
        catch (MalformedURLException ml) { return null;}
        catch (IOException io) {return null;}
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("output",result);


        try {
            JSONObject full = new JSONObject(result);
            String carbs = full.getJSONArray("hits").getJSONObject(0).getJSONObject("fields").getString("nf_total_carbohydrate");
            Snackbar.make(view, carbs, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } catch (Exception e) {
            Snackbar.make(view, "Failed", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }




    }
}

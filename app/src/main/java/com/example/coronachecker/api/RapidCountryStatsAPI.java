package com.example.coronachecker.api;

import android.os.AsyncTask;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RapidCountryStatsAPI extends AsyncTask<String,String,String> {
    public static String CountryStatsResponse ;
    OkHttpClient client =null;

    @Override
    protected String doInBackground(String... strings) {

        try {
             client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(strings[0])
                    .get()
                    .addHeader("x-rapidapi-host", "coronavirus-monitor.p.rapidapi.com")
                    .addHeader("x-rapidapi-key", "a05dea6e79msh4c763323e971e65p14eaa4jsna08b22ec980a")
                    .build();

            Response response = client.newCall(request).execute();
            return response.body().string();
        }catch (Exception e){e.printStackTrace();}
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        CountryStatsResponse = result;
    }
}

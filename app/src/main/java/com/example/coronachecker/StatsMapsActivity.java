package com.example.coronachecker;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coronachecker.api.RapidCountryStatsAPI;
import com.example.coronachecker.model_class.Country_Stats;
import com.example.coronachecker.model_class.Lat_lng;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;



public class StatsMapsActivity extends FragmentActivity implements OnMapReadyCallback {
    public static ArrayList<Country_Stats> country_stats_arrayLists;
    ImageView gps_img ;
    private GoogleMap mMap;
    ArrayList<Lat_lng> lat_lng = new ArrayList<>();
    ArrayList<String> lat=new ArrayList<>();
    ArrayList<String> lng=new ArrayList<>();
    SupportMapFragment mapFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats_maps);
        country_stats_arrayLists = new ArrayList<>();
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        gps_img = findViewById(R.id.transition_current_scene);
        updateMAP();
        getLatLng();
        handelSearchBar();
        onClickListener();
        if (isNetworkAvailable()){
            getDataFromAPI();

        }
        else{
//            Snackbar snackbar = Snackbar.make(findViewById(R.id.relative),"Oops!,Your internet might not be working.",Snackbar.LENGTH_INDEFINITE);
//            snackbar.setAction("RETRY", v-> {
//                if(isNetworkAvailable()) {
//                    getDataFromAPI();
//                }
//                else {
//                    snackbar.dismiss();
//                    snackbar.show();
//                }
//            });
//            snackbar.setActionTextColor(Color.RED);
//            snackbar.show();
            startActivity(new Intent(this,InternetCheckActivity.class));
            finish();
        }

    }

    private void onClickListener() {
        gps_img.setOnClickListener(v->{
            LatLng home = new LatLng(26.885790, 75.803240);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(home,3));
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    private void updateCount() {

        try {
            String response = new RapidCountryStatsAPI().execute("https://coronavirus-monitor.p.rapidapi.com/coronavirus/worldstat.php").get();
            JSONObject obj = new JSONObject(response);
             TextView details = findViewById(R.id.details);
             details.setVisibility(View.VISIBLE);
            details.setText(" Total Confirmed: "+obj.getString("total_cases")+"  |  Deaths: "+obj.getString("total_deaths")+"\n Recovered: "+obj.getString("total_recovered")+"  |  Countries Affected: "+country_stats_arrayLists.size());


        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void handelSearchBar() {

        androidx.appcompat.widget.SearchView search_bar = findViewById(R.id.search_bar);
//        search_bar.setSelected(true);
//        search_bar.requestFocus();
//        search_bar.setFocusable(true);
        search_bar.setQueryHint("Search Country");
        search_bar.setOnClickListener(v->{
            Log.e("ButtonClicked","buttonclicked");
            search_bar.onActionViewExpanded();
        });
        search_bar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e("Query Submitted",query);
                LatLng latlng=null;
                for(int j =0;j<lat_lng.size();j++){
                    if(lat_lng.get(j).getName().toLowerCase().contains(query.toLowerCase())){
                        latlng =new LatLng(lat_lng.get(j).getLat(),lat_lng.get(j).getLng());
                        break;
                    }
                }
                if (latlng != null){
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng,4));

                }
                else{
                    Snackbar.make(findViewById(R.id.relative),"Try again with a proper country name.",Snackbar.LENGTH_SHORT).show();
                }
                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void getLatLng() {
        InputStream is = getResources().openRawResource(R.raw.lat_lng_country);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        String line = "";
        try{
            while ((line = reader.readLine()) != null){
                String[] tokens = line.split(",");
                Lat_lng latLng = new Lat_lng();
                latLng.setCountry(tokens[0]);
                latLng.setName(tokens[3]);
                Log.e("Lat: ",tokens[0]);
                latLng.setLat(Double.parseDouble(tokens[1]));
                latLng.setLng(Double.parseDouble(tokens[2]));
                lat_lng.add(latLng);
            }

        }
        catch (Exception e){
            Log.wtf("Error Reading: ",line,e);
            e.printStackTrace();}

    }

    private void updateMAP() {

//        progressDialog.dismiss();
        mapFragment.getMapAsync(this);
    }

    private void getDataFromAPI() {
        String result;
        try {
            Log.e("API Call Map:","making API call");

            result = new RapidCountryStatsAPI().execute("https://coronavirus-monitor.p.rapidapi.com/coronavirus/cases_by_country.php").get();
            JSONObject obj= new JSONObject(result);
            JSONArray CountriesStat = obj.getJSONArray("countries_stat");
            Log.e("API Call Map:","data recieved");
            for(int i=0;i<CountriesStat.length();i++){
                String mJsonString = CountriesStat.getString(i);
                JsonParser parser = new JsonParser();
                JsonElement mJson =  parser.parse(mJsonString);
                Gson gson = new Gson();
                Country_Stats object = gson.fromJson(mJson, Country_Stats.class);
                country_stats_arrayLists.add(object);
            }
            updateMAP();
            updateCount();

        }catch (Exception e){e.printStackTrace();}

    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }


            @Override
            public View getInfoContents(Marker marker) {
                View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents,null);
                TextView country_name = infoWindow.findViewById(R.id.country_name);
                country_name.setText(marker.getTitle());
                TextView snippet = infoWindow.findViewById(R.id.snippet);
                snippet.setText(marker.getSnippet());

                return infoWindow;
            }

        });
    mMap.setOnInfoWindowClickListener(marker -> {
        Intent intent = new Intent(this,CountryDetails.class);
        intent.putExtra("country_name",marker.getTitle());
        startActivity(intent);
    });

        if(country_stats_arrayLists.size() >0){
            for (int i= 0;i<country_stats_arrayLists.size();i++){
                LatLng latlng = null;
                for(int j =0;j<lat_lng.size();j++){
                    if(lat_lng.get(j).getName().equalsIgnoreCase(country_stats_arrayLists.get(i).getCountry_name())){
                        latlng =new LatLng(lat_lng.get(j).getLat(),lat_lng.get(j).getLng());

                    }
                }
                if(latlng != null) {
                    if (latlng.equals(new LatLng(20.593684, 78.96288))){
                        mMap.addMarker(new MarkerOptions().position(latlng)
                                .title(country_stats_arrayLists.get(i).getCountry_name())
                                .snippet("Total Cases: " + country_stats_arrayLists.get(i).getCases() + "\n"
                                        + "Deaths: " + country_stats_arrayLists.get(i).getDeaths() + "\n"
                                        + "Total Recovered: " + country_stats_arrayLists.get(i).getTotal_recovered() + "\n"
                                        + "*Click for more information.*"
                                )).showInfoWindow();
                    }
                    else {
                        mMap.addMarker(new MarkerOptions().position(latlng)
                                .title(country_stats_arrayLists.get(i).getCountry_name())
                                .snippet("Total Cases: " + country_stats_arrayLists.get(i).getCases() + "\n"
                                        + "Deaths: " + country_stats_arrayLists.get(i).getDeaths() + "\n"
                                        + "Total Recovered: " + country_stats_arrayLists.get(i).getTotal_recovered() + "\n"
                                        + "*Click for more information.*"
                                ));
                    }

                }
            }
            LatLng home = new LatLng(26.885790, 75.803240);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(home,3));


        }
        else {
            LatLng home = new LatLng(26.885790, 75.803240);
            mMap.addMarker(new MarkerOptions().position(home).title("India").snippet("India")).showInfoWindow();
            mMap.addCircle(new CircleOptions().center(home).radius(99999).fillColor(5).strokeColor(8));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(home));

        }
    }


}
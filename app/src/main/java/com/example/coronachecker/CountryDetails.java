package com.example.coronachecker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.example.coronachecker.databinding.ActivityCountryDetailsBinding;

import static com.example.coronachecker.StatsMapsActivity.country_stats_arrayLists;


public class CountryDetails extends AppCompatActivity {
    ActivityCountryDetailsBinding view;
    String country_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       view = DataBindingUtil.setContentView(this,R.layout.activity_country_details);
       country_name = getIntent().getStringExtra("country_name");
       setDataonLayout();
    }

    private void setDataonLayout() {
        int i = 0;
    for(i = 0;i<country_stats_arrayLists.size();i++){
        if(country_stats_arrayLists.get(i).getCountry_name().equalsIgnoreCase(country_name)) break;
    }
    view.countryName.setText(country_stats_arrayLists.get(i).getCountry_name());
    view.totalCases.setText("Total cases:"+country_stats_arrayLists.get(i).getCases());
        view.deaths.setText("Deaths: "+country_stats_arrayLists.get(i).getDeaths());
        view.totalRecovered.setText("Total Recovered:"+country_stats_arrayLists.get(i).getTotal_recovered());
        view.newCases.setText("New Cases: "+country_stats_arrayLists.get(i).getNew_cases());
        view.newDeaths.setText("New Deaths: "+country_stats_arrayLists.get(i).getNew_deaths());
        view.seriousCritical.setText("Serious Critical: "+country_stats_arrayLists.get(i).getSerious_critical());
        view.activeCases.setText("Active Cases: "+country_stats_arrayLists.get(i).getActive_cases());
        view.totalCasesPer1m.setText("Total Cases Per_1m_population: "+country_stats_arrayLists.get(i).getTotal_cases_per_1m_population());



    }
}

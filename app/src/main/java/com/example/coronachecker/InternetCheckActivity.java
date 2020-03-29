package com.example.coronachecker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.example.coronachecker.databinding.ActivityInternetCheckBinding;

public class InternetCheckActivity extends AppCompatActivity {
    ActivityInternetCheckBinding view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = DataBindingUtil.setContentView(this,R.layout.activity_internet_check);
        onClickListener();
    }

    private void onClickListener() {
        view.retryBt.setOnClickListener(v->{
            if(isNetworkAvailable()){
                view.retryBt.setVisibility(View.GONE);
                view.progressCircular.setVisibility(View.VISIBLE);
                new Handler().postDelayed(() -> {
                    startActivity(new Intent(this,StatsMapsActivity.class));

                    finish();
                },1000);
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

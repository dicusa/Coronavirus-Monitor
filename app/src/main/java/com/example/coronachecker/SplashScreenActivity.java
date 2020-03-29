package com.example.coronachecker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.widget.Toast;

public class SplashScreenActivity extends AppCompatActivity {
    AlertDialog.Builder alertDialogBuilder;
    AlertDialog alertDialog ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        setUpAlertDialog();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        } else {
            intialize();
        }

    }

    private void setUpAlertDialog() {
        alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Location access is required to use Coronavirus Monitor ");
        alertDialogBuilder.setPositiveButton("Grant",(dialogInterface, i) -> {
            goToSettings();
        });
         alertDialog = alertDialogBuilder.create();
    }

    private  void intialize(){
        new Handler().postDelayed(() -> {
            startActivity(new Intent(this,StatsMapsActivity.class));
            finish();
        },2000);

    }
    private void checkPermission() {

        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
                || (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {

                alertDialog.show();
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},
                        125);
            }
        } else {
                intialize();
        }
    }

    private void goToSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 125);
        alertDialog.dismiss();
        Toast.makeText(this,"Go To Application Permission To Allow Access", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 125: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    intialize();
                } else {
                    checkPermission();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
}

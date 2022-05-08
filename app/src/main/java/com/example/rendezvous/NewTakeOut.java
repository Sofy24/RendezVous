package com.example.rendezvous;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NewTakeOut extends AppCompatActivity implements LocationListener {
    TextView dateRangeText;
    Button calendar;
    private String providerId = LocationManager.GPS_PROVIDER;
    private LocationManager locationManager = null;
    private static final int MIN_DIST = 20;
    private static final int MIN_PERIOD = 30000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_take_out);
        this.dateRangeText = findViewById(R.id.show_date);
        this.calendar = findViewById(R.id.button_open_calendar);
        MaterialDatePicker materialDatePicker = MaterialDatePicker.Builder.dateRangePicker()
                .setSelection(Pair.create(MaterialDatePicker.thisMonthInUtcMilliseconds(),
                        MaterialDatePicker.todayInUtcMilliseconds()))
                .build();
        System.out.println("getSupportFragmentManager().getFragments() = " + getSupportFragmentManager().getFragments());

        this.calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                materialDatePicker.show(getSupportFragmentManager(), "tag_picker");
                materialDatePicker.show(getSupportFragmentManager(), materialDatePicker.toString());
                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        dateRangeText.setText(materialDatePicker.getHeaderText());
                    }
                });
            }
        });


        FloatingActionButton floatingActionButton = findViewById(R.id.fab_check);
        Activity activity = this;
        App.setCurrentActivity(activity);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText((AppCompatActivity) activity, "Fab pressed", Toast.LENGTH_SHORT).show();
                Intent backHome = new Intent(NewTakeOut.this, HomeActivity.class);
                startActivity(backHome);
            }
        });
    }

    private void checkStatusGPS(Activity activity) {
        final LocationManager locationManager =
                (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        //if gps is off, show the alert message
        if (locationManager != null && !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            new AlertDialog.Builder(activity)
                    .setMessage("Your GPS is off, do you want to enable it?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", ((dialogInterface, i) ->
                            activity.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))))
                    .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel())
                    .create()
                    .show();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        checkStatusGPS(this);
        //locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //if (!locationManager.isProviderEnabled(providerId)) //controllo dello stato del provider
        //{
        //Intent gpsOptionsIntent = new Intent(
        //        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        //startActivity(gpsOptionsIntent);
        //}
        //else{
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(providerId, MIN_PERIOD, MIN_DIST, this); //attivazione degli aggiornamenti
        //this è l'oggetto che svolge il ruolo di LocationListener(in questo caso è l'activity stessa)

    }

    @Override
    protected void onPause()
    {
        super.onPause();
        locationManager.removeUpdates(this); //disattivazione aggiornamenti
    }

    private void updateGUI(Location location)
    {
        double latitude=location.getLatitude();
        double longitude=location.getLongitude();
        String msg="Ci troviamo in coordinate ("+latitude+","+longitude+")";
        //TextView txt= (TextView) findViewById(R.id.locationText);
        //txt.setText(msg);
    }

    @Override
    public void onLocationChanged(Location location)
    {
        updateGUI(location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle)
    { }

    @Override
    public void onProviderEnabled(String s)
    { }

    @Override
    public void onProviderDisabled(String s)
    {  }







    //TODO

}

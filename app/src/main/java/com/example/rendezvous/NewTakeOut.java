package com.example.rendezvous;

import static com.example.rendezvous.Utilities.REQUEST_IMAGE_CAPTURE;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.rendezvous.ViewModel.AddViewModel;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NewTakeOut extends AppCompatActivity implements LocationListener {
    TextView dateRangeText;
    Button calendar;
    private String providerId = LocationManager.GPS_PROVIDER;
    private final LocationManager locationManager = null;
    private static final int MIN_DIST = 20;
    private static final int MIN_PERIOD = 30000;
    private AddViewModel addViewModel;

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

        findViewById(R.id.capture_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (takePicture.resolveActivity(activity.getPackageManager()) != null) {
                    activity.startActivityForResult(takePicture, REQUEST_IMAGE_CAPTURE);
                }
            }
        });
        //addViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(AddViewModel.class);
        ImageView imageView = findViewById(R.id.picture_displayed_imageview);

        /*addViewModel.getImageBitmap().observe(this, new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap bitmap) {
                imageView.setImageBitmap(bitmap);
            }
        });*/


        /*findViewById(R.id.fab_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap
                        bitmap = addViewModel.getImageBitmap().getValue();

                String imageUriString;
                try {
                    if (bitmap != null) {
                        imageUriString = String.valueOf(saveImage(bitmap, activity));
                    } else {
                        imageUriString = "ic_baseline_insert_photo_24";
                    }
                    /*if (placeTIET.getText() != null && descriptionTIET.getText() != null
                            && dateTIET.getText() != null) {

                        addViewModel.addCardItem(new CardItem(imageUriString,
                                placeTIET.getText().toString(), descriptionTIET.getText().toString(),
                                dateTIET.getText().toString()));

                        //addViewModel.setImageBitmap(null);

                        //((AppCompatActivity) activity).getSupportFragmentManager().popBackStack();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });*/

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
        //locationManager.removeUpdates(this); //disattivazione aggiornamenti
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            //this.addViewModel.setImageBitmap(imageBitmap);
        }
    }

    /**
     * Method that saves the image taken by the user in the phone gallery.
     * @param bitmap the image taken by the user
     * @param activity the activity for this fragment
     * @throws FileNotFoundException if the file for the image was not created
     */
    private Uri saveImage(Bitmap bitmap, Activity activity) throws FileNotFoundException {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ITALY)
                .format(new Date());
        String name = "JPEG_" + timestamp + ".jpg";

        ContentResolver contentResolver = activity.getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg");

        Uri imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues);

        Log.d("AddFragment", String.valueOf(imageUri));

        OutputStream outputStream = contentResolver.openOutputStream(imageUri);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imageUri;

    }


    //TODO

}

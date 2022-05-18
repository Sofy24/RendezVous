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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.util.Pair;

import com.example.rendezvous.DB.Circle;
import com.example.rendezvous.DB.Converters;
import com.example.rendezvous.DB.Info;
import com.example.rendezvous.DB.RendezVous;
import com.example.rendezvous.DB.RendezVousDB;
import com.example.rendezvous.DB.User;
import com.example.rendezvous.ViewModel.AddViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class NewTakeOut extends AppCompatActivity implements LocationListener {
    TextView dateRangeText;
    Button calendar;
    private String providerId = LocationManager.GPS_PROVIDER;
    private final LocationManager locationManager = null;
    private Location location = null;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private TextView textView_location;
    private boolean requestingLocationUpdates = false;
    private static final int MIN_DIST = 20;
    private static final int MIN_PERIOD = 30000;
    private AddViewModel addViewModel;
    private Info info;
    private RendezVous rendezVous;
    private final List<Circle> selectedCircles = new ArrayList<>();
    private final List<CheckBox> checkBoxList = new ArrayList<>();
    private List<Circle> circleList;
    private List<String> alreadyMember;
    private User activeUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_take_out);
        if (NewTakeOut.this != null) {
            requestPermissionLauncher = registerForActivityResult(
                    new ActivityResultContracts.RequestPermission(),
                    new ActivityResultCallback<Boolean>() {
                        @Override
                        public void onActivityResult(Boolean result) {
                            if (result) {
                                startLocationUpdates(NewTakeOut.this);
                                Log.d("LAB-ADDFRAGMENT", "PERMISSION GRANTED");
                            } else {
                                Log.d("LAB-ADDFRAGMENT", "PERMISSION NOT GRANTED");
                                showDialog(NewTakeOut.this);
                            }
                        }
                    });
            initializeLocation(NewTakeOut.this);

            System.out.println("findViewById(R.id.gps_button)  = " + findViewById(R.id.gps_button).toString() );
            findViewById(R.id.gps_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    requestingLocationUpdates = true;
                    startLocationUpdates(NewTakeOut.this);
                }
            });
        }

        this.dateRangeText = findViewById(R.id.show_date);

        this.calendar = findViewById(R.id.button_open_calendar);
        MaterialDatePicker<Pair<Long, Long>> materialDatePicker = MaterialDatePicker.Builder.dateRangePicker()
                .setSelection(Pair.create(MaterialDatePicker.thisMonthInUtcMilliseconds(),
                        MaterialDatePicker.todayInUtcMilliseconds()))
                .build();
        //System.out.println("getSupportFragmentManager().getFragments() = " + getSupportFragmentManager().getFragments());

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

        RendezVousDB db = RendezVousDB.getInstance(NewTakeOut.this.getBaseContext());
        AsyncTask.execute(new Runnable() {
                      @Override
                      public void run() {
                          activeUser = db.databaseDAO().getActiveUser();
                          circleList = db.databaseDAO().getCircles();
                          alreadyMember = db.databaseDAO().getUserCircles(db.databaseDAO().getUID(activeUser.getUserName()));
                          ScrollView scrollView = findViewById(R.id.circle_checkbox);
                          LinearLayout layout = (LinearLayout) scrollView.getChildAt(0);

                          for (Circle c : circleList) {
                              CheckBox box = new CheckBox(NewTakeOut.this.getBaseContext());
                              box.setText(c.getC_name());
                              box.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                              box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                  @Override
                                  public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                      selectedCircles.add(c);
                                  }
                              });
                              if (alreadyMember.contains(c.getC_name())) {
                                  box.setChecked(true); //non testato
                              }
                              layout.addView(box);
                              checkBoxList.add(box);
                          }
                      }
                  });





        textView_location = (TextView) findViewById(R.id.location_edittext);

        FloatingActionButton floatingActionButton = findViewById(R.id.fab_check);
        AppCompatActivity activity = this;
        TextInputLayout take_out_name_view = findViewById(R.id.name_new_take_out);
        TextInputLayout take_out_description_view = findViewById(R.id.description_textinput);
        TextInputEditText take_out_location_view = findViewById(R.id.location_edittext);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameTakeOut = String.valueOf(Objects.requireNonNull(take_out_name_view.getEditText()).getText());
                String descriptionTakeOut = String.valueOf(Objects.requireNonNull(take_out_description_view.getEditText()).getText());
                String locationTakeOut = Objects.requireNonNull(take_out_location_view.getText()).toString();
                String[] days = String.valueOf(dateRangeText.getText()).split("- ");
                final List<String> circleOfFriendsSelected = new ArrayList<>();

                RendezVousDB db = RendezVousDB.getInstance(NewTakeOut.this.getBaseContext());
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {

                        if(location != null){
                            info = new Info(nameTakeOut, descriptionTakeOut, null, location.getLatitude(), location.getLongitude());

                        }else {
                            info = new Info(nameTakeOut, descriptionTakeOut, null, 0.0, 0.0);
                        }
                        Integer infoId = info.getI_ID();
                        db.databaseDAO().insertInfo(info);
                        for (CheckBox box : checkBoxList){
                            if(box.isChecked()){
                                circleOfFriendsSelected.add(String.valueOf(box.getText()));
                            }
                        }
                        Circle cc = new Circle("cc", "Pink");
                        db.databaseDAO().insertCircle(cc);
                        db.databaseDAO().insertRendezvous(new RendezVous("cc", 1000,1000, 45));



                    }
                });
                System.out.println("location null:"+location);
                Toast.makeText(activity, "Fab pressed", Toast.LENGTH_SHORT).show();
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

    private void initializeLocation(Activity activity) {
        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);
        this.locationRequest = com.google.android.gms.location.LocationRequest.create();
        //Set the desired interval for active location updates
        locationRequest.setInterval(1000);
        locationRequest.setPriority(com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);

                //Update UI with the location data
                location = locationResult.getLastLocation();
                String text = location.getLatitude() + ", " + location.getLongitude();
                textView_location.setText(text);
                System.out.println(textView_location);
                requestingLocationUpdates = false;
                stopLocationUpdates();
            }
        };
    }

    private void showDialog(Activity activity) {
        new AlertDialog.Builder(activity)
                .setMessage("Permission denied, but needed for gps functionality.")
                .setCancelable(false)
                .setPositiveButton("OK", ((dialogInterface, i) ->
                        activity.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))))
                .setNegativeButton("Cancel", ((dialogInterface, i) -> dialogInterface.cancel()))
                .create()
                .show();
    }

    private void startLocationUpdates(Activity activity) {
        final String PERMISSION_REQUESTED = Manifest.permission.ACCESS_FINE_LOCATION;
        //permission granted
        if (ActivityCompat.checkSelfPermission(activity, PERMISSION_REQUESTED)
                == PackageManager.PERMISSION_GRANTED) {
            checkStatusGPS(activity);
            fusedLocationProviderClient.requestLocationUpdates(locationRequest,
                    locationCallback,
                    Looper.getMainLooper());
        } else if (ActivityCompat
                .shouldShowRequestPermissionRationale(activity, PERMISSION_REQUESTED)) {
            //permission denied before
            showDialog(activity);
        } else {
            //ask for the permission
            requestPermissionLauncher.launch(PERMISSION_REQUESTED);
        }

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
        if (requestingLocationUpdates){
            startLocationUpdates(NewTakeOut.this);
        }
        /*checkStatusGPS(this);
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
            //
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //locationManager.requestLocationUpdates(providerId, MIN_PERIOD, MIN_DIST, this); //attivazione degli aggiornamenti
        //this è l'oggetto che svolge il ruolo di LocationListener(in questo caso è l'activity stessa)
*/
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        stopLocationUpdates();
        //locationManager.removeUpdates(this); //disattivazione aggiornamenti
    }

    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    private void updateGUI(Location location)
    {
        double latitude=location.getLatitude();
        double longitude=location.getLongitude();
        String msg="Ci troviamo in coordinate ("+latitude+","+longitude+")";
        textView_location = (TextView) findViewById(R.id.location_edittext);
        textView_location.setText(msg);
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



}

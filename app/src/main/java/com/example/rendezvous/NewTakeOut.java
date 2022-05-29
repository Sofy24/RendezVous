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
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.format.DateFormat;
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

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.util.Pair;

import com.example.rendezvous.DB.Circle;
import com.example.rendezvous.DB.Info;
import com.example.rendezvous.DB.Invited;
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
import com.google.type.LatLng;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

public class NewTakeOut extends AppCompatActivity implements LocationListener {
    TextView dateRangeText;
    Button calendar;
    private String providerId = LocationManager.GPS_PROVIDER;
    private final LocationManager locationManager = null;
    private Location location = null;
    private LatLng latLng = null;
    private float[] distance = new float[1];
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
    private Set<User> invitedUsers = new HashSet<>();
    private User activeUser;
    private Uri imageUri;
    long firstDay;
    long endDay;

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
                                Log.d("permission", "PERMISSION GRANTED");
                            } else {
                                Log.d("permission", "PERMISSION NOT GRANTED");
                                showDialog(NewTakeOut.this);
                            }
                        }
                    });
            initializeLocation(NewTakeOut.this);

            //TODO non ci sono i controlli se metti la destinazione dell'uscita senza aver attivato il gps
            //final String address = "Via San Vito, 149, 47822 Santarcangelo di Romagna RN";
            //final String address2 = "Rimini";
            findViewById(R.id.confirm_address).setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                      final TextInputEditText take_out_location_view = findViewById(R.id.take_out_location_edittext);
                      final String address = String.valueOf(take_out_location_view.getText());
                      latLng = getLocationFromAddress(getApplicationContext(), address);
                      System.out.println("latLng = " + latLng); //ho ottenuto le coordinate che volevo!!!
                      requestingLocationUpdates = true;
                      startLocationUpdates(NewTakeOut.this);
                      take_out_location_view.setText(Objects.requireNonNull(latLng).getLatitude() + ", " + Objects.requireNonNull(latLng).getLongitude() );
                      Location.distanceBetween(location.getLatitude(), location.getLongitude(), latLng.getLatitude(), latLng.getLongitude() , distance);
                      System.out.println("distance = " + distance[0]);
                  }
              });


            findViewById(R.id.check_distance).setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     new AlertDialog.Builder(NewTakeOut.this)
                             .setMessage(distance[0] == 0 ? "Please, enter all the data" : "The distance between you and the take out is "+ distance[0] + " metres.")
                             .setCancelable(true)
                             .setNeutralButton("Close", ((dialogInterface, i) -> dialogInterface.cancel()))
                             .create()
                             .show();
                 }
             });


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
                .setSelection(Pair.create(MaterialDatePicker.thisMonthInUtcMilliseconds(),MaterialDatePicker.todayInUtcMilliseconds()))
                .build();
        //System.out.println("getSupportFragmentManager().getFragments() = " + getSupportFragmentManager().getFragments());

        Long startDate = materialDatePicker.getSelection().first;
        Long endDate = materialDatePicker.getSelection().second;
        String startDateString = DateFormat.format("dd/MM/yyyy", new Date(startDate)).toString();
        String endDateString = DateFormat.format("dd/MM/yyyy", new Date(endDate)).toString();
        String date = startDateString + " - " + endDateString;

        dateRangeText.setText(date);
        this.calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDatePicker.show(getSupportFragmentManager(), materialDatePicker.toString());
                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
                    @Override
                    public void onPositiveButtonClick(Pair<Long, Long> selection) {
                        firstDay = selection.first;
                        endDay = selection.second;
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
                                      System.out.println("pressed check now b= " + b);
                                      if(b) {
                                          selectedCircles.add(c);
                                      }else {
                                          selectedCircles.remove(c);
                                      }
                                      System.out.println("selectedCircles = " + selectedCircles);
                                  }
                              });
                              layout.addView(box);
                              checkBoxList.add(box);
                          }
                      }
                  });



        textView_location = findViewById(R.id.location);

        FloatingActionButton doneButton = findViewById(R.id.fab_check);
        TextInputLayout take_out_name_view = findViewById(R.id.name_new_take_out);
        TextInputLayout take_out_description_view = findViewById(R.id.description_textinput);
        TextView take_out_location_view = findViewById(R.id.location);

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameTakeOut = String.valueOf(Objects.requireNonNull(take_out_name_view.getEditText()).getText());
                String descriptionTakeOut = String.valueOf(Objects.requireNonNull(take_out_description_view.getEditText()).getText());
                String locationTakeOut = Objects.requireNonNull(take_out_location_view.getText()).toString();
                //String[] days = String.valueOf(dateRangeText.getText()).split("- ");
                Pair<Long, Long> days = materialDatePicker.getSelection();
                final List<String> circleOfFriendsSelected = new ArrayList<>();

                if (nameTakeOut.length() > 0 ){
                    if (!selectedCircles.isEmpty()){
                        // title is ok
                        // groups are selected so i add data
                        AsyncTask.execute(new Runnable() {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void run() {
                                //insert Info and relative RendezVous
                                db.databaseDAO().insertInfo(new Info(nameTakeOut, descriptionTakeOut,
                                        imageUri == null ? null : imageUri.toString(),
                                        latLng == null ? 0.0 : latLng.getLatitude(),
                                        latLng == null ? 0.0 : latLng.getLongitude()
                                        ));
                                Integer info_id = db.databaseDAO().getInfoID(nameTakeOut);
                               for (Circle circle:
                                        selectedCircles) {
                                    db.databaseDAO().insertRendezvous(new RendezVous(circle.getC_name(), firstDay, endDay, info_id));
                                    invitedUsers.addAll(db.databaseDAO().getUsersInCircle(circle.getC_name()));
                               }
                                // Populate invited table

                                List<RendezVous> redezVousIDs = db.databaseDAO().getRendezVous(firstDay, endDay, info_id);
                                for (RendezVous rendezVous:
                                     redezVousIDs) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        invitedUsers.stream().distinct().forEach(user -> { //TODO non va il distinct maledetti stream.....
                                                db.databaseDAO().insertInvited(new Invited(rendezVous.getR_ID(), user.getUID(), "Received"));
                                        });
                                    }
                                }
//                                for (Pair<String, Integer> pair:
//                                     rendezVousIDs) {
//                                    System.out.println("pair = " + pair);
//                                }

                            }
                        });

                        Intent backHome = new Intent(NewTakeOut.this, HomeActivity.class);
                        startActivity(backHome);
                    } else{ //nome vuoto
                        System.out.println("circleOfFriendsSelected = " + circleOfFriendsSelected);
                        Toast.makeText(NewTakeOut.this, "Inserisci almeno un gruppo !", Toast.LENGTH_SHORT).show();
                    }
                } else{ //nome vuoto
                    Toast.makeText(NewTakeOut.this, "Inserisci il nome dell'uscita", Toast.LENGTH_SHORT).show();
                }

            }
        });


        ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();

                            try {
                                imageUri = data.getData();
                                getContentResolver().takePersistableUriPermission(imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION
                                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                                //reverse Uri.parse(stringUri);
                                AsyncTask.execute(new Runnable() {
                                    @Override
                                    public void run() {
//                                        db.databaseDAO().updateUserAvatar(, db.databaseDAO().getActiveUser().getUserName());

                                    }
                                });
                                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                                ImageView imageView = (ImageView)findViewById(R.id.picture_displayed_imageview);
                                imageView.setImageBitmap(selectedImage);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                                Toast.makeText(NewTakeOut.this, "Something went wrong", Toast.LENGTH_LONG).show();
                            }

                        }else {
                            Toast.makeText(NewTakeOut.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
                        }
                    }
                });

        Button addImageBtn = (Button) findViewById(R.id.capture_button);
        addImageBtn.setOnClickListener(view -> {
            Intent photoPickerIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            photoPickerIntent.setType("image/*");
            someActivityResultLauncher.launch(photoPickerIntent);
        });

//        findViewById(R.id.capture_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//                if (takePicture.resolveActivity(activity.getPackageManager()) != null) {
//                    activity.startActivityForResult(takePicture, REQUEST_IMAGE_CAPTURE);
//                }
//            }
//        });
//
        //addViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(AddViewModel.class);
//        ImageView imageView = findViewById(R.id.picture_displayed_imageview);

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

    private LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = LatLng.newBuilder().setLatitude(location.getLatitude()).setLongitude(location.getLongitude()).build();
            System.out.println("p1 = " + p1);


        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return p1;
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
        textView_location = (TextView) findViewById(R.id.location);
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

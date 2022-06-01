package com.example.rendezvous;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.location.Location;
import android.os.Looper;
import android.provider.Settings;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rendezvous.DB.Info;
import com.example.rendezvous.DB.RendezVousDB;
import com.example.rendezvous.ViewModel.RecyclerTouchListener;
import com.example.rendezvous.ViewModel.RecyclerviewAdapter;
import com.example.rendezvous.ViewModel.RendezVousCard;
import com.example.rendezvous.ViewModel.Task;

import java.util.ArrayList;
import java.util.List;

public class CardListActivity extends AppCompatActivity implements LocationListener {
    private RecyclerView recyclerView;
    private RecyclerviewAdapter recyclerviewAdapter;
    private RecyclerTouchListener touchListener;
    private List<Info> infos;
    ImageButton arrow;
    LinearLayout hiddenView;
    CardView cardView;
    //private String providerId = LocationManager.GPS_PROVIDER;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private boolean requestingLocationUpdates = false;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private Location location = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_take_out);
        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean result) {
                        if (result) {
                            startLocationUpdates(CardListActivity.this);
                            Log.d("permission", "PERMISSION GRANTED");
                        } else {
                            Log.d("permission", "PERMISSION NOT GRANTED");
                            showDialog(CardListActivity.this);
                        }
                    }
                });
        initializeLocation(CardListActivity.this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerviewAdapter = new RecyclerviewAdapter(this);
        recyclerView.setAdapter(recyclerviewAdapter);
        //recyclerviewAdapter.notifyDataStateChanged();

        final List<RendezVousCard> rendezVousCards = new ArrayList<>();
        RendezVousDB db = RendezVousDB.getInstance(CardListActivity.this.getBaseContext());

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                infos = db.databaseDAO().getListCardsForActiveUser();
                for (Info singleInfo:
                     infos) {
                    rendezVousCards.add(new RendezVousCard(singleInfo.getTitle(), singleInfo.getImageURL(), singleInfo.getI_ID(), singleInfo.getDescription(),
                            singleInfo.getLatitude(), singleInfo.getLongitude()));
                    recyclerviewAdapter.setTaskList(rendezVousCards);
                    recyclerView.setAdapter(recyclerviewAdapter);
                }
            }
        });

        findViewById(R.id.pulsante_gps_card).setOnClickListener(view1 -> {
            requestingLocationUpdates = true;
            startLocationUpdates(CardListActivity.this);
        });


        touchListener = new RecyclerTouchListener(this,recyclerView);
        touchListener
                .setClickable(new RecyclerTouchListener.OnRowClickListener() {
                    @Override
                    public void onRowClicked(int position) {
                        Toast.makeText(getApplicationContext(),rendezVousCards.get(position).getTitle(), Toast.LENGTH_SHORT).show();
//                        Intent openCardDetails = new Intent(CardListActivity.this, RendeVousCard.class);
//                        startActivity(openCardDetails);
                    }

                    @Override
                    public void onIndependentViewClicked(int independentViewID, int position) {
                        System.out.println("LA SOFIA E' BELLA");
                    }
                })
                .setSwipeOptionViews(R.id.delete_task,R.id.edit_task)
                .setSwipeable(R.id.rowFG, R.id.rowBG, new RecyclerTouchListener.OnSwipeOptionsClickListener() {
                    @Override
                    public void onSwipeOptionClicked(int viewID, int position) {
                        switch (viewID){
                            case R.id.delete_task:
                                AsyncTask.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        db.databaseDAO().setBusy();
                                        // qui andrebbe rimossa la info
                                    }
                                });
                                rendezVousCards.remove(position);
                                recyclerviewAdapter.setTaskList(rendezVousCards);
                                //TODO deve essere rimossa almeno la info con la quale viene caricata la card, ma bho al momento e' difficile
                                // ci pensero' piu' avanti <3
                                break;
                            case R.id.edit_task:
//                                if stato == invito ricevuto
                                AsyncTask.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        //TODO se premo 2 volte muore java.lang.RuntimeException: Only one Looper may be created per thread
                                        Looper.prepare();
                                        String state = db.databaseDAO().getInvitedState(rendezVousCards.get(position).getI_ID());
                                        System.out.println("state " + state);
                                        if(state.equals("Received")){
                                            Intent openEditTakeOut = new Intent(CardListActivity.this, EditTakeOut.class);
                                            openEditTakeOut.putExtra("R_title", rendezVousCards.get(position).getTitle());
                                            openEditTakeOut.putExtra("I_ID", rendezVousCards.get(position).getI_ID());
                                            startActivity(openEditTakeOut);
                                        }else if(state.equals("partecipa")){
                                            Toast.makeText(getApplicationContext(),"Wait the others members!",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                                break;

                        }
                    }
                });
        recyclerView.addOnItemTouchListener(touchListener);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_app_bar, menu);
        return true;
    }
    /*
        Necessary ???????
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
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
                //textView_location.setText(text);
                System.out.println("Ecco a voi le coordinate"+text);
                recyclerviewAdapter.setCurrentLocation(location);
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

    private void updateGUI(Location location)
    {
        double latitude=location.getLatitude();
        double longitude=location.getLongitude();
        String msg="Ci troviamo in coordinate ("+latitude+","+longitude+")";
        System.out.println("msg = " + msg);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (requestingLocationUpdates){
            startLocationUpdates(CardListActivity.this);
        }

    }

    @Override
    protected void onPause()
    {
        super.onPause();
        stopLocationUpdates();
    }
}

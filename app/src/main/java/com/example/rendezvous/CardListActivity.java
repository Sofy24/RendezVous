package com.example.rendezvous;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.location.Location;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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

public class CardListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerviewAdapter recyclerviewAdapter;
    private RecyclerTouchListener touchListener;
    private List<Info> infos;
    ImageButton arrow;
    LinearLayout hiddenView;
    CardView cardView;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private boolean requestingLocationUpdates = false;
    private Location location = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_take_out);
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
                    rendezVousCards.add(new RendezVousCard(singleInfo.getTitle(), singleInfo.getImageURL()));
                    recyclerviewAdapter.setTaskList(rendezVousCards);
                    recyclerView.setAdapter(recyclerviewAdapter);

                }
            }
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

                    }
                })
                .setSwipeOptionViews(R.id.delete_task,R.id.edit_task)
                .setSwipeable(R.id.rowFG, R.id.rowBG, new RecyclerTouchListener.OnSwipeOptionsClickListener() {
                    @Override
                    public void onSwipeOptionClicked(int viewID, int position) {
                        switch (viewID){
                            case R.id.delete_task:
                                rendezVousCards.remove(position);
                                recyclerviewAdapter.setTaskList(rendezVousCards);
                                break;
                            case R.id.edit_task:
                                Toast.makeText(getApplicationContext(),"Edit!",Toast.LENGTH_SHORT).show();
                                Intent openEditTakeOut = new Intent(CardListActivity.this, EditTakeOut.class);
                                startActivity(openEditTakeOut);
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
                requestingLocationUpdates = false;
                stopLocationUpdates();
            }
        };
    }
}

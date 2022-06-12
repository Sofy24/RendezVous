package com.example.rendezvous;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.View;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;

import com.example.rendezvous.DB.Circle;
import com.example.rendezvous.DB.ConfirmedRendezvous;
import com.example.rendezvous.DB.Converters;
import com.example.rendezvous.DB.RendezVous;
import com.example.rendezvous.DB.RendezVousDB;
import com.example.rendezvous.DB.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    final static String CHANNEL_ID = "1";

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Fragment cal =getSupportFragmentManager().findFragmentById(R.id.calendar_frame_layout);
        if(cal != null && cal.isVisible()){
            System.exit(0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("HomeActivity.this è stata creata " + HomeActivity.this);
        setContentView(R.layout.home);

        if (savedInstanceState == null){
            Utilities.insertFragment(this, new CalendarFragment(),
                    CalendarFragment.class.getSimpleName());
        }else {
            System.out.println(getSupportFragmentManager().findFragmentById(R.id.calendar_frame_layout));
        }
        FloatingActionButton floatingActionButton = findViewById(R.id.fab_add);
        AppCompatActivity activity = this;

        floatingActionButton.setOnClickListener(view -> {
            Intent openNewTakeOut = new Intent(HomeActivity.this, NewTakeOut.class);
            startActivity(openNewTakeOut);
        });

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.logo_rv);


        createNotificationChannel();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo_alpha)
                .setColor(ContextCompat.getColor(HomeActivity.this, R.color.colorPrimary))
                .setLargeIcon(largeIcon)
                .setContentTitle("Welcome")
                .setContentText("Your login has been successful")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Your login has been successful"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(69, builder.build());


    }


    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = getString(R.string.channel_name);
            CharSequence name = "nome canale";
            String description = "descrizione canale";
//            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * Initialize the contents of the Activity's standard options menu
     * @param menu The options menu in which you place your items.
     * @return true for the menu to be displayed; if you return false it will not be shown.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_app_bar, menu);
        return true;
    }

    /**
     *
     * /*@param item MenuItem: The menu item that was selected. This value cannot be null.
     * @return false to allow normal menu processing to proceed, true to consume it here.
     */


    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }
}

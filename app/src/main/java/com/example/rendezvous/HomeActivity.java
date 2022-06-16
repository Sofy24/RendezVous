package com.example.rendezvous;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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


//        createNotificationChannel();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel01")
                .setSmallIcon(R.drawable.logo_alpha)
                .setColor(ContextCompat.getColor(HomeActivity.this, R.color.colorPrimary))
                .setLargeIcon(largeIcon)
                .setContentTitle("Welcome")
                .setContentText("Your login has been successful")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Your login has been successful"))

                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationChannel channel = null;   // for heads-up notifications
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel("channel01", "RendezVous",
                    NotificationManager.IMPORTANCE_HIGH);
             channel.setDescription("description");
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(0, builder.build());
        }
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        // notificationId is a unique int for each notification that you must define
//        notificationManager.notify(69, builder.build());

    }


    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = getString(R.string.channel_name);
            CharSequence name = "nome canale";
            String description = "descrizione canale";
//            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH    ;
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

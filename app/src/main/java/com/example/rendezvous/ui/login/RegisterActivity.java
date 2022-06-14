package com.example.rendezvous.ui.login;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.rendezvous.CalendarFragment;
import com.example.rendezvous.HomeActivity;
import com.example.rendezvous.NewTakeOut;
import com.example.rendezvous.R;
import com.example.rendezvous.Utilities;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class RegisterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ImageButton confirmedButton = findViewById(R.id.confirmed);
        AppCompatActivity activity = this;

        confirmedButton.setOnClickListener(view -> {
            Intent openHome = new Intent(RegisterActivity.this, HomeActivity.class);
            startActivity(openHome);
        });

        /* TODO volendo si può inserire una notifica al completamento della registrazione
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.logo_rv);


        createNotificationChannel();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo_alpha)
                .setColor(ContextCompat.getColor(RegisterActivity.this, R.color.colorPrimary))
                .setLargeIcon(largeIcon)
                .setContentTitle("Welcome")
                .setContentText("Your registration has been successful")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Your login has been successful"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(69, builder.build());
*/

    }
}

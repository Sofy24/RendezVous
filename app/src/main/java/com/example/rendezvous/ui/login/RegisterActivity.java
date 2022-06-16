package com.example.rendezvous.ui.login;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.rendezvous.CalendarFragment;
import com.example.rendezvous.DB.RendezVous;
import com.example.rendezvous.DB.RendezVousDB;
import com.example.rendezvous.DB.User;
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

        Button signInButton = findViewById(R.id.login__register);
        ImageButton confirmedButton = findViewById(R.id.confirmed);
        AppCompatActivity activity = this;

        EditText register_mail = (EditText) findViewById(R.id.username_r);
        EditText register_password = (EditText) findViewById(R.id.password_r);
        EditText register_name = (EditText) findViewById(R.id.name_r);
        EditText register_surname = (EditText) findViewById(R.id.surname_r);

        RendezVousDB db = RendezVousDB.getInstance(this.getBaseContext());

        confirmedButton.setOnClickListener(view -> {
//            Intent openHome = new Intent(RegisterActivity.this, HomeActivity.class);
//            startActivity(openHome);
            User newbie = new User(
                    register_name.getText().toString(),
                    register_surname.getText().toString(),
                    register_mail.getText().toString(),
                    register_password.getText().toString(),
                    null
            );

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    db.databaseDAO().insertUser(newbie);
                    db.databaseDAO().setUserActive(db.databaseDAO().getUID(newbie.getUserName()));
                }
            });
            Intent openHome = new Intent(RegisterActivity.this, HomeActivity.class);
            startActivity(openHome);
            finish();
        });

        signInButton.setOnClickListener(view -> {
            Intent openSignIn = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(openSignIn);
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

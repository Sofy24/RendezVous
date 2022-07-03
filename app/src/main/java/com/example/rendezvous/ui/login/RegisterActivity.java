package com.example.rendezvous.ui.login;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
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
    final static String CHANNEL_ID = "1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button signInButton = findViewById(R.id.login__register);
        ImageButton confirmedButton = findViewById(R.id.confirmed);

        EditText register_mail = (EditText) findViewById(R.id.username_r);
        EditText register_password = (EditText) findViewById(R.id.password_r);
        EditText register_name = (EditText) findViewById(R.id.name_r);
        EditText register_surname = (EditText) findViewById(R.id.surname_r);








        RendezVousDB db = RendezVousDB.getInstance(this.getBaseContext());

        confirmedButton.setOnClickListener(view -> {
            if(register_password.getText().length() >= 6) {


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

                Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.logo_rv);


//        createNotificationChannel();

                Notification notification = new NotificationCompat.Builder(RegisterActivity.this, "channel01")
                        .setSmallIcon(R.drawable.logo_alpha)
                        .setColor(ContextCompat.getColor(RegisterActivity.this, R.color.colorPrimary))
                        .setLargeIcon(largeIcon)
                        .setContentTitle("Welcome")
                        .setContentText("Your registration has been successful")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText("Your registration has been successful"))

                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .build();

                NotificationChannel channel = null;   // for heads-up notifications
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    channel = new NotificationChannel("channel01", "RendezVous",
                            NotificationManager.IMPORTANCE_HIGH);
                    channel.setDescription("description");

                    NotificationManager notificationManager = getSystemService(NotificationManager.class);
                    notificationManager.createNotificationChannel(channel);

                    NotificationManagerCompat notificationManagers = NotificationManagerCompat.from(RegisterActivity.this);
                    notificationManagers.notify(0, notification);
                }


                Intent openHome = new Intent(RegisterActivity.this, HomeActivity.class);
                startActivity(openHome);
                finish();
            } else {
                register_password.setError("Password must be >5 characters");
            }
        });

        signInButton.setOnClickListener(view -> {
            Intent openSignIn = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(openSignIn);
        });




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
}

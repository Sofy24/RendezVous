package com.example.rendezvous;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rendezvous.DB.Circle;
import com.example.rendezvous.DB.Converters;
import com.example.rendezvous.DB.RendezVous;
import com.example.rendezvous.DB.RendezVousDB;
import com.example.rendezvous.DB.User;
import com.example.rendezvous.R;

import java.util.Date;

public class UserPage extends AppCompatActivity {
    private User activeUser;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_page_layout);

        RendezVousDB db = RendezVousDB.getInstance(UserPage.this.getBaseContext());
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                activeUser = db.databaseDAO().getActiveUser();

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_app_bar, menu);
        return true;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

}

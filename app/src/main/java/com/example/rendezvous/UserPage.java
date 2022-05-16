package com.example.rendezvous;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rendezvous.DB.Circle;
import com.example.rendezvous.DB.Converters;
import com.example.rendezvous.DB.RendezVous;
import com.example.rendezvous.DB.RendezVousDB;
import com.example.rendezvous.DB.User;
import com.example.rendezvous.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Date;

public class UserPage extends AppCompatActivity {
    private User activeUser;
    private  TextInputEditText name ;
    private TextInputEditText surname;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_page_layout);
        name  = (TextInputEditText) findViewById(R.id.name_active_user_edit);
        surname = (TextInputEditText) findViewById(R.id.surname_active_user_edit);
        RendezVousDB db = RendezVousDB.getInstance(UserPage.this.getBaseContext());
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                activeUser = db.databaseDAO().getActiveUser();
                TextView username = (TextView) findViewById(R.id.usernamePersonalPage);
                username.setText(activeUser.getUserName());
            }
        });


        FloatingActionButton done = (FloatingActionButton) findViewById(R.id.done_insert_info);

        done.setOnClickListener(view -> {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    activeUser = db.databaseDAO().getActiveUser();
                    db.databaseDAO().updateUser(db.databaseDAO().getUID(activeUser.getUserName()), name.getText().toString(), surname.getText().toString());
                }
            });

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

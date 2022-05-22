package com.example.rendezvous;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rendezvous.DB.RendezVousDB;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;


public class EditTakeOut extends AppCompatActivity {

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_take_out);


        List<CheckBox> datesList = new ArrayList<>();

        RendezVousDB db = RendezVousDB.getInstance(EditTakeOut.this.getBaseContext());

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
//                db.databaseDAO().
            }
        });








        FloatingActionButton floatingActionButton = findViewById(R.id.fab_edit);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EditTakeOut.this, "Fab pressed", Toast.LENGTH_SHORT).show();
                Intent backHome = new Intent(EditTakeOut.this, HomeActivity.class);
                startActivity(backHome);
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


    /*// To check a checkbox
    checkbox.isChecked = true;
// To listen for a checkbox's checked/unchecked state changes
        checkbox.setOnCheckedChangeListener { buttonView, isChecked
        // Responds to checkbox being checked/unchecked
        }*/

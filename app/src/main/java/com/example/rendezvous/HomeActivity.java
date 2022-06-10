package com.example.rendezvous;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.View;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rendezvous.DB.Circle;
import com.example.rendezvous.DB.Converters;
import com.example.rendezvous.DB.RendezVous;
import com.example.rendezvous.DB.RendezVousDB;
import com.example.rendezvous.DB.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;

public class HomeActivity extends AppCompatActivity {

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



        CalendarView calendar = (CalendarView) findViewById(R.id.calendarView2);
        System.out.println("calendar = " + calendar);
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

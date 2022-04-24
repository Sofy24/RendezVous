package com.example.rendezvous;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.rendezvous.ViewModel.AddViewModel;
import com.example.rendezvous.ui.login.LoginActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class HomeCalendarActivity extends AppCompatActivity {

    private AddViewModel addViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_layout);

        if (savedInstanceState == null)
            Utilities.insertFragment(this, new HomeCalendarFragment(), HomeCalendarFragment.class.getSimpleName());

       //  da implementare ????
        // addViewModel = new ViewModelProvider(this).get(AddViewModel.class);

        FloatingActionButton floatingActionButton = findViewById(R.id.fab_add);

        Activity activity = this;
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText((AppCompatActivity) activity, "Fab pressed", Toast.LENGTH_SHORT).show();
                Utilities.insertFragment((AppCompatActivity) activity, new NewTakeOutFragment(),
                        NewTakeOutFragment.class.getSimpleName());

            }
        });
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
     * @param item MenuItem: The menu item that was selected. This value cannot be null.
     * @return false to allow normal menu processing to proceed, true to consume it here.
     */


    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

}

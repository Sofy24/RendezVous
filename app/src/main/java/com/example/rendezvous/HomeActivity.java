package com.example.rendezvous;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.View;
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
        setContentView(R.layout.home);


        if (savedInstanceState == null){
            Utilities.insertFragment(this, new CalendarFragment(),
                    CalendarFragment.class.getSimpleName());
        }


        FloatingActionButton floatingActionButton = findViewById(R.id.fab_add);
        AppCompatActivity activity = this;



        floatingActionButton.setOnClickListener(view -> {
            RendezVousDB db = RendezVousDB.getInstance(activity.getBaseContext());
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    User mega = new User("Matteo", "Santoro", "Mega", "00000");
                    User sofy = new User("Sofia", "Tosi", "Sofy24", "123456");
                    User luis = new User("Luis", "Mi chiamo", "Lu1g1", "Ciao_sono_Luis");
                    User michi = new User("Michi", "Ferdinardo", "Clown", "Mi_piace_la_carne");
                    db.databaseDAO().insertUser(mega, sofy, luis, michi);
                    Circle coraggiosi = new Circle("Coraggiosi", "Rosso");
                    db.databaseDAO().insertCircle(coraggiosi);

                    db.databaseDAO().insertCircleOfFriends(coraggiosi.getC_name(), db.databaseDAO().getUID(mega.getUserName()));
                    db.databaseDAO().insertCircleOfFriends(coraggiosi.getC_name(), db.databaseDAO().getUID(sofy.getUserName()));

                    RendezVous rendezVous = new RendezVous(coraggiosi.getC_name(), Converters.dateToTimestamp(new Date()), Converters.dateToTimestamp(new Date()), 2);
                    db.databaseDAO().insertRendezvous(rendezVous);

                }
            });


            Toast.makeText(activity, "Fab pressed", Toast.LENGTH_SHORT).show();
            Intent openNewTakeOut = new Intent(HomeActivity.this, NewTakeOut.class);
            startActivity(openNewTakeOut);
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
     * /*@param item MenuItem: The menu item that was selected. This value cannot be null.
     * @return false to allow normal menu processing to proceed, true to consume it here.
     */


    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

}

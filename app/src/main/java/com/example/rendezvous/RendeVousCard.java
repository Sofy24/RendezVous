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
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class RendeVousCard extends AppCompatActivity {

        ImageButton arrow;
        LinearLayout hiddenView;
        CardView cardView;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.card_details);

            cardView = findViewById(R.id.base_cardview);
            arrow = findViewById(R.id.arrow_button);
            hiddenView = findViewById(R.id.hidden_view);

            arrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // If the CardView is already expanded, set its visibility
                    // to gone and change the expand less icon to expand more.
                    if (hiddenView.getVisibility() == View.VISIBLE) {

                        // The transition of the hiddenView is carried out
                        // by the TransitionManager class.
                        // Here we use an object of the AutoTransition
                        // Class to create a default transition.
                        TransitionManager.beginDelayedTransition(cardView,
                                new AutoTransition());
                        hiddenView.setVisibility(View.GONE);
                        arrow.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24);
                    }

                    // If the CardView is not expanded, set its visibility
                    // to visible and change the expand more icon to expand less.
                    else {

                        TransitionManager.beginDelayedTransition(cardView,
                                new AutoTransition());
                        hiddenView.setVisibility(View.VISIBLE);
                        arrow.setImageResource(R.drawable.ic_baseline_arrow_drop_up_24);
                    }
                }
            });
        }

}

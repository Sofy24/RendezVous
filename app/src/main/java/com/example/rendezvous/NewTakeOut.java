package com.example.rendezvous;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NewTakeOut extends AppCompatActivity {
    TextView dateRangeText;
    Button calendar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_take_out);
        this.dateRangeText = findViewById(R.id.show_date);
        this.calendar = findViewById(R.id.button_open_calendar);
        MaterialDatePicker materialDatePicker = MaterialDatePicker.Builder.dateRangePicker()
                .setSelection(Pair.create(MaterialDatePicker.thisMonthInUtcMilliseconds(),
                        MaterialDatePicker.todayInUtcMilliseconds()))
                .build();

        this.calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDatePicker.show(getSupportFragmentManager(), "tag_picker");
                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        dateRangeText.setText(materialDatePicker.getHeaderText());
                    }
                });
            }
        });


        FloatingActionButton floatingActionButton = findViewById(R.id.fab_add);
        Activity activity = this;
        App.setCurrentActivity(activity);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText((AppCompatActivity) activity, "Fab pressed", Toast.LENGTH_SHORT).show();
                Intent backHome = new Intent(NewTakeOut.this, HomeActivity.class);
                startActivity(backHome);
            }
        });
    }











    //private FragmentManager supportFragmentManager; //definiscilo con getSuportFragmentManager()
    //TODO
    /*val  dateRangePicker =
            MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Select dates")
        .build()
        .show(supportFragmentManager, "tag");*/
}

package com.example.rendezvous;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

public class NewTakeOut extends AppCompatActivity {
    /**to delete, but not now*/
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
                .setTitleText("Select dates")
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
    }











    //private FragmentManager supportFragmentManager; //definiscilo con getSuportFragmentManager()
    //TODO
    /*val  dateRangePicker =
            MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Select dates")
        .build()
        .show(supportFragmentManager, "tag");*/
}

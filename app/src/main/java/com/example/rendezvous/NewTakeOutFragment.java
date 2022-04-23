package com.example.rendezvous;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

public class NewTakeOutFragment extends Fragment {
    private static final String LOG_TAG = "NewTakeOutFragment";
    private TextView dateRangeText;
    private Button calendar;
    private FragmentActivity activity;


    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_take_out, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.activity = getActivity();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(activity != null){
            this.dateRangeText = activity.findViewById(R.id.show_date);
            this.calendar = activity.findViewById(R.id.button_open_calendar);
            MaterialDatePicker<Pair<Long, Long>> materialDatePicker = MaterialDatePicker.Builder.dateRangePicker()
                    .setSelection(Pair.create(MaterialDatePicker.thisMonthInUtcMilliseconds(),
                            MaterialDatePicker.todayInUtcMilliseconds()))
                    .setTitleText("Select dates")
                    .build();

            this.calendar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    materialDatePicker.show(activity.getSupportFragmentManager(), "tag_picker");
                    materialDatePicker.addOnPositiveButtonClickListener((MaterialPickerOnPositiveButtonClickListener) selection -> dateRangeText.setText(materialDatePicker.getHeaderText()));
                }
            });
        }



        //setHasOptionsMenu(true);
    }


}

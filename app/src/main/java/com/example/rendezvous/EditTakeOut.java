package com.example.rendezvous;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rendezvous.DB.Converters;
import com.example.rendezvous.DB.Info;
import com.example.rendezvous.DB.RendezVous;
import com.example.rendezvous.DB.RendezVousDB;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class EditTakeOut extends AppCompatActivity {

    private String R_title;
    private Integer I_ID;
    private List<Date> preferencies = new ArrayList<>();
    private boolean busy = false;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_take_out);

        LinearLayout scrollView = (LinearLayout) this.findViewById(R.id.dates_container);

        System.out.println("scrollView = " + scrollView);
        List<CheckBox> datesList = new ArrayList<>();

        RendezVousDB db = RendezVousDB.getInstance(EditTakeOut.this.getBaseContext());

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            R_title = (String) extras.get("R_title");
            I_ID = (Integer) extras.get("I_ID");
        }

        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Calendar cal = Calendar.getInstance();
                RendezVous rendezVous = db.databaseDAO().getRendezVousFromInfo(I_ID);
                cal.setTime(Converters.fromTimestamp(rendezVous.getR_DataI()));
                Date endDate = Converters.fromTimestamp(rendezVous.getR_DataF());

//                System.out.println(" fisrtDate = " + Converters.fromTimestamp(rendezVous.getR_DataI()));
//                System.out.println("endDate = " + endDate);
                while(!cal.getTime().equals(endDate)){
                    cal.add(Calendar.DATE, 1);

                    CheckBox box = new CheckBox(EditTakeOut.this.getBaseContext());
                    box.setText(cal.get(Calendar.YEAR) + "-" + cal.get(Calendar.MONTH) + "-" + cal.get(Calendar.DAY_OF_MONTH));
                    box.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            if(b){
                                try {
                                    preferencies.add(simpleDateFormat.parse(compoundButton.getText().toString()));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }else {
                                try {
                                    preferencies.remove(simpleDateFormat.parse(compoundButton.getText().toString()));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }
                            System.out.println("preferencies = " + preferencies);
                        }
                    });

                    scrollView.addView(box);
                    cal.setTime(cal.getTime());
                }
                CheckBox box = new CheckBox(EditTakeOut.this.getBaseContext());
                box.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                box.setText("I'm a busy person");
                box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        busy = b;
                    }
                });
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        scrollView.addView(box);

                    }
                });

            }
        });

        FloatingActionButton floatingActionButton = findViewById(R.id.fab_edit);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(busy){
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            db.databaseDAO().setBusy();
                        }
                    });
                }else if(preferencies.size() == 1){
                    System.out.println("Im running");
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            db.databaseDAO().updateInvited(Converters.dateToTimestamp(preferencies.get(0)));
                        }
                    });
                }else {
                    Toast.makeText(EditTakeOut.this, "You have to chose only 1 date", Toast.LENGTH_SHORT).show();
                }
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

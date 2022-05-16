package com.example.rendezvous;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.rendezvous.DB.Circle;
import com.example.rendezvous.DB.CircleOfFriends;
import com.example.rendezvous.DB.Converters;
import com.example.rendezvous.DB.RendezVous;
import com.example.rendezvous.DB.RendezVousDB;
import com.example.rendezvous.DB.User;
import com.example.rendezvous.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserPage extends AppCompatActivity {
    private User activeUser;
    private List<Circle> circleList;
    private  TextInputEditText name ;
    private TextInputEditText surname;
    private List<Circle> selectedCircles = new ArrayList<>();
    private List<String> alreadyMember;
    private List<CheckBox> checkBoxList = new ArrayList<>();

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
                circleList = db.databaseDAO().getCircles();
                alreadyMember = db.databaseDAO().getUserCircles(db.databaseDAO().getUID(activeUser.getUserName()));
                ScrollView scrollView = findViewById(R.id.circle_checkbox);
                LinearLayout layout = (LinearLayout) scrollView.getChildAt(0);

                for (Circle c: circleList) {
                    CheckBox box = new CheckBox(UserPage.this.getBaseContext());
                    box.setText(c.getC_name());
                    box.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            selectedCircles.add(c);
                        }
                    });
                    if(alreadyMember.contains(c.getC_name())){
                        box.setChecked(true); //non testato
                    }
                    layout.addView(box);
                    checkBoxList.add(box);
                }
            }
        });




        FloatingActionButton done = (FloatingActionButton) findViewById(R.id.done_insert_info);

        done.setOnClickListener(view -> {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    activeUser = db.databaseDAO().getActiveUser();
                    db.databaseDAO().updateUser(db.databaseDAO().getUID(activeUser.getUserName()), name.getText().toString(), surname.getText().toString());
                    for (Circle circleToEnter: selectedCircles
                         ) {
                    db.databaseDAO().insertCircleOfFriends(new CircleOfFriends(circleToEnter.getC_name(), db.databaseDAO().getUID(activeUser.getUserName())));
                    }
                    for (CheckBox box: checkBoxList
                         ) {
                       if(!box.isChecked()){
                            db.databaseDAO().removeFromCircle(db.databaseDAO().getUID(activeUser.getUserName()), box.getText().toString());
                       }
                    }
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

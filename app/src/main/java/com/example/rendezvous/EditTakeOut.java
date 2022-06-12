package com.example.rendezvous;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.amrdeveloper.lottiedialog.LottieDialog;
import com.example.rendezvous.DB.ConfirmedRendezvous;
import com.example.rendezvous.DB.Converters;
import com.example.rendezvous.DB.Info;
import com.example.rendezvous.DB.Invited;
import com.example.rendezvous.DB.RendezVous;
import com.example.rendezvous.DB.RendezVousDB;
import com.example.rendezvous.DB.User;
import com.example.rendezvous.ViewModel.CustomExpandableListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class EditTakeOut extends AppCompatActivity {

    private String R_title;
    private Integer I_ID;
    private List<Date> preferencies = new ArrayList<>();
    private boolean busy = false;
    String pattern = "yyyy-MM-dd";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

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

        setUpGUI(R_title, I_ID);


        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Calendar cal = Calendar.getInstance();
                RendezVous rendezVous = db.databaseDAO().getSingleRendezVousFromInfo(I_ID);
                cal.setTime(Converters.fromTimestamp(rendezVous.getR_DataI()));
                Date endDate = Converters.fromTimestamp(rendezVous.getR_DataF());

//                System.out.println(" fisrtDate = " + Converters.fromTimestamp(rendezVous.getR_DataI()));
//                System.out.println("endDate = " + endDate);
                while(!cal.getTime().equals(endDate)){
                    cal.add(Calendar.DATE, 1);

                    CheckBox box = new CheckBox(EditTakeOut.this.getBaseContext());
                    box.setText(cal.get(Calendar.YEAR) + "-" + cal.get(Calendar.MONTH) + "-" + cal.get(Calendar.DAY_OF_MONTH));
                    box.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    box.setPadding(20, 20, 20, 20);
                    box.setButtonDrawable(R.drawable.checkbox_design_user_page);
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

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.addView(box);
                        }
                    });

                    cal.setTime(cal.getTime());
                }
                CheckBox box = new CheckBox(EditTakeOut.this.getBaseContext());
                box.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                box.setPadding(20, 20, 20, 20);
                box.setButtonDrawable(R.drawable.checkbox_design_user_page);
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
        floatingActionButton.setBackgroundColor(Color.parseColor("#0da6f9"));
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        if (busy) {
                                    db.databaseDAO().setBusy();
                        } else if (preferencies.size() == 1) {
                                    db.databaseDAO().updateInvited(Converters.dateToTimestamp(preferencies.get(0)));
                        } else {
                            EditTakeOut.this.runOnUiThread(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(EditTakeOut.this, "You have to chose only 1 date", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                            );
                        }
                        if (busy || preferencies.size() == 1) {
                            db.databaseDAO().updateInvited(Converters.dateToTimestamp(preferencies.get(0)));

                            Integer totalInvited = db.databaseDAO().getTotalNumOfPartecipants(I_ID);
                            Integer actualResponse = db.databaseDAO().getNumOfPartecipants(I_ID);

                            if (totalInvited.equals(actualResponse)) {
//                            db.databaseDAO().insertConfirmedRendezVous(R_title, I_ID);
                                long date = db.databaseDAO().getConfirmedDate(I_ID);
                                List<Integer> partecipants = db.databaseDAO().getPartecipantsId(I_ID);

                                for (Integer partecipant_ID :
                                        partecipants) {
                                    db.databaseDAO().insertConfirmedRendezvous(new ConfirmedRendezvous(I_ID, date, partecipant_ID, I_ID));
                                }


                            }

                       }


//                        Intent backHome = new Intent(EditTakeOut.this, HomeActivity.class);
//                        startActivity(backHome);
                }
            });
                if (busy || preferencies.size() == 1) {
                    LottieDialog dialog = new LottieDialog(EditTakeOut.this)
                            .setAnimation(R.raw.message_sent)
                            .setAnimationRepeatCount(1)
                            .setAutoPlayAnimation(true)
                            .setMessage("Your reply has been sent, hope you said yes you shut-in NEET")
                            .setOnDismissListener(x -> {
                                EditTakeOut.this.finish();
                            });
                    dialog.show();
                }
            }
        });
    }

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

    private void setUpGUI(String r_title, Integer i_id) {
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);

        RendezVousDB db = RendezVousDB.getInstance(EditTakeOut.this.getBaseContext());

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Info takeOutInfo = db.databaseDAO().getInfo(r_title);
                RendezVous rendezVous = db.databaseDAO().getSingleRendezVousFromInfo(i_id);

                //name
                TextView name = (TextView) findViewById(R.id.name_rendez_vous);
                name.setText(takeOutInfo.getTitle());

                //message description
                TextView descr = (TextView) findViewById(R.id.descriptionMessage);
                System.out.println("takeOutInfo.getDescription() = " + takeOutInfo.getDescription());

                Calendar cal1 = Calendar.getInstance();
                cal1.setTime(Converters.fromTimestamp(rendezVous.getR_DataF()));
                Calendar cal2 = Calendar.getInstance();
                cal2.setTime(Converters.fromTimestamp(rendezVous.getR_DataI()));
                String pricelessDescription =  "You have been invited to partecipate to " + takeOutInfo.getTitle() + "." +
                            "\nWe'll meet in a day beetween " + cal1.get(Calendar.YEAR) + "-" + cal1.get(Calendar.MONTH) + "-" + cal1.get(Calendar.DAY_OF_MONTH) +
                            " and " + cal2.get(Calendar.YEAR) + "-" + cal2.get(Calendar.MONTH) + "-" + cal2.get(Calendar.DAY_OF_MONTH) +
                            "\nLet me know when you are available!";
                descr.setText(takeOutInfo.getDescription().isEmpty() ? pricelessDescription : pricelessDescription + "\n" + takeOutInfo.getDescription());

                //avatar author
                User author = db.databaseDAO().getUser(rendezVous.getR_authorID());
                TextView userAuthor = (TextView) findViewById(R.id.authorUsername);
                userAuthor.setText(author.getUserName() + " \n AKA \n" + author.getNome() + " " + author.getCognome());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(author.getURIavatar()!= null){
                            Uri imageUri = Uri.parse(author.getURIavatar().toString());
                            final InputStream imageStream;
                            try {
                                imageStream = getContentResolver().openInputStream(imageUri);
                                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                                ImageView avatarAuthor = (ImageView) findViewById(R.id.authorImage);
                                avatarAuthor.setImageBitmap(selectedImage);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });


                HashMap<String, String> peopleInvited = new HashMap<>();
                List<Invited> invitedList = db.databaseDAO().getInvited(i_id);
                for (Invited guest:
                     invitedList) {
                    User human = db.databaseDAO().getGuestNameSurname(guest.getIU_ID());
                    String concatRoom = human.getNome() + " " + human.getCognome();
                    // Busy -> testo rosso, impegnato | Partecipa -> testo verde | Invited -> grigio ?
                    peopleInvited.put(concatRoom, guest.getI_state() );
                }

                expandableListDetail.put("Partecipants to the event", new ArrayList<>(peopleInvited.keySet()));
                expandableListTitle = new ArrayList<>(expandableListDetail.keySet()); //qui tutto testing.....
                expandableListAdapter = new CustomExpandableListAdapter(EditTakeOut.this, expandableListTitle, expandableListDetail, peopleInvited);
                expandableListView.setAdapter(expandableListAdapter);

            }
        });

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        expandableListTitle.get(groupPosition) + " List Expanded.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        expandableListTitle.get(groupPosition) + " List Collapsed.",
                        Toast.LENGTH_SHORT).show();

            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Toast.makeText(
                        getApplicationContext(),
                        expandableListTitle.get(groupPosition)
                                + " -> "
                                + expandableListDetail.get(
                                expandableListTitle.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT
                ).show();
                return false;
            }
        });
        return;
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

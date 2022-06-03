package com.example.rendezvous;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.rendezvous.DB.RendezVousDB;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;
import java.util.Objects;

public class CalendarFragment extends Fragment {
    private static final String LOG_TAG = "HomeCalendarFragment";
    private Activity activity;
    private DrawerLayout dLayout;
    Fragment fragment = this;

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity){
            activity = (FragmentActivity) context;
        }
    }*/

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
        return inflater.inflate(R.layout.calendar_layout, container, false);

//        View view = inflater.inflate(R.layout.calendar_layout, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
//            setHasOptionsMenu(true);
        MaterialToolbar materialToolbar = (MaterialToolbar) activity.findViewById(R.id.toolbar);
        //Toolbar materialToolbar = activity.findViewById(R.id.toolbar);
        materialToolbar.setTitle(R.string.app_name);
        materialToolbar.setTitleTextColor(Color.WHITE);
        System.out.println("materialToolbar = " + materialToolbar);
        System.out.println("fragment = " + fragment);
        ((AppCompatActivity) activity).setSupportActionBar(materialToolbar);
//            AZZARDO
        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("IMMA GOT CLICKED");
                dLayout.openDrawer(Gravity.LEFT);
                //        Workaround per impedire che il fragment diventi timido quando la navigationView esce fuori

                FragmentManager fragmentManager = ((AppCompatActivity) activity).getSupportFragmentManager();
                fragmentManager.beginTransaction().hide(fragment).commit();

            }
        });
        setNavigationDrawer(activity);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }
        @Override
        public void onCreate (@Nullable Bundle savedInstanceState){
            super.onCreate(savedInstanceState);


    }




    public static long pushAppointmentsToCalender(Activity curActivity, String title, String addInfo, String place, int status, long startDate, boolean needReminder, boolean needMailService) {
        /***************** Event: note(without alert) *******************/

        String eventUriString = "content://com.android.calendar/events";
        ContentValues eventValues = new ContentValues();

        eventValues.put("calendar_id", 1); // id, We need to choose from
        // our mobile for primary
        // its 1
        eventValues.put("title", title);
        eventValues.put("description", addInfo);
        eventValues.put("eventLocation", place);

        long endDate = startDate + 1000 * 60 * 60; // For next 1hr

        eventValues.put("dtstart", startDate);
        eventValues.put("dtend", endDate);

        // values.put("allDay", 1); //If it is bithday alarm or such
        // kind (which should remind me for whole day) 0 for false, 1
        // for true
        eventValues.put("eventStatus", status); // This information is
        // sufficient for most
        // entries tentative (0),
        // confirmed (1) or canceled
        // (2):
        eventValues.put("eventTimezone", "UTC/GMT +2:00");
        /*Comment below visibility and transparency  column to avoid java.lang.IllegalArgumentException column visibility is invalid error */

    /*
    eventValues.put("visibility", 3); // visibility to default (0),
                                        // confidential (1), private
                                        // (2), or public (3):
    eventValues.put("transparency", 0); // You can control whether
                                        // an event consumes time
                                        // opaque (0) or transparent
                                        // (1).
    */
        eventValues.put("hasAlarm", 0); // 0 for false, 1 for true

        Uri eventUri = curActivity.getApplicationContext().getContentResolver().insert(Uri.parse(eventUriString), eventValues);
        long eventID = Long.parseLong(eventUri.getLastPathSegment());

        if (needReminder) {
            /***************** Event: Reminder(with alert) Adding reminder to event *******************/

            String reminderUriString = "content://com.android.calendar/reminders";

            ContentValues reminderValues = new ContentValues();

            reminderValues.put("event_id", eventID);
            reminderValues.put("minutes", 5); // Default value of the
            // system. Minutes is a
            // integer
            reminderValues.put("method", 1); // Alert Methods: Default(0),
            // Alert(1), Email(2),
            // SMS(3)

            Uri reminderUri = curActivity.getApplicationContext().getContentResolver().insert(Uri.parse(reminderUriString), reminderValues);
        }

        /***************** Event: Meeting(without alert) Adding Attendies to the meeting *******************/

        if (needMailService) {
            String attendeuesesUriString = "content://com.android.calendar/attendees";

            /********
             * To add multiple attendees need to insert ContentValues multiple
             * times
             ***********/
            ContentValues attendeesValues = new ContentValues();

            attendeesValues.put("event_id", eventID);
            attendeesValues.put("attendeeName", "xxxxx"); // Attendees name
            attendeesValues.put("attendeeEmail", "yyyy@gmail.com");// Attendee
            // E
            // mail
            // id
            attendeesValues.put("attendeeRelationship", 0); // Relationship_Attendee(1),
            // Relationship_None(0),
            // Organizer(2),
            // Performer(3),
            // Speaker(4)
            attendeesValues.put("attendeeType", 0); // None(0), Optional(1),
            // Required(2), Resource(3)
            attendeesValues.put("attendeeStatus", 0); // NOne(0), Accepted(1),
            // Decline(2),
            // Invited(3),
            // Tentative(4)

            Uri attendeuesesUri = curActivity.getApplicationContext().getContentResolver().insert(Uri.parse(attendeuesesUriString), attendeesValues);
        }

        return eventID;

    }






















    private void setNavigationDrawer(AppCompatActivity activity) {
        dLayout = (DrawerLayout) activity.findViewById(R.id.drawer_layout); // initiate a DrawerLayout
        NavigationView navView = (NavigationView) activity.findViewById(R.id.navigation); // initiate a Navigation View
// implement setNavigationItemSelectedListener event on NavigationView

//        Workaround per impedire che il fragment diventi timido quando la navigationView esce fuori
        dLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                FragmentManager fragmentManager = ((AppCompatActivity) activity).getSupportFragmentManager();
                fragmentManager.beginTransaction().show(fragment).commit();
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {


            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Fragment frag = null; // create a Fragment Object
                int itemId = menuItem.getItemId(); // get selected menu item's id
// check selected menu item's id and replace a Fragment Accordingly

                if (itemId == R.id.first) {
                    /*
                        Use of fragments... we need activity,  now we'll use intent
                     */
                    //frag = new CardList();
                    //CardListActivity cardListActivity = new CardListActivity();
                    startActivity(new Intent(activity, CardListActivity.class));
                    //startActivity(new Intent("CardListActivity"));
                } else if (itemId == R.id.second) {
                    /*
                        Redunant and useless, but this new fragment works, older is dead.....
                     */
//                    frag = new CalendarFragment();
                    startActivity(new Intent(activity, HomeActivity.class));
                }
                else if (itemId == R.id.third) {
                    startActivity(new Intent(activity, UserPage.class));
                } else if (itemId == R.id.fourth) {
                    RendezVousDB db = RendezVousDB.getInstance(activity.getBaseContext());
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            db.databaseDAO().setUserLoggedOut();
                        }
                    });
                    System.exit(0);
                }
// display a toast message with menu item's title
                Toast.makeText(activity.getApplicationContext(), menuItem.getTitle(), Toast.LENGTH_SHORT).show();


                if (frag != null) {
//                    Utilities.insertFragment((AppCompatActivity) activity, frag, frag.getClass().getSimpleName());

                    /*
                        Removes overlapping beetween fragments
                        FrameLayout fl = (FrameLayout) activity.findViewById(R.id.calendar_frame_layout);
                        fl.removeAllViews(); (queste righe non funzionano)
                        MANNAGGIA ALL'OVERLAPPING CHE NON RIESCO A TOGLIERLO
                     */

                    // Qui non ci arriva mai in realta, lo teniamo per completezza se dovessimo cambiare qualcosa al volo
                    activity.getSupportFragmentManager().beginTransaction().
                    remove(Objects.requireNonNull(activity.getSupportFragmentManager().findFragmentById(R.id.calendar_frame_layout))).commit();
                    FragmentTransaction transaction = ((AppCompatActivity)activity).getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame, frag); // replace a Fragment with Frame Layout
                    transaction.commit(); // commit the changes
                    dLayout.closeDrawers(); // close the all open Drawer Views
                    return true;
                }

                return false;
            }

        });

    }


}

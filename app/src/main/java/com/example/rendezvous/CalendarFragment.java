package com.example.rendezvous;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class CalendarFragment extends Fragment {
    private static final String LOG_TAG = "HomeCalendarFragment";
    private Activity activity;
    private DrawerLayout dLayout;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity){
            activity = (FragmentActivity) context;
        }
    }

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
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Activity activity = getActivity();


    }
        /*if (activity != null) {
            Utilities.setUpToolbar((AppCompatActivity) activity, getString(R.string.app_name));*/

            //setRecyclerView(activity);

            /*listViewModel = new ViewModelProvider(activity).get(ListViewModel.class);
            listViewModel.getCardItems().observe(activity, new Observer<List<CardItem>>() {
                @Override
                public void onChanged(List<CardItem> cardItems) {
                    adapter.setData(cardItems);
                }
            });*/


        /*} else {
            Log.e(LOG_TAG, "Activity is null");
        }*/



        @Override
        public void onCreate (@Nullable Bundle savedInstanceState){
            super.onCreate(savedInstanceState);

            Activity activity = getActivity();
            //setHasOptionsMenu(true);

            MaterialToolbar materialToolbar = (MaterialToolbar) activity.findViewById(R.id.toolbar);
            ((AppCompatActivity) activity).setSupportActionBar(materialToolbar);
//            AZZARDO

            materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dLayout.openDrawer(Gravity.LEFT);
                }
            });

            setNavigationDrawer(activity);

        }
    private void setNavigationDrawer(Activity activity) {
        dLayout = (DrawerLayout) activity.findViewById(R.id.drawer_layout); // initiate a DrawerLayout
        NavigationView navView = (NavigationView) activity.findViewById(R.id.navigation); // initiate a Navigation View
// implement setNavigationItemSelectedListener event on NavigationView
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Fragment frag = null; // create a Fragment Object
                int itemId = menuItem.getItemId(); // get selected menu item's id
// check selected menu item's id and replace a Fragment Accordingly

                if (itemId == R.id.first) {
                    frag = new CardList();
                } else if (itemId == R.id.second) {
                    frag = new CalendarFragment();
                }
//                else if (itemId == R.id.third) {
//                    frag = new ThirdFragment();
//                }
// display a toast message with menu item's title
                Toast.makeText(activity.getApplicationContext(), menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                if (frag != null) {
//                    Utilities.insertFragment((AppCompatActivity) activity, frag, frag.getClass().getSimpleName());

                    /*
                        Removes overlapping beetween fragments
                     */
                    FrameLayout fl = (FrameLayout) activity.findViewById(R.id.calendar_frame_layout);
                    fl.removeAllViews();

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

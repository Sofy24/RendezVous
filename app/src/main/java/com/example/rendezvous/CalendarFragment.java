package com.example.rendezvous;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

public class CalendarFragment extends Fragment {
    private static final String LOG_TAG = "HomeCalendarFragment";
    private Activity activity;
    private DrawerLayout dLayout;
    Fragment frrrrr = this;

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
//            setHasOptionsMenu(true);

            MaterialToolbar materialToolbar = (MaterialToolbar) activity.findViewById(R.id.toolbar);
            ((AppCompatActivity) activity).setSupportActionBar(materialToolbar);
//            AZZARDO

            materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dLayout.openDrawer(Gravity.LEFT);
//        Workaround per impedire che il fragment diventi timido quando la navigationView esce fuori

                    FragmentManager fragmentManager = ((AppCompatActivity) activity).getSupportFragmentManager();
                    fragmentManager.beginTransaction().hide(frrrrr).commit();

                }
            });

            setNavigationDrawer(activity);

        }

    private void setNavigationDrawer(Activity activity) {
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
                fragmentManager.beginTransaction().show(frrrrr).commit();
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
                        FrameLayout fl = (FrameLayout) activity.findViewById(R.id.calendar_frame_layout);
                        fl.removeAllViews(); (queste righe non funzionano)
                        MANNAGGIA ALL'OVERLAPPING CHE NON RIESCO A TOGLIERLO
                     */


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

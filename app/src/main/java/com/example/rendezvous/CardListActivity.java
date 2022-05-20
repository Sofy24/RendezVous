package com.example.rendezvous;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rendezvous.DB.Info;
import com.example.rendezvous.DB.RendezVousDB;
import com.example.rendezvous.ViewModel.RecyclerTouchListener;
import com.example.rendezvous.ViewModel.RecyclerviewAdapter;
import com.example.rendezvous.ViewModel.RendezVousCard;
import com.example.rendezvous.ViewModel.Task;

import java.util.ArrayList;
import java.util.List;

public class CardListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerviewAdapter recyclerviewAdapter;
    private RecyclerTouchListener touchListener;
    private List<Info> infos;
    ImageButton arrow;
    LinearLayout hiddenView;
    CardView cardView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_take_out);


        recyclerView = findViewById(R.id.recyclerView);
        recyclerviewAdapter = new RecyclerviewAdapter(this);
        recyclerView.setAdapter(recyclerviewAdapter);
        //recyclerviewAdapter.notifyDataStateChanged();

//        final List<Task> taskList = new ArrayList<>();
//        Task task = new Task("Buy Dress","Buy Dress at Shoppershop for coming functions");
//        taskList.add(task);
//        task = new Task("Go For Walk","Wake up 6AM go for walking");
//        taskList.add(task);
//        task = new Task("Office Work","Complete the office works on Time");
//        taskList.add(task);
//        task = new Task("watch Repair","Give watch to service center");
//        taskList.add(task);
//        task = new Task("Recharge Mobile","Recharge for 10$ to my **** number");
//        taskList.add(task);
//        task = new Task("Read book","Read android book completely");
//        taskList.add(task);
        cardView = findViewById(R.id.base_cardview);
        arrow = findViewById(R.id.arrow_button);
        hiddenView = findViewById(R.id.hidden_view);

        /*arrow.setOnClickListener(new View.OnClickListener() {
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
        });*/




        final List<RendezVousCard> rendezVousCards = new ArrayList<>();
        RendezVousDB db = RendezVousDB.getInstance(CardListActivity.this.getBaseContext());

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                infos = db.databaseDAO().getListCardsForActiveUser();
                for (Info singleInfo:
                     infos) {
                    rendezVousCards.add(new RendezVousCard(singleInfo.getTitle(), singleInfo.getImageURL()));
                    recyclerviewAdapter.setTaskList(rendezVousCards);
                    recyclerView.setAdapter(recyclerviewAdapter);

                }
            }
        });
//        rendezVousCards.add(new RendezVousCard("Uscita bellissima", null));
//        rendezVousCards.add(new RendezVousCard("Uscita spiacevole", null));
//        rendezVousCards.add(new RendezVousCard("Uscita tra uomini", null));


        touchListener = new RecyclerTouchListener(this,recyclerView);
        touchListener
                .setClickable(new RecyclerTouchListener.OnRowClickListener() {
                    @Override
                    public void onRowClicked(int position) {
                        Toast.makeText(getApplicationContext(),rendezVousCards.get(position).getTitle(), Toast.LENGTH_SHORT).show();
                        Intent openCardDetails = new Intent(CardListActivity.this, RendeVousCard.class);
                        startActivity(openCardDetails);
                    }

                    @Override
                    public void onIndependentViewClicked(int independentViewID, int position) {

                    }
                })
                .setSwipeOptionViews(R.id.delete_task,R.id.edit_task)
                .setSwipeable(R.id.rowFG, R.id.rowBG, new RecyclerTouchListener.OnSwipeOptionsClickListener() {
                    @Override
                    public void onSwipeOptionClicked(int viewID, int position) {
                        switch (viewID){
                            case R.id.delete_task:
                                rendezVousCards.remove(position);
                                recyclerviewAdapter.setTaskList(rendezVousCards);
                                break;
                            case R.id.edit_task:
                                Toast.makeText(getApplicationContext(),"Edit!",Toast.LENGTH_SHORT).show();
                                Intent openEditTakeOut = new Intent(CardListActivity.this, EditTakeOut.class);
                                startActivity(openEditTakeOut);
                                break;

                        }
                    }
                });
        recyclerView.addOnItemTouchListener(touchListener);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_app_bar, menu);
        return true;
    }
    /*
        Necessary ???????
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }
}

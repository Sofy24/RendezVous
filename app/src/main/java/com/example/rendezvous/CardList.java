package com.example.rendezvous;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rendezvous.ViewModel.RecyclerTouchListener;
import com.example.rendezvous.ViewModel.RecyclerviewAdapter;
import com.example.rendezvous.ViewModel.RendezVousCard;
import com.example.rendezvous.ViewModel.Task;

import java.util.ArrayList;
import java.util.List;

/*
    Cardlist dovrebbe essere un activity, altrimenti sembra non funzionare bene affatto......
 */
public class CardList extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerviewAdapter recyclerviewAdapter;
    private RecyclerTouchListener touchListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_take_out, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity activity = getActivity();

        System.out.println("activity = " + activity);
        recyclerView = activity.findViewById(R.id.recyclerView);
        recyclerviewAdapter = new RecyclerviewAdapter(activity.getBaseContext());

        System.out.println("recyclerView = " + recyclerView);
        final List<RendezVousCard> rendezVousCards = new ArrayList<>();
        recyclerviewAdapter.setTaskList(rendezVousCards);
        recyclerView.setAdapter(recyclerviewAdapter);

        touchListener = new RecyclerTouchListener(activity,recyclerView);
        touchListener
                .setClickable(new RecyclerTouchListener.OnRowClickListener() {
                    @Override
                    public void onRowClicked(int position) {
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
                                Toast.makeText(activity.getApplicationContext(),"Edit Not Available",Toast.LENGTH_SHORT).show();
                                break;

                        }
                    }
                });
        recyclerView.addOnItemTouchListener(touchListener);
    }
}

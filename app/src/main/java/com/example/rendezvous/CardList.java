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
        final List<Task> taskList = new ArrayList<>();
        Task task = new Task("Buy Dress","Buy Dress at Shoppershop for coming functions");
        taskList.add(task);
        task = new Task("Go For Walk","Wake up 6AM go for walking");
        taskList.add(task);
        task = new Task("Office Work","Complete the office works on Time");
        taskList.add(task);
        task = new Task("watch Repair","Give watch to service center");
        taskList.add(task);
        task = new Task("Recharge Mobile","Recharge for 10$ to my **** number");
        taskList.add(task);
        task = new Task("Read book","Read android book completely");
        taskList.add(task);
        recyclerviewAdapter.setTaskList(taskList);
        recyclerView.setAdapter(recyclerviewAdapter);

        touchListener = new RecyclerTouchListener(activity,recyclerView);
        touchListener
                .setClickable(new RecyclerTouchListener.OnRowClickListener() {
                    @Override
                    public void onRowClicked(int position) {
                        Toast.makeText(activity.getApplicationContext(),taskList.get(position).getName(), Toast.LENGTH_SHORT).show();
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
                                taskList.remove(position);
                                recyclerviewAdapter.setTaskList(taskList);
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

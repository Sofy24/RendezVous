package com.example.rendezvous;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rendezvous.ViewModel.RecyclerTouchListener;
import com.example.rendezvous.ViewModel.RecyclerviewAdapter;
import com.example.rendezvous.ViewModel.Task;

import java.util.ArrayList;
import java.util.List;

public class CardListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerviewAdapter recyclerviewAdapter;
    private RecyclerTouchListener touchListener;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_take_out);


        recyclerView = findViewById(R.id.recyclerView);
        recyclerviewAdapter = new RecyclerviewAdapter(this);

        System.out.println("You son of a bith mi in ");
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

        touchListener = new RecyclerTouchListener(this,recyclerView);
        touchListener
                .setClickable(new RecyclerTouchListener.OnRowClickListener() {
                    @Override
                    public void onRowClicked(int position) {
                        Toast.makeText(getApplicationContext(),taskList.get(position).getName(), Toast.LENGTH_SHORT).show();
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

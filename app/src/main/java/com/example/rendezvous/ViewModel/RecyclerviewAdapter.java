package com.example.rendezvous.ViewModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rendezvous.R;

import java.util.ArrayList;
import java.util.List;



public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewAdapter.MyViewHolder>{

    private Context mContext;
    private List<Task> taskList;

    public RecyclerviewAdapter(Context context){
        mContext = context;
        taskList = new ArrayList<>();
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerviewAdapter.MyViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.tvTaskName.setText(task.getName());
        holder.tvTaskDesc.setText(task.getDesc());
    }


    @NonNull
    @Override
    public RecyclerviewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View view = LayoutInflater.from(mContext).inflate(R.layout.task_item,parent,false);
        View view = LayoutInflater.from(mContext).inflate(R.layout.task,parent,false);
        System.out.println("view = " + view);
        return new MyViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTaskName;
        private TextView tvTaskDesc;
        public MyViewHolder(View itemView) {
            super(itemView);

            System.out.println("itemView = " + itemView);
            tvTaskName = itemView.findViewById(R.id.task_name);
            tvTaskDesc = itemView.findViewById(R.id.task_desc);
        }
    }
}

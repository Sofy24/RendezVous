package com.example.rendezvous.ViewModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rendezvous.R;

import java.util.ArrayList;
import java.util.List;



public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewAdapter.MyViewHolder>{

    private Context mContext;
    private List<RendezVousCard> taskList;

    public RecyclerviewAdapter(Context context){
        mContext = context;
        taskList = new ArrayList<>();
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerviewAdapter.MyViewHolder holder, int position) {
        RendezVousCard task = taskList.get(position);
        holder.tvCardTitle.setText(task.getTitle());
        if(task.getImageUri()!=null) {
            //TODO non e' un text ma un imgView -> ce' da piangere
            //holder.tvCardUri.setText(task.getImageUri());
        }
    }


    @NonNull
    @Override
    public RecyclerviewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View view = LayoutInflater.from(mContext).inflate(R.layout.task_item,parent,false);
        View view = LayoutInflater.from(mContext).inflate(R.layout.rendezvous_card,parent,false);
        System.out.println("view = " + view);
        return new MyViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public void setTaskList(List<RendezVousCard> taskList) {
        this.taskList = taskList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCardTitle;
        private ImageView tvCardUri;
        public MyViewHolder(View itemView) {
            super(itemView);

            System.out.println("itemView = " + itemView);
            tvCardTitle = itemView.findViewById(R.id.rendezvous_title_card);
            tvCardUri = itemView.findViewById(R.id.rendezvous_image_card);
        }
    }
}

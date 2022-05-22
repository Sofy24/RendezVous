package com.example.rendezvous.ViewModel;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rendezvous.CardListActivity;
import com.example.rendezvous.NewTakeOut;
import com.example.rendezvous.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;



public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewAdapter.MyViewHolder>{

    private Context mContext;
    private List<RendezVousCard> taskList;


    public RecyclerviewAdapter(Context context){
        mContext = context;
        taskList = new ArrayList<>();
    }


    /** codice google maps da inserire nella card
     *                     Intent intent_map = new Intent(Intent.ACTION_VIEW);
     *                     intent_map.setData(Uri.parse("geo:47.4925,19.0513"));
     *                     Intent chooser = Intent.createChooser(intent_map, "Launch maps");
     *                     startActivity(chooser); **/

    @Override
    public void onBindViewHolder(@NonNull RecyclerviewAdapter.MyViewHolder holder, int position) {
        RendezVousCard task = taskList.get(position);
        holder.tvCardTitle.setText(task.getTitle());
        if(task.getImageUri()!=null) {
            //TODO non e' un text ma un imgView -> c'è da piangere
            //holder.tvCardUri.setText(task.getImageUri());
        }
    }



    @NonNull
    @Override
    public RecyclerviewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {





        //View view = LayoutInflater.from(mContext).inflate(R.layout.task_item,parent,false);
        //View view = LayoutInflater.from(mContext).inflate(R.layout.rendezvous_card,parent,false);
        View view = LayoutInflater.from(mContext).inflate(R.layout.card_details,parent,false);

        CardView cardView = view.findViewById(R.id.base_cardview);
        LinearLayout hiddenView = view.findViewById(R.id.hidden_view);
        ImageButton arrow = (ImageButton)  view.findViewById(R.id.arrow_button);
        arrow.setOnClickListener(view1 -> {
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
        });
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
        private TextView distanceView;
        private float distance = 0.0F;

        public MyViewHolder(View itemView) {
            super(itemView);

            System.out.println("itemView = " + itemView);
            tvCardTitle = itemView.findViewById(R.id.rendezvous_title_card);
            tvCardUri = itemView.findViewById(R.id.rendezvous_image_card);
            this.distanceView = itemView.findViewById(R.id.distance_card);
            this.distanceView.setText(distance + " metri.");
        }
    }
}

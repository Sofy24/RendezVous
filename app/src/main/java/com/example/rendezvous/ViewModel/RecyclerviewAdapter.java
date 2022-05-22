package com.example.rendezvous.ViewModel;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.location.Location;
import android.os.AsyncTask;
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

import com.example.rendezvous.DB.Info;
import com.example.rendezvous.DB.RendezVousDB;
import com.example.rendezvous.NewTakeOut;
import com.example.rendezvous.R;
import java.util.ArrayList;
import java.util.List;



public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewAdapter.MyViewHolder>{

    private Context mContext;
    private List<RendezVousCard> taskList;
    private Location location = null;
    private int viewPosition;

    public RecyclerviewAdapter(Context context){
        mContext = context;
        taskList = new ArrayList<>();
        this.viewPosition = -1;
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
        this.viewPosition++;
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

    public void setCurrentLocation(Location value){
        this.location = value;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCardTitle;
        private ImageView tvCardUri;
        private TextView distanceView;
        private float[] distance = new float[1];
        private List<Double> allTheDistances = new ArrayList<>();
        private List<Info> infos;

        public MyViewHolder(View itemView) {
            super(itemView);

            System.out.println("itemView = " + itemView);
            tvCardTitle = itemView.findViewById(R.id.rendezvous_title_card);
            tvCardUri = itemView.findViewById(R.id.rendezvous_image_card);
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    //insert Info and relative RendezVous
                    RendezVousDB db = RendezVousDB.getInstance(getActivity(mContext).getBaseContext());
                    infos = db.databaseDAO().getListCardsForActiveUser();
                    for (Info singleInfo:
                            infos) {
                        if(location != null ){
                            Location.distanceBetween(location.getLatitude(), location.getLongitude(), singleInfo.getLatitude(), singleInfo.getLongitude() , distance);
                            allTheDistances.add((double) distance[0]);
                        } else {
                            allTheDistances.add(0.0);
                        }
                    }
                }
            });

            System.out.println("allTheDistances = " + allTheDistances);
            this.distanceView = itemView.findViewById(R.id.distance_card);
            /*for (Double dist : allTheDistances){

            }*/
            this.distanceView.setText(itemView.getId() + " metri.");
        }

        public Activity getActivity(Context context)
        {
            if (context == null)
            {
                return null;
            }
            else if (context instanceof ContextWrapper)
            {
                if (context instanceof Activity)
                {
                    return (Activity) context;
                }
                else
                {
                    return getActivity(((ContextWrapper) context).getBaseContext());
                }
            }

            return null;
        }

    }
}

package com.example.rendezvous.ViewModel;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.Settings;
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

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewAdapter.MyViewHolder>{

    private Context mContext;
    private List<RendezVousCard> taskList;
    private Location location = null;
    private float[] distance = new float[1];


    public RecyclerviewAdapter(Context context){
        mContext = context;
        taskList = new ArrayList<>();
    }



    @Override
    public void onBindViewHolder(@NonNull RecyclerviewAdapter.MyViewHolder holder, int position) {
        RendezVousCard task = taskList.get(position);
        holder.tvCardTitle.setText(task.getTitle());
        holder.description.setText(task.getDescription());
        final InputStream imageStream;
        try {
            if(task.getImageUri() != null){
                imageStream = getActivity(mContext).getContentResolver().openInputStream(Uri.parse(task.getImageUri()));
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                holder.card_image.setImageBitmap(selectedImage);

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if(location != null ){
            Location.distanceBetween(location.getLatitude(), location.getLongitude(), task.getLatitude(), task.getLongitude() , distance);
            holder.distanceCard.setText(String.format("%s metres", distance[0]));
        } else {
            System.out.println(" location null");
            holder.distanceCard.setText(String.format("Click the gps button to get your location"));
        }

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


    @NonNull
    @Override
    public RecyclerviewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View view = LayoutInflater.from(mContext).inflate(R.layout.task_item,parent,false);
        //View view = LayoutInflater.from(mContext).inflate(R.layout.rendezvous_card,parent,false);
        View view = LayoutInflater.from(mContext).inflate(R.layout.card_details,parent,false);



        CardView cardView = view.findViewById(R.id.base_cardview);
        LinearLayout hiddenView = view.findViewById(R.id.hidden_view);
        ImageButton arrow = (ImageButton)  view.findViewById(R.id.arrow_button);
        ImageView map = view.findViewById(R.id.map);

        arrow.setOnClickListener(view1 -> {
            if(location == null){
                showDialog(getActivity(mContext));
            }
            RecyclerviewAdapter.this.notifyDataSetChanged();
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
        map.setOnClickListener(view1 -> {
            Intent intent_map = new Intent(Intent.ACTION_VIEW);
            intent_map.setData(Uri.parse("geo:47.4925,19.0513"));
            Intent chooser = Intent.createChooser(intent_map, "Launch maps");
            getActivity(mContext).startActivity(chooser);
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

    public void setCurrentLocation(Location value){
        this.location = value;
    }

    private void showDialog(Activity activity) {
        new AlertDialog.Builder(activity)
                .setMessage("Click on the gps button to see the distance between you and your take out into the card!.")
                .setCancelable(false)
                .setNeutralButton("OK", ((dialogInterface, i) -> dialogInterface.cancel()))
                .create()
                .show();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCardTitle;
        private TextView distanceCard;
        private ImageView card_image;
        private TextView description;


        public MyViewHolder(View itemView) {
            super(itemView);

            System.out.println("itemView = " + itemView);
            System.out.println("itemView id = " + itemView.getId());
            tvCardTitle = itemView.findViewById(R.id.rendezvous_title_card);
            distanceCard = itemView.findViewById(R.id.distance_card);
            card_image = itemView.findViewById(R.id.rendezvous_image_card);
            description = itemView.findViewById(R.id.card_description);
        }



    }
}

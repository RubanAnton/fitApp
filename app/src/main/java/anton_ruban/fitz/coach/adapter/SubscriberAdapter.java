package anton_ruban.fitz.coach.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.List;

import anton_ruban.fitz.R;
import anton_ruban.fitz.coach.CreateTraining.view.SingleCreateTrainingActivity;
import anton_ruban.fitz.coach.notifyTraining.NotifyTrainingActivity;
import anton_ruban.fitz.coach.trainig.TrainingCreationActivity;
import anton_ruban.fitz.network.req.UserSubscrCoachReq;
import anton_ruban.fitz.utils.PreferenceManager;
import de.hdodenhof.circleimageview.CircleImageView;


/**
@author antonruban on 17.05.2018.
 */

public class SubscriberAdapter extends RecyclerView.Adapter<SubscriberAdapter.ViewHolder> {


    private List<UserSubscrCoachReq> resList;
    private Context context;
    private PreferenceManager preferenceManager;

    public SubscriberAdapter(List<UserSubscrCoachReq> resList, Context context){
        this.resList = resList;
        this.context = context;
    }

    public void setList(List<UserSubscrCoachReq> res){
        this.resList = res;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_coach_subscriber,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.userEmail.setText(resList.get(position).getUser().getEmail());
        if(resList.get(position).getUser().getImagePath() != null && URLUtil.isValidUrl(resList.get(position).getUser().getImagePath())) {
            Glide.with(context)
                    .asBitmap()
                    .load(resList.get(position).getUser().getImagePath())
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            holder.photo.setImageBitmap(resource);
                        }
                    });
        } else {
            holder.photo.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.t1));
        }
        holder.trainingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferenceManager = new PreferenceManager(context);
                Intent intent = new Intent(context, TrainingCreationActivity.class);
                intent.putExtra("USER_ID_EXERCISE", resList.get(position).getUser().getUserID());
                context.startActivity(intent);
            }
        });
        holder.checkTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferenceManager = new PreferenceManager(context);
                Intent intent = new Intent(context, NotifyTrainingActivity.class);
                intent.putExtra("USER_ID_EXERCISE", resList.get(position).getUser().getUserID());
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return resList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView userEmail;
        public  TextView statusTrainig;
        public Button trainingButton;
        public CircleImageView photo;
        public Button checkTraining;
        public ViewHolder(View itemView) {
            super(itemView);
            userEmail = (TextView) itemView.findViewById(R.id.userEmail);
            statusTrainig = (TextView) itemView.findViewById(R.id.status_training);
            trainingButton = (Button) itemView.findViewById(R.id.setTrainingButton);
            photo = (CircleImageView) itemView.findViewById(R.id.workout_photoCoach);
            checkTraining = itemView.findViewById(R.id.checkHistoryTraining);
        }
    }
}

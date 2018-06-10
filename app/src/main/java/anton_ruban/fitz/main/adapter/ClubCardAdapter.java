package anton_ruban.fitz.main.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.List;

import anton_ruban.fitz.R;
import anton_ruban.fitz.login.view.LoginActivity;
import anton_ruban.fitz.network.ServerApi;
import anton_ruban.fitz.network.ServerUtils;
import anton_ruban.fitz.network.req.SubscriberRequest;
import anton_ruban.fitz.network.res.SubscriptionRequestMain;
import anton_ruban.fitz.utils.PreferenceManager;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;

/**
 * Created by antonruban on 08.05.2018.
 */

public class ClubCardAdapter extends RecyclerView.Adapter<ClubCardAdapter.ViewHolder> {

    private Context context;
    private List<SubscriptionRequestMain> clubResList;
    private ServerApi serverApi;
    private SubscriberRequest req;
    private PreferenceManager preferenceManager;

    public ClubCardAdapter(Context context, List<SubscriptionRequestMain> res) {
        this.context = context;
        this.clubResList = res;
    }

    public void setClubResList(List<SubscriptionRequestMain> res) {
        this.clubResList = res;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card_club,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.nameClub.setText(clubResList.get(position).getClubRes().getNameClub());
        holder.emailClub.setText(clubResList.get(position).getClubRes().getAddres());
        if(preferenceManager == null){
            preferenceManager = new PreferenceManager(context);
        }
        if(clubResList.get(position).getStatus() == 1){
            holder.subscribeButton.setText(context.getResources().getString(R.string.inProgress));
            holder.subscribeButton.setBackground(context.getDrawable(R.drawable.progress));
        }
        if(clubResList.get(position).getStatus() == 2){
            holder.subscribeButton.setText(context.getResources().getString(R.string.unsubscribeButton));
            holder.subscribeButton.setBackground(context.getDrawable(R.drawable.corner_btn));
        }
        if(clubResList.get(position).getStatus() == -1){

        }

        if(clubResList.get(position).getClubRes().getImagePath() != null && URLUtil.isValidUrl(clubResList.get(position).getClubRes().getImagePath())) {
            Glide.with(context)
                    .asBitmap()
                    .load(clubResList.get(position).getClubRes().getImagePath())
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

        if (preferenceManager.getUserId() == clubResList.get(position).getClubRes().getIdOwner() ||
                preferenceManager.getUserId() == Integer.parseInt(clubResList.get(position).getClubRes().getIdsCoachs())) {
            holder.subscribeButton.setVisibility(GONE);
            holder.isOwnerOrCoach.setVisibility(View.VISIBLE);
            if(preferenceManager.getUserId() == clubResList.get(position).getClubRes().getIdOwner()) {
                holder.isOwnerOrCoach.setText(context.getString(R.string.youOwner));
            } else {
                holder.isOwnerOrCoach.setText(context.getString(R.string.youCoach));
            }
        }

        holder.subscribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                req = new SubscriberRequest();
                serverApi = ServerUtils.serverApi();
                if (clubResList.get(position).getStatus() == -1) {
                    req.setIdClub(clubResList.get(position).getClubRes().getIdClub());
                    req.setIdOwner(clubResList.get(position).getClubRes().getIdOwner());
                    req.setStatus(1);
                    req.setUserSubscriber(preferenceManager.getUserId());
                    serverApi.subscribClub(preferenceManager.getUserToken(), req).enqueue(new Callback<SubscriberRequest>() {
                        @Override
                        public void onResponse(Call<SubscriberRequest> call, Response<SubscriberRequest> response) {
                            if (response.code() > 200 && response.code() < 300 || response.isSuccessful()) {
                                holder.subscribeButton.setText(context.getResources().getString(R.string.inProgress));
                                holder.subscribeButton.setBackground(context.getDrawable(R.drawable.progress));
                                clubResList.get(position).setStatus(response.body().getStatus());
                                clubResList.get(position).setSubscriberID(response.body().getIdSubscriber());
                                notifyItemChanged(position);
                            }
                        }

                        @Override
                        public void onFailure(Call<SubscriberRequest> call, Throwable t) {

                        }
                    });
                }
                if(clubResList.get(position).getStatus() == 2) {
                    serverApi.unsibscribeClub(preferenceManager.getUserToken(),clubResList.get(position).getSubscriberID()).enqueue(new Callback<SubscriberRequest>() {
                        @Override
                        public void onResponse(Call<SubscriberRequest> call, Response<SubscriberRequest> response) {
                            if(response.isSuccessful() || response.code() >= 200 && response.code() > 300){
                                holder.subscribeButton.setText(context.getResources().getString(R.string.subscribe));
                                holder.subscribeButton.setBackground(context.getDrawable(R.drawable.corner_bgc));
                                clubResList.get(position).setStatus(-1);
                                clubResList.get(position).setSubscriberID(-1);
                                notifyItemChanged(position);
                            }
                        }

                        @Override
                        public void onFailure(Call<SubscriberRequest> call, Throwable t) {

                        }
                    });
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        if(clubResList != null){
            return clubResList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameClub;
        public  TextView emailClub;
        public  TextView isOwnerOrCoach;
        public Button subscribeButton;
        public CircleImageView photo;

        public ViewHolder(View itemView) {
            super(itemView);
            nameClub = (TextView) itemView.findViewById(R.id.userEmail);
            emailClub = (TextView) itemView.findViewById(R.id.club_email);
            subscribeButton = (Button) itemView.findViewById(R.id.subcribeButton);
            isOwnerOrCoach = (TextView) itemView.findViewById(R.id.isOwnerOrCoach);
            photo = (CircleImageView) itemView.findViewById(R.id.workout_photo);
        }
    }
}

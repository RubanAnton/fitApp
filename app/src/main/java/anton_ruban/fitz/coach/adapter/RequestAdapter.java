package anton_ruban.fitz.coach.adapter;

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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;
import java.util.List;

import anton_ruban.fitz.R;
import anton_ruban.fitz.network.ServerApi;
import anton_ruban.fitz.network.ServerUtils;
import anton_ruban.fitz.network.req.SubscriberRequest;
import anton_ruban.fitz.network.res.UserResponse;
import anton_ruban.fitz.utils.PreferenceManager;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 @author antonruban on 18.05.2018.
 */

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {

    private Context context;
    private List<UserResponse> responseList;
    private ServerApi serverApi;
    private PreferenceManager preferenceManager;
    SubscriberRequest subscriberRequest;

    public RequestAdapter(List<UserResponse> list, Context context){
        this.responseList = list;
        this.context = context;
    }

    public void setList(List<UserResponse> userResponses){
        this.responseList = userResponses;
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
        holder.checkTraining.setVisibility(View.GONE);
        holder.userEmail.setText(responseList.get(position).getEmail());
        holder.trainingButton.setText(context.getResources().getString(R.string.inProgress));
        preferenceManager = new PreferenceManager(context);
        serverApi = ServerUtils.serverApi();
        if(responseList.get(position).getImagePath() != null && URLUtil.isValidUrl(responseList.get(position).getImagePath())) {
            Glide.with(context)
                    .asBitmap()
                    .load(responseList.get(position).getImagePath())
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

        holder.trainingButton.setText(R.string.isSubscribed);

        holder.trainingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serverApi.getSubscriberList(preferenceManager.getUserToken()).enqueue(new Callback<ArrayList<SubscriberRequest>>() {
                    @Override
                    public void onResponse(Call<ArrayList<SubscriberRequest>> call, final Response<ArrayList<SubscriberRequest>> response) {
                        if(response.code() >= 200 && response.code() < 300 || response.isSuccessful()){
                            final List<SubscriberRequest> req = response.body();
                            for(SubscriberRequest request: req){
                                if(request.getUserSubscriber() == responseList.get(position).getUserID()
                                        && request.getIdClub() == preferenceManager.getCoachClub()){
                                    subscriberRequest = request;
                                    subscriberRequest.setStatus(2);
                                    serverApi.updateStatusSubscriber(preferenceManager.getUserToken(),request.getIdSubscriber(),subscriberRequest).enqueue(new Callback<SubscriberRequest>() {
                                        @Override
                                        public void onResponse(Call<SubscriberRequest> call, Response<SubscriberRequest> responseSubscr) {
                                            if(responseSubscr.code() > 200 && responseSubscr.code() < 300 || responseSubscr.isSuccessful() || responseSubscr.code() == 200){
                                                responseList.remove(position);
                                                notifyItemRemoved(position);

                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<SubscriberRequest> call, Throwable t) {

                                        }
                                    });

                                }
                            }
                        } else {
                            Toast.makeText(context, "Continue later", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<SubscriberRequest>> call, Throwable t) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return responseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView userEmail;
        public  TextView statusTrainig;
        public Button trainingButton;
        public Button checkTraining;
        public CircleImageView photo;
        public ViewHolder(View itemView) {
            super(itemView);
            userEmail = (TextView) itemView.findViewById(R.id.userEmail);
            statusTrainig = (TextView) itemView.findViewById(R.id.status_training);
            trainingButton = (Button) itemView.findViewById(R.id.setTrainingButton);
            checkTraining = itemView.findViewById(R.id.checkHistoryTraining);
            photo = (CircleImageView) itemView.findViewById(R.id.workout_photoCoach);
        }
    }
}

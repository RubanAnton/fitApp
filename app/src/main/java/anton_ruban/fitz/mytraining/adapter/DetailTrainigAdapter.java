package anton_ruban.fitz.mytraining.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;
import anton_ruban.fitz.R;
import anton_ruban.fitz.coach.trainig.VideoViewActivity;
import anton_ruban.fitz.network.ServerApi;
import anton_ruban.fitz.network.req.CreateSingleTrainingReq;
import anton_ruban.fitz.network.res.GetExerciseResp;
import anton_ruban.fitz.utils.PreferenceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mac-242 on 5/30/18.
 */

public class DetailTrainigAdapter extends RecyclerView.Adapter<DetailTrainigAdapter.ViewHolder> {

    private Activity context;
    private ServerApi serverApi;
    private List<GetExerciseResp> listResponse;
    private CreateSingleTrainingReq training;

    public DetailTrainigAdapter(Activity context,List<GetExerciseResp> list, ServerApi serverApi, CreateSingleTrainingReq training){
        this.context = context;
        this.listResponse = list;
        this.serverApi = serverApi;
        this.training = training;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_exercice_user,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.textName.setText(listResponse.get(position).getName());
        if (listResponse.get(position).getStatus() != 0) {
            holder.card.setBackgroundResource(R.drawable.exercise_done);
            holder.resolveBTN.setVisibility(View.GONE);
            holder.vidoBTN.setVisibility(View.GONE);
        }

        holder.resolveBTN.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                PreferenceManager preferenceManager = new PreferenceManager(context);
                GetExerciseResp exercise = listResponse.get(position);
                exercise.setStatus(1);
                serverApi.updateExercise(preferenceManager.getUserToken(),listResponse.get(position).getIdExercise(),exercise).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.code() > 200 && response.code() < 300 || response.isSuccessful() || response.code() == 200) {
                            listResponse.get(position).setStatus(1);
                            notifyItemChanged(position);
                        } else {
                            Toast.makeText(context, "BAD REQUEST", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                        Toast.makeText(context, "BAD REQUEST", Toast.LENGTH_SHORT).show();
                    }
                });

                if (isAllDone()) {
                   training.setIsDone(1);
                    serverApi.updateTraining(preferenceManager.getUserToken(),listResponse.get(position).getIdTraining(),training).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.code() > 200 && response.code() < 300 || response.isSuccessful() || response.code() == 200) {
                                Toast.makeText(context, context.getString(R.string.allDone), Toast.LENGTH_SHORT).show();
                                context.finish();
                            } else {
                                Toast.makeText(context, "BAD REQUEST", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {

                            Toast.makeText(context, "BAD REQUEST", Toast.LENGTH_SHORT).show();
                        }
                    });

                }


            }
        });

        holder.vidoBTN.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent = new Intent(context, VideoViewActivity.class);
                intent.putExtra("URI_STR",listResponse.get(position).getUrl());
                context.startActivity(intent);
            }
        });
    }

    public void setClubResList(List<GetExerciseResp> res) {
        this.listResponse = res;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(listResponse != null){
            return listResponse.size();
        } else
            return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textName;
        public Button vidoBTN;
        public Button resolveBTN;
        public LinearLayout card;
        public ViewHolder(View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.nameExerciseText);
            vidoBTN = itemView.findViewById(R.id.watchUserVideo);
            resolveBTN = itemView.findViewById(R.id.resolveExercise);
            card = itemView.findViewById(R.id.cardExercise);
        }
    }

    public boolean isAllDone() {
        for (GetExerciseResp exercise: listResponse) {
            if (exercise.getStatus() == 0) {
                return false;
            }
        }
        return true;
    }
}

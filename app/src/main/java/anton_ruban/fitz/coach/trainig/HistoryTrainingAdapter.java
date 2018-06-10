package anton_ruban.fitz.coach.trainig;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import anton_ruban.fitz.R;
import anton_ruban.fitz.coach.view.CoachActivity;
import anton_ruban.fitz.network.ServerApi;
import anton_ruban.fitz.network.ServerUtils;
import anton_ruban.fitz.network.req.AddTrainingReq;
import anton_ruban.fitz.network.req.CreateSingleTrainingReq;
import anton_ruban.fitz.network.res.GetExerciseResp;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by alina on 01.06.2018.
 */

public class HistoryTrainingAdapter extends RecyclerView.Adapter<HistoryTrainingAdapter.ViewHolder> {

    private Context context;
    private List<AddTrainingReq> listResponse;
    private AddTrainingReq req;
    private ServerApi serverApi;
    private CreateSingleTrainingReq trainingReq;
    private ArrayList<GetExerciseResp> exerciseResp;

    public HistoryTrainingAdapter(Context context,List<AddTrainingReq> list){
        this.context = context;
        this.listResponse = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_history_training,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.textView.setText(listResponse.get(position).getTraining().getNameTraining());
        holder.textView2.setText(listResponse.get(position).getTraining().getDescriptionTraining());

        serverApi = ServerUtils.serverApi();
        req = new AddTrainingReq();
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exerciseResp = new ArrayList<>();
                trainingReq = new CreateSingleTrainingReq();
                trainingReq.setNameTraining(listResponse.get(position).getTraining().getNameTraining());
                trainingReq.setDescriptionTraining(listResponse.get(position).getTraining().getDescriptionTraining());
                trainingReq.setIdCoach(listResponse.get(position).getTraining().getIdCoach());
                trainingReq.setIdUser(listResponse.get(position).getTraining().getIdUser());
                trainingReq.setIsDone(0);
//                exerciseResp.get(position).setName(listResponse.get(position).getExercises().get(position).getName());
//                exerciseResp.get(position).setType(listResponse.get(position).getExercises().get(position).getType());
//                exerciseResp.get(position).setUrl(listResponse.get(position).getExercises().get(position).getUrl());
//                exerciseResp.get(position).setStatus(0);
                listResponse.get(position).getExercises().get(position).setStatus(0);
                req.setTraining(trainingReq);
                req.setExercises(listResponse.get(position).getExercises());
                serverApi.createTraining(req).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.isSuccessful() || response.code() >= 200 || response.code() < 300) {
                            context.startActivity(new Intent(context, CoachActivity.class));
                            Toast.makeText(context, "Training add from history", Toast.LENGTH_SHORT).show();
                            ((Activity) (context)).finish();
                        } else {
                            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void setTrainingList(List<AddTrainingReq> res) {
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

        public TextView textView;
        public TextView textView2;
        public Button btn;
        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.nameTrainingHistory);
            textView2 = itemView.findViewById(R.id.descrTrainingHistory);
            btn = itemView.findViewById(R.id.selectTrainitHistoryBtn);
        }
    }
}

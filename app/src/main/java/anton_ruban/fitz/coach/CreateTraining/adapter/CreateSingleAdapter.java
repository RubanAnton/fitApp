package anton_ruban.fitz.coach.CreateTraining.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pedromassango.doubleclick.DoubleClick;
import com.pedromassango.doubleclick.DoubleClickListener;

import java.util.ArrayList;
import java.util.List;

import anton_ruban.fitz.R;
import anton_ruban.fitz.coach.CreateTraining.view.SingleCreateTrainingActivity;
import anton_ruban.fitz.network.ServerApi;
import anton_ruban.fitz.network.res.GetExerciseResp;
import anton_ruban.fitz.utils.PreferenceManager;

/**
 * Created by antonruban on 26.05.2018.
 */

public class CreateSingleAdapter extends RecyclerView.Adapter<CreateSingleAdapter.ViewHolder> {

    private Context context;
    private List<GetExerciseResp> exerciseResps;
    private ServerApi serverApi;
    private PreferenceManager preferenceManager;
    public ArrayList<GetExerciseResp> exerciseList = new ArrayList<>();
    private GetExerciseResp model = new GetExerciseResp();

    public CreateSingleAdapter(Context context,List<GetExerciseResp> res){
        this.context = context;
        this.exerciseResps = res;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_select_coach,parent,false);
        return new CreateSingleAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.textName.setText(exerciseResps.get(position).getName());
        preferenceManager = new PreferenceManager(context);
        holder.buttonSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.setName(exerciseResps.get(position).getName());
                model.setIdExercise(exerciseResps.get(position).getIdExercise());
                model.setUrl(exerciseResps.get(position).getUrl());
                model.setStatus(exerciseResps.get(position).getStatus());
                model.setIdTraining(0);
                model.setIdUser(preferenceManager.getUserSingleTraining());
                exerciseList.add(model);
                holder.buttonSelect.setBackground(context.getResources().getDrawable(R.drawable.corner_btn));
                //TODO: check simple select training
            }
        });
    }

    @Override
    public int getItemCount() {
        if(exerciseResps != null){
            return exerciseResps.size();
        }
        return 0;
    }

    public void setExerciseList(List<GetExerciseResp> list){
        this.exerciseResps = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textName;
        public Button buttonSelect;

        public ViewHolder(View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textSelectNameCoach);
            buttonSelect = itemView.findViewById(R.id.btnSelectCoach);
        }
    }
}

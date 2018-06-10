package anton_ruban.fitz.mytraining.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import anton_ruban.fitz.R;

import anton_ruban.fitz.mytraining.TrainingDetailActivity;
import anton_ruban.fitz.mytraining.view.MyTrainingActivity;
import anton_ruban.fitz.network.req.AddTrainingReq;
import anton_ruban.fitz.network.res.GetExerciseResp;
import anton_ruban.fitz.network.res.UserTrainingResponse;

/**
 * Created by antonruban on 27.05.2018.
 */

public class MyTrainingAdapter extends RecyclerView.Adapter<MyTrainingAdapter.ViewHolder> {

    private Context context;
    private List<AddTrainingReq> listResponse;

    public MyTrainingAdapter(Context context,List<AddTrainingReq> list){
        this.context = context;
        this.listResponse = list;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_training_user,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.textName.setText(listResponse.get(position).getTraining().getNameTraining());
        holder.textDescr.setText(listResponse.get(position).getTraining().getDescriptionTraining());
        if(listResponse.get(position).getTraining().getIsDone() == 0){
            holder.progressText.setText(context.getResources().getString(R.string.unresolvedTraining));
            holder.linerStatus.setBackground(context.getResources().getDrawable(R.drawable.status_training_false));
        } else {
            holder.progressText.setText(context.getResources().getString(R.string.resolvedTraining));
            holder.linerStatus.setBackground(context.getResources().getDrawable(R.drawable.status_training_true));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent = new Intent(context,TrainingDetailActivity.class);
                ArrayList<GetExerciseResp> list = (ArrayList<GetExerciseResp>) listResponse.get(position).getExercises();
                intent.putExtra("listUserExerciseArray",list);
                intent.putExtra("trainingSingle",listResponse.get(position).getTraining());
                context.startActivity(intent);
            }
        });
    }

    public void setClubResList(List<AddTrainingReq> res) {
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
        public TextView textDescr;
        public TextView textStatus;
        public TextView progressText;
        public LinearLayout linerStatus;
        public ViewHolder(View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.nameTrainingText);
            textDescr = itemView.findViewById(R.id.descriptionTraining);
            textStatus = itemView.findViewById(R.id.statusTraining);
            progressText = itemView.findViewById(R.id.resolvedStatus);
            linerStatus = itemView.findViewById(R.id.linearStatusTraining);
        }
    }
}

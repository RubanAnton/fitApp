package anton_ruban.fitz.coach.trainig;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import anton_ruban.fitz.R;
import anton_ruban.fitz.network.res.GetExerciseResp;

/**
 * Created by mac-242 on 5/29/18.
 */

public class AdapterAddedTrainings extends RecyclerView.Adapter<AdapterAddedTrainings.ViewHolder> {

    private Context context;
    private List<GetExerciseResp> list;

    public AdapterAddedTrainings(List<GetExerciseResp> list, Context context){
        this.list = list;
        this.context = context;
    }

    public void setList(List<GetExerciseResp> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_training_creation,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.nameExercise.setText(list.get(position).getName());
        holder.typeExercise.setText(list.get(position).getType());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(R.string.whichSelect)
                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                list.remove(position);
                                notifyItemRemoved(position);

                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameExercise;
        public TextView typeExercise;

        public ViewHolder(View itemView) {
            super(itemView);
            typeExercise = (TextView) itemView.findViewById(R.id.typeExerciseMY);
            nameExercise = (TextView) itemView.findViewById(R.id.nameExerciseMY);
        }
    }
}


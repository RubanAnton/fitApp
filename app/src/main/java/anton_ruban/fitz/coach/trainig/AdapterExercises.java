package anton_ruban.fitz.coach.trainig;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.ArrayList;
import java.util.List;

import anton_ruban.fitz.R;
import anton_ruban.fitz.coach.adapter.RequestAdapter;
import anton_ruban.fitz.network.res.GetExerciseResp;

/**
 * Created by mac-242 on 5/29/18.
 */

public class AdapterExercises extends RecyclerView.Adapter<AdapterExercises.ViewHolder> {

    private ExerciseCreationActivity context;
    private List<GetExerciseResp> list;
    private ArrayList<GetExerciseResp> array;

    public AdapterExercises(List<GetExerciseResp> list, ExerciseCreationActivity context, ArrayList<GetExerciseResp> array){
        this.list = list;
        this.context = context;
        this.array = array;
    }

    public void setList(List<GetExerciseResp> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_exercise_select,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.nameExercise.setText(list.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(R.string.whichSelect)
                        .setPositiveButton(R.string.watchVideo, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(context, VideoViewActivity.class);
                                intent.putExtra("URI_STR",list.get(position).getUrl());
                                context.startActivity(intent);

                            }
                        })
                        .setNegativeButton(R.string.addToList, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (ifContain(list.get(position))) {
                                    Intent intent = new Intent(context, TrainingCreationActivity.class);
                                    intent.putExtra("exerciseADDED", list.get(position));
                                    context.setResult(1003, intent);
                                    context.finish();
                                } else {
                                    Toast.makeText(context, context.getString(R.string.alreadyAdded), Toast.LENGTH_SHORT).show();
                                }
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

        public ViewHolder(View itemView) {
            super(itemView);
            nameExercise = (TextView) itemView.findViewById(R.id.exerciseName);
        }
    }

    public boolean ifContain(GetExerciseResp exercise) {
        for (GetExerciseResp exe: array) {
            if (exe.equals(exercise)){
                return false;
            }
        }
     return true;
    }
}




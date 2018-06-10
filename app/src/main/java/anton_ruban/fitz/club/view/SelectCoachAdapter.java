package anton_ruban.fitz.club.view;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import anton_ruban.fitz.R;
import anton_ruban.fitz.network.ServerApi;
import anton_ruban.fitz.network.ServerUtils;
import anton_ruban.fitz.network.res.ClubRes;
import anton_ruban.fitz.network.res.UserResponse;
import anton_ruban.fitz.utils.PreferenceManager;

/**
 * Created by antonruban on 16.05.2018.
 */

public class SelectCoachAdapter extends RecyclerView.Adapter<SelectCoachAdapter.ViewHolder> {

    private Context context;
    private Activity activity;
    private List<UserResponse> userResponseList;
    private PreferenceManager preferenceManager;
    public Integer selectedCoach;
    private ViewHolder viewHolderSelected;
    public SelectCoachAdapter(List<UserResponse> userResponses,Context context, Activity activity){
        this.userResponseList = userResponses;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_select_coach,parent,false);
        return new SelectCoachAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.textName.setText(userResponseList.get(position).getEmail());
        if(preferenceManager == null){
            preferenceManager = new PreferenceManager(context);
        }
        if(preferenceManager.getUserId() == userResponseList.get(position).getUserID()){
            holder.buttonSelect.setText("You owner this club");
            holder.buttonSelect.setBackground(context.getResources().getDrawable(R.drawable.corner_bgcr));
            holder.buttonSelect.setEnabled(false);
        }
        holder.buttonSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedCoach != null && userResponseList.get(position).getIsCoach() == 0  && userResponseList.get(position).getUserID() != preferenceManager.getUserId()){
                    viewHolderSelected.buttonSelect.setBackground(context.getResources().getDrawable(R.drawable.corner_bgc));
                }
                if (userResponseList.get(position).getIsCoach() == 0 ) {
                    if (userResponseList.get(position).getUserID() != preferenceManager.getUserId()) {
                        String userCoach = String.valueOf(userResponseList.get(position).getUserID());
                        selectedCoach = Integer.parseInt(userCoach);
                        viewHolderSelected = holder;
                        holder.buttonSelect.setBackground(context.getResources().getDrawable(R.drawable.progress));
                        holder.buttonSelect.setEnabled(false);
                    }
                } else {
                    String message = activity.getString(R.string.isTrainerAlreadyError2) + " " + userResponseList.get(position).getEmail();
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle(R.string.isTrainerAlreadyError).setMessage(message);
                    AlertDialog dialog = builder.create();
                    dialog.setCancelable(true);
                    dialog.show();
                }



            }
        });
    }

    @Override
    public int getItemCount() {
        if(userResponseList != null)
            return userResponseList.size();
        return 0;
    }

    public void setUserList(List<UserResponse> userList){
        this.userResponseList = userList;
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

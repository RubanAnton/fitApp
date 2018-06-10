package anton_ruban.fitz.coach.notifyTraining;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;
import anton_ruban.fitz.R;
import anton_ruban.fitz.network.ServerApi;
import anton_ruban.fitz.network.req.AddTrainingReq;

public class ResolvedAdapter extends RecyclerView.Adapter<ResolvedAdapter.ViewHolder>{

    private Context context;
    private List<AddTrainingReq> listResponse;
    private AddTrainingReq req;
    private ServerApi serverApi;

    public ResolvedAdapter(Context context, List<AddTrainingReq> list){
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textName.setText(listResponse.get(position).getTraining().getNameTraining());
        holder.textDescr.setText(listResponse.get(position).getTraining().getDescriptionTraining());
        holder.progressText.setText(context.getResources().getString(R.string.resolvedTraining));
        holder.linerStatus.setBackground(context.getResources().getDrawable(R.drawable.status_training_true));
    }

    @Override
    public int getItemCount() {
        if(listResponse != null){
            return listResponse.size();
        } else
            return 0;
    }

    public void setListResolved(List<AddTrainingReq> res) {
        this.listResponse = res;
        notifyDataSetChanged();
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

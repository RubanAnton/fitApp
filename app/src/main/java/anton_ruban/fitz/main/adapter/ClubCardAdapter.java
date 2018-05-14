package anton_ruban.fitz.main.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import anton_ruban.fitz.R;
import anton_ruban.fitz.network.ServerApi;
import anton_ruban.fitz.network.ServerUtils;
import anton_ruban.fitz.network.req.SubscriberRequest;
import anton_ruban.fitz.network.res.ClubRes;
import anton_ruban.fitz.utils.PreferenceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by antonruban on 08.05.2018.
 */

public class ClubCardAdapter extends RecyclerView.Adapter<ClubCardAdapter.ViewHolder> {

    private Context context;
    private List<ClubRes> clubResList;
    private ServerApi serverApi;
    private SubscriberRequest req;
    private PreferenceManager preferenceManager;

    public ClubCardAdapter(Context context, List<ClubRes> res) {
        this.context = context;
        this.clubResList = res;
    }

    public void setClubResList(List<ClubRes> res) {
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
        holder.nameClub.setText(clubResList.get(position).getNameClub());
        holder.emailClub.setText(clubResList.get(position).getEmailClub());
        if(preferenceManager == null){
            preferenceManager = new PreferenceManager(context);
        }
        holder.subscribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serverApi = ServerUtils.serverApi();
                req = new SubscriberRequest(preferenceManager.getUserId(),clubResList.get(position).getIdClub(),clubResList.get(position).getIdOwner(),1);
                serverApi.subscribClub(preferenceManager.getUserToken(),req).enqueue(new Callback<SubscriberRequest>() {
                    @Override
                    public void onResponse(Call<SubscriberRequest> call, Response<SubscriberRequest> response) {
                        if(response.code() > 200 && response.code() < 300 || response.isSuccessful()){
                            holder.subscribeButton.setText(context.getResources().getString(R.string.unsubscribeButton));
                            Toast.makeText(context, "Now you can get news at this club", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<SubscriberRequest> call, Throwable t) {

                    }
                });
            }
        });
    }

    public void removeItem(int position){
        clubResList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(ClubRes item, int position){
        clubResList.add(position,item);
        notifyItemInserted(position);
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
        public Button subscribeButton;

        public ViewHolder(View itemView) {
            super(itemView);
            nameClub = (TextView) itemView.findViewById(R.id.club_name);
            emailClub = (TextView) itemView.findViewById(R.id.club_email);
            subscribeButton = (Button) itemView.findViewById(R.id.subcribeButton);
        }
    }
}

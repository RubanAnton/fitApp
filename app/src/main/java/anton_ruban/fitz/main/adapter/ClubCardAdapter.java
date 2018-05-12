package anton_ruban.fitz.main.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import anton_ruban.fitz.R;
import anton_ruban.fitz.network.res.ClubRes;

/**
 * Created by antonruban on 08.05.2018.
 */

public class ClubCardAdapter extends RecyclerView.Adapter<ClubCardAdapter.ViewHolder> {

    private Context context;
    private List<ClubRes> clubResList;

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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nameClub.setText(clubResList.get(position).getNameClub());
        holder.emailClub.setText(clubResList.get(position).getEmailClub());
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

        public ViewHolder(View itemView) {
            super(itemView);
            nameClub = (TextView) itemView.findViewById(R.id.club_name);
            emailClub = (TextView) itemView.findViewById(R.id.club_email);
        }
    }
}

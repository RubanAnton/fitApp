package anton_ruban.fitz.coach.trainig;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import anton_ruban.fitz.R;
import anton_ruban.fitz.mytraining.adapter.MyTrainingAdapter;
import anton_ruban.fitz.mytraining.view.MyTrainingActivity;
import anton_ruban.fitz.network.ServerApi;
import anton_ruban.fitz.network.ServerUtils;
import anton_ruban.fitz.network.req.AddTrainingReq;
import anton_ruban.fitz.utils.PreferenceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryActivityActivity extends AppCompatActivity {

    private List<AddTrainingReq> resList;
    private RecyclerView recyclerView;
    private HistoryTrainingAdapter recyclerAdapter;
    private ServerApi serverApi;
    private PreferenceManager preferenceManager;
    private ProgressDialog mProgressDialog;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_activity);
        userId = this.getIntent().getIntExtra("userIdEXERCISE",0);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerTrainingHistory);
        resList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter = new HistoryTrainingAdapter(this,resList);
        recyclerView.setAdapter(recyclerAdapter);
        resList.clear();
        serverApi = ServerUtils.serverApi();
        preferenceManager = new PreferenceManager(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        serverApi.getListUserTraining(userId).enqueue(new Callback<List<AddTrainingReq>>() {
            @Override
            public void onResponse(Call<List<AddTrainingReq>> call, Response<List<AddTrainingReq>> response) {
                if(response.isSuccessful() || response.code() > 200 && response.code() < 300){
                    resList = response.body();
                    for(int i = 0; i < resList.size(); i++){
                        if(resList.get(i).getTraining().getIsDone() == 1){
                            recyclerAdapter.setTrainingList(resList);
                            recyclerAdapter = new HistoryTrainingAdapter(HistoryActivityActivity.this,resList);
                            recyclerView.setAdapter(recyclerAdapter);
                            hideProgressDialog();
                        }
                    }
                    if(response.body() == null){
                        hideProgressDialog();
                        Toast.makeText(HistoryActivityActivity.this, "Don't have trainings", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(HistoryActivityActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    hideProgressDialog();
                }
            }

            @Override
            public void onFailure(Call<List<AddTrainingReq>> call, Throwable t) {
                t.getMessage();
                Toast.makeText(HistoryActivityActivity.this, "bad connection", Toast.LENGTH_SHORT).show();
                hideProgressDialog();
            }
        });
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}

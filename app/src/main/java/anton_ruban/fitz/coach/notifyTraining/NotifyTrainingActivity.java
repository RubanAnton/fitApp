package anton_ruban.fitz.coach.notifyTraining;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import anton_ruban.fitz.R;
import anton_ruban.fitz.mytraining.adapter.MyTrainingAdapter;
import anton_ruban.fitz.network.ServerApi;
import anton_ruban.fitz.network.ServerUtils;
import anton_ruban.fitz.network.req.AddTrainingReq;
import anton_ruban.fitz.utils.PreferenceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotifyTrainingActivity extends AppCompatActivity {

    private List<AddTrainingReq> resList;
    private List<AddTrainingReq> resList2;
    private RecyclerView recyclerView;
    private RecyclerView recyclerView2;
    private ResolvedAdapter recyclerAdapter;
    private UnresolvedAdapter unresolvedAdapter;
    private ServerApi serverApi;
    private ProgressDialog mProgressDialog;
    private int USER_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify_training);

        serverApi = ServerUtils.serverApi();
        USER_ID = getIntent().getIntExtra("USER_ID_EXERCISE",0);
        //Recycler for Resolved
        recyclerView = (RecyclerView)findViewById(R.id.resolvedRecyclerTrainint);
        recyclerView2 = findViewById(R.id.unresolvedRecyclerTraining);

        resList2 = new ArrayList<>();
        resList = new ArrayList<>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView2.setLayoutManager(layoutManager2);

        recyclerAdapter = new ResolvedAdapter(this,resList);
        recyclerView.setAdapter(recyclerAdapter);
        unresolvedAdapter = new UnresolvedAdapter(this,resList2);
        recyclerView2.setAdapter(unresolvedAdapter);

        resList2.clear();
        resList.clear();

    }

    @Override
    protected void onResume() {
        super.onResume();
        serverApi.getListUserTraining(USER_ID).enqueue(new Callback<List<AddTrainingReq>>() {
            @Override
            public void onResponse(Call<List<AddTrainingReq>> call, Response<List<AddTrainingReq>> response) {
                showProgressDialog();
                if(response.isSuccessful() || response.code() >= 200 && response.code() < 300){
                    for (int i = 0; i < response.body().size(); i++){
                        if(response.body().get(i).getTraining().getIsDone() == 0){
                            resList2.add(response.body().get(i));
                            unresolvedAdapter.setListUnResolved(resList);
                            recyclerView2.setAdapter(unresolvedAdapter);
                        }
                        if(response.body().get(i).getTraining().getIsDone() == 1){
                            resList.add(response.body().get(i));
                            recyclerAdapter.setListResolved(resList);
                            recyclerView.setAdapter(recyclerAdapter);
                        }
                    }
                }
                else {
                    hideProgressDialog();
                }
            }

            @Override
            public void onFailure(Call<List<AddTrainingReq>> call, Throwable t) {
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

package anton_ruban.fitz.mytraining.view;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import anton_ruban.fitz.R;
import anton_ruban.fitz.mytraining.adapter.MyTrainingAdapter;
import anton_ruban.fitz.network.ServerApi;
import anton_ruban.fitz.network.ServerUtils;
import anton_ruban.fitz.network.req.AddTrainingReq;
import anton_ruban.fitz.network.res.UserTrainingResponse;
import anton_ruban.fitz.utils.PreferenceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyTrainingActivity extends AppCompatActivity {

    private List<AddTrainingReq> resList;
    private RecyclerView recyclerView;
    private MyTrainingAdapter recyclerAdapter;
    private ServerApi serverApi;
    private PreferenceManager preferenceManager;
    private ProgressDialog mProgressDialog;
    private TextView textNoTraining;

    @Override
    protected void onResume() {
        super.onResume();
        showProgressDialog();
        serverApi.getListUserTraining(preferenceManager.getUserId()).enqueue(new Callback<List<AddTrainingReq>>() {
            @Override
            public void onResponse(Call<List<AddTrainingReq>> call, Response<List<AddTrainingReq>> response) {
                if(response.isSuccessful() || response.code() > 200 && response.code() < 300){
                    if(response.body().size() == 0){
                        textNoTraining.setVisibility(View.VISIBLE);
                    } else {
                        textNoTraining.setVisibility(View.GONE);
                    }
                    resList = response.body();
                    recyclerAdapter.setClubResList(resList);
                    recyclerAdapter = new MyTrainingAdapter(MyTrainingActivity.this,resList);
                    recyclerView.setAdapter(recyclerAdapter);
                    hideProgressDialog();
                    if(response.body() == null){
                        Toast.makeText(MyTrainingActivity.this, "Don't have trainings", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MyTrainingActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    hideProgressDialog();
                }
            }

            @Override
            public void onFailure(Call<List<AddTrainingReq>> call, Throwable t) {
                t.getMessage();
                Toast.makeText(MyTrainingActivity.this, "", Toast.LENGTH_SHORT).show();
                hideProgressDialog();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_training);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerMyTraining);
        resList = new ArrayList<>();
        textNoTraining = findViewById(R.id.textNoTraining);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter = new MyTrainingAdapter(this,resList);
        recyclerView.setAdapter(recyclerAdapter);
        resList.clear();
        serverApi = ServerUtils.serverApi();
        preferenceManager = new PreferenceManager(this);

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

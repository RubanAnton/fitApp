package anton_ruban.fitz.main.view;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import anton_ruban.fitz.utils.BaseActivity;
import anton_ruban.fitz.R;
import anton_ruban.fitz.main.adapter.ClubCardAdapter;
import anton_ruban.fitz.network.ServerApi;
import anton_ruban.fitz.network.ServerUtils;
import anton_ruban.fitz.network.res.SubscriptionRequestMain;
import anton_ruban.fitz.utils.PreferenceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {

    private List<SubscriptionRequestMain> resList;
    private RecyclerView recyclerView;
    private ClubCardAdapter recyclerAdapter;
    private ServerApi serverApi;
    private PreferenceManager preferenceManager;
    private ProgressDialog mProgressDialog;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        android.support.v7.app.ActionBar ab = getSupportActionBar();
//        ab.setTitle("Fitz");

        if(preferenceManager == null){
            preferenceManager = new PreferenceManager(this);
        }
        swipeRefreshLayout = findViewById(R.id.constraintMain);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerMain);
        resList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter = new ClubCardAdapter(getApplicationContext(),resList);
        recyclerView.setAdapter(recyclerAdapter);
        resList.clear();
        serverApi = ServerUtils.serverApi();
        showProgressDialog();
        serverApi.getClubList(preferenceManager.getUserToken(),preferenceManager.getUserId()).enqueue(new Callback<ArrayList<SubscriptionRequestMain>>() {
            @Override
            public void onResponse(Call<ArrayList<SubscriptionRequestMain>> call, Response<ArrayList<SubscriptionRequestMain>> response) {
                if(response.isSuccessful()){
                    resList = response.body();
                    recyclerAdapter.setClubResList(resList);
                    recyclerAdapter = new ClubCardAdapter(getApplicationContext(),resList);
                    recyclerView.setAdapter(recyclerAdapter);
                    hideProgressDialog();
                    if (response.body() == null){
                        Logger.d("body null", response.message());
                    }
                }else {
                    hideProgressDialog();

                }
            }

            @Override
            public void onFailure(Call<ArrayList<SubscriptionRequestMain>> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(MainActivity.this, "Check your connection", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                resList.clear();
                swipeRefreshLayout.setRefreshing(true);
                serverApi.getClubList(preferenceManager.getUserToken(),preferenceManager.getUserId()).enqueue(new Callback<ArrayList<SubscriptionRequestMain>>() {
                    @Override
                    public void onResponse(Call<ArrayList<SubscriptionRequestMain>> call, Response<ArrayList<SubscriptionRequestMain>> response) {
                        if(response.isSuccessful()){
                            resList = response.body();
                            recyclerAdapter.setClubResList(resList);
                            recyclerAdapter = new ClubCardAdapter(getApplicationContext(),resList);
                            recyclerView.setAdapter(recyclerAdapter);
                            swipeRefreshLayout.setRefreshing(false);
                            if (response.body() == null){
                                Logger.d("body null", response.message());
                            }
                        }else {
                            swipeRefreshLayout.setRefreshing(false);

                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<SubscriptionRequestMain>> call, Throwable t) {

                        Toast.makeText(MainActivity.this, "Check your connection", Toast.LENGTH_SHORT).show();
                    }
                });
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

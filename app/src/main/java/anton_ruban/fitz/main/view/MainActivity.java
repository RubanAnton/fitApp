package anton_ruban.fitz.main.view;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import anton_ruban.fitz.BaseActivity;
import anton_ruban.fitz.BuilderActivity;
import anton_ruban.fitz.R;
import anton_ruban.fitz.data.RecyclerTouchHelper;
import anton_ruban.fitz.main.adapter.ClubCardAdapter;
import anton_ruban.fitz.network.ServerApi;
import anton_ruban.fitz.network.ServerUtils;
import anton_ruban.fitz.network.res.ClubRes;
import anton_ruban.fitz.nutri.NutriActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity implements RecyclerTouchHelper.RecyclerItemTouchHelperListener{

    private List<ClubRes> resList;
    private RecyclerView recyclerView;
    private ClubCardAdapter recyclerAdapter;
    private ServerApi serverApi;
    private ProgressDialog mProgressDialog;
    private ConstraintLayout constraintLayoutMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        android.support.v7.app.ActionBar ab = getSupportActionBar();
//        ab.setTitle("Fitz");

        constraintLayoutMain = findViewById(R.id.constraintMain);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerMain);
        resList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter = new ClubCardAdapter(getApplicationContext(),resList);
        recyclerView.setAdapter(recyclerAdapter);
        resList.clear();
        serverApi = ServerUtils.serverApi();
        showProgressDialog();
        serverApi.getClubList().enqueue(new Callback<ArrayList<ClubRes>>() {
            @Override
            public void onResponse(Call<ArrayList<ClubRes>> call, Response<ArrayList<ClubRes>> response) {
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
                    Toast.makeText(MainActivity.this, "Check your connection", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ClubRes>> call, Throwable t) {
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

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof ClubCardAdapter.ViewHolder) {

            String name = resList.get(viewHolder.getAdapterPosition()).getNameClub();
            final ClubRes deletedItem = resList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            recyclerAdapter.removeItem(viewHolder.getAdapterPosition());
            Snackbar snackbar = Snackbar.make(constraintLayoutMain, name + " removed club!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recyclerAdapter.restoreItem(deletedItem, deletedIndex);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }
}

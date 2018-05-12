package anton_ruban.fitz.main.view;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import anton_ruban.fitz.BaseActivity;
import anton_ruban.fitz.BuilderActivity;
import anton_ruban.fitz.R;
import anton_ruban.fitz.main.adapter.ClubCardAdapter;
import anton_ruban.fitz.network.ServerApi;
import anton_ruban.fitz.network.ServerUtils;
import anton_ruban.fitz.network.res.ClubRes;
import anton_ruban.fitz.nutri.NutriActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    private List<ClubRes> resList;
    private RecyclerView recyclerView;
    private ClubCardAdapter recyclerAdapter;
    private ServerApi serverApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setTitle("Fitz");
        findViewById(R.id.polygonWorkout).setOnClickListener(this);
        findViewById(R.id.polygonFood).setOnClickListener(this);
        findViewById(R.id.polygonProgress).setOnClickListener(this);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerMain);
        resList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter = new ClubCardAdapter(getApplicationContext(),resList);
        recyclerView.setAdapter(recyclerAdapter);
//        serverApi = ServerUtils.serverApi();
//        serverApi.getClubList().enqueue(new Callback<ArrayList<ClubRes>>() {
//
//            @Override
//            public void onResponse(Call<ArrayList<ClubRes>> call, Response<ArrayList<ClubRes>> response) {
//                if(response.isSuccessful()){
//                    recyclerAdapter.setClubResList(resList);
//                    Toast.makeText(MainActivity.this, "Club loaded", Toast.LENGTH_SHORT).show();
//                }else {
//                    Toast.makeText(MainActivity.this, "Check your connection", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ArrayList<ClubRes>> call, Throwable t) {
//
//            }
//        });

    }



    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.polygonWorkout) {
            Intent intent = new Intent(MainActivity.this,BuilderActivity.class);
            startActivity(intent);
        } else if (i == R.id.polygonFood) {
            Intent intent = new Intent(MainActivity.this,NutriActivity.class);
            startActivity(intent);
        } else if (i == R.id.polygonProgress) {
            Toast.makeText(this, "Progress Button Pressed", Toast.LENGTH_LONG).show();
        }
    }
}

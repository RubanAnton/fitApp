package anton_ruban.fitz.coach.CreateTraining.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import anton_ruban.fitz.R;
import anton_ruban.fitz.coach.CreateTraining.adapter.CreateSingleAdapter;
import anton_ruban.fitz.data.models.Exercise;
import anton_ruban.fitz.network.ServerApi;
import anton_ruban.fitz.network.ServerUtils;
import anton_ruban.fitz.network.res.GetExerciseResp;
import anton_ruban.fitz.utils.PreferenceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectSingleExerActivity extends AppCompatActivity {

    public ProgressDialog mProgressDialog;
    private CreateSingleAdapter adapter;
    private RecyclerView recyclerView;
    private List<GetExerciseResp> exerciseResps;
    private ServerApi serverApi;
    private PreferenceManager preferenceManager;
    List<GetExerciseResp> listExercise;
    private Button btnToTraining;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_single_exer);

        preferenceManager = new PreferenceManager(this);
        serverApi = ServerUtils.serverApi();
        recyclerView = findViewById(R.id.recyclerSingleExercise);
        btnToTraining = findViewById(R.id.btnToTraining);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        exerciseResps = new ArrayList<>();
        adapter = new CreateSingleAdapter(this,exerciseResps);
        recyclerView.setAdapter(adapter);
        showProgress();
        final GetExerciseResp responseExer = new GetExerciseResp();
        final List<GetExerciseResp> list = new ArrayList<>();
        serverApi.getAllExercise(preferenceManager.getUserToken()).enqueue(new Callback<List<GetExerciseResp>>() {
            @Override
            public void onResponse(Call<List<GetExerciseResp>> call, Response<List<GetExerciseResp>> response) {
                if(response.code() > 200 && response.code() < 300 || response.code() == 200 || response.isSuccessful()){
                    exerciseResps = response.body();
                    if(preferenceManager.getTypeExe().equalsIgnoreCase("warm")){
                        for (int i = 0; i < exerciseResps.size(); i++){
                            if(exerciseResps.get(i).getType().equalsIgnoreCase("warm_up")){
                                responseExer.setIdExercise(exerciseResps.get(i).getIdExercise());
                                responseExer.setName(exerciseResps.get(i).getName());
                                responseExer.setType(exerciseResps.get(i).getType());
                                responseExer.setStatus(exerciseResps.get(i).getStatus());
                                responseExer.setUrl(exerciseResps.get(i).getUrl());
                                list.add(responseExer);
                                adapter.setExerciseList(list);
                                adapter = new CreateSingleAdapter(SelectSingleExerActivity.this,list);
                                recyclerView.setAdapter(adapter);
                                hideProgress();
                            }else {
                                hideProgress();
                            }
                        }
                    }
                    if(preferenceManager.getTypeExe().equalsIgnoreCase("emom")){
                        for (int i = 0; i < exerciseResps.size(); i++){
                            if(exerciseResps.get(i).getType().equalsIgnoreCase("emom")){
                                responseExer.setIdExercise(exerciseResps.get(i).getIdExercise());
                                responseExer.setName(exerciseResps.get(i).getName());
                                responseExer.setType(exerciseResps.get(i).getType());
                                responseExer.setStatus(exerciseResps.get(i).getStatus());
                                responseExer.setUrl(exerciseResps.get(i).getUrl());
                                list.add(responseExer);
                                adapter.setExerciseList(list);
                                adapter = new CreateSingleAdapter(SelectSingleExerActivity.this,list);
                                recyclerView.setAdapter(adapter);
                                hideProgress();
                            }else {
                                hideProgress();
                            }
                        }
                    }
                    if(preferenceManager.getTypeExe().equalsIgnoreCase("amrap")){
                        for (int i = 0; i < exerciseResps.size(); i++){
                            if(exerciseResps.get(i).getType().equalsIgnoreCase("amrap")){
                                responseExer.setIdExercise(exerciseResps.get(i).getIdExercise());
                                responseExer.setName(exerciseResps.get(i).getName());
                                responseExer.setType(exerciseResps.get(i).getType());
                                responseExer.setStatus(exerciseResps.get(i).getStatus());
                                responseExer.setUrl(exerciseResps.get(i).getUrl());
                                list.add(responseExer);
                                adapter.setExerciseList(list);
                                adapter = new CreateSingleAdapter(SelectSingleExerActivity.this,list);
                                recyclerView.setAdapter(adapter);
                                hideProgress();
                            }else {
                                hideProgress();
                            }
                        }
                    }
                    if(preferenceManager.getTypeExe().equalsIgnoreCase("amrap_for_min")){
                        for (int i = 0; i < exerciseResps.size(); i++){
                            if(exerciseResps.get(i).getType().equalsIgnoreCase("amrap_minutes")){
                                responseExer.setIdExercise(exerciseResps.get(i).getIdExercise());
                                responseExer.setName(exerciseResps.get(i).getName());
                                responseExer.setType(exerciseResps.get(i).getType());
                                responseExer.setStatus(exerciseResps.get(i).getStatus());
                                responseExer.setUrl(exerciseResps.get(i).getUrl());
                                list.add(responseExer);
                                adapter.setExerciseList(list);
                                adapter = new CreateSingleAdapter(SelectSingleExerActivity.this,list);
                                recyclerView.setAdapter(adapter);
                                hideProgress();
                            }else {
                                hideProgress();
                            }
                        }
                    }
                }else {
                    hideProgress();
                    Toast.makeText(SelectSingleExerActivity.this, "Error load exercise", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<GetExerciseResp>> call, Throwable t) {
                hideProgress();
                Toast.makeText(SelectSingleExerActivity.this, "Bad connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onClickToTraining(View view){
        Intent intent = new Intent(this,SingleCreateTrainingActivity.class);
        ArrayList<GetExerciseResp> list = adapter.exerciseList;
        intent.putExtra("ListExercise",list);
        startActivity(intent);
    }

    public void showProgress(){
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgress(){
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}

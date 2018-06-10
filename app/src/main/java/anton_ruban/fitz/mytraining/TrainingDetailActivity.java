package anton_ruban.fitz.mytraining;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import anton_ruban.fitz.R;
import anton_ruban.fitz.mytraining.adapter.DetailTrainigAdapter;
import anton_ruban.fitz.mytraining.adapter.MyTrainingAdapter;
import anton_ruban.fitz.network.ServerApi;
import anton_ruban.fitz.network.ServerUtils;
import anton_ruban.fitz.network.req.AddTrainingReq;
import anton_ruban.fitz.network.req.CreateSingleTrainingReq;
import anton_ruban.fitz.network.res.GetExerciseResp;

public class TrainingDetailActivity extends AppCompatActivity {

    private List<GetExerciseResp> resList;
    private RecyclerView recyclerView;
    private DetailTrainigAdapter recyclerAdapter;
    private ServerApi serverApi;
    private CreateSingleTrainingReq trainingReq;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_detail);
        resList = getIntent().getParcelableArrayListExtra("listUserExerciseArray");
        trainingReq = getIntent().getParcelableExtra("trainingSingle");
        serverApi = ServerUtils.serverApi();
        recyclerView = (RecyclerView)findViewById(R.id.trainingDetailRV);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter = new DetailTrainigAdapter(this,resList, serverApi, trainingReq);
        recyclerView.setAdapter(recyclerAdapter);
    }
}

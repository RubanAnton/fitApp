package anton_ruban.fitz.coach.CreateTraining.view;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import anton_ruban.fitz.R;
import anton_ruban.fitz.coach.CreateTraining.adapter.CreateSingleAdapter;
import anton_ruban.fitz.coach.view.CoachActivity;
import anton_ruban.fitz.network.ServerApi;
import anton_ruban.fitz.network.ServerUtils;
import anton_ruban.fitz.network.req.AddTrainingReq;
import anton_ruban.fitz.network.req.CreateSingleTrainingReq;
import anton_ruban.fitz.network.res.GetExerciseResp;
import anton_ruban.fitz.utils.PreferenceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SingleCreateTrainingActivity extends AppCompatActivity {

    public ProgressDialog mProgressDialog;
    private ServerApi serverApi;
    private PreferenceManager preferenceManager;
    private int mYear, mMonth, mDay;
    private EditText editName,editDate,editDescr;
    private CreateSingleTrainingReq req;
    private LinearLayout lin1,lin2,lin3,lin4;
    private ArrayList<GetExerciseResp> exerciseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_create_training);

        editDate = findViewById(R.id.editDateTraining);
        editName = findViewById(R.id.editNameTraining);
        editDescr = findViewById(R.id.editDescriptionTraining);
        lin1 = findViewById(R.id.linearWramTypeSingle);
        lin2 = findViewById(R.id.linearEmomSingle);
        lin3 = findViewById(R.id.linearAmrapSingle);
        lin4 = findViewById(R.id.linearAmrap15Single);

        exerciseList = (ArrayList<GetExerciseResp>) getIntent().getSerializableExtra("ListExercise");

        preferenceManager = new PreferenceManager(this);
        serverApi = ServerUtils.serverApi();
        if(preferenceManager.isSelectedWrap()){
            lin1.setBackground(getResources().getDrawable(R.drawable.corner_linear_training));
        }
        if(preferenceManager.isSelectedEmom()){
            lin2.setBackground(getResources().getDrawable(R.drawable.corner_linear_training));
        }
        if(preferenceManager.isSelectedAR()){
            lin3.setBackground(getResources().getDrawable(R.drawable.corner_linear_training));
        }
        if(preferenceManager.isSelectedARMin()){
            lin4.setBackground(getResources().getDrawable(R.drawable.corner_linear_training));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void setLinearWarm(View view){
        preferenceManager.setTypeExe("warm");
        startActivity(new Intent(this,SelectSingleExerActivity.class));
        finish();
    }

    public void setLinearEmom(View view) {
        preferenceManager.setTypeExe("emom");
        startActivity(new Intent(this,SelectSingleExerActivity.class));
        finish();
    }

    public void setLinearAmRap(View view) {
        preferenceManager.setTypeExe("amrap");
        startActivity(new Intent(this,SelectSingleExerActivity.class));
        finish();
    }

    public void setLinearAmrap15(View view) {
        preferenceManager.setTypeExe("amrap_for_min");
        startActivity(new Intent(this,SelectSingleExerActivity.class));
        finish();
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

    public void onSelectDate(View view){
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        editDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public void clickCreateSingleTraining(View view) {
        req = new CreateSingleTrainingReq();
        req.setNameTraining(editName.getText().toString());
        req.setDescriptionTraining(editDescr.getText().toString());
        req.setDateTraining(editDate.getText().toString());
        req.setIdCoach(preferenceManager.getCoachClub());
        req.setIdUser(preferenceManager.getUserSingleTraining());
        req.setIdExercise(String.valueOf(preferenceManager.getExerID()));
        req.setIsDone(0);
        AddTrainingReq trainingReq = new AddTrainingReq();
        trainingReq.setTraining(req);
        trainingReq.setExercises(exerciseList);
        showProgress();
        serverApi.createTraining(trainingReq).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code() > 200 && response.code() < 300 || response.isSuccessful() || response.code() == 200){
                    preferenceManager.setSelectedWarm(false);
                    preferenceManager.setSelectedEmom(false);
                    preferenceManager.setSelectedAr(false);
                    preferenceManager.setSelectedArmin(false);
                    hideProgress();
                    startActivity(new Intent(SingleCreateTrainingActivity.this, CoachActivity.class));
                    finish();
                }else {
                    hideProgress();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                hideProgress();
            }
        });
    }
}

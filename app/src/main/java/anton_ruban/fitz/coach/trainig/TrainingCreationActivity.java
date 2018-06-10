package anton_ruban.fitz.coach.trainig;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import anton_ruban.fitz.R;
import anton_ruban.fitz.coach.CreateTraining.view.SingleCreateTrainingActivity;
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

public class TrainingCreationActivity extends AppCompatActivity {

    private int mYear, mMonth, mDay;
    private Button datebb;
    private GetExerciseResp exerciseAdded;
    private int USER_ID;
    private CreateSingleTrainingReq req;
    private ServerApi serverApi;
    private RecyclerView recyclerView;
    EditText text1,text2;
    private AdapterAddedTrainings adapter;
    private Date date;
    private ArrayList<GetExerciseResp> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_creation);
        datebb = findViewById(R.id.date_BTN);
        serverApi = ServerUtils.serverApi();
        USER_ID = getIntent().getIntExtra("USER_ID_EXERCISE",0);
        recyclerView = findViewById(R.id.trainingExercisesRV);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        list = new ArrayList<>();
        text1 = (EditText) findViewById(R.id.nameEditCreate);
        text2 = (EditText) findViewById(R.id.descriptionEditCreate);
        adapter = new AdapterAddedTrainings(list,this);
        recyclerView.setAdapter(adapter);

    }

    public void onClickDateTraining(View view) {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        date = new Date();
                        date.setYear(year);
                        date.setMonth(monthOfYear);
                        date.setDate(dayOfMonth);
                        datebb.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    public void warmUpPressed(View view) {
        Intent intent = new Intent(this, ExerciseCreationActivity.class);
        intent.putExtra("typeEXERCISE", 0);
        intent.putExtra("userIdEXERCISE", USER_ID);
        intent.putExtra("listExercises", list);
        startActivityForResult(intent, 1003);
    }

    public void emomPressed(View view) {
        Intent intent = new Intent(this, ExerciseCreationActivity.class);
        intent.putExtra("typeEXERCISE", 1);
        intent.putExtra("userIdEXERCISE", USER_ID);
        intent.putExtra("listExercises", list);
        startActivityForResult(intent, 1003);
    }

    public void amrapPressed(View view) {
        Intent intent = new Intent(this, ExerciseCreationActivity.class);
        intent.putExtra("typeEXERCISE", 2);
        intent.putExtra("userIdEXERCISE", USER_ID);
        intent.putExtra("listExercises", list);
        startActivityForResult(intent, 1003);
    }

    public void amrap15Pressed(View view) {
        Intent intent = new Intent(this, ExerciseCreationActivity.class);
        intent.putExtra("typeEXERCISE", 3);
        intent.putExtra("userIdEXERCISE", USER_ID);
        intent.putExtra("listExercises", list);
        startActivityForResult(intent, 1003);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 1003 && data != null) {
                GetExerciseResp exercise = data.getParcelableExtra("exerciseADDED");
                list.add(exercise);
                adapter.notifyDataSetChanged();

        }
    }

    public void addTrainingFromHistory(View view){
        Intent intent = new Intent(this,HistoryActivityActivity.class);
        intent.putExtra("userIdEXERCISE", USER_ID);
        startActivityForResult(intent, 1003);
    }

    public void addTraining(View view) {
        if (!text1.getText().equals("") && !text2.getText().equals("") && !datebb.getText().equals(getString(R.string.notSelected)) && !list.isEmpty()) {
            PreferenceManager preferenceManager = new PreferenceManager(this);
            req = new CreateSingleTrainingReq();
            req.setNameTraining(text1.getText().toString());
            req.setDescriptionTraining(text2.getText().toString());
            req.setDateTraining(date.toString());
            req.setIdCoach(preferenceManager.getCoachClub());
            req.setIdUser(USER_ID);
            req.setIdExercise(String.valueOf(preferenceManager.getExerID()));
            req.setIsDone(0);
            AddTrainingReq trainingReq = new AddTrainingReq();
            trainingReq.setTraining(req);
            trainingReq.setExercises(list);

            serverApi.createTraining(trainingReq).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.code() > 200 && response.code() < 300 || response.isSuccessful() || response.code() == 200) {
                        finish();
                    } else {
                        Toast.makeText(TrainingCreationActivity.this, "BAD REQUEST", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {

                    Toast.makeText(TrainingCreationActivity.this, "BAD REQUEST", Toast.LENGTH_SHORT).show();
                }
            });
        }  else {
            Toast.makeText(this, getString(R.string.noChooseen), Toast.LENGTH_SHORT).show();
        }
    }
}

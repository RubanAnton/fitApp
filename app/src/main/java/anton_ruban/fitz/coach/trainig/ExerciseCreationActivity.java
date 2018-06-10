package anton_ruban.fitz.coach.trainig;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

import anton_ruban.fitz.R;
import anton_ruban.fitz.exercise.AMRAP15Exercise;
import anton_ruban.fitz.exercise.AMRAPExercise;
import anton_ruban.fitz.exercise.EMOMExercise;
import anton_ruban.fitz.exercise.WarmUpExercise;
import anton_ruban.fitz.network.res.GetExerciseResp;

public class ExerciseCreationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdapterExercises adapter;
    private int type;
    private ArrayList<GetExerciseResp> array;
    private int userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_creation);
        type = this.getIntent().getIntExtra("typeEXERCISE",0);
        userId = this.getIntent().getIntExtra("userIdEXERCISE",0);
        array = this.getIntent().getParcelableArrayListExtra("listExercises");
    }

    @Override
    protected void onResume() {
        super.onResume();
        recyclerView = findViewById(R.id.exercisesRECYCLER);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        switch (type) {
            case 0: {
                WarmUpExercise exercise = new WarmUpExercise(userId);
                adapter = new AdapterExercises(exercise.getExercises(),this, array);
                recyclerView.setAdapter(adapter);
            } break;

            case 1: {
                EMOMExercise exercise = new EMOMExercise(userId);
                adapter = new AdapterExercises(exercise.getExercises(),this, array);
                recyclerView.setAdapter(adapter);
            } break;

            case 2: {
                AMRAPExercise exercise = new AMRAPExercise(userId);
                adapter = new AdapterExercises(exercise.getExercises(),this, array);
                recyclerView.setAdapter(adapter);
            } break;

            case 3: {
                AMRAP15Exercise exercise = new AMRAP15Exercise(userId);
                adapter = new AdapterExercises(exercise.getExercises(),this, array);
                recyclerView.setAdapter(adapter);
            } break;
        }
    }
}

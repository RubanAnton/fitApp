package anton_ruban.fitz.network.req;

import java.util.ArrayList;
import java.util.List;

import anton_ruban.fitz.network.res.GetExerciseResp;

/**
 * Created by antonruban on 28.05.2018.
 */

public class AddTrainingReq {

    private CreateSingleTrainingReq training;
    private ArrayList<GetExerciseResp> exercises;

    public CreateSingleTrainingReq getTraining() {
        return training;
    }

    public void setTraining(CreateSingleTrainingReq training) {
        this.training = training;
    }

    public ArrayList<GetExerciseResp> getExercises() {
        return exercises;
    }

    public void setExercises(ArrayList<GetExerciseResp> exercises) {
        this.exercises = exercises;
    }
}

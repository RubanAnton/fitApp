package anton_ruban.fitz.network.res;

import com.google.gson.annotations.SerializedName;

import anton_ruban.fitz.network.req.CreateSingleTrainingReq;

/**
 * Created by antonruban on 27.05.2018.
 */

public class UserTrainingResponse {

    @SerializedName("exercises")
    private GetExerciseResp exerciseResp;
    @SerializedName("training")
    private CreateSingleTrainingReq singleTrainingReq;

    public GetExerciseResp getExerciseResp() {
        return exerciseResp;
    }

    public void setExerciseResp(GetExerciseResp exerciseResp) {
        this.exerciseResp = exerciseResp;
    }

    public CreateSingleTrainingReq getSingleTrainingReq() {
        return singleTrainingReq;
    }

    public void setSingleTrainingReq(CreateSingleTrainingReq singleTrainingReq) {
        this.singleTrainingReq = singleTrainingReq;
    }
}

package anton_ruban.fitz.exercise;

import java.util.ArrayList;
import java.util.List;

import anton_ruban.fitz.network.res.GetExerciseResp;

/**
 * Created by mac-242 on 5/29/18.
 */

public class WarmUpExercise {

    public List<GetExerciseResp> getExercises() {
        return exercises;
    }

    public void setExercises(List<GetExerciseResp> exercises) {
        this.exercises = exercises;
    }

    public List<GetExerciseResp> exercises;


    public WarmUpExercise(int idUser) {
        this.exercises = new ArrayList<>();
        exercises.add(new GetExerciseResp(0,0,"Strict survival in a rack on the hands (upside down)","WarmUp", "https://www.demonuts.com/Demonuts/smallvideo.mp4", 0, idUser ));
        exercises.add(new GetExerciseResp(0,0,"Rapid survival in a rack on the hands (upside down)","WarmUp", "https://www.demonuts.com/Demonuts/smallvideo.mp4", 0, idUser ));
        exercises.add(new GetExerciseResp(0,0,"Concentrated lifting dumbbells to the biceps","WarmUp", "https://www.demonuts.com/Demonuts/smallvideo.mp4", 0, idUser ));
        exercises.add(new GetExerciseResp(0,0,"Lifting dumbbells from behind back over head to triceps","WarmUp", "https://www.demonuts.com/Demonuts/smallvideo.mp4", 0, idUser ));
        exercises.add(new GetExerciseResp(0,0,"Pulling on the crossbar","WarmUp", "https://www.demonuts.com/Demonuts/smallvideo.mp4", 0, idUser ));
        exercises.add(new GetExerciseResp(0,0,"Air Bicycle","WarmUp", "https://www.demonuts.com/Demonuts/smallvideo.mp4", 0, idUser ));
        exercises.add(new GetExerciseResp(0,0,"Lifting the heels","WarmUp", "https://www.demonuts.com/Demonuts/smallvideo.mp4", 0, idUser ));
        exercises.add(new GetExerciseResp(0,0,"Double Jump on a Rope","WarmUp", "https://www.demonuts.com/Demonuts/smallvideo.mp4", 0, idUser ));
        exercises.add(new GetExerciseResp(0,0,"Ascent to the biceps, dumbbells of 20 kg","WarmUp", "https://www.demonuts.com/Demonuts/smallvideo.mp4", 0, idUser ));
        exercises.add(new GetExerciseResp(0,0,"Boat","WarmUp", "https://www.demonuts.com/Demonuts/smallvideo.mp4", 0, idUser ));
    }
}

package anton_ruban.fitz.exercise;

import java.util.ArrayList;
import java.util.List;

import anton_ruban.fitz.network.res.GetExerciseResp;

/**
 * Created by mac-242 on 5/29/18.
 */

public class AMRAPExercise {
    public List<GetExerciseResp> getExercises() {
        return exercises;
    }

    public void setExercises(List<GetExerciseResp> exercises) {
        this.exercises = exercises;
    }

    public List<GetExerciseResp> exercises;

    public AMRAPExercise(int idUser) {

        this.exercises = new ArrayList<>();
        exercises.add(new GetExerciseResp(0,0,"10 barbell lifts","AMRAP", "https://www.demonuts.com/Demonuts/smallvideo.mp4", 0, idUser ));
        exercises.add(new GetExerciseResp(0,0,"10 replays of the French press","AMRAP", "https://www.demonuts.com/Demonuts/smallvideo.mp4", 0, idUser ));
        exercises.add(new GetExerciseResp(0,0,"10 pull-ups on the biceps (palms aimed at you)","AMRAP", "https://www.demonuts.com/Demonuts/smallvideo.mp4", 0, idUser ));
        exercises.add(new GetExerciseResp(0,0,"10 horizontal push-ups on the rings","AMRAP", "https://www.demonuts.com/Demonuts/smallvideo.mp4", 0, idUser ));
        exercises.add(new GetExerciseResp(0,0,"20 dumbbell lifts in the style of \"Hammer\", dumbbells of 16 kg ","AMRAP", "https://www.demonuts.com/Demonuts/smallvideo.mp4", 0, idUser ));
        exercises.add(new GetExerciseResp(0,0,"20 Hammer Curls Variable, dumbbells of 16 kg","AMRAP", "https://www.demonuts.com/Demonuts/smallvideo.mp4", 0, idUser ));
        exercises.add(new GetExerciseResp(0,0,"50 meter walk of the farmer, weights of 32 kg","AMRAP", "https://www.demonuts.com/Demonuts/smallvideo.mp4", 0, idUser ));
        exercises.add(new GetExerciseResp(0,0,"10 bar press reps lying on a narrow grip, 62 kg","AMRAP", "https://www.demonuts.com/Demonuts/smallvideo.mp4", 0, idUser ));
        exercises.add(new GetExerciseResp(0,0,"10 push-ups with a palm rest in the lower position","AMRAP", "https://www.demonuts.com/Demonuts/smallvideo.mp4", 0, idUser ));
        exercises.add(new GetExerciseResp(0,0,"10 shvung zhimova, 62 kg","AMRAP", "https://www.demonuts.com/Demonuts/smallvideo.mp4", 0, idUser ));
    }

}

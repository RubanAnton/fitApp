package anton_ruban.fitz.exercise;

import java.util.ArrayList;
import java.util.List;

import anton_ruban.fitz.data.models.Exercise;
import anton_ruban.fitz.network.res.GetExerciseResp;

/**
 * Created by mac-242 on 5/29/18.
 */

public class AMRAP15Exercise {
    public List<GetExerciseResp> getExercises() {
        return exercises;
    }

    public void setExercises(List<GetExerciseResp> exercises) {
        this.exercises = exercises;
    }

    public List<GetExerciseResp> exercises;

    public AMRAP15Exercise(int idUser) {

        this.exercises = new ArrayList<>();
        exercises.add(new GetExerciseResp(0,0,"10 pull-ups on the crossbar to the chest","AMRAP15", "https://www.demonuts.com/Demonuts/smallvideo.mp4", 0, idUser ));
        exercises.add(new GetExerciseResp(0,0,"10 pull-ups","AMRAP15", "https://www.demonuts.com/Demonuts/smallvideo.mp4", 0, idUser ));
        exercises.add(new GetExerciseResp(0,0,"10 Push-ups","AMRAP15", "https://www.demonuts.com/Demonuts/smallvideo.mp4", 0, idUser ));
        exercises.add(new GetExerciseResp(0,0,"10 slopes with dumbbells, 22 kg","AMRAP15", "https://www.demonuts.com/Demonuts/smallvideo.mp4", 0, idUser ));
        exercises.add(new GetExerciseResp(0,0,"10 shvung zhimovoj, dumbbells of 22 kg","AMRAP15", "https://www.demonuts.com/Demonuts/smallvideo.mp4", 0, idUser ));
        exercises.add(new GetExerciseResp(0,0,"50 pull-ups on the crossbar","AMRAP15", "https://www.demonuts.com/Demonuts/smallvideo.mp4", 0, idUser ));
        exercises.add(new GetExerciseResp(0,0,"5 Man makers, 22 kg dumbbells","AMRAP15", "https://www.demonuts.com/Demonuts/smallvideo.mp4", 0, idUser ));
        exercises.add(new GetExerciseResp(0,0,"10 strict pulls on the crossbar","AMRAP15", "https://www.demonuts.com/Demonuts/smallvideo.mp4", 0, idUser ));
        exercises.add(new GetExerciseResp(0,0,"5 strict push-ups in the rack on the hands (upside down)","AMRAP15", "https://www.demonuts.com/Demonuts/smallvideo.mp4", 0, idUser ));
        exercises.add(new GetExerciseResp(0,0,"Concentrated lifting of the bar to the biceps, 20 kg","AMRAP15", "https://www.demonuts.com/Demonuts/smallvideo.mp4", 0, idUser ));
    }

}

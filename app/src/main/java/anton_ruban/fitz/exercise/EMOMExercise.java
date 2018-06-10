package anton_ruban.fitz.exercise;

import java.util.ArrayList;
import java.util.List;

import anton_ruban.fitz.network.res.GetExerciseResp;

/**
 * Created by mac-242 on 5/29/18.
 */

public class EMOMExercise {
    public List<GetExerciseResp> getExercises() {
        return exercises;
    }

    public void setExercises(List<GetExerciseResp> exercises) {
        this.exercises = exercises;
    }

    public List<GetExerciseResp> exercises;

    public EMOMExercise(int idUser) {

        this.exercises = new ArrayList<>();
        exercises.add(new GetExerciseResp(0,0,"200 push-ups","EMOM", "http://training365.ru/wp-content/uploads/2017/10/5-минут-для-плоского-живота.mp4?_=1", 0, idUser ));
        exercises.add(new GetExerciseResp(0,0,"300 sit-ups","EMOM", "http://training365.ru/wp-content/uploads/2017/10/пресс-2.mp4?_=2", 0, idUser ));
        exercises.add(new GetExerciseResp(0,0,"10 horizontal push-ups on the rings," +
                "10 horizontal pull-ups on the rings","EMOM", "http://training365.ru/wp-content/uploads/2017/10/ягодицы-и-ноги-2.mp4?_=4", 0, idUser ));
        exercises.add(new GetExerciseResp(0,0,"5 lifts per rope (each time change the starting hand)","EMOM", "http://training365.ru/wp-content/uploads/2017/10/как-похудеть-в-ногах.mp4?_=3", 0, idUser ));
        exercises.add(new GetExerciseResp(0,0,"bench press, 61 kg," +
                "GHD sit-up," ,"EMOM", "http://training365.ru/wp-content/uploads/2017/10/ягодицы-и-ноги.mp4?_=5", 0, idUser ));
        exercises.add(new GetExerciseResp(0,0,"3 manmaker","EMOM", "http://dolgoyjizni.ru/wp-content/uploads/2014/03/Фитнес-с-фитболом-для-похудения.mp4?_=11", 0, idUser ));
        exercises.add(new GetExerciseResp(0,0,"strict pull-ups,","EMOM", "http://dolgoyjizni.ru/wp-content/uploads/2014/03/Упражнения-с-фитболом-для-похудения-живота.mp4?_=12", 0, idUser ));
        exercises.add(new GetExerciseResp(0,0,"push-ups with a palm rest in the bottom position," +
                "sit-ups","EMOM", "http://dolgoyjizni.ru/wp-content/uploads/2014/03/Упражнения-для-бедер-и-ягодиц-на-фитболе.mp4?_=13", 0, idUser ));
        exercises.add(new GetExerciseResp(0,0,"11 sit-ups with a barbell on the back","EMOM", "http://dolgoyjizni.ru/wp-content/uploads/2014/03/Pink-фитнес-сет-№3-Упражнения-для-стройных-ног.mp4?_=14", 0, idUser ));
        exercises.add(new GetExerciseResp(0,0,"7 dumbbell lifts through the sides","EMOM", "http://dolgoyjizni.ru/wp-content/uploads/2014/03/Упражнения-для-рук-от-Камерон-Диас.-Убираем-жир-с-рук-за-2-недели.mp4?_=15", 0, idUser ));
        exercises.add(new GetExerciseResp(0,0,"7 dumbbell lifts in front of you","EMOM", "https://www.demonuts.com/Demonuts/smallvideo.mp4", 0, idUser ));
    }
}

package anton_ruban.fitz.network.req;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by antonruban on 26.05.2018.
 */

public class CreateSingleTrainingReq implements Parcelable {

    private int idTraining;
    private String nameTraining;
    private String dateTraining;
    private String descriptionTraining;
    private int idUser;
    private String idExercise;
    private int idCoach;
    private int isDone;

    public CreateSingleTrainingReq() {
        idTraining = 0;
        nameTraining = "";
        dateTraining = "";
        descriptionTraining = "";
        idUser = 0;
        idExercise = "";
        idCoach = 0;
        isDone = 0;
    }

    protected CreateSingleTrainingReq(Parcel in) {
        idTraining = in.readInt();
        nameTraining = in.readString();
        dateTraining = in.readString();
        descriptionTraining = in.readString();
        idUser = in.readInt();
        idExercise = in.readString();
        idCoach = in.readInt();
        isDone = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idTraining);
        dest.writeString(nameTraining);
        dest.writeString(dateTraining);
        dest.writeString(descriptionTraining);
        dest.writeInt(idUser);
        dest.writeString(idExercise);
        dest.writeInt(idCoach);
        dest.writeInt(isDone);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CreateSingleTrainingReq> CREATOR = new Creator<CreateSingleTrainingReq>() {
        @Override
        public CreateSingleTrainingReq createFromParcel(Parcel in) {
            return new CreateSingleTrainingReq(in);
        }

        @Override
        public CreateSingleTrainingReq[] newArray(int size) {
            return new CreateSingleTrainingReq[size];
        }
    };

    public int getIdTraining() {
        return idTraining;
    }

    public void setIdTraining(int idTraining) {
        this.idTraining = idTraining;
    }

    public String getNameTraining() {
        return nameTraining;
    }

    public void setNameTraining(String nameTraining) {
        this.nameTraining = nameTraining;
    }

    public String getDateTraining() {
        return dateTraining;
    }

    public void setDateTraining(String dateTraining) {
        this.dateTraining = dateTraining;
    }

    public String getDescriptionTraining() {
        return descriptionTraining;
    }

    public void setDescriptionTraining(String descriptionTraining) {
        this.descriptionTraining = descriptionTraining;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getIdExercise() {
        return idExercise;
    }

    public void setIdExercise(String idExercise) {
        this.idExercise = idExercise;
    }

    public int getIdCoach() {
        return idCoach;
    }

    public void setIdCoach(int idCoach) {
        this.idCoach = idCoach;
    }

    public int getIsDone() {
        return isDone;
    }

    public void setIsDone(int isDone) {
        this.isDone = isDone;
    }
}

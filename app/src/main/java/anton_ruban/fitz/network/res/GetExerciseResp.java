package anton_ruban.fitz.network.res;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by antonruban on 26.05.2018.
 */

public class GetExerciseResp implements  Parcelable {

    private int idExercise;
    private int status;
    private String name;
    private String type;
    private String url;
    private int idTraining;
    private int idUser;

    public GetExerciseResp() {
        this.idExercise = 0;
        this.status = 0;
        this.name = "";
        this.type = "";
        this.url = "";
        this.idTraining = 0;
        this.idUser = 0;
    }

    public GetExerciseResp(int idExercise, int status, String name, String type, String url, int idTraining, int idUser) {
        this.idExercise = idExercise;
        this.status = status;
        this.name = name;
        this.type = type;
        this.url = url;
        this.idTraining = idTraining;
        this.idUser = idUser;
    }


    protected GetExerciseResp(Parcel in) {
        idExercise = in.readInt();
        status = in.readInt();
        name = in.readString();
        type = in.readString();
        url = in.readString();
        idTraining = in.readInt();
        idUser = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idExercise);
        dest.writeInt(status);
        dest.writeString(name);
        dest.writeString(type);
        dest.writeString(url);
        dest.writeInt(idTraining);
        dest.writeInt(idUser);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GetExerciseResp> CREATOR = new Creator<GetExerciseResp>() {
        @Override
        public GetExerciseResp createFromParcel(Parcel in) {
            return new GetExerciseResp(in);
        }

        @Override
        public GetExerciseResp[] newArray(int size) {
            return new GetExerciseResp[size];
        }
    };

    public int getIdExercise() {
        return idExercise;
    }

    public void setIdExercise(int idExercise) {
        this.idExercise = idExercise;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getIdTraining() {
        return idTraining;
    }

    public void setIdTraining(int idTraining) {
        this.idTraining = idTraining;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

}


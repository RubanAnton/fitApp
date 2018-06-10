package anton_ruban.fitz.network.req;

import com.google.gson.annotations.SerializedName;

/**
 * Created by antonruban on 13.05.2018.
 */

public class SubscriberRequest {

    @SerializedName("idSubscriber")
    private int idSubscriber;
    @SerializedName("idClub")
    private int idClub;
    @SerializedName("idOwner")
    private int idOwner;
    @SerializedName("status")
    private int status;
    @SerializedName("userSubscribeID")
    private int userSubscriber;

    public SubscriberRequest() {

    }

    public int getIdSubscriber() {
        return idSubscriber;
    }

    public void setIdSubscriber(int idSubscriber) {
        this.idSubscriber = idSubscriber;
    }

    public int getIdClub() {
        return idClub;
    }

    public void setIdClub(int idClub) {
        this.idClub = idClub;
    }

    public int getIdOwner() {
        return idOwner;
    }

    public void setIdOwner(int idOwner) {
        this.idOwner = idOwner;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getUserSubscriber() {
        return userSubscriber;
    }

    public void setUserSubscriber(int userSubscriber) {
        this.userSubscriber = userSubscriber;
    }
}

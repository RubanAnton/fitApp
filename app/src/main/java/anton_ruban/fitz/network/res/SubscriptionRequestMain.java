package anton_ruban.fitz.network.res;

import com.google.gson.annotations.SerializedName;

/**
 * Created by antonruban on 14.05.2018.
 */

public class SubscriptionRequestMain {

    @SerializedName("club")
    private ClubRes clubRes;
    @SerializedName("subscriberID")
    private int subscriberID;
    @SerializedName("status")
    private int status;

    public SubscriptionRequestMain(ClubRes clubRes, int subscriberID, int status) {
        this.clubRes = clubRes;
        this.subscriberID = subscriberID;
        this.status = status;
    }

    public ClubRes getClubRes() {
        return clubRes;
    }

    public void setClubRes(ClubRes clubRes) {
        this.clubRes = clubRes;
    }

    public int getSubscriberID() {
        return subscriberID;
    }

    public void setSubscriberID(int subscriberID) {
        this.subscriberID = subscriberID;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

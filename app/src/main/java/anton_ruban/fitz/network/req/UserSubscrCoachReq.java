package anton_ruban.fitz.network.req;

import com.google.gson.annotations.SerializedName;

import anton_ruban.fitz.network.res.UserResponse;

/**
 @author antonruban on 18.05.2018.
 */

public class UserSubscrCoachReq {

    @SerializedName("user")
    private UserResponse user;
    @SerializedName("idSubscription")
    private int idSubscription;

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }

    public int getIdSubscription() {
        return idSubscription;
    }

    public void setIdSubscription(int idSubscription) {
        this.idSubscription = idSubscription;
    }
}

package anton_ruban.fitz.network.res;

import com.google.gson.annotations.SerializedName;

/**
@author antonruban on 07.05.2018.
 */

public class UserTokenResp {

    @SerializedName("access_token")
    public String userToken;

    @SerializedName("userID")
    public int userID;

}

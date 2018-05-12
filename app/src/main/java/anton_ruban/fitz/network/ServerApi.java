package anton_ruban.fitz.network;

import java.util.ArrayList;
import java.util.List;

import anton_ruban.fitz.network.req.ClubReq;
import anton_ruban.fitz.network.req.UserRegistrationReq;
import anton_ruban.fitz.network.res.ClubRes;
import anton_ruban.fitz.network.res.UserTokenResp;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
@author antonruban on 25.04.2018.
 */

public interface ServerApi {

    @POST("api/Account/Register")
    Call<UserRegistrationReq> registerUser(@Body UserRegistrationReq userRegistrationReq);

    @FormUrlEncoded
    @POST("Token")
    Call<UserTokenResp> getUserToken(@Field("username") String username,
                                     @Field("password") String password,
                                     @Field("grant_type") String grantType);

    @POST("api/Clubs")
    Call<ClubReq> createClub(@Body ClubReq req);

    @GET("api/Clubs")
    Call<ArrayList<ClubRes>> getClubList();
}

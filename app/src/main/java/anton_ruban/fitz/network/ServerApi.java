package anton_ruban.fitz.network;

import java.util.ArrayList;

import anton_ruban.fitz.network.req.ClubReq;
import anton_ruban.fitz.network.req.SubscriberRequest;
import anton_ruban.fitz.network.req.UserRegistrationReq;
import anton_ruban.fitz.network.res.ClubRes;
import anton_ruban.fitz.network.res.UserResponse;
import anton_ruban.fitz.network.res.UserTokenResp;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

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

    @GET("api/Users/{id}")
    Call<UserResponse> getUserByID(@Header("Authorization") String token,@Path("id") int userID);

    @POST("api/Subscribers")
    Call<SubscriberRequest> subscribClub(@Header("Authorization") String token,@Body SubscriberRequest subscriberRequest);

    @PUT("api/Users/{id}")
    Call<UserResponse> updateUser(@Header("Authorization") String token, @Path("id") int userID, @Body UserResponse response);

}

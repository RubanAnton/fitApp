package anton_ruban.fitz.network;

import java.util.ArrayList;
import java.util.List;

import anton_ruban.fitz.network.req.AddTrainingReq;
import anton_ruban.fitz.network.req.ClubReq;
import anton_ruban.fitz.network.req.CreateSingleTrainingReq;
import anton_ruban.fitz.network.req.SubscriberRequest;
import anton_ruban.fitz.network.req.UserRegistrationReq;
import anton_ruban.fitz.network.req.UserSubscrCoachReq;
import anton_ruban.fitz.network.res.ClubRes;
import anton_ruban.fitz.network.res.GetExerciseResp;
import anton_ruban.fitz.network.res.SubscriptionRequestMain;
import anton_ruban.fitz.network.res.UserResponse;
import anton_ruban.fitz.network.res.UserTokenResp;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
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
    Call<Void> registerUser(@Body UserRegistrationReq userRegistrationReq);

    @FormUrlEncoded
    @POST("Token")
    Call<UserTokenResp> getUserToken(@Field("username") String username,
                                     @Field("password") String password,
                                     @Field("grant_type") String grantType);

    @POST("api/Clubs")
    Call<ClubReq> createClub(@Body ClubReq req);

    @GET("api/Clubs/MySubscriptions/{idUser}")
    Call<ArrayList<SubscriptionRequestMain>> getClubList(@Header("Authorization") String token, @Path("idUser") int userID);

    @GET("api/Users/{id}")
    Call<UserResponse> getUserByID(@Header("Authorization") String token,@Path("id") int userID);

    @POST("api/Subscribers")
    Call<SubscriberRequest> subscribClub(@Header("Authorization") String token,@Body SubscriberRequest subscriberRequest);

    @PUT("api/Subscribers/{id}")
    Call<SubscriberRequest> updateStatusSubscriber(@Header("Authorization") String token,@Path("id") int id,@Body SubscriberRequest subscriberRequest);

    @GET("api/Subscribers")
    Call<ArrayList<SubscriberRequest>> getSubscriberList(@Header("Authorization") String token);

    @PUT("api/Users/{id}")
    Call<UserResponse> updateUser(@Header("Authorization") String token, @Path("id") int userID, @Body UserResponse response);

    @PUT("api/Clubs/{id}")
    Call<ClubRes> updateClub(@Header("Authorization") String token,@Path("id") int clubID,@Body ClubReq res);

    @GET("api/Clubs")
    Call<List<ClubReq>> getClubAllList(@Header("Authorization") String token);

    @PUT("api/Exercises/{id}")
    Call<Void> updateExercise(@Header("Authorization") String token,@Path("id") int exerciseId,@Body GetExerciseResp res);

    @PUT("api/Trainings/{id}")
    Call<Void> updateTraining(@Header("Authorization") String token,@Path("id") int trainingId,@Body CreateSingleTrainingReq trainig);

    @DELETE("api/Subscribers/{id}")
    Call<SubscriberRequest> unsibscribeClub(@Header("Authorization") String token,@Path("id") int subscriberID);

    @GET("api/Users/")
    Call<ArrayList<UserResponse>> getListUser(@Header("Authorization") String token);

    @GET("api/Clubs/ListUsersSubscribed/{idClub}")
    Call<List<UserSubscrCoachReq>> getListSubscriber(@Header("Authorization") String token, @Path("idClub") int id);

    @GET("api/Clubs/ListUsersInProgress/{idClub}")
    Call<List<UserResponse>> getListRequest(@Header("Authorization") String token, @Path("idClub") int id);

    @GET("api/Exercises")
    Call<List<GetExerciseResp>> getAllExercise(@Header("Authorization") String token);

    @POST("api/Trainings/AddTraining")
    Call<Void> createTraining(@Body AddTrainingReq req);

    @GET("api/Trainings/UsersTraining/{idUser}")
    Call<List<AddTrainingReq>> getListUserTraining(@Path("idUser") int id);
}

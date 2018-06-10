package anton_ruban.fitz.club.presenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.orhanobut.logger.Logger;

import java.io.ByteArrayOutputStream;

import anton_ruban.fitz.club.view.ICreateClubView;
import anton_ruban.fitz.main.view.MainActivity;
import anton_ruban.fitz.network.ServerApi;
import anton_ruban.fitz.network.ServerUtils;
import anton_ruban.fitz.network.req.ClubReq;
import anton_ruban.fitz.network.res.UserResponse;
import anton_ruban.fitz.profile.view.ProfileActivity;
import anton_ruban.fitz.utils.PreferenceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CreateClubPresenter implements ICreateClubPresenter {

    private ICreateClubView view;
    private ServerApi serverApi;
    private PreferenceManager preferenceManager;
    private Context context;
    private UserResponse userResponse;

    public CreateClubPresenter(ICreateClubView view,ServerApi serverApi){
        this.view = view;
        this.serverApi = serverApi;
    }

    @Override
    public void createClub(final ClubReq req, final Integer selectedCoach, Bitmap bitmap, StorageReference reference) {
        if(preferenceManager == null){
            preferenceManager = new PreferenceManager(view.getContextView());
        }

        if (bitmap!=null) {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            userResponse = new UserResponse();
            view.showProgress();
            serverApi = ServerUtils.serverApi();

            UploadTask uploadTask = reference.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    view.hideProgress();
                    Toast.makeText(view.getContextView(), "Problem with uploading image.", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    req.setImagePath(taskSnapshot.getDownloadUrl().toString());
                    serverApi.createClub(req).enqueue(new Callback<ClubReq>() {
                        @Override
                        public void onResponse(Call<ClubReq> call, final Response<ClubReq> response) {
                            if (response.isSuccessful()) {
                                final ClubReq club = response.body();
                                serverApi.getUserByID(preferenceManager.getUserToken(), selectedCoach).enqueue(new Callback<UserResponse>() {
                                    @Override
                                    public void onResponse(Call<UserResponse> call, Response<UserResponse> responseID) {
                                        if (responseID.isSuccessful()) {
                                            userResponse = responseID.body();
                                            userResponse.setIsCoach(club.id);
                                            userResponse.setIdClub(club.id);
                                            serverApi.updateUser(preferenceManager.getUserToken(), responseID.body().getUserID(), userResponse).enqueue(new Callback<UserResponse>() {
                                                @Override
                                                public void onResponse(Call<UserResponse> call, Response<UserResponse> response1) {
                                                    if (response1.isSuccessful() || response1.code() >= 200 && response1.code() < 300) {
                                                        updateOwner(club.id);
                                                    } else {
                                                        view.hideProgress();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<UserResponse> call, Throwable t) {
                                                    view.hideProgress();
                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<UserResponse> call, Throwable t) {
                                        view.hideProgress();
                                    }
                                });

                            } else {
                                view.hideProgress();
                                Logger.d(response.errorBody().toString());
                            }
                        }

                        @Override
                        public void onFailure(Call<ClubReq> call, Throwable t) {
                            view.hideProgress();
                        }
                    });
                }
            });
        } else {
            serverApi.createClub(req).enqueue(new Callback<ClubReq>() {
                @Override
                public void onResponse(Call<ClubReq> call, final Response<ClubReq> response) {
                    if (response.isSuccessful()) {
                        final ClubReq club = response.body();
                        serverApi.getUserByID(preferenceManager.getUserToken(), selectedCoach).enqueue(new Callback<UserResponse>() {
                            @Override
                            public void onResponse(Call<UserResponse> call, Response<UserResponse> responseID) {
                                if (responseID.isSuccessful()) {
                                    userResponse = responseID.body();
                                    userResponse.setIsCoach(club.id);
                                    userResponse.setIdClub(club.id);
                                    serverApi.updateUser(preferenceManager.getUserToken(), responseID.body().getUserID(), userResponse).enqueue(new Callback<UserResponse>() {
                                        @Override
                                        public void onResponse(Call<UserResponse> call, Response<UserResponse> response1) {
                                            if (response1.isSuccessful() || response1.code() >= 200 && response1.code() < 300) {
                                                updateOwner(club.id);
                                            } else {
                                                view.hideProgress();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<UserResponse> call, Throwable t) {
                                            view.hideProgress();
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onFailure(Call<UserResponse> call, Throwable t) {
                                view.hideProgress();
                            }
                        });

                    } else {
                        view.hideProgress();
                        Logger.d(response.errorBody().toString());
                    }
                }

                @Override
                public void onFailure(Call<ClubReq> call, Throwable t) {
                    view.hideProgress();
                }
            });
        }

    }

    void updateOwner(final Integer clubId){
        serverApi.getUserByID(preferenceManager.getUserToken(), preferenceManager.getUserId()).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> responseID) {
                if(responseID.isSuccessful()){
                    userResponse = responseID.body();
                    userResponse.setIsOwner(clubId);
                    userResponse.setIdClub(clubId);
                    serverApi.updateUser(preferenceManager.getUserToken(), responseID.body().getUserID(),userResponse).enqueue(new Callback<UserResponse>() {
                        @Override
                        public void onResponse(Call<UserResponse> call, Response<UserResponse> response1) {
                            if(response1.isSuccessful() || response1.code() >= 200 && response1.code() < 300){
                                view.hideProgress();
                                preferenceManager.setUserIsOwner(clubId);
                                view.intentToMain();
                                view.finishActivity();
                            }else {
                                view.hideProgress();
                            }
                        }
                        @Override
                        public void onFailure(Call<UserResponse> call, Throwable t) {
                            view.hideProgress();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                view.hideProgress();
            }
        });
    }
}

package anton_ruban.fitz.club.presenter;

import android.util.Log;

import anton_ruban.fitz.club.view.ICreateClubView;
import anton_ruban.fitz.network.ServerApi;
import anton_ruban.fitz.network.ServerUtils;
import anton_ruban.fitz.network.req.ClubReq;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by antonruban on 08.05.2018.
 */

public class CreateClubPresenter implements ICreateClubPresenter {

    private ICreateClubView view;
    private ServerApi serverApi;

    public CreateClubPresenter(ICreateClubView view,ServerApi serverApi){
        this.view = view;
        this.serverApi = serverApi;
    }

    @Override
    public void createClub(ClubReq req) {
        serverApi = ServerUtils.serverApi();
        serverApi.createClub(req).enqueue(new Callback<ClubReq>() {
            @Override
            public void onResponse(Call<ClubReq> call, Response<ClubReq> response) {
                if(response.isSuccessful()){
                    Log.d("response", String.valueOf(response.body()));
                    view.intentToMain();
                    view.finishActivity();
                }else {
                    Log.d("response", String.valueOf(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<ClubReq> call, Throwable t) {

            }
        });
    }
}

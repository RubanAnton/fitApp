package anton_ruban.fitz.login.presenter;

import android.util.Log;

import anton_ruban.fitz.login.view.ILoginView;
import anton_ruban.fitz.network.ServerApi;
import anton_ruban.fitz.network.ServerUtils;
import anton_ruban.fitz.network.res.UserTokenResp;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by antonruban on 07.05.2018.
 */

public class LoginPresenter implements ILoginPresenter {

    ILoginView view;
    ServerApi serverApi;

    public LoginPresenter(ServerApi serverApi, ILoginView view){
        this.serverApi = serverApi;
        this.view = view;
    }

    @Override
    public void login(String username,String password,String grant_type) {
        serverApi = ServerUtils.serverApi();
        serverApi.getUserToken(username,password,grant_type).enqueue(new Callback<UserTokenResp>() {
            @Override
            public void onResponse(Call<UserTokenResp> call, Response<UserTokenResp> response) {
                if(response.isSuccessful()){
                    Log.d("response", String.valueOf(response.body()));
                    view.intentMain();
                }else {
                    Log.d("respose", String.valueOf(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<UserTokenResp> call, Throwable t) {

            }
        });
    }
}

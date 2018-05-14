package anton_ruban.fitz.registration.presenter;

import anton_ruban.fitz.network.ServerApi;
import anton_ruban.fitz.network.ServerUtils;
import anton_ruban.fitz.network.req.UserRegistrationReq;
import anton_ruban.fitz.registration.view.IRegistrationView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 @author antonruban on 07.05.2018.
 */

public class RegistrationPresenter implements IRegistrationPresenter {

    private ServerApi serverApi;
    private IRegistrationView view;

    public RegistrationPresenter(ServerApi serverApi, IRegistrationView view){
        this.serverApi = serverApi;
        this.view = view;
    }

    @Override
    public void registrationUser(UserRegistrationReq registrationReq) {
        serverApi = ServerUtils.serverApi();
        view.showProgress();
        serverApi.registerUser(registrationReq).enqueue(new Callback<UserRegistrationReq>() {
            @Override
            public void onResponse(Call<UserRegistrationReq> call, Response<UserRegistrationReq> response) {
                if(response.code() > 200 && response.code() < 300 || response.isSuccessful()){
                    view.hideProgress();
                    view.intentLogin();
                    view.finishActivity();
                }else {
                    view.hideProgress();
                    view.error();
                }
            }

            @Override
            public void onFailure(Call<UserRegistrationReq> call, Throwable t) {
                view.hideProgress();
            }
        });
    }
}

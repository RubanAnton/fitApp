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
        String type = "application/json";
        view.showProgress();
        serverApi.registerUser(registrationReq).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code() >= 200 && response.code() < 300 || response.isSuccessful() || response.code() == 200){
                    view.hideProgress();
                    view.intentLogin();
                    view.finishActivity();
                }else {
                    view.hideProgress();
                    view.error();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                view.hideProgress();
            }
        });
    }
}

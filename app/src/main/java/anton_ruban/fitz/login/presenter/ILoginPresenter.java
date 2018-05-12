package anton_ruban.fitz.login.presenter;

import anton_ruban.fitz.network.res.UserTokenResp;

/**
 * Created by antonruban on 07.05.2018.
 */

public interface ILoginPresenter {

    void login(String username,String password,String grant_type);
}

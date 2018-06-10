package anton_ruban.fitz.registration.view;

import android.content.Context;

/**
 * Created by antonruban on 07.05.2018.
 */

public interface IRegistrationView {

    void showProgress();
    void hideProgress();
    void error();
    void intentLogin();
    void finishActivity();}

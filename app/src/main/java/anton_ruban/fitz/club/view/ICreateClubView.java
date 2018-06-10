package anton_ruban.fitz.club.view;

import android.content.Context;

/**
 * Created by antonruban on 08.05.2018.
 */

public interface ICreateClubView {

    void showProgress();
    void hideProgress();
    void finishActivity();
    void intentToMain();
    Context getContextView();
    String setCoachClub();
}

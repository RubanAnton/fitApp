package anton_ruban.fitz.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by antonruban on 12.05.2018.
 */

public class PreferenceManager {

    private static final String PREF_NAME = "PreferenceManager.anton_ruban.fitz";
    private static final String USER_TOKEN = "PreferenceManager.USER_TOKEN";
    private static final String USER_ID = "PreferenceManager.USER_ID";
    private static final String USER_NAME = "PreferenceManager.USER_NAME";
    private static final String REMEMBER_ME = "PreferenceManager.REMEMBER_ME";
    int PRIVATE_MODE = 0;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context mContext;

    public PreferenceManager(Context context) {
        this.mContext = context;
        pref = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setUserToken(String token){
        editor.putString(USER_TOKEN, token);
        editor.commit();
    }

    public String getUserToken() {
        return pref.getString(USER_TOKEN,"");
    }

    public void setUserId(int userId){
        editor.putInt(USER_ID,userId);
        editor.commit();
    }

    public int getUserId() {
        return pref.getInt(USER_ID,0);
    }

    public void setUserName(String userName){
        editor.putString(USER_NAME,userName);
        editor.commit();
    }

    public String getUserName(){
        return  pref.getString(USER_NAME,"");
    }

    public void setRememberMe(boolean rememberMe){
        editor.putBoolean(REMEMBER_ME,rememberMe);
        editor.commit();
    }

    public Boolean getRememberMe(){
        return pref.getBoolean(REMEMBER_ME,false);
    }

    public void deleteSharedPref(){
        mContext.getSharedPreferences(PREF_NAME,PRIVATE_MODE).edit().clear().apply();
    }
}

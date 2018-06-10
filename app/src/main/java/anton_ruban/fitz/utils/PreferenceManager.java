package anton_ruban.fitz.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by antonruban on 12.05.2018.
 */

public class PreferenceManager {

    private static final String PREF_NAME = "PreferenceManager.anton_ruban.fitz";
    private static final String USER_TOKEN = "PreferenceManager.USER_TOKEN";
    private static final String USER_ID = "PreferenceManager.USER_ID";
    private static final String USER_NAME = "PreferenceManager.USER_NAME";
    private static final String REMEMBER_ME = "PreferenceManager.REMEMBER_ME";
    private static final String COACH_ID = "PreferenceManager.COACH_ID";
    private static final String ADDRESS = "PreferenceManager.ADDRESS";
    private static final String LAT = "PreferenceManager.LAT";
    private static final String LNG = "PreferenceManager.LNG";
    private static final String ISOWNER = "PreferenceManager.OWNER";
    private static final String COACH_CLUB = "PreferenceManager.COACH_CLUB";
    private static final String TYPE_EXE = "PreferenceManager.TYPE_EXE";
    private static final String USER_SINGLE_TRAINING = "PreferenceManager.USER_SINGLE_TRAINING";
    private static final String EXER_ID = "PreferenceManager.EXER_ID";
    private static final String SELECTED_WARM = "PreferenceManager.SELECTED_EXER";
    private static final String SELECTED_EMOM = "PreferenceManager.SELECTED_EMOM";
    private static final String SELECTED_AR = "PreferenceManager.SELECTED_AR";
    private static final String SELECTED_ARMIN = "PreferenceManager.SELECTED_ARMIN";
    private static final String IMAGE_USER = "PreferenceManager.IMAGE_USER";

    private static final String SIMPLE_TR = "PreferenceManager.SIMPLE_TR";
    private static final String MULTI_TR = "PreferenceManager.MULTI_TR";

    int PRIVATE_MODE = 0;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context mContext;

    public PreferenceManager(Context context) {
        this.mContext = context;
        pref = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void setImageUser(Bitmap image){
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        editor.putString(IMAGE_USER, imageEncoded);
        editor.commit();
    }

    public void setImageUserNull(){
        editor.putString(IMAGE_USER, "");
        editor.commit();
    }

    public Bitmap getImageUser() {

            String imageBase = pref.getString(IMAGE_USER,"");
            if (!imageBase.equals("")) {
                byte[] decodedByte = Base64.decode(imageBase, 0);
                return BitmapFactory
                        .decodeByteArray(decodedByte, 0, decodedByte.length);
            } else {
                return null;
            }
    }


    public void setUserToken(String token){
        editor.putString(USER_TOKEN, token);
        editor.commit();
    }

    public void setUserIsOwner(int owner){
        editor.putInt(ISOWNER, owner);
        editor.commit();
    }

    public int getUserIsOwner() {
        return pref.getInt(ISOWNER,0);
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

    public String getCoachId(){
        return pref.getString(COACH_ID,"");
    }

    public void setCoachId(String coachId){
        editor.putString(COACH_ID,coachId);
        editor.commit();
    }

    public String getAddress (){
        return pref.getString(ADDRESS,"");
    }

    public void setAddress(String address){
        editor.putString(ADDRESS,address);
        editor.commit();
    }

    public  Float getLat(){
        return pref.getFloat(LAT,0f);
    }
    public void setLat(float lat){
        editor.putFloat(LAT,lat);
        editor.commit();
    }

    public  Float getLng(){
        return pref.getFloat(LNG,0f);
    }
    public void setLng(float lng){
        editor.putFloat(LAT,lng);
        editor.commit();
    }

    public int getCoachClub(){
        return pref.getInt(COACH_CLUB,-1);
    }
    public  void setCoachClub(int coachClub){
        editor.putInt(COACH_CLUB,coachClub);
        editor.commit();
    }

    public String getTypeExe(){
        return pref.getString(TYPE_EXE,"");
    }
    public void setTypeExe(String typeExe){
        editor.putString(TYPE_EXE,typeExe);
        editor.commit();
    }

    public int getUserSingleTraining(){
        return  pref.getInt(USER_SINGLE_TRAINING,-1);
    }

    public void setUserSingleTraining(int userId){
        editor.putInt(USER_SINGLE_TRAINING,userId);
        editor.commit();
    }

    public int getExerID(){
        return pref.getInt(EXER_ID,-1);
    }

    public void setExerId(int exerId){
        editor.putInt(EXER_ID,exerId);
        editor.commit();
    }

    public boolean isSelectedWrap(){
        return  pref.getBoolean(SELECTED_WARM, false);
    }

    public void setSelectedWarm(boolean warm){
        editor.putBoolean(SELECTED_WARM,warm);
        editor.commit();
    }

    public boolean isSelectedEmom(){
        return  pref.getBoolean(SELECTED_EMOM, false);
    }

    public void setSelectedEmom(boolean emom){
        editor.putBoolean(SELECTED_EMOM,emom);
        editor.commit();
    }

    public boolean isSelectedAR(){
        return  pref.getBoolean(SELECTED_AR, false);
    }

    public void setSelectedAr(boolean ar){
        editor.putBoolean(SELECTED_AR,ar);
        editor.commit();
    }

    public boolean isSelectedARMin(){
        return  pref.getBoolean(SELECTED_ARMIN, false);
    }

    public void setSelectedArmin(boolean armin){
        editor.putBoolean(SELECTED_ARMIN,armin);
        editor.commit();
    }
}

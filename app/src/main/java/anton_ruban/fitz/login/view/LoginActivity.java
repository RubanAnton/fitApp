package anton_ruban.fitz.login.view;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.URLUtil;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import anton_ruban.fitz.coach.view.CoachActivity;
import anton_ruban.fitz.main.view.MainActivity;
import anton_ruban.fitz.R;
import anton_ruban.fitz.network.ServerApi;
import anton_ruban.fitz.network.ServerUtils;
import anton_ruban.fitz.network.res.UserResponse;
import anton_ruban.fitz.network.res.UserTokenResp;
import anton_ruban.fitz.registration.view.RegistrationActivity;
import anton_ruban.fitz.utils.PreferenceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public ProgressDialog mProgressDialog;
    private EditText mEmailField;
    private EditText mPasswordField;
    private ServerApi serverApi;
    private ImageView image;
    private PreferenceManager preferenceManager;
    private CheckBox checkRemember;
    private int[] myImageList = new int[]{R.drawable.back2, R.drawable.back3, R.drawable.back1};
    private int countImage = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        try {
            getSupportActionBar().hide();
        } catch (Exception error){ }

        mEmailField = findViewById(R.id.field_email);
        mPasswordField = findViewById(R.id.field_password);
        serverApi = ServerUtils.serverApi();
        image = findViewById(R.id.carousel);

        if(preferenceManager == null){
            preferenceManager = new PreferenceManager(this);
        }

        findViewById(R.id.email_sign_in_button).setOnClickListener(this);
        findViewById(R.id.email_create_account_button).setOnClickListener(this);
        checkRemember = findViewById(R.id.checkRemember);

        final Animation fadeIn = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.fade_in);
        final Animation fadeOut = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.fade_out);

        image.startAnimation(fadeIn);
        try {
            fadeIn.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    image.setImageDrawable(getDrawable(myImageList[countImage]));
                    if (countImage != 2) {
                        countImage++;
                    } else {
                        countImage = 0;
                    }
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                    image.startAnimation(fadeOut);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });

            fadeOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {


                    image.startAnimation(fadeIn);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        } catch (Exception eror) {}
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.email_create_account_button) {
            startActivity(new Intent(this, RegistrationActivity.class));
        } else if (i == R.id.email_sign_in_button) {
            String email = mEmailField.getText().toString();
            String password = mPasswordField.getText().toString();
            String grant_type = "password";
            login(email,password,grant_type);
        }
    }


    public void login(String username,String password,String grant_type) {
        showProgressDialog();
        serverApi = ServerUtils.serverApi();
        serverApi.getUserToken(username,password,grant_type).enqueue(new Callback<UserTokenResp>() {
            @Override
            public void onResponse(Call<UserTokenResp> call, Response<UserTokenResp> response) {
                if(response.isSuccessful()){
                    if (TextUtils.isEmpty(mEmailField.getText().toString()) || TextUtils.isEmpty(mPasswordField.getText().toString())) {
                        mEmailField.setError("");
                        mPasswordField.setError("");
                    } else {

                        preferenceManager.setUserToken(response.body().userToken);
                        preferenceManager.setUserId(response.body().userID);
                        if(checkRemember.isChecked()){
                            preferenceManager.setRememberMe(true);
                        }
                        serverApi.getUserByID(preferenceManager.getUserToken(),preferenceManager.getUserId()).enqueue(new Callback<UserResponse>() {
                            @Override
                            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                                preferenceManager.setUserIsOwner(response.body().getIsOwner());
                                preferenceManager.setUserName(response.body().getEmail());
                                if(response.body().getImagePath() != null && URLUtil.isValidUrl(response.body().getImagePath())) {
                                     Glide.with(LoginActivity.this)
                                             .asBitmap()
                                             .load(response.body().getImagePath())
                                             .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                             preferenceManager.setImageUser(resource);
                                         }
                                     });
                                } else {
                                    preferenceManager.setImageUserNull();
                                }
                                if(response.isSuccessful()){
                                    if (response.body().getIsCoach() == 0){
                                        hideProgressDialog();
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                         finish();
                                    }else{
                                        preferenceManager.setCoachClub(response.body().getIsCoach());
                                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(LoginActivity.this);
                                        builder.setTitle(getResources().getString(R.string.continue_coach))
                                                .setCancelable(false)
                                                .setPositiveButton(getResources().getString(R.string.coachCon),
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                startActivity(new Intent(LoginActivity.this, CoachActivity.class));
                                                                hideProgressDialog();
                                                                finish();
                                                                dialog.cancel();
                                                            }
                                                        });
                                        builder.setNegativeButton(getResources().getString(R.string.userCon), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                                hideProgressDialog();
                                                finish();
                                                dialog.dismiss();
                                            }
                                        });
                                        android.support.v7.app.AlertDialog alert = builder.create();
                                        alert.show();
                                    }
                                }
                            }
                            @Override
                            public void onFailure(Call<UserResponse> call, Throwable t) {
                                hideProgressDialog();
                            }
                        });
                    }
                }else {
                    hideProgressDialog();
                    Toast.makeText(LoginActivity.this, "Date is not validate", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<UserTokenResp> call, Throwable t) {
                hideProgressDialog();
            }
        });
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }
        return valid;
    }



}
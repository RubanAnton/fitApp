package anton_ruban.fitz.registration.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import anton_ruban.fitz.login.view.LoginActivity;
import anton_ruban.fitz.R;
import anton_ruban.fitz.network.ServerApi;
import anton_ruban.fitz.network.ServerUtils;
import anton_ruban.fitz.network.req.UserRegistrationReq;
import anton_ruban.fitz.registration.presenter.RegistrationPresenter;
import anton_ruban.fitz.utils.PreferenceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity implements IRegistrationView{

    private ServerApi serverApi;
    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private UserRegistrationReq req;
    private RegistrationPresenter presenter;
    public ProgressDialog mProgressDialog;
    private ImageView image;
    private PreferenceManager preferenceManager;
    private int[] myImageList = new int[]{R.drawable.back2, R.drawable.back3, R.drawable.back1};
    private int countImage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        try {
            getSupportActionBar().hide();
        } catch (Exception error){ }

        serverApi = ServerUtils.serverApi();

        if(preferenceManager == null){
            preferenceManager = new PreferenceManager(this);
        }

        if(presenter == null){
            presenter = new RegistrationPresenter(serverApi,this);
        }
        email = findViewById(R.id.field_email_up);
        password = findViewById(R.id.field_password_up);
        confirmPassword = findViewById(R.id.field_confirm_password);
        image = findViewById(R.id.carouselRegistration);

        final Animation fadeIn = AnimationUtils.loadAnimation(RegistrationActivity.this, R.anim.fade_in);
        final Animation fadeOut = AnimationUtils.loadAnimation(RegistrationActivity.this, R.anim.fade_out);

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

    public void signUp(View view){
        String type = "application/json";
        req = new UserRegistrationReq();
        req.setEmail(email.getText().toString());
        req.setPassword(password.getText().toString());
        req.setConfirmPassword(confirmPassword.getText().toString());
        showProgress();
        serverApi.registerUser(req).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code() >= 200 && response.code() < 300 || response.isSuccessful()){
                    hideProgress();
                    intentLogin();
                    finishActivity();
                }else {
                    hideProgress();
                    error();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                hideProgress();
            }
        });
//        presenter.registrationUser(req);
    }

    @Override
    public void showProgress() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    @Override
    public void hideProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void error() {
        Toast.makeText(RegistrationActivity.this, "Error", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void intentLogin() {
        startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
    }

    @Override
    public void finishActivity() {
        finish();
    }
}

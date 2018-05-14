package anton_ruban.fitz.login.view;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

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


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public ProgressDialog mProgressDialog;
    private EditText mEmailField;
    private EditText mPasswordField;
    private ServerApi serverApi;
    private PreferenceManager preferenceManager;
    private CheckBox checkRemember;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailField = findViewById(R.id.field_email);
        mPasswordField = findViewById(R.id.field_password);
        serverApi = ServerUtils.serverApi();

        if(preferenceManager == null){
            preferenceManager = new PreferenceManager(this);
        }

        findViewById(R.id.email_sign_in_button).setOnClickListener(this);
        findViewById(R.id.email_create_account_button).setOnClickListener(this);
        checkRemember = findViewById(R.id.checkRemember);
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
        customDialog();
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
                                if(response.isSuccessful()){
                                    if (response.body().getIsCoach() == 0){
                                        preferenceManager.setUserName(response.body().getEmail());
                                        hideProgressDialog();
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));

                                    }else{
                                        startActivity(new Intent(LoginActivity.this, CoachActivity.class));
                                        hideProgressDialog();
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

    public void customDialog(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.load,null);
        AVLoadingIndicatorView avi = mView.findViewById(R.id.avi);
        mBuilder.setView(mView);
        avi.show();
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
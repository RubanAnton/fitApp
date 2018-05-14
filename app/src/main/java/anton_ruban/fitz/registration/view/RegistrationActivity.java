package anton_ruban.fitz.registration.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import anton_ruban.fitz.login.view.LoginActivity;
import anton_ruban.fitz.R;
import anton_ruban.fitz.network.ServerApi;
import anton_ruban.fitz.network.ServerUtils;
import anton_ruban.fitz.network.req.UserRegistrationReq;
import anton_ruban.fitz.registration.presenter.RegistrationPresenter;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        serverApi = ServerUtils.serverApi();
        if(presenter == null){
            presenter = new RegistrationPresenter(serverApi,this);
        }
        email = findViewById(R.id.field_email_up);
        password = findViewById(R.id.field_password_up);
        confirmPassword = findViewById(R.id.field_confirm_password);
    }

    public void signUp(View view){
        String email_ = email.getText().toString();
        String password_ = password.getText().toString();
        String confirmPassword_ = confirmPassword.getText().toString();
        req = new UserRegistrationReq(email_,password_,confirmPassword_);
        presenter.registrationUser(req);
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
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public void finishActivity() {
        finish();
    }
}

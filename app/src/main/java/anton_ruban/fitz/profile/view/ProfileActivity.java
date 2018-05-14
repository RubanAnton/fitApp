package anton_ruban.fitz.profile.view;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import java.util.Calendar;
import java.util.Date;

import anton_ruban.fitz.R;
import anton_ruban.fitz.main.view.MainActivity;
import anton_ruban.fitz.network.ServerApi;
import anton_ruban.fitz.network.ServerUtils;
import anton_ruban.fitz.network.res.UserResponse;
import anton_ruban.fitz.utils.PreferenceManager;
import de.hdodenhof.circleimageview.CircleImageView;
import io.ghyeok.stickyswitch.widget.StickySwitch;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private CircleImageView imageView;
    private ServerApi serverApi;
    private PreferenceManager preferenceManager;
    private EditText email;
    private EditText firstname;
    private EditText lastname;
    private EditText phoneUser;
    private TextView textDate;
    private StickySwitch gender;
    private UserResponse response;
    private int mYear, mMonth, mDay;
    private Date dateDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        imageView = findViewById(R.id.profile_image);
        email = findViewById(R.id.email);
        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        phoneUser = findViewById(R.id.phoneUser);
        textDate = findViewById(R.id.textDateBith);
        gender = findViewById(R.id.switchGender);

        serverApi = ServerUtils.serverApi();
        if(preferenceManager == null){
            preferenceManager = new PreferenceManager(this);
        }
        serverApi.getUserByID(preferenceManager.getUserToken(),preferenceManager.getUserId()).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if(response.body() != null || response.isSuccessful() || response.code() >200 && response.code() < 300){
                    email.setText(response.body().getEmail());
                    email.setEnabled(false);
                    firstname.setText(response.body().getFirstName());
                    lastname.setText(response.body().getLastName());
                    phoneUser.setText(response.body().getPhone());
                    textDate.setText((CharSequence) response.body().getBithDate());
                    if(response.body().getGender() == 0){
                        gender.setDirection(StickySwitch.Direction.LEFT);
                    }
                    if(response.body().getGender() == 1){
                        gender.setDirection(StickySwitch.Direction.RIGHT);
                    }
                }else {
                    Logger.d(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Logger.d("Error");
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA},
                                MY_CAMERA_PERMISSION_CODE);
                    } else {
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    }
                }
            }
        });
    }

    public void onClickDate(View view){
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        textDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public void onClickSave(View view){
        String email_ = email.getText().toString();
        String firstName = firstname.getText().toString();
        String lastName = lastname.getText().toString();
        String phone_ = phoneUser.getText().toString();
        int gender_type = 0;
        if(gender.getDirection() == StickySwitch.Direction.LEFT){
            gender_type = 0;
        }
        if (gender.getDirection() == StickySwitch.Direction.RIGHT){
            gender_type = 1;
        }
        response = new UserResponse(preferenceManager.getUserId(),email_,gender_type,firstName,lastName,phone_);
        serverApi.updateUser(preferenceManager.getUserToken(),preferenceManager.getUserId(),response).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if(response.code() > 200 && response.code() < 300 || response.isSuccessful()){
                    Toast.makeText(ProfileActivity.this, "Update user profile OKEY", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                    finish();
                }else {
                    Toast.makeText(ProfileActivity.this, "Incorrect date", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {

            }
        });
    }

    public void onCLickCancel(View view){
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new
                        Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
        }
    }
}

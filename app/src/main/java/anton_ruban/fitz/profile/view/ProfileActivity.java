package anton_ruban.fitz.profile.view;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.orhanobut.logger.Logger;

import org.joda.time.DateTime;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
    private Button dateBTN;
    private StickySwitch gender;
    private UserResponse response;
    private int mYear, mMonth, mDay;
    private Date dateDB;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private StorageReference userImagesRef;
    public ProgressDialog mProgressDialog;
    private Date dateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        try {
            getSupportActionBar().hide();
        } catch (Exception error){ }
        imageView = findViewById(R.id.profile_image);
        email = findViewById(R.id.email);
        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        phoneUser = findViewById(R.id.phoneUser);
        textDate = findViewById(R.id.textDateBith);
        gender = findViewById(R.id.switchGender);
        dateBTN = findViewById(R.id.dateBTN);


        serverApi = ServerUtils.serverApi();
        if(preferenceManager == null){
            preferenceManager = new PreferenceManager(this);
        }
        showProgress();
        serverApi.getUserByID(preferenceManager.getUserToken(),preferenceManager.getUserId()).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if(response.body() != null || response.isSuccessful() || response.code() >200 && response.code() < 300){
                    email.setText(response.body().getEmail());
                    email.setEnabled(false);
                    firstname.setText(response.body().getFirstName());
                    lastname.setText(response.body().getLastName());
                    dateBTN.setText(response.body().getBithDate());
                    phoneUser.setText(response.body().getPhone());
                    if(response.body().getGender() == 0){
                        gender.setDirection(StickySwitch.Direction.LEFT);
                    }
                    if(response.body().getGender() == 1){
                        gender.setDirection(StickySwitch.Direction.RIGHT);
                    }
                    hideProgress();
                }else {
                    hideProgress();
                    Logger.d(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                hideProgress();
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

    @Override
    protected void onResume() {
        super.onResume();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        userImagesRef = storageRef.child("users/" + preferenceManager.getUserId() + ".jpg");
        if (preferenceManager.getImageUser() != null) {
            try {
                imageView.setImageBitmap(preferenceManager.getImageUser());
            } catch (Exception error) {}
        }
    }

    public void onClickDate(View view){
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        final DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        dateBTN.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        dateTime = new Date();
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();



    }

    public void onClickSave(View view){
        dateTime = new Date();
        response = new UserResponse();
        response.setUserID(preferenceManager.getUserId());
        response.setEmail(email.getText().toString());
        response.setFirstName(firstname.getText().toString());
        response.setLastName(lastname.getText().toString());
        response.setPhone(phoneUser.getText().toString());
        response.setBithDate("2018-06-10T20:20:05.4533823+02:00");
        int gender_type = 0;
        if(gender.getDirection() == StickySwitch.Direction.LEFT){
            response.setGender(0);
        }
        if (gender.getDirection() == StickySwitch.Direction.RIGHT){
            response.setGender(1);
        }
        showProgress();


        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        final Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = userImagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                hideProgress();
                Toast.makeText(ProfileActivity.this, "Problem with uploading image.", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                preferenceManager.setImageUser(bitmap);
                response.setImagePath(taskSnapshot.getDownloadUrl().toString());
                serverApi.updateUser(preferenceManager.getUserToken(),preferenceManager.getUserId(),response).enqueue(new Callback<UserResponse>() {
                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                        if(response.code() > 200 && response.code() < 300 || response.isSuccessful()){
                            Toast.makeText(ProfileActivity.this, "Update user profile OKEY", Toast.LENGTH_SHORT).show();
                            hideProgress();
                            startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                            finish();
                        }else {
                            hideProgress();
                            Toast.makeText(ProfileActivity.this, "Incorrect date", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {
                        hideProgress();
                    }
                });
            }
        });






    }

    public void showProgress(){
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgress(){
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
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

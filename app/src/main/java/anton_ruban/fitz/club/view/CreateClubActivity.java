package anton_ruban.fitz.club.view;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import anton_ruban.fitz.main.view.MainActivity;
import anton_ruban.fitz.R;
import anton_ruban.fitz.club.presenter.CreateClubPresenter;
import anton_ruban.fitz.network.ServerApi;
import anton_ruban.fitz.network.req.ClubReq;
import anton_ruban.fitz.utils.PreferenceManager;
import de.hdodenhof.circleimageview.CircleImageView;

public class CreateClubActivity extends AppCompatActivity implements ICreateClubView {

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private CircleImageView imageView;
    private EditText clubName;
    private EditText descriptionClub;
    private EditText emailClub;
    private EditText phoneClub;
    public ProgressDialog mProgressDialog;

    private CreateClubPresenter presenter;
    private ServerApi serverApi;
    private PreferenceManager preferenceManager;
    private ClubReq req;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_club);

        if(presenter == null){
            presenter = new CreateClubPresenter(this,serverApi);
        }
        if(preferenceManager == null){
            preferenceManager = new PreferenceManager(this);
        }

        imageView = findViewById(R.id.club_image);
        clubName = findViewById(R.id.clubName);
        descriptionClub = findViewById(R.id.descriptionClub);
        emailClub = findViewById(R.id.emailClub);
        phoneClub = findViewById(R.id.phoneClub);

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

    public void onClickSaveClub(View view){
        String clubName_ = clubName.getText().toString();
        String desrc = descriptionClub.getText().toString();
        String email = emailClub.getText().toString();
        String phone = phoneClub.getText().toString();
        int idOwner  = preferenceManager.getUserId();
        req = new ClubReq(clubName_,desrc,email,phone,idOwner);
        presenter.createClub(req);
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

    @Override
    public void showProgress(){
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    @Override
    public void hideProgress(){
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    public void intentToMain() {
        startActivity(new Intent(this, MainActivity.class));
    }
}

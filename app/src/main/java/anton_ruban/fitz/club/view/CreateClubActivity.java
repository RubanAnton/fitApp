package anton_ruban.fitz.club.view;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import anton_ruban.fitz.main.view.MainActivity;
import anton_ruban.fitz.R;
import anton_ruban.fitz.club.presenter.CreateClubPresenter;
import anton_ruban.fitz.network.ServerApi;
import anton_ruban.fitz.network.ServerUtils;
import anton_ruban.fitz.network.req.ClubReq;
import anton_ruban.fitz.network.res.UserResponse;
import anton_ruban.fitz.utils.PreferenceManager;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateClubActivity extends AppCompatActivity implements ICreateClubView {

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private CircleImageView imageView;
    private EditText clubName;
    private EditText descriptionClub;
    private EditText emailClub;
    private EditText phoneClub;
    private Double longitude = 0.0;
    private Double latitude = 0.0;
    private String addr = "";
    private Button selectAddress;
    public ProgressDialog mProgressDialog;
    private SelectCoachAdapter adapter;
    private RecyclerView recyclerView;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private StorageReference clubImagesRef;
    private List<UserResponse> userResponseList;
    private  Bitmap bitmap;

    private CreateClubPresenter presenter;
    private ServerApi serverApi;
    private PreferenceManager preferenceManager;
    private ClubReq req;
    private Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_club);
        activity = this;
        serverApi = ServerUtils.serverApi();
        if(presenter == null){
            presenter = new CreateClubPresenter(this,serverApi);
        }
        if(preferenceManager == null){
            preferenceManager = new PreferenceManager(this);
        }
        recyclerView = findViewById(R.id.recyclerSelectCoach);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        userResponseList = new ArrayList<>();
        adapter = new SelectCoachAdapter(userResponseList,this,this);
        recyclerView.setAdapter(adapter);
        showProgress();
        serverApi.getListUser(preferenceManager.getUserToken()).enqueue(new Callback<ArrayList<UserResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<UserResponse>> call, Response<ArrayList<UserResponse>> response) {
                if(response.isSuccessful() || response.code() >=200 && response.code() < 300){
                    userResponseList = response.body();
                    adapter.setUserList(userResponseList);
                    adapter = new SelectCoachAdapter(userResponseList,getApplicationContext(),activity);
                    recyclerView.setAdapter(adapter);
                    hideProgress();
                }else {
                    hideProgress();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<UserResponse>> call, Throwable t) {
                hideProgress();
            }
        });

        imageView = findViewById(R.id.club_image);
        clubName = findViewById(R.id.clubName);
        descriptionClub = findViewById(R.id.descriptionClub);
        emailClub = findViewById(R.id.emailClub);
        phoneClub = findViewById(R.id.phoneClub);

        selectAddress = findViewById(R.id.selectAddres);

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

        selectAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(CreateClubActivity.this);
                    builder.setTitle(getString(R.string.oneedMaps))
                            .setCancelable(false)
                            .setPositiveButton("YES",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Intent intent5 = new Intent(CreateClubActivity.this,MapsActivity.class);
                                            intent5.putExtra("mapStateFind",false);
                                            startActivityForResult(intent5, 1007);
                                            dialog.cancel();
                                        }
                                    });
                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(CreateClubActivity.this, "OKEY", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                    android.support.v7.app.AlertDialog alert = builder.create();
                    alert.show();

            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        clubImagesRef = storageRef.child("clubs/" + preferenceManager.getUserId() + ".jpg");
    }

    public void onClickSaveClub(View view){
        Integer selectedcoach = adapter.selectedCoach;

        if (selectedcoach != null) {
            if (latitude != 0.0 && longitude != 0.0) {
                req = new ClubReq();
                req.setNameClub(clubName.getText().toString());
                req.setDescription(descriptionClub.getText().toString());
                req.setEmailClub(emailClub.getText().toString());
                req.setPhone(phoneClub.getText().toString());
                req.setIdOwner(preferenceManager.getUserId());
                req.setIdsCoachs(selectedcoach.toString());
                preferenceManager.setUserIsOwner(1);
                if (imageView.getDrawable() != getDrawable(R.mipmap.ic_launcher)) {
                    imageView.setDrawingCacheEnabled(true);
                    imageView.buildDrawingCache();
                    bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                } else {
                    bitmap = null;
                }
                req.setLat(latitude.floatValue());
                req.setLng(longitude.floatValue());
                req.setAddres(addr);
                presenter.createClub(req, selectedcoach, bitmap, clubImagesRef);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getString(R.string.selectAddr));
                AlertDialog dialog = builder.create();
                dialog.setCancelable(true);
                dialog.show();
            }
        } else  {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.selectTrainerError).setMessage(R.string.selectTrainerError2);
            AlertDialog dialog = builder.create();
            dialog.setCancelable(true);
            dialog.show();
        }
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

        if (requestCode == 1007 && resultCode == 1007) {
           longitude = data.getDoubleExtra("longitude", 0.1);
            latitude = data.getDoubleExtra("latitude", 0.1);
            addr = data.getStringExtra("addr");
            if (!addr.equals("")) {
                selectAddress.setText(addr);
            }
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
       // startActivity(new Intent(this, MainActivity.class));
        finishActivity();
    }

    @Override
    public Context getContextView() {
        return getApplicationContext();
    }

    @Override
    public String setCoachClub() {
        return preferenceManager.getCoachId();
    }
}

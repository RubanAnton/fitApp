package anton_ruban.fitz.club.updateClub.view;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;
import java.util.List;

import anton_ruban.fitz.R;
import anton_ruban.fitz.main.view.MainActivity;
import anton_ruban.fitz.network.ServerApi;
import anton_ruban.fitz.network.ServerUtils;
import anton_ruban.fitz.network.req.ClubReq;
import anton_ruban.fitz.network.res.ClubRes;
import anton_ruban.fitz.network.res.UserResponse;
import anton_ruban.fitz.utils.PreferenceManager;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateClubActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    private ProgressDialog mProgressDialog;
    private CircleImageView img;
    private EditText edtMail, edtDescr, edtName, edtPhone;
    private Button btnAddres;
    private TextView txtCoach;
    private List<ClubReq> resp;
    private ServerApi serverApi;
    private PreferenceManager preferenceManager;
    private int idClub;
    private Button UpdateBtn;
    private ClubReq reqClub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_club);

        preferenceManager = new PreferenceManager(this);
        showProgress();
        img = findViewById(R.id.club_img_update);
        edtName = findViewById(R.id.clubNameUpdate);
        edtDescr = findViewById(R.id.descriptionClubUpdate);
        edtMail = findViewById(R.id.emailClubUpdate);
        edtPhone = findViewById(R.id.phoneClubUpdate);
        txtCoach = findViewById(R.id.CoachEmail);
        btnAddres = findViewById(R.id.selectAddresUpdate);
        serverApi = ServerUtils.serverApi();
        UpdateBtn = findViewById(R.id.UpdateBtn);
        resp = new ArrayList<>();
        serverApi.getClubAllList(preferenceManager.getUserToken()).enqueue(new Callback<List<ClubReq>>() {
            @Override
            public void onResponse(Call<List<ClubReq>> call, final Response<List<ClubReq>> response) {
                if (response.isSuccessful() || response.code() >= 200 && response.code() < 300) {
                    resp = response.body();
                    for (int i = 0; i < resp.size(); i++){
                        if(resp.get(i).getIdOwner() == preferenceManager.getUserId()){
                            if(resp.get(i).getId() != 0){
                                idClub = resp.get(i).getId();
                            }
                            if(resp.get(i).getNameClub() != null){
                                edtName.setText(resp.get(i).getNameClub());
                            }
                            if(resp.get(i).getDescription() != null){
                                edtDescr.setText(resp.get(i).getDescription());
                            }
                            if(resp.get(i).getEmailClub() != null){
                                edtMail.setText(resp.get(i).getEmailClub());
                            }
                            if(resp.get(i).getPhone()!= null){
                                edtPhone.setText(resp.get(i).getPhone());
                            }
                            if(resp.get(i).getAddres() != null){
                                btnAddres.setText(resp.get(i).getAddres());
                            }
                            if(resp.get(i).getImagePath() != null && URLUtil.isValidUrl(resp.get(i).getImagePath())) {
                                Glide.with(UpdateClubActivity.this)
                                        .asBitmap()
                                        .load(resp.get(i).getImagePath())
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                                img.setImageBitmap(resource);
                                            }
                                        });
                            }
                            int idCoach = Integer.parseInt(resp.get(i).getIdsCoachs());
                            serverApi.getUserByID(preferenceManager.getUserToken(),idCoach).enqueue(new Callback<UserResponse>() {
                                @Override
                                public void onResponse(Call<UserResponse> call, Response<UserResponse> responseUser) {
                                    if(responseUser.isSuccessful() || responseUser.code() >=200 && responseUser.code() < 300){
                                        txtCoach.setText(responseUser.body().getEmail());
                                    }else {
                                        hideProgress();
                                    }
                                }
                                @Override
                                public void onFailure(Call<UserResponse> call, Throwable t) {
                                    hideProgress();
                                }
                            });
                            hideProgress();
                        }
                    }
                } else {
                    hideProgress();
                }
            }

            @Override
            public void onFailure(Call<List<ClubReq>> call, Throwable t) {
                hideProgress();
            }
        });

        img.setOnClickListener(new View.OnClickListener() {
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

        UpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reqClub = new ClubReq();
                reqClub.setEmailClub(edtMail.getText().toString());
                reqClub.setDescription(edtDescr.getText().toString());
                reqClub.setNameClub(edtName.getText().toString());
                reqClub.setPhone(edtPhone.getText().toString());
                serverApi.updateClub(preferenceManager.getUserToken(),idClub,reqClub).enqueue(new Callback<ClubRes>() {
                    @Override
                    public void onResponse(Call<ClubRes> call, Response<ClubRes> response) {
                        if(response.isSuccessful() || response.code() >=200 && response.code() < 300){
                            Toast.makeText(UpdateClubActivity.this, getResources().getString(R.string.updateOkClub), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(UpdateClubActivity.this, MainActivity.class));
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<ClubRes> call, Throwable t) {

                    }
                });
            }
        });
    }

    public void showProgress() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}

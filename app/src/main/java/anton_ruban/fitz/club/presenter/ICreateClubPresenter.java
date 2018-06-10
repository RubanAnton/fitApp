package anton_ruban.fitz.club.presenter;

import android.graphics.Bitmap;

import com.google.firebase.storage.StorageReference;

import anton_ruban.fitz.network.req.ClubReq;

/**
 * Created by antonruban on 08.05.2018.
 */

public interface ICreateClubPresenter {

    void createClub(ClubReq req, Integer selectedCoach,  Bitmap bitmap, StorageReference reference );
}

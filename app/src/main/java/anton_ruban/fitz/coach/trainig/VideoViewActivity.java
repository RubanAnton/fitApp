package anton_ruban.fitz.coach.trainig;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import anton_ruban.fitz.R;

public class VideoViewActivity extends AppCompatActivity {

    private MediaController mediacontroller;
    private Uri uri;
    private boolean isContinuously = false;
    public VideoView vidoView;
    private String uriString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);
        uriString = getIntent().getStringExtra("URI_STR");
        vidoView = (VideoView) findViewById(R.id.webViewCreation);
        mediacontroller = new MediaController(this);
        mediacontroller.setAnchorView(vidoView);

        uri = Uri.parse(uriString);
        vidoView.setMediaController(mediacontroller);
        vidoView.setVideoURI(uri);
        vidoView.requestFocus();
        vidoView.start();
        vidoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

            }
        });
    }

    public void cancell(View view) {
        finish();
    }
}

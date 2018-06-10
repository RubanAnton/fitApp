package anton_ruban.fitz.timers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;

import anton_ruban.fitz.R;
import butterknife.BindView;
import butterknife.OnClick;

public class TimersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timers);
        try {
            getSupportActionBar().hide();
        } catch (Exception error){ }

    }

    public void onCLickEmom(View view){
        startActivity(new Intent(this,EmomActivity.class));
    }

    public void onClickStopWatch(View view){
        startActivity(new Intent(this,StopWatchActivity.class));
    }

    public void onClickAmrap(View view){
        startActivity(new Intent(this,AmrapActivity.class));
    }

}

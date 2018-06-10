package anton_ruban.fitz.timers;

import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.orhanobut.hawk.Hawk;

import anton_ruban.fitz.R;
import anton_ruban.fitz.utils.Utils;
import butterknife.BindView;
import butterknife.OnClick;

public class StopWatchActivity extends AppCompatActivity {

    private static final int REFERENCE_START_DELAY = 101;
    private static final int REFERENCE_SOUND_EVERY = 102;
    private static final String PREFERENCE_START_DELAY_STATUS = "PREFERENCE_START_DELAY_STATUS";
    private static final String PREFERENCE_SOUND_EVERY_STATUS = "PREFERENCE_SOUND_EVERY_STATUS";
    private static final String PREFERENCE_STOPWATCH_SOUND_EVERY_H = "PREFERENCE_STOPWATCH_SOUND_EVERY_H";
    private static final String PREFERENCE_STOPWATCH_SOUND_EVERY_M = "PREFERENCE_STOPWATCH_SOUND_EVERY_M";
    private static final String PREFERENCE_STOPWATCH_SOUND_EVERY_S = "PREFERENCE_STOPWATCH_SOUND_EVERY_S";
    private static final String PREFERENCE_STOPWATCH_START_DELAY_H = "PREFERENCE_STOPWATCH_START_DELAY_H";
    private static final String PREFERENCE_STOPWATCH_START_DELAY_M = "PREFERENCE_STOPWATCH_START_DELAY_M";
    private static final String PREFERENCE_STOPWATCH_START_DELAY_S = "PREFERENCE_STOPWATCH_START_DELAY_S";
    private static final String PREFERENCE_SHOWCASE = "stopwatch_showcase_rand_1234";

    Toolbar toolbar;
    ImageView reset;
    ImageView playpause;
    TextView time;
    long millisecondTime, StartTime, TimeBuff, updateTime = 0L;
    Handler handler;
    int seconds, minutes, milliSeconds;
    Thread runnableStopwatch;
    int currentState = 0; // 0 = stopped. 1 = running
    TextView startDelay;
    TextView soundEvery;
    int countdown;
    Thread countdownRunnable;
    CheckBox soundEveryStatus;
    CheckBox startDelayStatus;
    private int startDelayH;
    private int startDelayM;
    private int startDelayS;
    private int soundEveryH;
    private int soundEveryM;
    private int soundEveryS;
    AlertDialog.Builder alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_watch);
        try {
            getSupportActionBar().hide();
        } catch (Exception error){ }

        toolbar = findViewById(R.id.toolbar);
        reset = findViewById(R.id.reset);
        playpause = findViewById(R.id.playpause);
        time = findViewById(R.id.time);
        startDelay = findViewById(R.id.start_delay);
        soundEvery = findViewById(R.id.sound_every);
        soundEveryStatus = findViewById(R.id.sound_every_status);
        startDelayStatus = findViewById(R.id.start_delay_status);
        Hawk.init(this).build();
        handler = new Handler();
        runnableStopwatch = new Thread() {

            public void run() {
                millisecondTime = SystemClock.uptimeMillis() - StartTime;
                updateTime = TimeBuff + millisecondTime;
                seconds = (int) (updateTime / 1000);
                minutes = seconds / 60;
                seconds = seconds % 60;
                milliSeconds = (int) (updateTime % 1000);
                time.setText("" + minutes + ":"
                        + String.format("%02d", seconds) + ":"
                        + String.format("%03d", milliSeconds));
                handler.postDelayed(this, 0);

                long secondTimeSoundEvery = soundEveryH * 60 * 60 + soundEveryM * 60 + soundEveryS;
                if (secondTimeSoundEvery != 0) {
                    if (minutes * 60 + seconds >= secondTimeSoundEvery) {
                        if (minutes * 60 + seconds % secondTimeSoundEvery == 0) {

                        }
                    }
                }
            }

        };
        countdownRunnable = new Thread() {
            @Override
            public void run() {
                time.setText(String.valueOf(countdown));
                countdown -= 1;
                if (countdown >= 0) {
                    handler.postDelayed(this, 1000);
                    return;
                }
                handler.removeCallbacks(this);
                startStopwatch();
            }
        };

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        showCase();
    }

    private void showDialog(){
        if (!this.isFinishing()) {
            alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Предупреждение");
            alertDialog.setMessage("Вы действительно хотите выйти из таймера? В этом случае работа таймера остановится.");
            alertDialog.setPositiveButton("Выйти", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int arg1) {
                    runnableStopwatch.interrupt();
                    countdownRunnable.interrupt();
                    onBackPressed();
                }
            });
            alertDialog.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int arg1) {
                    dialog.cancel();
                }
            });
            alertDialog.show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (currentState == 1) { // started
            showDialog();
        } else {
            onBackPressed();
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onSupportNavigateUp();
        }
        return super.onKeyDown(keyCode, event);
    }


    void showCase() {
        if (!Hawk.contains(PREFERENCE_SHOWCASE)) {
            new TapTargetSequence(this)
                    .targets(
                            TapTarget.forView(findViewById(R.id.start_delay), getString(R.string.click_to_set_start_delay)),
                            TapTarget.forView(findViewById(R.id.sound_every), getString(R.string.click_to_set_sound_every))
                    ).start();
            Hawk.put(PREFERENCE_SHOWCASE, true);
        }
    }

    @OnClick({R.id.reset, R.id.playpause, R.id.start_delay, R.id.sound_every, R.id.sound_every_status, R.id.start_delay_status})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.reset:
                //region reset
                millisecondTime = 0L;
                StartTime = 0L;
                TimeBuff = 0L;
                updateTime = 0L;
                seconds = 0;
                minutes = 0;
                milliSeconds = 0;

                time.setText("0:00:000");
                //endregion
                break;
            case R.id.playpause:
                //region playpause
                if (currentState == 0) {
                    currentState = 3; // disable playpause button
                    playpause.setImageDrawable(getResources().getDrawable(R.drawable.button_pause));
                    countdown = startDelayH * 60 * 60 + startDelayM * 60 + startDelayS;
                    startStopwatch();
                    reset.setEnabled(false);
                } else if (currentState == 1) {
                    currentState = 0;
                    playpause.setImageDrawable(getResources().getDrawable(R.drawable.button_play));
                    TimeBuff += millisecondTime;
                    handler.removeCallbacks(runnableStopwatch);
                    reset.setEnabled(true);
                }
                //endregion
                break;
            case R.id.start_delay:
                new Utils.TimeSpinnerPicker(this).setOnTimeChangeListener(new Utils.TimeSpinnerPicker.OnTimeChangeListener() {
                    @Override
                    public void onChange(int h, int m, int s) {
                        startDelay.setText(String.format(getString(R.string.start_delay__), h, m, s));
                        Hawk.put(PREFERENCE_STOPWATCH_START_DELAY_H, h);
                        Hawk.put(PREFERENCE_STOPWATCH_START_DELAY_M, m);
                        Hawk.put(PREFERENCE_STOPWATCH_START_DELAY_S, s);
                        startDelayStatus.setChecked(true);
                        Hawk.put(PREFERENCE_START_DELAY_STATUS, startDelayStatus.isChecked());
                        loadPrefs();
                    }
                }).show();
                break;
            case R.id.sound_every:
                new Utils.TimeSpinnerPicker(this).setOnTimeChangeListener(new Utils.TimeSpinnerPicker.OnTimeChangeListener() {
                    @Override
                    public void onChange(int h, int m, int s) {
                        soundEvery.setText(String.format(getString(R.string.sound_every__), h, m, s));
                        Hawk.put(PREFERENCE_STOPWATCH_SOUND_EVERY_H, h);
                        Hawk.put(PREFERENCE_STOPWATCH_SOUND_EVERY_M, m);
                        Hawk.put(PREFERENCE_STOPWATCH_SOUND_EVERY_S, s);
                        soundEveryStatus.setChecked(true);
                        Hawk.put(PREFERENCE_SOUND_EVERY_STATUS, soundEveryStatus.isChecked());
                        loadPrefs();
                    }
                }).show();
                break;
            case R.id.sound_every_status:
                Hawk.put(PREFERENCE_SOUND_EVERY_STATUS, soundEveryStatus.isChecked());
                break;
            case R.id.start_delay_status:
                Hawk.put(PREFERENCE_START_DELAY_STATUS, startDelayStatus.isChecked());
                break;
        }
    }

    private void startStopwatch() {
        if (startDelayStatus.isChecked()) {
            if (countdown > 0) {
                time.setText(String.valueOf(countdown));
                handler.postDelayed(countdownRunnable, 0);
                return;
            }
        }
        // run stopwatch
        currentState = 1;
        StartTime = SystemClock.uptimeMillis();
        handler.postDelayed(runnableStopwatch, 0);
    }

    void loadPrefs() {
        try {
            startDelayH = Hawk.get(PREFERENCE_STOPWATCH_START_DELAY_H);
            startDelayM = Hawk.get(PREFERENCE_STOPWATCH_START_DELAY_M);
            startDelayS = Hawk.get(PREFERENCE_STOPWATCH_START_DELAY_S);

            startDelay.setText(String.format(getString(R.string.start_delay__), startDelayH, startDelayM, startDelayS));
        } catch (Exception ignored) {
        }
        try {
            soundEveryH = Hawk.get(PREFERENCE_STOPWATCH_SOUND_EVERY_H);
            soundEveryM = Hawk.get(PREFERENCE_STOPWATCH_SOUND_EVERY_M);
            soundEveryS = Hawk.get(PREFERENCE_STOPWATCH_SOUND_EVERY_S);

            soundEvery.setText(String.format(getString(R.string.sound_every__), soundEveryH, soundEveryM, soundEveryS));
        } catch (Exception ignored) {
        }

        try {
            startDelayStatus.setChecked((Boolean) Hawk.get(PREFERENCE_START_DELAY_STATUS));
        } catch (Exception ignored) {
        }

        try {
            soundEveryStatus.setChecked((Boolean) Hawk.get(PREFERENCE_SOUND_EVERY_STATUS));
        } catch (Exception ignored) {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPrefs();
    }
}

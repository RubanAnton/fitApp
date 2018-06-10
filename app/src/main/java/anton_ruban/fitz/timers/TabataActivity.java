package anton_ruban.fitz.timers;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.codetroopers.betterpickers.numberpicker.NumberPickerBuilder;
import com.codetroopers.betterpickers.numberpicker.NumberPickerDialogFragment;
import com.orhanobut.logger.Logger;

import java.math.BigDecimal;
import java.math.BigInteger;

import anton_ruban.fitz.R;
import anton_ruban.fitz.utils.Utils;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TabataActivity extends AppCompatActivity {

    ImageView reset;
    ImageView playpause;
    TextView time;
    TextView status;
    TextView prepareValue;
    LinearLayout prepareLayout;
    TextView restValue;
    LinearLayout restLayout;
    TextView workValue;
    LinearLayout workLayout;
    TextView roundsValue;
    LinearLayout roundsLayout;
    Toolbar toolbar;
    ProgressBar progressTrainingBar;
    ProgressBar progressRoundsBar;
    ProgressBar progressRestBar;
    TextView textViewStatus;
    TextView roundsLeft;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabata2);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        try {
            getSupportActionBar().hide();
        } catch (Exception error){ }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        playpause = findViewById(R.id.playpause);
        time = findViewById(R.id.time);
        status = findViewById(R.id.status);
        prepareValue = findViewById(R.id.prepare_value);
        prepareLayout = findViewById(R.id.prepare_layout);
        restValue = findViewById(R.id.rest_value);
        restLayout = findViewById(R.id.rest_layout);
        workValue = findViewById(R.id.work_value);
        workLayout = findViewById(R.id.work_layout);
        roundsValue = findViewById(R.id.rounds_value);
        roundsLayout = findViewById(R.id.rounds_layout);
        toolbar = findViewById(R.id.toolbar);
        progressTrainingBar = findViewById(R.id.progressTrainingBar);
        progressRoundsBar = findViewById(R.id.progressRoundsBar);
        progressRestBar = findViewById(R.id.progressRestBar);
        textViewStatus = findViewById(R.id.textViewStatus);
        roundsLeft = findViewById(R.id.rounds_left);

        loadData();
        resetState();
        showParameters();
    }

    //region declaration
    Handler handler = new Handler();
    int prepare_all_time_h, prepare_all_time_m, prepare_all_time_s, prepare_current_time;
    int work_all_time_h, work_all_time_m, work_all_time_s, work_current_time;
    int rest_all_time_h, rest_all_time_m, rest_all_time_s, rest_current_time;

    int all_current_time, all_time_h, all_time_m, all_time_s;

    int rounds_all = 0, rounds_current;
    boolean isPause = true;
    boolean isReseted = true;
    Runnable pauseRunnableCallback;
    Runnable pauseRunnableCallback2;

    Runnable pauseTick2 = new Runnable() {
        @Override
        public void run() {
            if (!isPause) {
                handler.postDelayed(pauseRunnableCallback2, 1000);
            } else {
                handler.postDelayed(this, 100);
            }
        }
    };

    Runnable pauseTick = new Runnable() {
        @Override
        public void run() {
            if (!isPause) {
                handler.postDelayed(pauseRunnableCallback, 1000);
            } else {
                handler.postDelayed(this, 100);
            }
        }
    };
    private MediaPlayer mediaPlayer;


    Runnable restTick = new Runnable() {
        @Override
        public void run() {
            if (isPause) {
                pauseRunnableCallback = this;
                handler.post(pauseTick);
                return;
            }
            textViewStatus.setText(getResources().getString(R.string.rest_round));
            roundsLeft.setText(getResources().getString(R.string.rest_round));
            time.setText(String.valueOf(rest_current_time));
            if(rest_current_time < 1){
                if(rounds_current <= rounds_all){
                    work_current_time = work_all_time();
                    rest_current_time = rest_all_time();
                    handler.postDelayed(workTick, 1000);
                    progressRestBar.setVisibility(View.INVISIBLE);
                } else{
                    Logger.d("Rounds finished");
                    progressRoundsBar.setProgress(0);
                    textViewStatus.setText(getResources().getString(R.string.traing_prepaer));
                    roundsLeft.setText(getResources().getString(R.string.traing_prepaer));
                    progressRestBar.setVisibility(View.VISIBLE);
                    progressRoundsBar.setVisibility(View.VISIBLE);
                    resetState();
                    return;
                }
            }
            else {
                progressRestBar.setVisibility(View.VISIBLE);
                handler.postDelayed(this, 1000);
                rest_current_time -= 1;
                progressRestBar.setProgress(rest_all_time() - rest_current_time);
            }
        }
    };

    Runnable workTick = new Runnable() {
        @Override
        public void run() {
            if (isPause) {
                pauseRunnableCallback = this;
                handler.post(pauseTick);
                return;
            } else {
                status.setText(R.string.progress_training);
                textViewStatus.setText(getResources().getString(R.string.round_work));
                time.setText(String.valueOf(work_current_time));
                roundsLeft.setText(String.format("%s %d из %d", "Раунд", rounds_current, rounds_all));
                if (work_current_time < 1) {// work finished
                    rest_current_time = rest_all_time();
                    progressRoundsBar.setProgress(0);
                    handler.postDelayed(restTick, 1000);
                    rounds_current++;
                    progressRoundsBar.setVisibility(View.INVISIBLE);
                } else {
                    progressRoundsBar.setVisibility(View.VISIBLE);
                    handler.postDelayed(this, 1000);
                    work_current_time -= 1;
                    progressRoundsBar.setProgress(work_all_time() - work_current_time);

                }
            }
        }
    };

    Runnable prepareTick = new Runnable() {
        @Override
        public void run() {
            if (isPause) {
                pauseRunnableCallback = this;
                handler.post(pauseTick);
                return;
            }
            textViewStatus.setText(getResources().getString(R.string.traing_prepaer));
            time.setText(String.valueOf(prepare_current_time));
            if (prepare_current_time < 1) { // prepare finished
                work_current_time = work_all_time();
                all_current_time = all_time();
                rounds_current = 1;
                handler.postDelayed(workTick, 1000);
                handler.postDelayed(allTick, 1000);
                return;
            }
            prepare_current_time -= 1;
            progressRestBar.setVisibility(View.INVISIBLE);
            progressRoundsBar.setVisibility(View.INVISIBLE);
            progressTrainingBar.setVisibility(View.INVISIBLE);
            handler.postDelayed(this, 1000);

        }
    };

    Runnable allTick = new Runnable() {
        @Override
        public void run() {

            if (isPause) {
                pauseRunnableCallback2 = this;
                handler.post(pauseTick2);
                return;
            }
            if (all_current_time < 1) {
                all_current_time = all_time();
                progressTrainingBar.setProgress(0);
                return;
            } else {
                progressTrainingBar.setVisibility(View.VISIBLE);
                handler.postDelayed(this, 1000);
                progressTrainingBar.setProgress(all_time() - all_current_time);
                all_current_time -= 1;

            }
        }
    };


    private int prepare_all_time() {
        return prepare_all_time_h * 60 * 60 + prepare_all_time_m * 60 + prepare_all_time_s;
    }

    private int work_all_time() {
        return work_all_time_h * 60 * 60 + work_all_time_m * 60 + work_all_time_s;
    }

    private int rest_all_time() {
        return rest_all_time_h * 60 * 60 + rest_all_time_m * 60 + rest_all_time_s;
    }

    private int all_time(){
        all_time_h = rest_all_time_h * 60 * 60 + work_all_time_h * 60 * 60 + prepare_all_time_h * 60 * 60;
        all_time_m = rest_all_time_m * 60 + work_all_time_m * 60 + prepare_all_time_m * 60;
        all_time_s = work_all_time_s + rest_all_time_s + prepare_all_time_s;
        return (all_time_s + all_time_m + all_time_h)*rounds_all;
    }

    //endregion


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onSupportNavigateUp();
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onSupportNavigateUp() {
        if (!isPause) {
            showDialog();
        } else {
            onBackPressed();
        }
        return true;
    }

    void loadData() {

        //endregion
        prepare_all_time_h = 0;
        prepare_all_time_m = 0;
        prepare_all_time_s = 10; //10
        work_all_time_h = 0;
        work_all_time_m = 0;
        work_all_time_s = 20;//20
        rest_all_time_h = 0;
        rest_all_time_m = 0;
        rest_all_time_s = 10;//10
        all_time_h = 0;
        all_time_m = 0;
        all_time_s = 320;//320
        rounds_all = 8; //8
    }


    @SuppressLint("DefaultLocale")
    void showParameters() {
        roundsValue.setText(String.valueOf(rounds_all));
        prepareValue.setText(String.format("%02d:%02d:%02d", prepare_all_time_h, prepare_all_time_m, prepare_all_time_s));
        workValue.setText(String.format("%02d:%02d:%02d", work_all_time_h, work_all_time_m, work_all_time_s));
        restValue.setText(String.format("%02d:%02d:%02d", rest_all_time_h, rest_all_time_m, rest_all_time_s));
    }

    public void clickRest(View view){
        resetState();
    }

    public void playPause(View view){
        if (isReseted) {
            prepare_current_time = prepare_all_time();
            handler.post(prepareTick);
            progressTrainingBar.setMax(all_current_time);
            progressRoundsBar.setMax(work_current_time);
            progressRestBar.setMax(rest_current_time);
            isReseted = false;
        }

        isPause = !isPause;

        if (isPause) {
            playpause.setImageDrawable(getResources().getDrawable(R.drawable.button_play));

        } else {
            playpause.setImageDrawable(getResources().getDrawable(R.drawable.button_pause));
        }
    }

    public void clickPrepareLayout(View view){
        new Utils.TimeSpinnerPicker(this).setOnTimeChangeListener(new Utils.TimeSpinnerPicker.OnTimeChangeListener() {
            @Override
            public void onChange(int h, int m, int s) {
                prepare_all_time_h = h;
                prepare_all_time_m = m;
                prepare_all_time_s = s;
                resetState();
                showParameters();
            }
        }).show();
    }

    public void clickRestLayout(View view){
        new Utils.TimeSpinnerPicker(this).setOnTimeChangeListener(new Utils.TimeSpinnerPicker.OnTimeChangeListener() {
            @Override
            public void onChange(int h, int m, int s) {
                rest_all_time_h = h;
                rest_all_time_m = m;
                rest_all_time_s = s;
                resetState();
                showParameters();
            }
        }).show();
    }


    public void clickWorkLayout(View view){
        new Utils.TimeSpinnerPicker(this).setOnTimeChangeListener(new Utils.TimeSpinnerPicker.OnTimeChangeListener() {
            @Override
            public void onChange(int h, int m, int s) {
                work_all_time_h = h;
                work_all_time_m = m;
                work_all_time_s = s;
                resetState();
                showParameters();
            }
        }).show();
    }

    public void clickRoundsLayout(View view){
        NumberPickerBuilder numberPicker = new NumberPickerBuilder()
                .setMinNumber(BigDecimal.valueOf(1))
                .addNumberPickerDialogHandler(new NumberPickerDialogFragment.NumberPickerDialogHandlerV2() {
                    @Override
                    public void onDialogNumberSet(int reference, BigInteger number, double decimal, boolean isNegative, BigDecimal fullNumber) {
                        rounds_all = number.intValue();
                        resetState();
                        showParameters();
                    }
                })
                .setFragmentManager(getSupportFragmentManager())
                .setDecimalVisibility(View.INVISIBLE)
                .setPlusMinusVisibility(View.INVISIBLE)
                .setStyleResId(R.style.BetterPickersDialogFragment);
        numberPicker.show();
    }

    private void resetState() {
//        status.setText(R.string.progress_training);

        time.setText(String.valueOf(prepare_all_time()));
        progressTrainingBar.setProgress(0);
        progressRoundsBar.setProgress(0);
        progressRestBar.setProgress(0);
        progressRestBar.setVisibility(View.INVISIBLE);
        progressRoundsBar.setVisibility(View.INVISIBLE);
        progressTrainingBar.setVisibility(View.INVISIBLE);

        prepare_current_time = prepare_all_time();
        work_current_time = work_all_time();
        rest_current_time = rest_all_time();
        all_current_time = all_time();
        rounds_current = 0;
        isReseted = true;
        isPause = true;

        playpause.setImageDrawable(getResources().getDrawable(R.drawable.button_play));
        roundsLeft.setText("");

        handler.removeCallbacksAndMessages(null);
    }

    private void showDialog(){
        if (!this.isFinishing()) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Предупреждение");
            alertDialog.setMessage("Вы действительно хотите выйти из таймера? В этом случае работа таймера остановится.");
            alertDialog.setPositiveButton("Выйти", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int arg1) {
                    resetState();
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
}


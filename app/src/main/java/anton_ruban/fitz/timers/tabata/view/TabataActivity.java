package anton_ruban.fitz.timers.tabata.view;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codetroopers.betterpickers.numberpicker.NumberPickerBuilder;
import com.codetroopers.betterpickers.numberpicker.NumberPickerDialogFragment;

import java.math.BigDecimal;
import java.math.BigInteger;

import anton_ruban.fitz.R;
import anton_ruban.fitz.utils.Utils;
import butterknife.OnClick;

public class TabataActivity extends AppCompatActivity {

    private TextView time;
    private TextView rest_key;
    private TextView rest_value;
    private TextView prepare_key;
    private TextView prepare_value;
    private TextView work_key;
    private TextView work_value;
    private TextView round_key;
    private TextView round_value;
    private ImageView play;
    private ImageView stop;

    private boolean isPause = true;
    private boolean isReseted = true;
    private Runnable pauseRunnableCallback;
    private Handler handler = new Handler();

    int prepare_all_time_h, prepare_all_time_m, prepare_all_time_s, prepare_current_time;
    int work_all_time_h, work_all_time_m, work_all_time_s, work_current_time;
    int rest_all_time_h, rest_all_time_m, rest_all_time_s, rest_current_time;
    int rounds_all = 0, rounds_current;

    Runnable restTick = new Runnable() {
        @Override
        public void run() {
            if (isPause) {
                pauseRunnableCallback = this;
                handler.post(pauseTick);
                return;
            }
//            textViewStatus.setText(getResources().getString(R.string.rest_round));
//            roundsLeft.setText(getResources().getString(R.string.rest_round));
            time.setText(String.valueOf(rest_current_time));
            if(rest_current_time < 1){
                if(rounds_current <= rounds_all){
                    work_current_time = work_all_time();
                    rest_current_time = rest_all_time();
                    handler.postDelayed(workTick, 1000);
                } else{
                    Log.d("Rounds finished", "");
//                    textViewStatus.setText(getResources().getString(R.string.traing_prepaer));
//                    roundsLeft.setText(getResources().getString(R.string.traing_prepaer));
                    resetState();
                    return;
                }
            }
            else {
                handler.postDelayed(this, 1000);
                rest_current_time -= 1;
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
                time.setText(String.valueOf(work_current_time));
                if (work_current_time < 1) {
                    rest_current_time = rest_all_time();
                    handler.postDelayed(restTick, 1000);
                    rounds_current++;
                } else {
                    handler.postDelayed(this, 1000);
                    work_current_time -= 1;
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
//            textViewStatus.setText(getResources().getString(R.string.traing_prepaer));
            time.setText(String.valueOf(prepare_current_time));
            if (prepare_current_time < 1) { // prepare finished
                work_current_time = work_all_time();
                rounds_current = 1;
                handler.postDelayed(workTick, 1000);
                return;
            }
            prepare_current_time -= 1;
            handler.postDelayed(this, 1000);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabata);

        time = findViewById(R.id.textTime);
        prepare_key = findViewById(R.id.prepare_key);
        prepare_value = findViewById(R.id.prepare_value);
        work_key = findViewById(R.id.work_key);
        work_value = findViewById(R.id.work_value);
        rest_key = findViewById(R.id.rest_key);
        rest_value = findViewById(R.id.rest_value);
        round_key = findViewById(R.id.round_key);
        round_value = findViewById(R.id.round_value);
        play = findViewById(R.id.playpause);
        stop = findViewById(R.id.reset);
    }

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

    private int prepare_all_time() {
        return prepare_all_time_h * 60 * 60 + prepare_all_time_m * 60 + prepare_all_time_s;
    }

    private int work_all_time() {
        return work_all_time_h * 60 * 60 + work_all_time_m * 60 + work_all_time_s;
    }

    private int rest_all_time() {
        return rest_all_time_h * 60 * 60 + rest_all_time_m * 60 + rest_all_time_s;
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
    }

    @OnClick({R.id.reset, R.id.playpause, R.id.prepairLinear, R.id.restLinear, R.id.workLinear, R.id.roundLinear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.reset:
                resetState();
                break;
            case R.id.playpause:
                if (isReseted) {
                    prepare_current_time = prepare_all_time();
                    handler.post(prepareTick);
                    isReseted = false;
                }

                isPause = !isPause;

                if (isPause) {
                    play.setImageDrawable(getResources().getDrawable(R.drawable.button_play));

                } else {
                    play.setImageDrawable(getResources().getDrawable(R.drawable.button_pause));
                }
                break;
            case R.id.prepairLinear:
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
                break;
            case R.id.restLinear:
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
                break;
            case R.id.workLinear:
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
                break;
            case R.id.roundLinear:
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
                break;
        }
    }

    @SuppressLint("DefaultLocale")
    void showParameters() {
        round_value.setText(String.valueOf(rounds_all));
        prepare_value.setText(String.format("%02d:%02d:%02d", prepare_all_time_h, prepare_all_time_m, prepare_all_time_s));
        work_value.setText(String.format("%02d:%02d:%02d", work_all_time_h, work_all_time_m, work_all_time_s));
        rest_value.setText(String.format("%02d:%02d:%02d", rest_all_time_h, rest_all_time_m, rest_all_time_s));
    }

    private void resetState() {
//        status.setText(R.string.progress_training);
        time.setText(String.valueOf(prepare_all_time()));

        prepare_current_time = prepare_all_time();
        work_current_time = work_all_time();
        rest_current_time = rest_all_time();
        isReseted = true;
        isPause = true;
        handler.removeCallbacksAndMessages(null);
    }

}

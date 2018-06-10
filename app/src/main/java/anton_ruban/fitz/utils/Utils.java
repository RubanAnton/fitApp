package anton_ruban.fitz.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import anton_ruban.fitz.R;

/**
 * Created by antonruban on 24.04.2018.
 */

public class Utils {

    public static class TimeSpinnerPicker {
        com.shawnlin.numberpicker.NumberPicker hp;
        com.shawnlin.numberpicker.NumberPicker mp;
        com.shawnlin.numberpicker.NumberPicker sp;
        private Dialog d;
        private OnTimeChangeListener onTimeChangeListener;
        private int h = 0;
        private int m = 0;
        private int s = 0;

        public TimeSpinnerPicker(Context context) {
            d = new Dialog(context);
            d.setCancelable(true);
            d.setContentView(R.layout.dialog_duration_picker);
            hp = d.findViewById(R.id.hoursPicker);
            mp = d.findViewById(R.id.minutesPicker);
            sp = d.findViewById(R.id.secondsPicker);
            Button btnOk = d.findViewById(R.id.submitBtn);
            Button btnCancel = d.findViewById(R.id.cancelBtn);
            hp.setMaxValue(10);
            hp.setMinValue(0);
            hp.setValue(getH());
            hp.setWrapSelectorWheel(true);
            mp.setMaxValue(59);
            mp.setMinValue(0);
            mp.setValue(getM());
            mp.setWrapSelectorWheel(true);
            sp.setMaxValue(59);
            sp.setMinValue(0);
            sp.setValue(getS());
            sp.setWrapSelectorWheel(true);
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getOnTimeChangeListener() != null) {
                        getOnTimeChangeListener().onChange(hp.getValue(), mp.getValue(), sp.getValue());
                    }
                    d.dismiss();
                }
            });
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    d.cancel();
                }
            });
        }

        public int getH() {
            return h;
        }

        public TimeSpinnerPicker setH(int h) {
            this.h = h;
            hp.setValue(getH());
            return this;
        }

        public int getM() {
            return m;
        }

        public TimeSpinnerPicker setM(int m) {
            this.m = m;
            mp.setValue(getM());
            return this;
        }

        public int getS() {
            return s;
        }

        public TimeSpinnerPicker setS(int s) {
            this.s = s;
            sp.setValue(getS());
            return this;
        }

        public OnTimeChangeListener getOnTimeChangeListener() {
            return onTimeChangeListener;
        }

        public TimeSpinnerPicker setOnTimeChangeListener(OnTimeChangeListener onTimeChangeListener) {
            this.onTimeChangeListener = onTimeChangeListener;
            return this;
        }

        public void show() {
            d.show();
        }

        public interface OnTimeChangeListener {
            void onChange(int h, int m, int s);
        }
    }
}

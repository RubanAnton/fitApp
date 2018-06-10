package anton_ruban.fitz.timers;

public class EmomActivity extends TabataActivity {
    @Override
    void loadData() {
        prepare_all_time_h = 0;
        prepare_all_time_m = 0;
        prepare_all_time_s = 3;//3
        work_all_time_h = 0;
        work_all_time_m = 1;//1
        work_all_time_s = 0;
        rest_all_time_h = 0;
        rest_all_time_m = 0;
        rest_all_time_s = 0;//0
        rounds_all = 1; //5
    }
}

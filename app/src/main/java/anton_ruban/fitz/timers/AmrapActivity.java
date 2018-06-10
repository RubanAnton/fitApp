package anton_ruban.fitz.timers;

public class AmrapActivity extends TabataActivity {
    @Override
    void loadData() {
        prepare_all_time_h = 0;
        prepare_all_time_m = 0;
        prepare_all_time_s = 10;//10
        work_all_time_h = 0;
        work_all_time_m = 15;//15
        work_all_time_s = 0;
        rest_all_time_h = 0;
        rest_all_time_m = 0;
        rest_all_time_s = 0;//0
        rounds_all = 1; //1
    }
}
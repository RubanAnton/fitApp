package anton_ruban.fitz.tools.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import anton_ruban.fitz.R;
import anton_ruban.fitz.timers.tabata.view.TabataActivity;

public class ToolsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools);
    }

    public void onClickTabata(View view){
        startActivity(new Intent(this, TabataActivity.class));
    }
}

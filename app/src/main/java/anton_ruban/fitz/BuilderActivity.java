package anton_ruban.fitz;


import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import anton_ruban.fitz.data.EmptyRecyclerView;
import anton_ruban.fitz.data.RVAdapter;
import anton_ruban.fitz.data.RecyclerTouchHelper;
import anton_ruban.fitz.data.models.Workout;


public class BuilderActivity extends BaseActivity implements View.OnClickListener, RecyclerTouchHelper.RecyclerItemTouchHelperListener {


    String[] type_data = {"Strength", "Hypertrophy", "Maintenance", "Endurance"};
    String[] lvl_data = {"Novice", "Intermediate", "Advanced", "Elite"};
    String[] day_data = {"1 Days", "2 Days", "3 Days", "4 Days", "5 Days", "6 days", "7 Days"};
    Dialog workoutDialog;
    public List<Workout> workouts;
    private ConstraintLayout constraintLayout;
    private List<Workout> workoutList;
    public RVAdapter adapter;
    EmptyRecyclerView rv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_builder);
        rv = findViewById(R.id.rv);
        View emptyView = findViewById(R.id.empty_view);
        FloatingActionButton fab = findViewById(R.id.fab);
        constraintLayout = findViewById(R.id.constraintLayout);
        workouts = new LinkedList<>();
        workoutList = new ArrayList<>();
        // mRef = FirebaseDatabase.getInstance().getReference().child("Users");

        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setTitle("Program Manager");

        RecyclerView.LayoutManager llm = new LinearLayoutManager(getApplicationContext());
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setLayoutManager(llm);
        rv.setEmptyView(emptyView);

        ItemTouchHelper.SimpleCallback touchHelperCallBack = new RecyclerTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(touchHelperCallBack).attachToRecyclerView(rv);

        workoutDialog = new Dialog(this);

    }

    @Override
    public void onClick(View v) {

    }

    public void popUp(View view) {

        Button btnClose;
        Button btnOK;
        workoutDialog.setContentView(R.layout.addworkout);
        final TextInputLayout name = workoutDialog.findViewById(R.id.input_workout_name);
        btnClose = workoutDialog.findViewById(R.id.cancel_adding);
        btnOK = workoutDialog.findViewById(R.id.done_adding);
        final MaterialBetterSpinner workoutTypeSpinner = workoutDialog.findViewById(R.id.type_spinner);
        final MaterialBetterSpinner workoutDateSpinner = workoutDialog.findViewById(R.id.day_spinner);
        final MaterialBetterSpinner workoutLevelSpinner = workoutDialog.findViewById(R.id.lvl_spinner);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, type_data);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, day_data);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, lvl_data);
        workoutTypeSpinner.setAdapter(adapter1);
        workoutDateSpinner.setAdapter(adapter2);
        workoutLevelSpinner.setAdapter(adapter3);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workoutDialog.dismiss();
            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Workout w = new Workout();
                w.setName(name.getEditText().getText().toString());
                w.setType(workoutTypeSpinner.getText().toString());
                w.setId(randomFind());
                w.setSize(Integer.parseInt(workoutDateSpinner.getText().toString().substring(0, 1)));
                w.setLevel(workoutLevelSpinner.getText().toString());
                workoutList.add(w);
                workoutDialog.dismiss();
                adapter.notifyDataSetChanged();

            }
        });
        workoutDialog.show();
    }

    public int randomFind() {
        Random r = new Random();
        int rr = r.nextInt(100);
        for (int i = 0; i < workoutList.size(); i++)
            if (workoutList.get(i).getId() == rr)
                randomFind();
        return rr;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, final int position) {
        if (viewHolder instanceof RVAdapter.WorkoutViewHolder) {

            String name = workoutList.get(viewHolder.getAdapterPosition()).getName();
            final Workout deletedItem = workoutList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();


            adapter.removeItem(viewHolder.getAdapterPosition());
            Snackbar snackbar = Snackbar
                    .make(constraintLayout, name + " removed from workouts!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    adapter.restoreItem(deletedItem, deletedIndex);
//                    mRef.child(String.valueOf(workoutList.get(deletedIndex).getId())).setValue(workoutList.get(position));
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }
}

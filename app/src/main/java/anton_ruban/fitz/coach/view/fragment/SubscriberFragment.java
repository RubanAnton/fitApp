package anton_ruban.fitz.coach.view.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import anton_ruban.fitz.R;
import anton_ruban.fitz.coach.adapter.SubscriberAdapter;
import anton_ruban.fitz.network.ServerApi;
import anton_ruban.fitz.network.ServerUtils;
import anton_ruban.fitz.network.req.UserSubscrCoachReq;
import anton_ruban.fitz.utils.PreferenceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by antonruban on 12.05.2018.
 */

public class SubscriberFragment extends Fragment {

    private RecyclerView recyclerView;
    private SubscriberAdapter adapter;
    private PreferenceManager preferenceManager;
    private ProgressDialog mProgressDialog;
    private ServerApi serverApi;
    private List<UserSubscrCoachReq> responsesList;
    private SwipeRefreshLayout swipeRefreshLayout;

    private int page;
    private String title;
    
    public static SubscriberFragment newInstance(int page, String title) {
        SubscriberFragment fragmentFirst = new SubscriberFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subscriber1, container, false);
        if(preferenceManager == null){
            preferenceManager = new PreferenceManager(getContext());
        }
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeSubscriber);
        responsesList = new ArrayList<>();
        serverApi = ServerUtils.serverApi();
        recyclerView = view.findViewById(R.id.subscriber1Recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SubscriberAdapter(responsesList,getContext());
        recyclerView.setAdapter(adapter);
        responsesList.clear();
        showProgressDialog();
        serverApi.getListSubscriber(preferenceManager.getUserToken(),preferenceManager.getCoachClub()).enqueue(new Callback<List<UserSubscrCoachReq>>() {
            @Override
            public void onResponse(Call<List<UserSubscrCoachReq>> call, Response<List<UserSubscrCoachReq>> response) {
                if(response.isSuccessful() || response.code() >= 200 && response.code() < 300){
                    if(response.body().size() == 0){
                        hideProgressDialog();
                    } else {
                        responsesList = response.body();
                        adapter.setList(responsesList);
                        recyclerView.setAdapter(adapter);
                        hideProgressDialog();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<UserSubscrCoachReq>> call, Throwable t) {

            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                responsesList.clear();
                swipeRefreshLayout.setRefreshing(true);
                serverApi.getListSubscriber(preferenceManager.getUserToken(),preferenceManager.getCoachClub()).enqueue(new Callback<List<UserSubscrCoachReq>>() {
                    @Override
                    public void onResponse(Call<List<UserSubscrCoachReq>> call, Response<List<UserSubscrCoachReq>> response) {
                        if(response.isSuccessful() || response.code() >= 200 && response.code() < 300){
                            if(response.body().size() == 0){
                                swipeRefreshLayout.setRefreshing(false);
                            } else {
                                responsesList = response.body();
                                adapter.setList(responsesList);
                                recyclerView.setAdapter(adapter);
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<UserSubscrCoachReq>> call, Throwable t) {

                    }
                });
            }
        });
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}

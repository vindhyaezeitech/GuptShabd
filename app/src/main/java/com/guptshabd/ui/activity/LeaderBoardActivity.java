package com.guptshabd.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.guptshabd.GameActivity;
import com.guptshabd.GamePresenter;
import com.guptshabd.GameView;
import com.guptshabd.R;
import com.guptshabd.model.leaderboard.LeaderboardListModel;
import com.guptshabd.ui.adapter.GetLeaderboardListAdapter;

import java.util.List;

public class LeaderBoardActivity extends AppCompatActivity implements GameView, View.OnClickListener {

    private String type;
    private GamePresenter gamePresenter;
    private GetLeaderboardListAdapter adapter;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);
        inIt();
    }

    private void inIt() {
        findViewById(R.id.iv_back_btn).setOnClickListener(this);
        recyclerView=findViewById(R.id.rv_getLeaderboard_List);
        Intent intent = getIntent();
        type = intent.getStringExtra("type");

        callGetLeaderBoardListAPI();

    }

    private void callGetLeaderBoardListAPI() {
        gamePresenter = new GamePresenter(this);
        //  compositeDisposable = interestPresenter.loadCategoryData();
        gamePresenter.fetchLeaderBoardList();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back_btn: {
                if (!TextUtils.isEmpty(type) && type.equals("1")) {
                    Intent intent = new Intent(this, ShabdamActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(this, GameActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }
        }
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void onError(String errorMsg) {

    }

    @Override
    public void onGetLeaderBoardListFetched(List<LeaderboardListModel> list) {
        adapter = new GetLeaderboardListAdapter(this, list);
        RecyclerView.LayoutManager layoutManagerLive = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManagerLive);
        recyclerView.setAdapter(adapter);

    }
}
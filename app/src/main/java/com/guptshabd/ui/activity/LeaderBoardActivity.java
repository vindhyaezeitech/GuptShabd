package com.guptshabd.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
    private RelativeLayout rl_one, rl_two, rl_three;
    private TextView tv_name_one, tv_name_two, tv_name_three;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);
        inIt();
    }

    private void inIt() {
        findViewById(R.id.iv_back_btn).setOnClickListener(this);
        recyclerView = findViewById(R.id.rv_getLeaderboard_List);
        rl_one = findViewById(R.id.rl_one);
        rl_two = findViewById(R.id.rl_two);
        rl_three = findViewById(R.id.rl_three);
        tv_name_one = findViewById(R.id.tv_name_one);
        tv_name_two = findViewById(R.id.tv_name_two);
        tv_name_three = findViewById(R.id.tv_name_three);
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
                if (type.equals("1")) {
                    Intent intent = new Intent(this, ShabdamActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else if (type.equals("2")) {
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
        if (list.size() == 1) {
            rl_one.setVisibility(View.VISIBLE);
            rl_two.setVisibility(View.GONE);
            rl_three.setVisibility(View.GONE);
            tv_name_one.setText(list.get(0).getName());
        } else if (list.size() == 2) {
            rl_one.setVisibility(View.VISIBLE);
            rl_two.setVisibility(View.VISIBLE);
            rl_three.setVisibility(View.GONE);
            tv_name_one.setText(list.get(0).getName());
            tv_name_two.setText(list.get(1).getName());

        } else if (list.size() == 3) {
            rl_one.setVisibility(View.VISIBLE);
            rl_two.setVisibility(View.VISIBLE);
            rl_three.setVisibility(View.VISIBLE);
            tv_name_one.setText(list.get(0).getName());
            tv_name_two.setText(list.get(1).getName());
            tv_name_three.setText(list.get(2).getName());
        } else if (list.size() == 4) {
            rl_one.setVisibility(View.VISIBLE);
            rl_two.setVisibility(View.VISIBLE);
            rl_three.setVisibility(View.VISIBLE);
            tv_name_one.setText(list.get(0).getName());
            tv_name_two.setText(list.get(1).getName());
            tv_name_three.setText(list.get(2).getName());
        }

        adapter = new GetLeaderboardListAdapter(this, list);
        RecyclerView.LayoutManager layoutManagerLive = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManagerLive);
        recyclerView.setAdapter(adapter);
    }
}
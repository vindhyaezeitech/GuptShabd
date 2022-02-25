package com.guptshabd;


import com.guptshabd.model.leaderboard.LeaderboardListModel;
import com.guptshabd.model.getwordresp.Datum;
import com.guptshabd.model.statistics.Data;

import java.util.List;

public interface GameView {

    void showProgress();
    void hideProgress();
    void onError(String errorMsg);

    default void onWordFetched(Datum correctWord){

    }

    default void onGetLeaderBoardListFetched(List<LeaderboardListModel> list){

    }

    default void onStatisticsDataFetched(Data data){

    }
}

package com.shabdamsdk;


import com.shabdamsdk.model.getwordresp.Datum;
import com.shabdamsdk.model.leaderboard.LeaderboardListModel;
import com.shabdamsdk.model.statistics.Data;

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

    default void onAddUser(com.shabdamsdk.model.adduser.Data data){

    }

    default void onWordCheckDic(boolean isMatched){

    }

    default void onGameSubmit(){

    }
}

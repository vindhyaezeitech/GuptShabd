package com.guptshabd;

import com.guptshabd.model.Datum;

import java.util.List;

public interface GameView {

    void showProgress();
    void hideProgress();
    void onError(String errorMsg);

    default void onWordFetched(){

    }

    default void onGetLeaderBoardListFetched(List<Datum> list){

    }
}

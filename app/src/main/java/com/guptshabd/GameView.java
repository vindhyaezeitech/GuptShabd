package com.guptshabd;

import com.guptshabd.model.getwordresp.Datum;

public interface GameView {

    void showProgress();
    void hideProgress();
    void onError(String errorMsg);

    default void onWordFetched(Datum correctWord){

    }

}

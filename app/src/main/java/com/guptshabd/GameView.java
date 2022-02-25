package com.guptshabd;

public interface GameView {

    void showProgress();
    void hideProgress();
    void onError(String errorMsg);

    default void onWordFetched(){

    }

}

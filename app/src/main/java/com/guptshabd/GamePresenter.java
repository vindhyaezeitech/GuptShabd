package com.guptshabd;

import com.guptshabd.model.GetWordRequest;
import com.guptshabd.network.ApiService;
import com.guptshabd.network.RetrofitClient;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class GamePresenter {
    private GameView gameView;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ApiService apiService = RetrofitClient.getInstance();

    public GamePresenter(GameView gameView){
        this.gameView = gameView;
    }

    public void fetchNewWord(GetWordRequest request){
        if(gameView != null){
            gameView.showProgress();
        }
        compositeDisposable.add(apiService.fetchNewWord(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
            if(response != null ){
                if(gameView != null){
                    gameView.hideProgress();
                }
                gameView.onWordFetched();
            }
        }, throwable -> {

        }));


    }



    public void onDestroy(){

    }
}

package com.shabdamsdk;

import android.content.Context;
import android.os.Handler;

import com.shabdamsdk.db.AppDatabase;
import com.shabdamsdk.db.DatabaseClient;
import com.shabdamsdk.db.Task;
import com.shabdamsdk.model.dictionary.CheckWordDicRequest;
import com.shabdamsdk.model.GetWordRequest;
import com.shabdamsdk.model.gamesubmit.SubmitGameRequest;
import com.shabdamsdk.model.adduser.AddUserRequest;
import com.shabdamsdk.model.leaderboard.GetLeaderboardRequest;
import com.shabdamsdk.network.ApiService;
import com.shabdamsdk.network.RetrofitClient;
import com.shabdamsdk.pref.CommonPreference;

import java.util.ArrayList;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class GamePresenter {
    private GameView gameView;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ApiService apiService = RetrofitClient.getInstance();
    private Context context;


    public GamePresenter(GameView gameView, Context context){
        this.gameView = gameView;
    }

    public void fetchNewWord(Context context,GetWordRequest request){
        if(GameDataManager.getInstance().getDataList() != null && GameDataManager.getInstance().getDataList().size()>0){
            if(gameView != null){
                gameView.hideProgress();
            }
            gameView.onWordFetched(GameDataManager.getInstance().getDataList().get(0));
        }else {
            if(gameView != null){
                gameView.showProgress();
            }

            compositeDisposable.add(DatabaseClient.getInstance(context).getAppDatabase().taskDao().getAll()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> {
                        ArrayList<String> wordIdList = new ArrayList<>();
                        if(response != null ){
                            if(response.size() > 0){
                                for (int i = 0; i <response.size() ; i++) {
                                    wordIdList.add(response.get(i).getWordId());
                                }
                            }
                        }

                        request.setWordId(wordIdList);
                        callWordAPI(request);
                    }, throwable -> {
                        callWordAPI(request);
                    }));
        }



    }

    public void callWordAPI(GetWordRequest request){
        compositeDisposable.add(apiService.fetchNewWord(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if(response != null && response.getData() != null &&response.getData() != null && response.getData().size() > 0){
                        if(gameView != null){
                            gameView.hideProgress();
                        }
                        if(!response.getWoord_status().equalsIgnoreCase("true")){
                            CommonPreference.getInstance(context).clear();
                        }
                        GameDataManager.getInstance().addData(response.getData());
                        gameView.onWordFetched(response.getData().get(0));
                    }
                }, throwable -> {
                    if(gameView != null){
                        gameView.hideProgress();
                    }
                }));
    }

    public void fetchLeaderBoardList(String game_id){

        GetLeaderboardRequest request=new GetLeaderboardRequest();
        request.setGameUserId(game_id);

        if(gameView != null){
            gameView.showProgress();
        }
        compositeDisposable.add(apiService.getLeaderBoardAPIList(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if(response != null ){
                        if(gameView != null){
                            gameView.hideProgress();
                        }
                        gameView.onGetLeaderBoardListFetched(response.getData());
                    }
                }, throwable -> {

                }));

    }
    public void fetchStatisticsData(String game_id){
        GetLeaderboardRequest request=new GetLeaderboardRequest();
        request.setGameUserId(game_id);

        if(gameView != null){
            gameView.showProgress();
        }
        compositeDisposable.add(apiService.getStreakData(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if(response != null ){
                        if(gameView != null){
                            gameView.hideProgress();
                        }
                        gameView.onStatisticsDataFetched(response.getData());
                    }
                }, throwable -> {

                }));

    }

    public void addUser(AddUserRequest addUserRequest){

        if(gameView != null){
            gameView.showProgress();
        }
        compositeDisposable.add(apiService.addUser(addUserRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if(response != null ){
                        if(gameView != null){
                            gameView.hideProgress();
                        }
                        gameView.onAddUser(response.getData());
                    }
                }, throwable -> {

                }));

    }

    public void submitGame(SubmitGameRequest submitGameRequest){

        if(gameView != null){
            gameView.showProgress();
        }
        GameDataManager.getInstance().removeData();
        compositeDisposable.add(apiService.submitGame(submitGameRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if(response != null ){
                        if(gameView != null){
                            gameView.hideProgress();
                        }
                        gameView.onGameSubmit();
                    }
                }, throwable -> {

                }));

    }

    public void checkDictionary(CheckWordDicRequest checkWordDicRequest){

        if(gameView != null){
            gameView.showProgress();
        }
        compositeDisposable.add(apiService.checkWord(checkWordDicRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if(response != null ){
                        if(gameView != null){
                            gameView.hideProgress();
                        }
                        gameView.onWordCheckDic(response.getStatus().equalsIgnoreCase("true")?true:false);
                    }
                }, throwable -> {

                }));

    }


    public void saveIDLocalDB(Context context, Task task){

        compositeDisposable.add(Completable.fromAction(() -> DatabaseClient.getInstance(context).getAppDatabase().taskDao().insert(task))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe());

    }

    public void onDestroy(){

        if(compositeDisposable != null){
            compositeDisposable.clear();
            compositeDisposable.dispose();
        }
        context = null;

    }
}

package com.shabdamsdk.network;

import com.shabdamsdk.model.dictionary.CheckWordDicRequest;
import com.shabdamsdk.model.GetWordRequest;
import com.shabdamsdk.model.gamesubmit.GameSubmitResponse;
import com.shabdamsdk.model.gamesubmit.SubmitGameRequest;
import com.shabdamsdk.model.adduser.AddUserRequest;
import com.shabdamsdk.model.adduser.AddUserResponse;
import com.shabdamsdk.model.dictionary.CheckWordDicResponse;
import com.shabdamsdk.model.getwordresp.GetWordResponse;
import com.shabdamsdk.model.leaderboard.GetLeaderboardList;
import com.shabdamsdk.model.leaderboard.GetLeaderboardRequest;
import com.shabdamsdk.model.statistics.StatisticsMainModel;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("get_words")
    Observable<GetWordResponse> fetchNewWord(@Body GetWordRequest body);

    @POST("get_leaderboard")
    Observable<GetLeaderboardList> getLeaderBoardAPIList(@Body GetLeaderboardRequest body);

    @POST("get_streak")
    Observable<StatisticsMainModel> getStreakData(@Body GetLeaderboardRequest body);

    @POST("add_gameuser")
    Observable<AddUserResponse> addUser(@Body AddUserRequest body);

    @POST("check_words")
    Observable<CheckWordDicResponse> checkWord(@Body CheckWordDicRequest body);

    @POST("game_submit")
    Observable<GameSubmitResponse> submitGame(@Body SubmitGameRequest body);

}

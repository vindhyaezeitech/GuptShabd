package com.guptshabd.network;

import com.guptshabd.model.leaderboard.GetLeaderboardList;
import com.guptshabd.model.leaderboard.GetLeaderboardRequest;
import com.guptshabd.model.GetWordRequest;
import com.guptshabd.model.getwordresp.GetWordResponse;
import com.guptshabd.model.statistics.StatisticsMainModel;

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

}

package com.guptshabd.network;

import com.guptshabd.model.GetLeaderboardList;
import com.guptshabd.model.GetLeaderboardRequest;
import com.guptshabd.model.GetWordRequest;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("get_words")
    Observable<GetWordRequest> fetchNewWord(@Body GetWordRequest body);

    @POST("get_leaderboard")
    Observable<GetLeaderboardList> getLeaderBoardAPIList(@Body GetLeaderboardRequest body);


}

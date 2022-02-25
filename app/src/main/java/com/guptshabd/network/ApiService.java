package com.guptshabd.network;

import com.guptshabd.model.GetWordRequest;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("get_words")
    Observable<GetWordRequest> fetchNewWord(@Body GetWordRequest body);




}

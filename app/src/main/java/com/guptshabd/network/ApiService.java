package com.guptshabd.network;

import com.guptshabd.model.GetWordRequest;
import com.guptshabd.model.getwordresp.GetWordResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {

    @POST("get_words")
    Observable<GetWordResponse> fetchNewWord(@Body GetWordRequest body);
}

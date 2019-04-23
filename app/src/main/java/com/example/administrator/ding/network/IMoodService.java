package com.example.administrator.ding.network;


import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface IMoodService {

    @POST("MoodServlet")
    @FormUrlEncoded
    Observable<Boolean> saveGoodNailEditInfo(@Field("IGoodNail") String nail, @Field("OperationType") String type);

    @POST("MoodServlet")
    @FormUrlEncoded
    Observable<Boolean> saveBadNailEditInfo(@Field("IBadNail")String badNail, @Field("Crack")String crack, @Field("OperationType") String type);
}

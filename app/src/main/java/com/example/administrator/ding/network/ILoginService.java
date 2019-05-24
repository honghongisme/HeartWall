package com.example.administrator.ding.network;

import com.example.administrator.ding.model.view.LoginInfoModel;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface ILoginService {

    @POST("LoginServlet")
    @FormUrlEncoded
    Observable<LoginInfoModel> doLogin(@Field("AccountNumber") String account, @Field("Password") String password);
}

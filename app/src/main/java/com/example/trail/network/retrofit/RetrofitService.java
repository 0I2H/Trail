package com.example.trail.network.retrofit;

import com.example.trail.model.login.LoginDTO;
import com.example.trail.model.user.UserDTO;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;


public interface RetrofitService {

    @POST("/api/user/login")
    @FormUrlEncoded
    Single<LoginDTO> loginUser(
            @Field("email") String email,
            @Field("password") String password
    );

    @GET("/api/user/auth")
    Single<UserDTO> authUser();

}

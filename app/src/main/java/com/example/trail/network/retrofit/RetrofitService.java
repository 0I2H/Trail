package com.example.trail.network.retrofit;

import android.os.Message;

import com.example.trail.model.login.LoginDTO;
import com.example.trail.model.message.MessageDTO;
import com.example.trail.model.pin.PinDTO;
import com.example.trail.model.trail.TrailDTO;
import com.example.trail.model.user.UserDTO;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

import static com.example.trail.constants.AppConstants.JSON_KEY;


public interface RetrofitService {

    @POST("/api/user/login")
    @FormUrlEncoded
    Single<LoginDTO> loginUser (
            @Field("email") String email,
            @Field("password") String password
    );

    @GET("/api/user/auth")
    Single<UserDTO> authUser();

    @POST("/api/user/register")
    @FormUrlEncoded
    Single<UserDTO> signupUser (
            @Field("userName") String userName,
            @Field("email") String email,
            @Field("password") String password,
            @Field("lifeStyle") String lifeStyle,
            @Field("journeyType") String journeyType
    );

    @POST("/api/user/profile-upload")
    @FormUrlEncoded
    Single<UserDTO> uploadProfileImage (
            @Field("image") String image,
            @Field("userId") String userId
    );

//    @POST("/api/place/upload")
//    @FormUrlEncoded
//    Single<MessageDTO> uploadPlace (
////            @Field("image") String image,
//            @Field("placeName") RequestBody placeName,
//            @Field("pinTime") RequestBody pinTime,
//            @Field("journeysId") RequestBody journeyId,
////            @Field("category") String category,
////            @Field("note") String note,
//            @Field("longitude") RequestBody longitude,
//            @Field("latitude") RequestBody latitude,
//            @Field("status") RequestBody status,
//            @Field("userId") RequestBody userId,
//            @Field("userName") RequestBody userName
//    );

    @Multipart
    @POST("/api/place/upload")
    @FormUrlEncoded
    Single<MessageDTO> uploadPlace (
            @Part MultipartBody.Part image,
            @Part("body") RequestBody body
    );

//    @POST("/api/place/update")
//    @FormUrlEncoded
//    Single<MessageDTO> updatePlace (
//            @Field("image") String image,
//            @Field("placeName") String placeName,
//            @Field("pinTime") String pinTime,
//            @Field("journeysId") int journeyId,
//            @Field("category") String category,
//            @Field("note") String note,
//            @Field("longitude") double longitude,
//            @Field("latitude") double latitude,
//            @Field("status") int status,
//            @Field("userId") int userId,
//            @Field("userName") int userName
//    );

    @POST("/api/place/update")
    @FormUrlEncoded
    Single<MessageDTO> updatePlace (
            @Field(JSON_KEY) String PinDTO
    );

    @GET("/api/place/journey/{journeysId}")
    Single<List<PinDTO>> getJourneyPins(
            @Path("journeysId") int id
    );

    @GET("/api/place/list")
    Single<List<PinDTO>> getTotalPins();

    @POST("/api/journey/upload")
    @FormUrlEncoded
    Single<MessageDTO> createTrail(
            @Field("pinTime") String pinTime,
            @Field("journeysId") int journeyId,
            @Field("category") String category,
            @Field("note") String note,
            @Field("longitude") double longitude,
            @Field("latitude") double latitude,
            @Field("status") int status,
            @Field("userId") int userId,
            @Field("userName") int userName
    );

}

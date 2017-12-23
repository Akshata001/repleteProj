package com.optionsmoneymaker.optionsmoneymaker.rest;

import com.optionsmoneymaker.optionsmoneymaker.model.About;
import com.optionsmoneymaker.optionsmoneymaker.model.ContactUS;
import com.optionsmoneymaker.optionsmoneymaker.model.MessageDetail;
import com.optionsmoneymaker.optionsmoneymaker.model.MessageResult;
import com.optionsmoneymaker.optionsmoneymaker.model.NotificationResult;
import com.optionsmoneymaker.optionsmoneymaker.model.SuccessResult;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by Sagar on 04-10-2016.
 */
public interface MoneyMaker {

    @POST("/login_omm")
    void login(@Query("api_call") String apiName,
               @Query("username") String username,
               @Query("password") String password,
               @Query("device_id") String device_token,
               Callback<SuccessResult> callback);

    @GET("/omm_api.php")
    void latestMessages(@Query("api_call") String apiName,
                        @Query("userId") String userID,
                        Callback<MessageResult> callback);

    @GET("/latest_message")
    void lastMessage(@Query("userId") String userID,
                     Callback<Response> callback);

    @GET("/omm_api.php")
    void aboutUS(@Query("api_call") String apiName,
                 Callback<About> callback);

    @GET("/omm_api.php")
    void contactUS(@Query("api_call") String apiName,
                   Callback<ContactUS> callback);

    @GET("/message_detail")
    void messageDetail(@Query("message_id") String messageID,
                       @Query("current_time") String curTime,
                       @Query("user_id") String userID,
                       Callback<MessageDetail> callback);

    @GET("/message_read")
    void messageRead(@Query("message_id") String msgID,
                     @Query("user_id") String userID,
                     @Query("read_time") String readTime,
                     Callback<NotificationResult> callback);

    @GET("/mark_message_unread")
    void messageUnRead(@Query("message_id") String messageId,
                       @Query("user_id") String userID,
                       @Query("read_time") String strTime,
                       Callback<NotificationResult> callback);


    @GET("/delete_message")
    void messageDelete(@Query("message_id") String messageId,
                       @Query("user_id") String userID,
                       @Query("read_time") String strTime,
                       Callback<NotificationResult> callback);

    @GET("/omm_api.php")
    void syncTime(@Query("api_call") String apiName,
                  @Query("current_time") String curTime,
                  @Query("user_id") String userID,
                  @Query("select_time") String selectTime,
                  Callback<NotificationResult> callback);

    @GET("/logout")
    void logout(@Query("user_id") String userID,
                @Query("device_token") String regID,
                Callback<SuccessResult> callback);

}

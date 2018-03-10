package com.jade.walkinggroupbus.walkingschoolbus.proxy;

import java.util.List;

import com.jade.walkinggroupbus.walkingschoolbus.model.UserInfo;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * The ProxyBuilder class will handle the apiKey and token being injected as a header to all calls
 * This is a Retrofit interface.
 */
public interface WGServerProxy {
    @GET("getApiKey")
    Call<String> getApiKey(@Query("groupName") String groupName, @Query("sfuUserId") String sfuId);

    @POST("/users/signup")
    Call<UserInfo> createNewUser(@Body UserInfo user);

    @POST("/login")
    Call<Void> login(@Body UserInfo userWithEmailAndPassword);

    @GET("/users")
    Call<List<UserInfo>> getUsers();

    @GET("/users/{id}")
    Call<UserInfo> getUserById(@Path("id") Long userId);

    @GET("/users/byEmail")
    Call<UserInfo> getUserByEmail(@Query("email") String email);

    @GET("/users/{id}/monitorsUsers")
    Call<List<UserInfo>> getMonitoredUsers(@Path("id") Long userId);
    /**
     * MORE GOES HERE:
     * - Monitoring
     * - Groups
     */
}

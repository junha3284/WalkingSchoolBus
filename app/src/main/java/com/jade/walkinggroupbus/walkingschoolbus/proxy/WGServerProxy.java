package com.jade.walkinggroupbus.walkingschoolbus.proxy;

import java.util.List;

import com.jade.walkinggroupbus.walkingschoolbus.model.ChildInfo;
import com.jade.walkinggroupbus.walkingschoolbus.model.Group;
import com.jade.walkinggroupbus.walkingschoolbus.model.UserInfo;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
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
    Call<List<UserInfo>> getUsers() ;

    @GET("/users/{id}")
    Call<UserInfo> getUserById(@Path("id") Long userId);

    @GET("/users/byEmail")
    Call<UserInfo> getUserByEmail(@Query("email") String email);

    @GET("/users/{id}/monitorsUsers")
    Call<List<UserInfo>> getMonitoredUsers(@Path("id") Long userId);

    @POST("/users/{id}/monitorsUsers")
    Call<List<UserInfo>> addMonitoredUser(@Path("id") Long userID, @Body UserInfo user);

    @DELETE("/users/{idA}/monitorsUsers/{idB}")
    Call<Void> deleteMonitoredUser(@Path("idA") Long monitorUserID, @Path("idB") Long monitoredUserID);

    @GET("/groups")
    Call<List<Group>> getGroups();

    @POST("/groups")
    Call<Group> createGroup(@Body Group newGroup);

    @POST("/groups/{id}/memberUsers")
    Call<List<UserInfo>> addNewMemberOfGroup(@Path("id") Long groupID, @Body UserInfo user);

    @POST("/groups/{id}/memberUsers")
    Call<List<UserInfo>> addNewMemberOfGroup(@Path("id") Long groupID, @Body ChildInfo user);

    @DELETE("/groups/{groupId}/memberUsers/{userId}")
    Call<Void> leaveGroup(@Path("groupId") Long groupID, @Path("userID") Long userID);

    /**
     * MORE GOES HERE:
     * - Monitoring
     * - Groups
     */
}

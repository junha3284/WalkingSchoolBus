package com.jade.walkinggroupbus.walkingschoolbus.proxy;

import java.util.List;

import com.jade.walkinggroupbus.walkingschoolbus.model.ChildInfo;
import com.jade.walkinggroupbus.walkingschoolbus.model.GPSLocation;
import com.jade.walkinggroupbus.walkingschoolbus.model.Group;
import com.jade.walkinggroupbus.walkingschoolbus.model.Message;
import com.jade.walkinggroupbus.walkingschoolbus.model.Permission;
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

    @POST("/users/{id}")
    Call<UserInfo> editChildUser(@Path("id") Long userID, @Body ChildInfo user);

    @POST("/users/{id}")
    Call<UserInfo> editUser(@Path("id") Long userID, @Body UserInfo user);

    @GET("/users/{id}/monitorsUsers")
    Call<List<UserInfo>> getMonitoredUsers(@Path("id") Long userId);

    @POST("/users/{id}/monitorsUsers")
    Call<List<UserInfo>> addMonitoredUser(@Path("id") Long userID, @Body UserInfo user);

    @DELETE("/users/{idA}/monitorsUsers/{idB}")
    Call<Void> deleteMonitoredUser(@Path("idA") Long monitorUserID, @Path("idB") Long monitoredUserID);

    @GET("/groups")
    Call<List<Group>> getGroups();

    @GET("/groups/{id}")
    Call<Group> getGroupByID(@Path("id") Long groupID);

    @POST("/groups")
    Call<Group> createGroup(@Body Group newGroup);

    @POST("/groups/{id}/memberUsers")
    Call<List<UserInfo>> addNewMemberOfGroup(@Path("id") Long groupID, @Body UserInfo user);

    @POST("/groups/{id}/memberUsers")
    Call<List<UserInfo>> addNewMemberOfGroup(@Path("id") Long groupID, @Body ChildInfo user);

    @GET("/groups/{id}/memberUsers")
    Call<List<UserInfo>> getMembersOfGroup(@Path("id") Long groupID);

    @DELETE("/groups/{groupId}/memberUsers/{userID}")
    Call<Void> leaveGroup(@Path("groupId") Long groupID, @Path("userID") Long userID);

    @POST("/messages/togroup/{groupId}")
    Call<Message> newMessageToGroup(@Path("groupId") Long groupID, @Body Message msg);

    @POST("/messages/toparentsof/{userId}")
    Call<Message> newMessageToParents(@Path("userId") Long userID, @Body Message msg);

    @POST("/messages/{messageId}/readby/{userId}")
    Call<UserInfo> readMessage(@Path("messageId") Long messageID, @Path("userId") Long userID, @Body boolean notRead);

    @POST("/users/{id}/lastGpsLocation")
    Call<GPSLocation> setNewGPSLocation(@Path("id") Long userID, @Body GPSLocation newGpsLocation);

    @GET("/users/{id}/lastGpsLocation")
    Call<GPSLocation> getLastGPSLocation(@Path("id") Long userID);

    @GET("/permissions")
    Call<List<Permission>> getAllPermissions();

    @GET("/permissions?userId={id}&statusForUser=APPROVED")
    Call<List<Permission>> getApprovedPermissionsByUserID(@Path("id") Long userID);

    @GET("/permissions?userId={id}&statusForUser=DENIED")
    Call<List<Permission>> getDeniedPermissionsByUserID(@Path("id") Long userID);

    @GET("/permissions/{id}")
    Call<Permission> getPermissionByID(@Path("id") Long permissionID);

    @POST("/permissions/{id}")
    Call<Permission> respondToPermissionRequest(@Path("id") Long permissionID);
}

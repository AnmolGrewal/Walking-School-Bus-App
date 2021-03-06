package com.cmpt276.project.walkinggroupapp.proxy;

import java.util.List;

import com.cmpt276.project.walkinggroupapp.model.GpsLocation;
import com.cmpt276.project.walkinggroupapp.model.Message;
import com.cmpt276.project.walkinggroupapp.model.Permission;
import com.cmpt276.project.walkinggroupapp.model.User;
import com.cmpt276.project.walkinggroupapp.model.WalkingGroup;

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
    Call<User> createNewUser(@Body User user);

    @POST("/login")
    Call<Void> login(@Body User userWithEmailAndPassword);

    @GET("/users")
    Call<List<User>> getAllUsers();

    @GET("/users/{id}")
    Call<User> getUserById(@Path("id") Long userId);

    @POST("/users/{id}")
    Call<User> editUserWithId(@Path("id") Long userId, @Body User user);

    @GET("/users/byEmail")
    Call<User> getUserByEmail(@Query("email") String email);


    @GET("/users/{id}/monitorsUsers")
    Call<List<User>> getMonitorsUsersById(@Path("id") Long userId);

    @POST("/users/{id}/monitorsUsers")
    Call<List<User>> addNewMonitorsUser(@Path("id") Long userId, @Body User monitorsUser);

    @DELETE("/users/{idA}/monitorsUsers/{idB}")
    Call<Void> removeMonitorsUser(@Path("idA") Long userId, @Path("idB") Long monitoredUserId);


    @GET("/users/{id}/monitoredByUsers")
    Call<List<User>> getMonitoredByUsersById(@Path("id") Long userId);

    @POST("/users/{id}/monitoredByUsers")
    Call<List<User>> addNewMonitoredByUser(@Path("id") Long userId, @Body User monitoredByUser);

    @DELETE("/users/{idA}/monitoredByUsers/{idB}")
    Call<Void> removeMonitoredByUser(@Path("idA") Long userId, @Path("idB") Long monitoredUserId);


    @GET("/groups")
    Call<List<WalkingGroup>> getAllWalkingGroups();

    @POST("/groups")
    Call<WalkingGroup> createNewWalkingGroup(@Body WalkingGroup newWalkingGroup);

    @GET("/groups/{id}")
    Call<WalkingGroup> getWalkingGroupById(@Path("id") Long groupId);

    @POST("/groups/{id}")
    Call<WalkingGroup> updateWalkingGroupById(@Path("id") Long groupId, @Body WalkingGroup updatedGroup);

    @DELETE("/groups/{id}")
    Call<Void> deleteGroupById(@Path("id") Long groupId);

    @GET("/groups/{id}/memberUsers")
    Call<List<User>> getAllMemberUsersByGroupId(@Path("id") Long groupId);

    @POST("/groups/{id}/memberUsers")
    Call<List<User>> addNewMemberToGroup(@Path("id") Long groupId, @Body User newMemberWithUserId);

    @DELETE("/groups/{groupId}/memberUsers/{userId}")
    Call<Void> removeMemberFromGroup(@Path("groupId") Long groupId, @Path("userId") Long userId);



    @GET("/users/{id}/lastGpsLocation")
    Call<GpsLocation> getLastGpsLocation(@Path("id") Long userId);

    @POST("/users/{id}/lastGpsLocation")
    Call<GpsLocation> setLastGpsLocation(@Path("id") Long userId, @Body GpsLocation gpsLocation);




    @GET("/messages")
    Call<List<Message>> getALlMessages();

    @GET("/messages")
    Call<List<Message>> getMessagesToGroup(@Query("togroup") long groupId);

    @GET("/messages?is-emergency=true")
    Call<List<Message>> getEmergencyMessagesToGroup(@Query("togroup") long groupId);

    @GET("/messages")
    Call<List<Message>> getMessagesForUser(@Query("foruser") long userId);

    @GET("/messages?status=read")
    Call<List<Message>> getReadMessagesForUser(@Query("foruser") long userId);

    @GET("/messages?status=unread")
    Call<List<Message>> getUnreadMessagesForUser(@Query("foruser") long userId);

    @GET("/messages?is-emergency=true&status=unread")
    Call<List<Message>> getUnreadEmergencyMessagesForUser(@Query("foruser") long userId);





    @POST("/messages/togroup/{groupId}")
    Call<Message> sendMessageToGroup(@Path("groupId") long groupId, @Body Message message);


    @POST("/messages/toparentsof/{userId}")
    Call<Message> sendMessageToParentsOf(@Path("userId") long userId, @Body Message message);




    @GET("/messages/{id}")
    Call<Message> getMessageById(@Path("id") long messageId);


    @DELETE("/messages/{id}")
    Call<Void> deleteMessageById(@Path("id") long messageId);



    @POST("/messages/{messageId}/readby/{userId}")
    Call<User> changeMessageStatus(@Path("messageId") long messageId, @Path("userId") long userId, @Body boolean isRead);


    @GET("/permissions?statusForUser=PENDING")
    Call<List<Permission>> getPendingPermissions(@Query("userId") long userId);

    @GET("/permissions")
    Call<List<Permission>> getPastPermissions(@Query("userId") long userId);

    @POST("/permissions/{id}")
    Call<Void> changePermissionStatus(@Path("id") long permissionId, @Body String status);


    @GET("/permissions/{id}")
    Call<Permission> getPermissionById(@Path("id") long permissionId);



    /**
     * MORE GOES HERE:
     * - Monitoring
     * - Groups
     */
}

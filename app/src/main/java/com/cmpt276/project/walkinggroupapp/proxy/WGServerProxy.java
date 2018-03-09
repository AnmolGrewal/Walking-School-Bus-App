package com.cmpt276.project.walkinggroupapp.proxy;

import java.util.List;

import com.cmpt276.project.walkinggroupapp.model.User;
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
    Call<List<User>> getUsers();

    @GET("/users/{id}")
    Call<User> getUserById(@Path("id") Long userId);

    @GET("/users/byEmail")
    Call<User> getUserByEmail(@Query("email") String email);

    @POST("/users/{id}/monitorsUsers")
    Call<List<User>> addNewMonitorsUser(@Path("id") Long userId, @Body User monitorUser);

    @GET("/users/{id}/monitorsUsers")
    Call<List<User>> getMonitorsUsersById(@Path("id") Long userId);

    @DELETE("/users/{idA}/monitorsUsers/{idB}")
    Call<Void> removeMonitorsUser(@Path("idA") Long userId, Long monitoredUserId);

//    @POST("/users/{id}/monitoredByUsers")
//    Call<List<User>> addNewMonitoredByUser(@Path("id") Long userId);

    @GET("/users/{id}/monitoredByUsers")
    Call<List<User>> getMonitoredByUsersById(@Path("id") Long userId);

    /**
     * MORE GOES HERE:
     * - Monitoring
     * - Groups
     */
}

package com.cmpt276.project.walkinggroupapp.model;

import android.content.Context;
import android.util.Log;

import com.cmpt276.project.walkinggroupapp.proxy.ProxyBuilder;
import com.cmpt276.project.walkinggroupapp.proxy.WGServerProxy;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.security.Policy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;

/**
 * Created by zherenx on 2018-03-03.
 */

public class ModelManager {

    private static ModelManager instance = null;

    private String apiKey = null;

    private WGServerProxy proxy = null;

    private String token = null;

    private User user = null;











    public static ModelManager getInstance() {
        if (instance == null) {
            instance = new ModelManager();
        }
        return instance;
    }


    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
        proxy = ProxyBuilder.getProxy(apiKey, token);
    }



    public void register(Context context,
                         ProxyBuilder.SimpleCallback<Void> callback,
                         String name, String emailAddress, String password,
                         Integer birthYear, Integer birthMonth,
                         String address, String cellPhone,
                         String homePhone, String grade,
                         String teacherName, String emergencyContactInfo ) {


        User newUser = new User();
        newUser.setName(name);
        newUser.setEmail(emailAddress);
        newUser.setPassword(password);
        //Testing
        newUser.setBirthMonth(birthMonth);
        newUser.setBirthYear(birthYear);
        newUser.setAddress(address);
        newUser.setCellPhone(cellPhone);
        newUser.setHomePhone(homePhone);
        newUser.setGrade(grade);
        newUser.setTeacherName(teacherName);
        newUser.setEmergencyContactInfo(emergencyContactInfo);


        // for gamification
        newUser.setCurrentPoints(0);
        newUser.setTotalPointsEarned(0);

        Gamification custom = new Gamification();
        newUser.setGamification(custom);
        try {
            // Convert custom object to a JSON string:
            String customAsJson = new ObjectMapper().writeValueAsString(custom);
            // Store JSON string into the user object, which will be sent to server.
            newUser.setCustomJson(customAsJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


        Call<User> caller = proxy.createNewUser(newUser);
        ProxyBuilder.callProxy(context, caller, returnedUser -> {
            user = returnedUser;

            // this if condition is just a guard, it shouldn't get executed unless something else went wrong.
            if (user.getCustomJson() == null) {
                user.setCustomJson("{\"currentAvatar\":0,\"ownedAvatars\":[0]}");
            }

            // Deserialize the custom object from the user:
            try {
                Gamification customObjectFromServer = new ObjectMapper().readValue(user.getCustomJson(), Gamification.class);
                user.setGamification(customObjectFromServer);
            } catch (IOException e) {
                e.printStackTrace();
            }

            callback.callback(null);
        });

    }


    public void login(Context context,
                      ProxyBuilder.SimpleCallback<Void> onResponseCallback,
                      ProxyBuilder.SimpleCallback<String> onFailureCallback,
                      String emailAddress, String password) {
        user = new User();
        user.setEmail(emailAddress);
        user.setPassword(password);
        ProxyBuilder.setOnTokenReceiveCallback(token -> onReceiveToken(token));
        Call<Void> caller = proxy.login(user);
        ProxyBuilder.callProxy(
                context,
                caller,
                returnNothing -> {
                    Call<User> getUserCaller = proxy.getUserByEmail(user.getEmail());
                    ProxyBuilder.callProxy(context, getUserCaller, returnedUser -> {
                        user = returnedUser;

//                        // this if condition is just a guard, it shouldn't get executed unless something else went wrong.
//                        if (user.getCustomJson() == null) {
//                            user.setCustomJson("{\"currentAvatar\":0,\"ownedAvatars\":[0]}");
//                        }
//
//                        // Deserialize the custom object from the user:
//                        try {
//                            Gamification customObjectFromServer = new ObjectMapper().readValue(user.getCustomJson(), Gamification.class);
//                            user.setGamification(customObjectFromServer);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }

                        onResponseCallback.callback(null);
                    });
                },
                onFailureCallback
        );
    }

    private void onReceiveToken(String token) {
        proxy = ProxyBuilder.getProxy(apiKey, token);
    }



    public long getLocalUserId() {
        return user.getId();
    }


    public void getMonitorsUsers(Context context, ProxyBuilder.SimpleCallback<List<User>> callback) {
        Call<List<User>> caller = proxy.getMonitorsUsersById(user.getId());
        ProxyBuilder.callProxy(context, caller, returnedUsersList -> {
            for (User returnedUser: returnedUsersList) {

                // this if condition is just a guard, it shouldn't get executed unless something else went wrong.
                if (returnedUser.getCustomJson() == null) {
                    returnedUser.setCustomJson("{\"currentAvatar\":0,\"ownedAvatars\":[0]}");
                }

                // Deserialize the custom object from the user:
                try {
                    Gamification customObjectFromServer = new ObjectMapper().readValue(returnedUser.getCustomJson(), Gamification.class);
                    returnedUser.setGamification(customObjectFromServer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            callback.callback(returnedUsersList);
        });
    }

    public void getMonitoredByUsers(Context context, ProxyBuilder.SimpleCallback<List<User>> callback) {
        Call<List<User>> caller = proxy.getMonitoredByUsersById(user.getId());
        ProxyBuilder.callProxy(context, caller, returnedUsersList -> {
            for (User returnedUser: returnedUsersList) {

                // this if condition is just a guard, it shouldn't get executed unless something else went wrong.
                if (returnedUser.getCustomJson() == null) {
                    returnedUser.setCustomJson("{\"currentAvatar\":0,\"ownedAvatars\":[0]}");
                }

                // Deserialize the custom object from the user:
                try {
                    Gamification customObjectFromServer = new ObjectMapper().readValue(returnedUser.getCustomJson(), Gamification.class);
                    returnedUser.setGamification(customObjectFromServer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            callback.callback(returnedUsersList);
        });
    }

    public void addNewMonitorsUserById(Context context,
                                       ProxyBuilder.SimpleCallback<List<User>> callback,
                                       long targetId) {
        User newUser = new User();
        newUser.setId(targetId);
        Call<List<User>> caller = proxy.addNewMonitorsUser(user.getId(), newUser);
        ProxyBuilder.callProxy(context, caller, returnedUsersList -> {
            for (User returnedUser: returnedUsersList) {

                // this if condition is just a guard, it shouldn't get executed unless something else went wrong.
                if (returnedUser.getCustomJson() == null) {
                    returnedUser.setCustomJson("{\"currentAvatar\":0,\"ownedAvatars\":[0]}");
                }

                // Deserialize the custom object from the user:
                try {
                    Gamification customObjectFromServer = new ObjectMapper().readValue(returnedUser.getCustomJson(), Gamification.class);
                    returnedUser.setGamification(customObjectFromServer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            callback.callback(returnedUsersList);
        });
    }

    public void addNewMonitorsUserByEmail(Context context,
                                       ProxyBuilder.SimpleCallback<List<User>> callback,
                                       String emailAddress) {
        Call<User> getUserByEmailCaller = proxy.getUserByEmail(emailAddress);
        ProxyBuilder.callProxy(context, getUserByEmailCaller, targetUser -> {
            Call<List<User>> caller = proxy.addNewMonitorsUser(user.getId(), targetUser);
            ProxyBuilder.callProxy(context, caller, returnedUsersList -> {
                for (User returnedUser: returnedUsersList) {

                    // this if condition is just a guard, it shouldn't get executed unless something else went wrong.
                    if (returnedUser.getCustomJson() == null) {
                        returnedUser.setCustomJson("{\"currentAvatar\":0,\"ownedAvatars\":[0]}");
                    }

                    // Deserialize the custom object from the user:
                    try {
                        Gamification customObjectFromServer = new ObjectMapper().readValue(returnedUser.getCustomJson(), Gamification.class);
                        returnedUser.setGamification(customObjectFromServer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                callback.callback(returnedUsersList);
            });
        });
    }

    public void addNewMonitoredByUserById(Context context,
                                          ProxyBuilder.SimpleCallback<List<User>> callback,
                                          long targetId) {
        User newUser = new User();
        newUser.setId(targetId);
        Call<List<User>> caller = proxy.addNewMonitoredByUser(user.getId(), newUser);
        ProxyBuilder.callProxy(context, caller, returnedUsersList -> {
            for (User returnedUser: returnedUsersList) {

                // this if condition is just a guard, it shouldn't get executed unless something else went wrong.
                if (returnedUser.getCustomJson() == null) {
                    returnedUser.setCustomJson("{\"currentAvatar\":0,\"ownedAvatars\":[0]}");
                }

                // Deserialize the custom object from the user:
                try {
                    Gamification customObjectFromServer = new ObjectMapper().readValue(returnedUser.getCustomJson(), Gamification.class);
                    returnedUser.setGamification(customObjectFromServer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            callback.callback(returnedUsersList);
        });
    }

    public void addNewMonitoredByUserByEmail(Context context,
                                          ProxyBuilder.SimpleCallback<List<User>> callback,
                                          String emailAddress) {
        Call<User> getUserByEmailCaller = proxy.getUserByEmail(emailAddress);
        ProxyBuilder.callProxy(context, getUserByEmailCaller, targetUser -> {
            Call<List<User>> caller = proxy.addNewMonitoredByUser(user.getId(), targetUser);
            ProxyBuilder.callProxy(context, caller, returnedUsersList -> {
                for (User returnedUser: returnedUsersList) {

                    // this if condition is just a guard, it shouldn't get executed unless something else went wrong.
                    if (returnedUser.getCustomJson() == null) {
                        returnedUser.setCustomJson("{\"currentAvatar\":0,\"ownedAvatars\":[0]}");
                    }

                    // Deserialize the custom object from the user:
                    try {
                        Gamification customObjectFromServer = new ObjectMapper().readValue(returnedUser.getCustomJson(), Gamification.class);
                        returnedUser.setGamification(customObjectFromServer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                callback.callback(returnedUsersList);
            });
        });
    }

    public void removeMonitorsUser(Context context,
                                   ProxyBuilder.SimpleCallback<Void> callback,
                                   long targetId) {
//        User userToRemove = new User();
//        userToRemove.setId(targetId);
        Call<Void> caller = proxy.removeMonitorsUser(user.getId(), targetId);
        ProxyBuilder.callProxy(context, caller, callback);
    }

    public void removeMonitoredByUser(Context context,
                                      ProxyBuilder.SimpleCallback<Void> callback,
                                      long targetId) {
//        User userToRemove = new User();
//        userToRemove.setId(targetId);
        Call<Void> caller = proxy.removeMonitoredByUser(user.getId(), targetId);
        ProxyBuilder.callProxy(context, caller, callback);
    }


    public void getAllWalkingGroups(Context context,
                                   ProxyBuilder.SimpleCallback<List<WalkingGroup>> callback) {
        Call<List<WalkingGroup>> caller = proxy.getAllWalkingGroups();
        ProxyBuilder.callProxy(context, caller, callback);
    }

    // TODO: return only the near walking group.
//    public void getNearWalkingGroups(Context context,
//                                     ProxyBuilder.SimpleCallback<List<WalkingGroup>> callback,
//                                     double latitude, double longitude) {
//        Call<List<WalkingGroup>> caller = proxy.getAllWalkingGroups();
//        ProxyBuilder.callProxy(context, caller, allWalkingGroups -> {
//            for (WalkingGroup group: allWalkingGroups) {
//                if ()
//            }
//        });
//
//    }


    public void createNewWalkingGroup(Context context,
                                      ProxyBuilder.SimpleCallback<WalkingGroup> callback,
                                      String groupDescription) {
        WalkingGroup newWalkingGroup = new WalkingGroup();
        newWalkingGroup.setGroupDescription(groupDescription);

        User userWithId = new User();
        userWithId.setId(user.getId());

        newWalkingGroup.setLeader(userWithId);

        Call<WalkingGroup> caller = proxy.createNewWalkingGroup(newWalkingGroup);
        ProxyBuilder.callProxy(context, caller, callback);
    }

    public void createNewWalkingGroup(Context context,
                                      ProxyBuilder.SimpleCallback<WalkingGroup> callback,
                                      String groupDescription,
                                      double startLatitude, double startLongitude,
                                      double destinationLatitude, double destinationLongitude) {

        WalkingGroup newWalkingGroup = new WalkingGroup();

        newWalkingGroup.setGroupDescription(groupDescription);

        // TODO: delete this when server get updated.
        long idInvalid = -1;
        newWalkingGroup.setId(idInvalid);

        newWalkingGroup.setLeader(user);

//        User userWithId = new User();
//        userWithId.setId(user.getId());
//        newWalkingGroup.setLeader(userWithId);

        double[] routeLatArray = new double[2];
        routeLatArray[0] = startLatitude;
        routeLatArray[1] = destinationLatitude;

        double[] routeLngArray = new double[2];
        routeLngArray[0] = startLongitude;
        routeLngArray[1] = destinationLongitude;

        newWalkingGroup.setRouteLatArray(routeLatArray);
        newWalkingGroup.setRouteLngArray(routeLngArray);

        Call<WalkingGroup> caller = proxy.createNewWalkingGroup(newWalkingGroup);
        ProxyBuilder.callProxy(context, caller, callback);
    }

//    public void createNewWalkingGroup(Context context,
//                                      ProxyBuilder.SimpleCallback<WalkingGroup> callback,
//                                      String groupDescription,
//                                      double[] routeLagArray, double[] routeLngArray) {
//        // TODO:
//    }

    // Only update group description.
    public void updateWalkingGroupById(Context context,
                                       ProxyBuilder.SimpleCallback<WalkingGroup> callback,
                                       WalkingGroup walkingGroup,
                                       String groupDescription) {
        walkingGroup.setGroupDescription(groupDescription);
        Call<WalkingGroup> caller = proxy.updateWalkingGroupById(walkingGroup.getId(), walkingGroup);
        ProxyBuilder.callProxy(context, caller, callback);
    }

    // Update group description and route with start and end points.
    public void updateWalkingGroupById(Context context,
                                       ProxyBuilder.SimpleCallback<WalkingGroup> callback,
                                       WalkingGroup walkingGroup,
                                       String groupDescription,
                                       double startLatitude, double startLongitude,
                                       double destinationLatitude, double destinationLongitude) {
        walkingGroup.setGroupDescription(groupDescription);

        double[] routeLatArray = new double[2];
        routeLatArray[0] = startLatitude;
        routeLatArray[1] = destinationLatitude;

        double[] routeLngArray = new double[2];
        routeLngArray[0] = startLongitude;
        routeLngArray[1] = destinationLongitude;

        walkingGroup.setRouteLatArray(routeLatArray);
        walkingGroup.setRouteLngArray(routeLngArray);

        Call<WalkingGroup> caller = proxy.updateWalkingGroupById(walkingGroup.getId(), walkingGroup);
        ProxyBuilder.callProxy(context, caller, callback);
    }

    // Update group description and route with route arrays.
//    public void updateWalkingGroupById(Context context,
//                                       ProxyBuilder.SimpleCallback<WalkingGroup> callback,
//                                       WalkingGroup walkingGroup,
//                                       String groupDescription,
//                                       double[] routeLatArray, double[] routeLngArray) {
//
//        // take care of invalid routeLatArray and routeLngArray.
//        if (routeLatArray.length < 2 || routeLngArray.length < 2) {
//            updateWalkingGroupById(context, callback, walkingGroup, groupDescription);
//        } else {
//            walkingGroup.setGroupDescription(groupDescription);
//
//            walkingGroup.setRouteLatArray(routeLatArray);
//            walkingGroup.setRouteLngArray(routeLngArray);
//
//            Call<WalkingGroup> caller = proxy.updateWalkingGroupById(walkingGroup.getId(), walkingGroup);
//            ProxyBuilder.callProxy(context, caller, callback);
//        }
//
//    }

    // Update route with start and end points.
    public void updateWalkingGroupById(Context context,
                                       ProxyBuilder.SimpleCallback<WalkingGroup> callback,
                                       WalkingGroup walkingGroup,
                                       double startLatitude, double startLongitude,
                                       double destinationLatitude, double destinationLongitude) {

        double[] routeLatArray = new double[2];
        routeLatArray[0] = startLatitude;
        routeLatArray[1] = destinationLatitude;

        double[] routeLngArray = new double[2];
        routeLngArray[0] = startLongitude;
        routeLngArray[1] = destinationLongitude;

        walkingGroup.setRouteLatArray(routeLatArray);
        walkingGroup.setRouteLngArray(routeLngArray);

        Call<WalkingGroup> caller = proxy.updateWalkingGroupById(walkingGroup.getId(), walkingGroup);
        ProxyBuilder.callProxy(context, caller, callback);

    }

    // Update route with route arrays.
//    public void updateWalkingGroupById(Context context,
//                                       ProxyBuilder.SimpleCallback<WalkingGroup> callback,
//                                       WalkingGroup walkingGroup,
//                                       double[] routeLatArray, double[] routeLngArray) {
//
//        // take care of invalid routeLatArray and routeLngArray.
//        if (routeLatArray.length < 2 || routeLngArray.length < 2) {
//            callback.callback(walkingGroup);
//        } else {
//
//            walkingGroup.setRouteLatArray(routeLatArray);
//            walkingGroup.setRouteLngArray(routeLngArray);
//
//            Call<WalkingGroup> caller = proxy.updateWalkingGroupById(walkingGroup.getId(), walkingGroup);
//            ProxyBuilder.callProxy(context, caller, callback);
//        }
//
//    }

    public void getIdsOfGroupsYouAreLeading(Context context,
                                            ProxyBuilder.SimpleCallback<List<Long>> callback) {
        Call<User> getUserCaller = proxy.getUserById(user.getId());
        ProxyBuilder.callProxy(context, getUserCaller, returnedUser -> {
            user = returnedUser;

            List<Long> groupIds = new ArrayList<>();

            for (WalkingGroup group: user.getLeadsGroups()) {
                groupIds.add(group.getId());
            }

            callback.callback(groupIds);

        });
    }

    public void getIdsOfGroupsYouAreMemberOf(Context context,
                                             ProxyBuilder.SimpleCallback<List<Long>> callback) {
        Call<User> getUserCaller = proxy.getUserById(user.getId());
        ProxyBuilder.callProxy(context, getUserCaller, returnedUser -> {
            user = returnedUser;

            List<Long> groupIds = new ArrayList<>();

            for (WalkingGroup group: user.getMemberOfGroups()) {
                groupIds.add(group.getId());
            }

            callback.callback(groupIds);

        });
    }

    // return a list of groupId that the user with userId is member of.
    public void getIdsOfGroupsAUserIsMemberOf(Context context,
                                              ProxyBuilder.SimpleCallback<List<Long>> callback,
                                              long userId) {
        Call<User> getUserCaller = proxy.getUserById(userId);
        ProxyBuilder.callProxy(context, getUserCaller, returnedUser -> {

            List<Long> groupIds = new ArrayList<>();

            for (WalkingGroup group: returnedUser.getMemberOfGroups()) {
                groupIds.add(group.getId());
            }

            callback.callback(groupIds);

        });
    }

    public void getWalkingGroupById(Context context,
                                    ProxyBuilder.SimpleCallback<WalkingGroup> callback,
                                    long groupId) {
        Call<WalkingGroup> caller = proxy.getWalkingGroupById(groupId);
        ProxyBuilder.callProxy(context, caller, callback);
    }

    public void deleteGroupById(Context context,
                                ProxyBuilder.SimpleCallback<Void> callback,
                                long groupId) {
        Call<Void> caller = proxy.deleteGroupById(groupId);
        ProxyBuilder.callProxy(context, caller, callback);
    }


    public void getAllMemberUsersByGroupId(Context context,
                                           ProxyBuilder.SimpleCallback<List<User>> callback,
                                           long groupId) {
        Call<List<User>> caller = proxy.getAllMemberUsersByGroupId(groupId);
        ProxyBuilder.callProxy(context, caller, returnedUsersList -> {
            for (User returnedUser: returnedUsersList) {

                // this if condition is just a guard, it shouldn't get executed unless something else went wrong.
                if (returnedUser.getCustomJson() == null) {
                    returnedUser.setCustomJson("{\"currentAvatar\":0,\"ownedAvatars\":[0]}");
                }

                // Deserialize the custom object from the user:
                try {
                    Gamification customObjectFromServer = new ObjectMapper().readValue(returnedUser.getCustomJson(), Gamification.class);
                    returnedUser.setGamification(customObjectFromServer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            callback.callback(returnedUsersList);
        });
    }

    public void addUserToGroup(Context context,
                               ProxyBuilder.SimpleCallback<List<User>> callback,
                               long groupId) {
        User newMember = new User();
        newMember.setId(user.getId());
        Call<List<User>> caller = proxy.addNewMemberToGroup(groupId, newMember);
        ProxyBuilder.callProxy(context, caller, returnedUsersList -> {
            for (User returnedUser: returnedUsersList) {

                // this if condition is just a guard, it shouldn't get executed unless something else went wrong.
                if (returnedUser.getCustomJson() == null) {
                    returnedUser.setCustomJson("{\"currentAvatar\":0,\"ownedAvatars\":[0]}");
                }

                // Deserialize the custom object from the user:
                try {
                    Gamification customObjectFromServer = new ObjectMapper().readValue(returnedUser.getCustomJson(), Gamification.class);
                    returnedUser.setGamification(customObjectFromServer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            callback.callback(returnedUsersList);
        });
    }

    public void addUserToGroup(Context context,
                               ProxyBuilder.SimpleCallback<List<User>> callback,
                               long groupId, long userId) {
        User newMember = new User();
        newMember.setId(userId);
        Call<List<User>> caller = proxy.addNewMemberToGroup(groupId, newMember);
        ProxyBuilder.callProxy(context, caller, returnedUsersList -> {
            for (User returnedUser: returnedUsersList) {

                // this if condition is just a guard, it shouldn't get executed unless something else went wrong.
                if (returnedUser.getCustomJson() == null) {
                    returnedUser.setCustomJson("{\"currentAvatar\":0,\"ownedAvatars\":[0]}");
                }

                // Deserialize the custom object from the user:
                try {
                    Gamification customObjectFromServer = new ObjectMapper().readValue(returnedUser.getCustomJson(), Gamification.class);
                    returnedUser.setGamification(customObjectFromServer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            callback.callback(returnedUsersList);
        });
    }

    public void leaveGroup(Context context,
                           ProxyBuilder.SimpleCallback<List<User>> callback,
                           long groupId) {
        Call<Void> caller = proxy.removeMemberFromGroup(groupId, user.getId());
        ProxyBuilder.callProxy(context, caller, returnedNothing -> {
            Call<List<User>> getAllMembersCaller = proxy.getAllMemberUsersByGroupId(groupId);
            ProxyBuilder.callProxy(context, getAllMembersCaller, returnedUsersList -> {
                for (User returnedUser: returnedUsersList) {

                    // this if condition is just a guard, it shouldn't get executed unless something else went wrong.
                    if (returnedUser.getCustomJson() == null) {
                        returnedUser.setCustomJson("{\"currentAvatar\":0,\"ownedAvatars\":[0]}");
                    }

                    // Deserialize the custom object from the user:
                    try {
                        Gamification customObjectFromServer = new ObjectMapper().readValue(returnedUser.getCustomJson(), Gamification.class);
                        returnedUser.setGamification(customObjectFromServer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                callback.callback(returnedUsersList);
            });
        });
    }

    public void removeUserFromGroup(Context context,
                                    ProxyBuilder.SimpleCallback<List<User>> callback,
                                    long groupId, long userId) {
        Call<Void> caller = proxy.removeMemberFromGroup(groupId, userId);
        ProxyBuilder.callProxy(context, caller, returnedNothing -> {
            Call<List<User>> getAllMembersCaller = proxy.getAllMemberUsersByGroupId(groupId);
            ProxyBuilder.callProxy(context, getAllMembersCaller, returnedUsersList -> {
                for (User returnedUser: returnedUsersList) {

                    // this if condition is just a guard, it shouldn't get executed unless something else went wrong.
                    if (returnedUser.getCustomJson() == null) {
                        returnedUser.setCustomJson("{\"currentAvatar\":0,\"ownedAvatars\":[0]}");
                    }

                    // Deserialize the custom object from the user:
                    try {
                        Gamification customObjectFromServer = new ObjectMapper().readValue(returnedUser.getCustomJson(), Gamification.class);
                        returnedUser.setGamification(customObjectFromServer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                callback.callback(returnedUsersList);
            });
        });
    }




    // this method returns a list of all users sorted by totalPointsEarned (in descending order).
    public void getAllUsers(Context context, ProxyBuilder.SimpleCallback<List<User>> callback) {
        Call<List<User>> caller = proxy.getAllUsers();
        ProxyBuilder.callProxy(context, caller, returnedUsersList -> {
            for (User returnedUser: returnedUsersList) {

                // this if condition is just a guard, it shouldn't get executed unless something else went wrong.
                if (returnedUser.getCustomJson() == null) {
                    returnedUser.setCustomJson("{\"currentAvatar\":0,\"ownedAvatars\":[0]}");
                }

                // Deserialize the custom object from the user:
                try {
                    Gamification customObjectFromServer = new ObjectMapper().readValue(returnedUser.getCustomJson(), Gamification.class);
                    returnedUser.setGamification(customObjectFromServer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            java.util.Collections.sort(returnedUsersList, new Comparator<User>() {
                @Override
                public int compare(User user1, User user2) {
                    return user2.getTotalPointsEarned() - user1.getTotalPointsEarned();
                }
            });

            callback.callback(returnedUsersList);
        });
    }



    public void getLocalUser(Context context, ProxyBuilder.SimpleCallback<User> callback) {
        Call<User> caller = proxy.getUserById(user.getId());
        ProxyBuilder.callProxy(context, caller, returnedUser -> {
            user = returnedUser;

            // this if condition is just a guard, it shouldn't get executed unless something else went wrong.
            if (user.getCustomJson() == null) {
                user.setCustomJson("{\"currentAvatar\":0,\"ownedAvatars\":[0]}");
            }

            // Deserialize the custom object from the user:
            try {
                Gamification customObjectFromServer = new ObjectMapper().readValue(user.getCustomJson(), Gamification.class);
                user.setGamification(customObjectFromServer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            callback.callback(user);
        });
    }

    public void getUserById(Context context,
                            ProxyBuilder.SimpleCallback<User> callback,
                            long userId) {
        Call<User> caller = proxy.getUserById(userId);
        ProxyBuilder.callProxy(context, caller, returnedUser -> {

            // this if condition is just a guard, it shouldn't get executed unless something else went wrong.
            if (returnedUser.getCustomJson() == null) {
                returnedUser.setCustomJson("{\"currentAvatar\":0,\"ownedAvatars\":[0]}");
            }

            // Deserialize the custom object from the user:
            try {
                Gamification customObjectFromServer = new ObjectMapper().readValue(returnedUser.getCustomJson(), Gamification.class);
                returnedUser.setGamification(customObjectFromServer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            callback.callback(returnedUser);
        });
    }

    public void getUserById(Context context,
                            ProxyBuilder.SimpleCallback<User> callback,
                            ProxyBuilder.SimpleCallback<String> onFailureCallback,
                            long userId) {
        Call<User> caller = proxy.getUserById(userId);
        ProxyBuilder.callProxy(context, caller, returnedUser -> {

            // this if condition is just a guard, it shouldn't get executed unless something else went wrong.
            if (returnedUser.getCustomJson() == null) {
                returnedUser.setCustomJson("{\"currentAvatar\":0,\"ownedAvatars\":[0]}");
            }

            // Deserialize the custom object from the user:
            try {
                Gamification customObjectFromServer = new ObjectMapper().readValue(returnedUser.getCustomJson(), Gamification.class);
                returnedUser.setGamification(customObjectFromServer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            callback.callback(returnedUser);
        }, onFailureCallback);
    }


    // TODO: need to add three args, currentPoints, totalPoints, and customJson.
    // this one is for editing local user.
    public void editLocalUser(Context context,
                              ProxyBuilder.SimpleCallback<User> callback,
                              String name, String emailAddress,
                              Integer birthYear, Integer birthMonth,
                              String address,
                              String cellPhone, String homePhone,
                              String grade, String teacherName,
                              String emergencyContactInfo,
                              Integer currentPoints,
                              Integer totalPointsEarned,
                              String customJson) {

        User editedUser = new User();
        editedUser.setName(name);
        editedUser.setEmail(emailAddress);
        editedUser.setBirthYear(birthYear);
        editedUser.setBirthMonth(birthMonth);
        editedUser.setAddress(address);
        editedUser.setCellPhone(cellPhone);
        editedUser.setHomePhone(homePhone);
        editedUser.setGrade(grade);
        editedUser.setTeacherName(teacherName);
        editedUser.setEmergencyContactInfo(emergencyContactInfo);


//        // for now, it just re-initialize these three fields.
//        editedUser.setCurrentPoints(0);
//        editedUser.setTotalPointsEarned(0);
//        editedUser.setCustomJson("{\"currentAvatar\":0,\"ownedAvatars\":[0]}");

        editedUser.setCurrentPoints(currentPoints);
        editedUser.setTotalPointsEarned(totalPointsEarned);
        editedUser.setCustomJson(customJson);

        Call<User> caller = proxy.editUserWithId(user.getId(), editedUser);
        ProxyBuilder.callProxy(context, caller, returnedUser -> {
            user = returnedUser;
            // Deserialize the custom object from the user:
            try {
                Gamification customObjectFromServer = new ObjectMapper().readValue(user.getCustomJson(), Gamification.class);
                user.setGamification(customObjectFromServer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            callback.callback(user);
        });

    }

    // TODO: need to add three args, currentPoints, totalPoints, and customJson.
    // this one is for editing user with id (e.g. a child user).
    public void editUserById(Context context,
                             ProxyBuilder.SimpleCallback<User> callback,
                             long userId,
                             String name, String emailAddress,
                             Integer birthYear, Integer birthMonth,
                             String address,
                             String cellPhone,
                             String homePhone,
                             String grade, String teacherName,
                             String emergencyContactInfo,
                             Integer currentPoints,
                             Integer totalPointsEarned,
                             String customJson) {

        User editedUser = new User();
        editedUser.setName(name);
        editedUser.setEmail(emailAddress);
        editedUser.setBirthYear(birthYear);
        editedUser.setBirthMonth(birthMonth);
        editedUser.setAddress(address);
        editedUser.setCellPhone(cellPhone);
        editedUser.setHomePhone(homePhone);
        editedUser.setGrade(grade);
        editedUser.setTeacherName(teacherName);
        editedUser.setEmergencyContactInfo(emergencyContactInfo);

//        // for now, it just re-initialize these three fields.
//        editedUser.setCurrentPoints(0);
//        editedUser.setTotalPointsEarned(0);
//        editedUser.setCustomJson("{\"currentAvatar\":0,\"ownedAvatars\":[0]}");

        editedUser.setCurrentPoints(currentPoints);
        editedUser.setTotalPointsEarned(totalPointsEarned);
        editedUser.setCustomJson(customJson);

        Call<User> caller = proxy.editUserWithId(userId, editedUser);
        ProxyBuilder.callProxy(context, caller, returnedUser -> {
            // Deserialize the custom object from the user:
            try {
                Gamification customObjectFromServer = new ObjectMapper().readValue(returnedUser.getCustomJson(), Gamification.class);
                returnedUser.setGamification(customObjectFromServer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            callback.callback(returnedUser);
        });

    }




    //for setting and getting lastGpsLocation
    public void getLastGpsLocation(Context context,
                                   ProxyBuilder.SimpleCallback<GpsLocation> callback,
                                   long userId) {
        Call<GpsLocation> getLastGpsLocationCaller = proxy.getLastGpsLocation(userId);
        ProxyBuilder.callProxy(context, getLastGpsLocationCaller, callback);
    }

    public void setLastGpsLocation(Context context,
                                   ProxyBuilder.SimpleCallback<GpsLocation> callback,
                                   long userId,
                                   GpsLocation gpsLocation) {
        Call<GpsLocation> setLastGpsLocationCaller = proxy.setLastGpsLocation(userId,gpsLocation );
        ProxyBuilder.callProxy(context, setLastGpsLocationCaller, callback);
    }






    // this part support togroup message receiving.

    public void getEmergencyMessagesToGroup(Context context,
                                            ProxyBuilder.SimpleCallback<List<Message>> callback,
                                            long groupId) {
        Call<List<Message>> getEmergencyMessagesToGroupCaller = proxy.getEmergencyMessagesToGroup(groupId);
        ProxyBuilder.callProxy(context, getEmergencyMessagesToGroupCaller, callback);
    }

    public void getMessagesToGroup(Context context,
                                   ProxyBuilder.SimpleCallback<List<Message>> callback,
                                   long groupId) {
        Call<List<Message>> getMessagesToGroupCaller = proxy.getMessagesToGroup(groupId);
        ProxyBuilder.callProxy(context, getMessagesToGroupCaller, callback);
    }




    // this part support foruser message receiving.

    public void getUnreadEmergencyMessagesForUser(Context context,
                                                  ProxyBuilder.SimpleCallback<List<Message>> callback) {
        Call<List<Message>> getUnreadEmergencyMessagesForUserCaller = proxy.getUnreadEmergencyMessagesForUser(user.getId());
        ProxyBuilder.callProxy(context, getUnreadEmergencyMessagesForUserCaller, callback);
    }

    public void getUnreadMessagesForUser(Context context,
                                                    ProxyBuilder.SimpleCallback<List<Message>> callback){
        Call<List<Message>> getUnreadMessageForUserCaller = proxy.getUnreadMessagesForUser(user.getId());
        ProxyBuilder.callProxy(context, getUnreadMessageForUserCaller, callback);
    }

    public void getMessagesForUser(Context context,
                                   ProxyBuilder.SimpleCallback<List<Message>> callback) {

        List<Message> messages = new ArrayList<>();

        Call<List<Message>> getReadMessagesForUserCaller = proxy.getReadMessagesForUser(user.getId());
        Call<List<Message>> getUnreadMessagesForUserCaller = proxy.getUnreadMessagesForUser(user.getId());

        ProxyBuilder.callProxy(context, getReadMessagesForUserCaller, readMessages -> {

            for(Message message: readMessages){
                message.setRead(true);
            }

            messages.addAll(readMessages);
            ProxyBuilder.callProxy(context, getUnreadMessagesForUserCaller, unreadMessages -> {

                for(Message message: unreadMessages){
                    message.setRead(false);
                }

                messages.addAll(unreadMessages);
                java.util.Collections.sort(messages, new Comparator<Message>() {
                    // TODO: consider moving this to an actual class if it is needed somewhere else.
                    @Override
                    public int compare(Message msg1, Message msg2) {
                        // this gives us descending ordering.
                        return -(msg1.getTimestamp().compareTo(msg2.getTimestamp()));
                    }
                });
                callback.callback(messages);
            });
        });

    }












    // this part is for sending messages.

    public void sendMessageToGroup(Context context,
                                   ProxyBuilder.SimpleCallback<Message> callback,
                                   long groupId,
                                   String text,
                                   boolean isEmergency) {

        Message message = new Message();
        message.setText(text);
        message.setEmergency(isEmergency);
        Call<Message> sendMessageToGroupCaller = proxy.sendMessageToGroup(groupId, message);
        ProxyBuilder.callProxy(context, sendMessageToGroupCaller, callback);
    }



    public void sendMessageToParentsOf(Context context,
                                       ProxyBuilder.SimpleCallback<Message> callback,
                                       String text,
                                       boolean isEmergency) {

        Message message = new Message();
        message.setText(text);
        message.setEmergency(isEmergency);
        Call<Message> sendMessageToParentsCaller = proxy.sendMessageToParentsOf(user.getId(), message);
        ProxyBuilder.callProxy(context, sendMessageToParentsCaller, callback);
    }




    // This part is mostly for message management.

    // I don't think we actually need this method,
    // I put it here just in case.
    public void getMessageByMessageId(Context context,
                                      ProxyBuilder.SimpleCallback<Message> callback,
                                      long messageId) {
        Call<Message> caller = proxy.getMessageById(messageId);
        ProxyBuilder.callProxy(context, caller, callback);
    }


    // This method delete the message reference in server,
    // i.e. it affects all users that can view this message;
    // it should NOT be use in release build;
    // I put this method here just for debugging purpose.

//public void deleteMessageByMessageId(Context context,
//                                     ProxyBuilder.SimpleCallback<Void> callback,
//                                     long messageId) {
//       Call<Void> caller = proxy.deleteMessageById(messageId);
//       ProxyBuilder.callProxy(context, caller, callback);
//   }


    public void markMessageAsRead(Context context,
                                  ProxyBuilder.SimpleCallback<Void> callback,
                                  long messageId) {
        Call<User> caller = proxy.changeMessageStatus(messageId, user.getId(), true);
        ProxyBuilder.callProxy(context, caller, returnedUser -> {
            user = returnedUser;
            callback.callback(null);
        });
    }


    public void markMessageAsUnread(Context context,
                                    ProxyBuilder.SimpleCallback<Void> callback,
                                    long messageId) {
        Call<User> caller = proxy.changeMessageStatus(messageId, user.getId(), false);
        ProxyBuilder.callProxy(context, caller, returnedUser -> {
            user = returnedUser;
            callback.callback(null);
        });
    }


    // this method is used to update points.
    public void addPoints(Context context, ProxyBuilder.SimpleCallback<User> callback, int pointsEarned) {
        Call<User> getUserCaller = proxy.getUserById(user.getId());
        ProxyBuilder.callProxy(context, getUserCaller, targetUser -> {
            int totalPoints = targetUser.getTotalPointsEarned() + pointsEarned;
            int currentPoints = targetUser.getCurrentPoints() + pointsEarned;
            targetUser.setTotalPointsEarned(totalPoints);
            targetUser.setCurrentPoints(currentPoints);
            Call<User> editUserCaller = proxy.editUserWithId(targetUser.getId(), targetUser);
            ProxyBuilder.callProxy(context, editUserCaller, returnedUser -> {
                // Deserialize the custom object from the user:
                try {
                    Gamification customObjectFromServer = new ObjectMapper().readValue(returnedUser.getCustomJson(), Gamification.class);
                    returnedUser.setGamification(customObjectFromServer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                callback.callback(returnedUser);
            });
        });
    }

    // this method is used to change avatar.
    public void changeAvatar(Context context, ProxyBuilder.SimpleCallback<User> callback, int avatarId) {
        Call<User> getUserCaller = proxy.getUserById(user.getId());
        ProxyBuilder.callProxy(context, getUserCaller, targetUser -> {

                // this if condition is just a guard, it shouldn't get executed unless something else went wrong.
            if (targetUser.getCustomJson() == null) {
                targetUser.setCustomJson("{\"currentAvatar\":0,\"ownedAvatars\":[0]}");
            }

            // Deserialize the custom object from the user:
            try {
                Gamification customObjectFromServer = new ObjectMapper().readValue(targetUser.getCustomJson(), Gamification.class);

                // change avatar.
                customObjectFromServer.changeAvatar(avatarId);
                targetUser.setGamification(customObjectFromServer);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                // Convert custom object to a JSON string:
                String customAsJson = new ObjectMapper().writeValueAsString(user.getGamification());
                // Store JSON string into the user object, which will be sent to server.
                targetUser.setCustomJson(customAsJson);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }


            Call<User> editUserCaller = proxy.editUserWithId(targetUser.getId(), targetUser);
            ProxyBuilder.callProxy(context, editUserCaller, returnedUser -> {
                // Deserialize the custom object from the user:
                try {
                    Gamification customObjectFromServer = new ObjectMapper().readValue(returnedUser.getCustomJson(), Gamification.class);
                    returnedUser.setGamification(customObjectFromServer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                callback.callback(returnedUser);
            });
        });
    }

    // this method is used when user bought new avatar.
    public void buyAvatar(Context context, ProxyBuilder.SimpleCallback<User> callback, int avatarId, int price) {
        Call<User> getUserCaller = proxy.getUserById(user.getId());
        ProxyBuilder.callProxy(context, getUserCaller, targetUser -> {

            // this if condition is just a guard, it shouldn't get executed unless something else went wrong.
            if (targetUser.getCustomJson() == null) {
                targetUser.setCustomJson("{\"currentAvatar\":0,\"ownedAvatars\":[0]}");
            }

            // Deserialize the custom object from the user:
            try {
                Gamification customObjectFromServer = new ObjectMapper().readValue(targetUser.getCustomJson(), Gamification.class);

                // add new avatar.
                customObjectFromServer.addNewAvatar(avatarId);
                targetUser.setGamification(customObjectFromServer);
                int currentPoints = targetUser.getCurrentPoints();
                currentPoints = currentPoints - price;
                targetUser.setCurrentPoints(currentPoints);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                // Convert custom object to a JSON string:
                String customAsJson = new ObjectMapper().writeValueAsString(user.getGamification());
                // Store JSON string into the user object, which will be sent to server.
                targetUser.setCustomJson(customAsJson);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }


            Call<User> editUserCaller = proxy.editUserWithId(targetUser.getId(), targetUser);
            ProxyBuilder.callProxy(context, editUserCaller, returnedUser -> {
                // Deserialize the custom object from the user:
                try {
                    Gamification customObjectFromServer = new ObjectMapper().readValue(returnedUser.getCustomJson(), Gamification.class);
                    returnedUser.setGamification(customObjectFromServer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                callback.callback(returnedUser);
            });
        });
    }





    // TODO: we should use the getLocalUserId() instead of using this method.
    //get the private field user
    public User getPrivateFieldUser() {
        return user;
    }

}

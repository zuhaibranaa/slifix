package com.slifix.slifix;

public class DataManager {
    private static String phoneNumber;
    private static String authToken;
    private static String userName;

    public static String getUserEmail() {
        return userEmail;
    }

    public static void setUserEmail(String userEmail) {
        DataManager.userEmail = userEmail;
    }

    private static String userEmail;
    private static String userLatitude = "10",userLongitude = "5";
    DataManager(){
        authToken = phoneNumber = null;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        DataManager.userName = userName;
    }

    public static String getPhoneNumber() {
        return phoneNumber;
    }

    public static void setPhoneNumber(String phoneNumber) {
        DataManager.phoneNumber = phoneNumber;
    }

    public static String getAuthToken() {
        return authToken;
    }

    public static void setAuthToken(String authToken) {
        DataManager.authToken = authToken;
    }

    public static String getUserLatitude() {
        return userLatitude;
    }

    public static void setUserLatitude(String userLatitude) {
        DataManager.userLatitude = userLatitude;
    }

    public static String getUserLongitude() {
        return userLongitude;
    }

    public static void setUserLongitude(String userLongitude) {
        DataManager.userLongitude = userLongitude;
    }
}

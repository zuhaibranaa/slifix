package com.slifix.slifix;

public class DataManager {
    private static String phoneNumber;
    private static String authToken;
    DataManager(){
        authToken = phoneNumber = null;
    }
    public static String getPhoneNumber(){
        return phoneNumber;
    }
    public static String getAuth(){
        return authToken;
    }
    public static void setPhoneNumber(String number){
        phoneNumber = number;
    }
    public static void setAuthToken(String token){
        authToken = token;
    }

}

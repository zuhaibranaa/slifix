package com.slifix.slifix;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DataManager extends Service {
    private static String userImage,userID,itemsInCart,userLatitude,phoneNumber,userLongitude,authToken,userName,activeRestaurantId,bill,activeRestaurantName,userLocation,userEmail;
    public static String getItemsInCart() {
        return itemsInCart;
    }
    public static void setItemsInCart(String itemsInCart) {
        DataManager.itemsInCart = itemsInCart;
    }

    public static String getUserImage() {
        return userImage;
    }

    public static void setUserImage(String userImage) {
        DataManager.userImage = userImage;
    }

    public static String getUserID() {
        return userID;
    }

    public static void setUserID(String userID) {
        DataManager.userID = userID;
    }

    public static String getUserLocation() {
        return userLocation;
    }

    public static void setUserLocation(String userLocation) {
        DataManager.userLocation = userLocation;
    }

    public static void setBill(String bill) {
        DataManager.bill = bill;
    }

    public static String getBill() {
        return bill;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        authToken = phoneNumber = null;
        return START_STICKY;
    }
    public static String getActiveRestaurantName() {
        return activeRestaurantName;
    }

    public static void setActiveRestaurantName(String activeRestaurantName) {
        DataManager.activeRestaurantName = activeRestaurantName;
    }

    public static String getActiveRestaurantId() {
        return activeRestaurantId;
    }

    public static void setActiveRestaurantId(String activeRestaurantId) {
        DataManager.activeRestaurantId = activeRestaurantId;
    }

    public static String getUserEmail() {
        return userEmail;
    }

    public static void setUserEmail(String userEmail) {
        DataManager.userEmail = userEmail;
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

    @Override
    public void onDestroy() {
        Toast.makeText(this, "DataManager Destroyed", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }
}

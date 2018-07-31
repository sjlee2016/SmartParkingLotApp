package com.example.jisupark.firebaseloginapp;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by JisuPark on 2018-07-19.
 */

@IgnoreExtraProperties
public class FirebasePost {
    public String ID;
    public String name;
    public int alert;

    public FirebasePost(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }

    public FirebasePost(String ID, String name) {
        this.ID = ID;
        this.name = name;
    }

    public FirebasePost(int alert){
        this.alert = alert;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("User Email", ID);
        result.put("Name", name);
        return result;
    }

}
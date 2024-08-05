package com.dabakarov10.shoppinglist;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class User {
    public static String name;
    public static String email;
    public static String password;
    public static String phone;
    public static String user_UID;
    public static ArrayList<String> userEvents; // כאן אני אכניס את כל האירועים שיש לאותו משתמש ומכאן אני אקח אותם להציג לו

    public User() {
        this.name = null;
        this.email = null;
        this.password = null;
        this.phone = null;
        this.user_UID = null;
        this.userEvents = null;
    }

    public User(String name, String email, String password, String phone, String user_UID, ArrayList<String> userEvents) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.user_UID = user_UID;
        this.userEvents = userEvents;
    }

    public User(User copyUser) {
        this.name = copyUser.getName();
        this.email = copyUser.getEmail();
        this.password = copyUser.getPassword();
        this.phone = copyUser.getPhone();
        this.user_UID = copyUser.getUser_UID();
        this.userEvents = copyUser.getUserEvents();
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    /* Get && Set*/
    public  String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUser_UID() {
        return user_UID;
    }

    public void setUser_UID(String user_UID) {
        this.user_UID = user_UID;
    }

    public ArrayList<String> getUserEvents() {
        return userEvents;
    }

    public void setUserEvents(ArrayList<String> userEvents) {
        this.userEvents = userEvents;
    }


}

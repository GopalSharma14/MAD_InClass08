package com.example.inclass08;

import android.content.Intent;

public class User {
   String status,token,user_email,user_fname,user_lname,user_role,user_id;

    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "status='" + status + '\'' +
                ", token='" + token + '\'' +
                ", user_email='" + user_email + '\'' +
                ", user_fname='" + user_fname + '\'' +
                ", user_lname='" + user_lname + '\'' +
                ", user_role='" + user_role + '\'' +
                ", user_id='" + user_id + '\'' +
                '}';
    }
}
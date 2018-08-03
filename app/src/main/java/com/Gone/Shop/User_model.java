package com.Gone.Shop;

/**
 * Created by pappu on 7/11/17.
 */

public class User_model {
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    String name,last_message,time,userEmail;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    int count;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLast_message() {
        return last_message;
    }

    public void setLast_message(String last_message) {
        this.last_message = last_message;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

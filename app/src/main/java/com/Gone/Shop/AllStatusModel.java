package com.Gone.Shop;


public class AllStatusModel {
    String id;
    String email;
    String post;
    String date;
    String total_comment;

    String post_user_name;
    String post_user_image;





    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPostUserName() {
        return post_user_name;
    }

    public void setPostUserName(String post_user_name) {
        this.post_user_name = post_user_name;
    }

    public String getPostUserImage() {
        return post_user_image;
    }

    public void setPostUserImage(String post_user_image) {
        this.post_user_image = post_user_image;
    }

    public String getTotalComment() {
        return total_comment;
    }

    public void setTotalComment(String total_comment) {
        this.total_comment = total_comment;
    }


}

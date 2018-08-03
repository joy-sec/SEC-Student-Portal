package com.Gone.Shop;

/**
 * Created by joy on 4/21/17.
 */

public class AllCommentModel {
    String commentid;
    String comment;
    String postId;
    String commentuserEmail;
    String commentuserName;
    String commentuserImage;
    String commentDate;
    String commentuserRating;
    String postWritterEmail;


    public String getId() {
        return commentid;
    }

    public void setId(String commentid) {
        this.commentid = commentid;
    }


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPostID() {
        return postId;
    }

    public void setPostID(String postId) {
        this.postId = postId;
    }

    public String getUserEmail() {
        return commentuserEmail;
    }

    public void setUserEmail(String commentuserEmail) {
        this.commentuserEmail = commentuserEmail;
    }

    public String getUserName() {
        return commentuserName;
    }

    public void setUserName(String commentuserName) {
        this.commentuserName = commentuserName;
    }

    public String getUserImage() {
        return commentuserImage;
    }

    public void setUserImage(String commentuserImage) {
        this.commentuserImage = commentuserImage;
    }

    public String getUserRating() {
        return commentuserRating;
    }

    public void setUserRating(String commentuserRating) {
        this.commentuserRating = commentuserRating;
    }

    public String getCommentDate() {
        return commentDate;
    }
    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }
    public void setWritterEmail(String postWritterEmail) {
        this.postWritterEmail = postWritterEmail;
    }

    public String getWritterEmail() {
        return postWritterEmail;
    }




}

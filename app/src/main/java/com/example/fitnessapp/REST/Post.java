package com.example.fitnessapp.REST;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Post implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("description")
    private String description;
    @SerializedName("author")
    private String author;
    @SerializedName("publication_date")
    private String publicationDate;
    @SerializedName("video")
    private String videoFrame;
    @SerializedName("comments")
    private List<Comment> commentList;

    public List<Comment> getCommentList(){
        return this.commentList;
    }
    public void setCommentList(List<Comment>commentList){
        this.commentList = commentList;
    }

    public String getTitle(){
        return this.title;
    }
    public int getId(){return this.id;}
    public String getDescription(){
        return this.description;
    }
    public String getAuthor(){
        return this.author;
    }
    public String getPublicationDate(){
        return this.publicationDate;
    }
    public String getVideoFrame(){
        return this.videoFrame;
    }
}

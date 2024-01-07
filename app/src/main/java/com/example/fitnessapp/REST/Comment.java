package com.example.fitnessapp.REST;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Comment implements Serializable {
    @SerializedName("author")
    private String author;
    @SerializedName("publication_date")
    private String publication_date;
    @SerializedName("content")
    private String content;
    public Comment(String author, String publication_date, String content){
        this.author = author;
        this.publication_date = publication_date;
        this.content = content;
    }

    public String getAuthor(){
        return this.author;
    }
    public String getPublication_date(){
        return this.publication_date;
    }
    public String getContent(){
        return this.content;
    }
}

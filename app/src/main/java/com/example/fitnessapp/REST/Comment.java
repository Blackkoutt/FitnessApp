package com.example.fitnessapp.REST;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Comment implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("author")
    private String author;
    @SerializedName("publication_date")
    private String publication_date;
    @SerializedName("content")
    private String content;
    public Comment(int id, String author, String publication_date, String content){
        this.id = id;
        this.author = author;
        this.publication_date = publication_date;
        this.content = content;
    }
    public int getId() {return this.id; }

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

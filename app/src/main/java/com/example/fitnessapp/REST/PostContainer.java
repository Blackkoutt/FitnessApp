package com.example.fitnessapp.REST;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PostContainer {
    @SerializedName("posts")

    private List<Post> postList;

    public List<Post> getPostList() { return this.postList;}
    public void setPostList(List<Post> postList){this.postList = postList;}
}

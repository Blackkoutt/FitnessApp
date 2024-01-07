package com.example.fitnessapp.REST;

import androidx.room.Query;

import retrofit2.Call;
import retrofit2.http.GET;

public interface PostService {
    @GET("posts")
    Call<PostContainer> findPosts();
}

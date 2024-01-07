package com.example.fitnessapp.REST;

import androidx.room.Query;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PostService {
    @GET("posts")
    Call<PostContainer> findPosts();
    @PUT("posts/{postId}/comments")
    Call<Void> addComment(@Path("postId") int postId, @Body List<Comment> comments);
}

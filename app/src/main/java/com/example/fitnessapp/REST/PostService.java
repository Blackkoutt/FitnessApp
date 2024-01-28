package com.example.fitnessapp.REST;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

// Wykorzystywane metody HTTP do komunikacji z serwerem
public interface PostService {
    @GET("posts")
    Call<PostContainer> findPosts();
    @PUT("posts/{postId}/comments")
    Call<Void> addComment(@Path("postId") int postId, @Body List<Comment> comments);
    @DELETE("posts/{postId}/comments/{commentId}")
    Call<Void> deleteComment(@Path("postId") int postId, @Path("commentId") int commentId);
}

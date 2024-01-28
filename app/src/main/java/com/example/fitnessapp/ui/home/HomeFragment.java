package com.example.fitnessapp.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessapp.R;
import com.example.fitnessapp.REST.Comment;
import com.example.fitnessapp.REST.Post;
import com.example.fitnessapp.REST.PostContainer;
import com.example.fitnessapp.REST.PostService;
import com.example.fitnessapp.REST.RetrofitInstance;
import com.example.fitnessapp.databinding.FragmentHomeBinding;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private View view;
    private View activityRootView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        activityRootView = getActivity() != null ? getActivity().getWindow().getDecorView().getRootView() : null;
        fetchPostsData();
        return view;
    }
    private void fetchPostsData(){
        PostService postService = RetrofitInstance.getRetrofitInstance().create(PostService.class);
        Call<PostContainer> postApiCall = postService.findPosts();
        postApiCall.enqueue(new Callback<PostContainer>() {
            @Override
            public void onResponse(Call<PostContainer> call, Response<PostContainer> response) {
                if(response.body()!=null){
                    setupPostListView(response.body().getPostList());
                }
                response.body();
            }

            @Override
            public void onFailure(Call<PostContainer> call, Throwable t) {
                String errorMessage = "Error: " + t.getMessage();
                Log.e("API Error", errorMessage);

                Snackbar.make(activityRootView.findViewById(R.id.container), getString(R.string.error),
                  BaseTransientBottomBar.LENGTH_LONG).show();
                //Snackbar.make(view.findViewById(R.id.main_home_view), getString(R.string.error),
                //        BaseTransientBottomBar.LENGTH_LONG).show();
            }
        });
    }
    private void setupPostListView(List<Post> postList){
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        final PostAdapter adapter = new PostAdapter();
        adapter.setPosts(postList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
       // binding = null;
    }
    private class PostHolder extends RecyclerView.ViewHolder{
        private final TextView postTitleTextView;
        private final TextView postAuthorTextView;
        private final TextView postDescriptionTextView;
        private final TextView postPublicationDateTextView;
        private final WebView webView;
        private final TextInputEditText userName;
        private final TextInputEditText commentContent;
        private final MaterialButton buttonAddComment;
        private final RecyclerView commentRecyclerView;
        private CommentAdapter commentAdapter;
        private Post post;
        public PostHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.post_list_item, parent, false));

            postTitleTextView = itemView.findViewById(R.id.post_title);
            postDescriptionTextView = itemView.findViewById(R.id.post_description);
            postAuthorTextView = itemView.findViewById(R.id.post_author);
            postPublicationDateTextView = itemView.findViewById(R.id.post_pub_date);
            webView = itemView.findViewById(R.id.webView);
            commentRecyclerView = itemView.findViewById(R.id.comment_recyclerview);
            userName = itemView.findViewById(R.id.text_input_EditText_name);
            commentContent = itemView.findViewById(R.id.text_input_EditText_comment_content);

            commentAdapter = new CommentAdapter();
            buttonAddComment = itemView.findViewById(R.id.post_comment_button);
            buttonAddComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String newUserName = String.valueOf(userName.getText());
                    String newCommentContent = String.valueOf(commentContent.getText());
                    if(newUserName!=""&&newCommentContent!=""){
                        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                        String currentDate = sdf.format(new Date());
                        List<Comment> commentList = post.getCommentList();
                        int maxCommentId = Integer.MIN_VALUE;
                        if(commentList.size() == 0){
                            maxCommentId = 0;
                        }
                        else{
                            for (Comment comment : commentList) {
                                if (comment.getId() > maxCommentId) {
                                    maxCommentId = comment.getId();
                                }
                            }
                            maxCommentId+=1;
                        }

                        Comment newComment = new Comment(maxCommentId,newUserName, currentDate, newCommentContent);
                        commentList.add(0,newComment);
                        addCommentToPost(post, commentList, commentAdapter);

                        userName.setText("");
                        commentContent.setText("");
                        userName.clearFocus();
                        commentContent.clearFocus();
                        InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                }
            });
        }
        public void bind(Post post){
            this.post = post;
            if(post != null){
                postTitleTextView.setText(post.getTitle());
                postDescriptionTextView.setText(post.getDescription());
                postAuthorTextView.setText(getString(R.string.author)+" "+post.getAuthor());
                webView.loadData(post.getVideoFrame(), "text/html","utf-8");
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setWebChromeClient(new WebChromeClient());
                postPublicationDateTextView.setText(getString(R.string.publication_date)+" "+post.getPublicationDate());

                //RecyclerView commentRecyclerView = view.findViewById(R.id.comment_recyclerview);
                //final
                commentAdapter = new CommentAdapter();
                commentAdapter.setComments(post);
                commentRecyclerView.setAdapter(commentAdapter);
                commentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


            }
        }
        private void addCommentToPost(Post post, List<Comment> comments, CommentAdapter adapter) {
            PostService postService = RetrofitInstance.getRetrofitInstance().create(PostService.class);

            Call<Void> addCommentCall = postService.addComment(post.getId(), comments);
            addCommentCall.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Log.d("API Success", "Comment added successfully");
                        updateCommentView(post, comments, adapter);
                    } else {
                        Log.e("API Error", "Error adding comment: " + response.message());
                        Snackbar.make(view.findViewById(R.id.main_home_view), getString(R.string.error),
                                BaseTransientBottomBar.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    String errorMessage = "Error: " + t.getMessage();
                    Log.e("API Error", errorMessage);
                    Snackbar.make(activityRootView.findViewById(R.id.container), getString(R.string.error),
                            BaseTransientBottomBar.LENGTH_LONG).show();
                    //Snackbar.make(view.findViewById(R.id.main_home_view), getString(R.string.error),
                    //     BaseTransientBottomBar.LENGTH_LONG).show();
                }
            });
        }
        private void updateCommentView(Post post, List<Comment> comments, CommentAdapter adapter){
            post.setCommentList(comments);
            //commentAdapter.setComments(post);
            commentAdapter.notifyDataSetChanged();
        }
    }
    private class PostAdapter extends RecyclerView.Adapter<PostHolder>{
        private List<Post> posts;
        @NonNull
        @Override
        public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new PostHolder(getLayoutInflater(), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull PostHolder holder, int position) {
            if(posts != null){
                Post post = posts.get(position);
                holder.bind(post);
            }
            else{
                Log.d("MainActivity", "No books");
            }
        }

        @Override
        public int getItemCount() {
            if(posts != null){
                return posts.size();
            }
            else{
                return 0;
            }
        }
        void setPosts(List<Post> posts){
            this.posts = posts;
            notifyDataSetChanged();
        }
    }



    private class CommentAdapter extends RecyclerView.Adapter<CommentHolder>{
        private List<Comment> comments;
        private Post post;
        @NonNull
        @Override
        public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new CommentHolder(getLayoutInflater(), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull CommentHolder holder, int position) {
            if(comments != null){
                Comment comment = comments.get(position);
                holder.bind(post, comment, this);
            }
        }

        @Override
        public int getItemCount() {
            if(comments != null){
                return comments.size();
            }
            else{
                return 0;
            }
        }
        void setComments(Post post){
            this.post = post;
            this.comments = post.getCommentList();
            notifyDataSetChanged();
        }
    }
    private class CommentHolder extends RecyclerView.ViewHolder{
        private final TextView commentAuthorTextView;
        private final TextView commentPublicationDateTextView;
        private final TextView commentContentTextView;
        private final ImageView deleteComment;
        private Post post;
        private CommentAdapter commentAdapter;
        private Comment comment;
        public CommentHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.comment_list_item, parent, false));

            commentAuthorTextView = itemView.findViewById(R.id.comment_author);
            commentPublicationDateTextView = itemView.findViewById(R.id.comment_publication_date);
            commentContentTextView = itemView.findViewById(R.id.comment_content);
            deleteComment = itemView.findViewById(R.id.delete_icon);

            deleteComment.setOnClickListener(this::deleteComment);
        }

        private void deleteComment(View view) {
            if (comment != null && post != null) {
                String message = getResources().getString(R.string.delete_comment, comment.getAuthor());
                AlertDialog.Builder builder = new AlertDialog.Builder((getActivity()));
                builder.setTitle(getResources().getString(R.string.delete_comment, comment.getAuthor()));
                builder.setPositiveButton(R.string.yes_button, this::deleteCommentOnServer);
                builder.setNegativeButton(R.string.no_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builder.create();
                builder.show();
            }
        }

        private void deleteCommentOnServer(DialogInterface dialogInterface, int i) {
            PostService postService = RetrofitInstance.getRetrofitInstance().create(PostService.class);

            int postId = post.getId();
            int commentId = comment.getId(); // Tutaj załóżmy, że komentarz ma pole 'id'

            Call<Void> deleteCommentCall = postService.deleteComment(postId, commentId);
            deleteCommentCall.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        // Usunięcie komentarza z lokalnej listy
                        int position = post.getCommentList().indexOf(comment);
                        if (position != -1) {
                            post.getCommentList().remove(position);
                            commentAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Log.e("API Error", "Error deleting comment: " + response.message());
                        Snackbar.make(view.findViewById(R.id.main_home_view), getString(R.string.error),
                                BaseTransientBottomBar.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    String errorMessage = "Error: " + t.getMessage();
                    Log.e("API Error", errorMessage);
                    Snackbar.make(activityRootView.findViewById(R.id.container), getString(R.string.error),
                            BaseTransientBottomBar.LENGTH_LONG).show();
                }
            });
        }

        public void bind(Post post, Comment comment, CommentAdapter commentAdapter){
            this.post = post;
            this.comment = comment;
            this.commentAdapter = commentAdapter;
            if(comment != null){
                commentAuthorTextView.setText(comment.getAuthor());
                commentPublicationDateTextView.setText(comment.getPublication_date());
                commentContentTextView.setText(comment.getContent());
            }
        }
    }
}
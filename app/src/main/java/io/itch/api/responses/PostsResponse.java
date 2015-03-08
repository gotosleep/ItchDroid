package io.itch.api.responses;

import io.itch.models.tumblr.Post;

import java.util.List;

public class PostsResponse extends TumblrResponse {

    private Response response;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public static final class Response {
        private List<Post> posts;

        public List<Post> getPosts() {
            return posts;
        }

        public void setPosts(List<Post> posts) {
            this.posts = posts;
        }

    }

}

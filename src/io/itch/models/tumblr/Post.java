package io.itch.models.tumblr;

import android.annotation.SuppressLint;

public class Post {

    public static enum Type {
        UNKNOWN,
        TEXT,
        LINK,
        VIDEO,
        AUDIO
    }

    private String title;
    private String body;
    private Long id;
    private String type;
    private String url;
    private String description;
    private String caption;
    private Type postType;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    @SuppressLint("DefaultLocale")
    public Type getPostType() {
        if (this.postType == null) {
            try {
                this.postType = Type.valueOf(this.getType().toUpperCase());
            } catch (Exception e) {
                this.postType = Type.UNKNOWN;
            }
        }
        return this.postType;
    }
}

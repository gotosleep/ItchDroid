package io.itch.models;

import java.util.Date;

public class Key {

    private Date updateAt;
    private String source;
    private Long userId;
    private String key;
    private Date createdAt;

    private Key(Date updateAt, String source, Long userId, String key, Date createdAt) {
        super();
        this.updateAt = updateAt;
        this.source = source;
        this.userId = userId;
        this.key = key;
        this.createdAt = createdAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

}

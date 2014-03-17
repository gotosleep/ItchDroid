package io.itch.models;

import java.util.Date;
import java.util.List;

public class Game {

    private Boolean pOSX;
    private Boolean pAndroid;
    private Boolean pWindows;
    private Boolean pLinux;
    private Float minPrice;
    private Long id;
    private Boolean published;
    private Long viewsCount;
    private Date createdAt;
    private Date publishedAt;
    private Long downloadsCount;
    private String title;
    private String url;
    private Long purchasesCount;
    private String shortText;
    private String type;
    private String coverUrl;
    private List<Earning> earnings;

    public Boolean getpOSX() {
        return pOSX;
    }

    public void setpOSX(Boolean pOSX) {
        this.pOSX = pOSX;
    }

    public Boolean getpAndroid() {
        return pAndroid;
    }

    public void setpAndroid(Boolean pAndroid) {
        this.pAndroid = pAndroid;
    }

    public Boolean getpWindows() {
        return pWindows;
    }

    public void setpWindows(Boolean pWindows) {
        this.pWindows = pWindows;
    }

    public Boolean getpLinux() {
        return pLinux;
    }

    public void setpLinux(Boolean pLinux) {
        this.pLinux = pLinux;
    }

    public Float getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Float minPrice) {
        this.minPrice = minPrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getPublished() {
        return published;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    public Long getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(Long viewsCount) {
        this.viewsCount = viewsCount;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Date publishedAt) {
        this.publishedAt = publishedAt;
    }

    public Long getDownloadsCount() {
        return downloadsCount;
    }

    public void setDownloadsCount(Long downloadCount) {
        this.downloadsCount = downloadCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getPurchasesCount() {
        return purchasesCount;
    }

    public void setPurchasesCount(Long purchasesCount) {
        this.purchasesCount = purchasesCount;
    }

    public String getShortText() {
        return shortText;
    }

    public void setShortText(String shortText) {
        this.shortText = shortText;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public List<Earning> getEarnings() {
        return earnings;
    }

    public void setEarnings(List<Earning> earnings) {
        this.earnings = earnings;
    }

    public Earning getDefaultEarnings() {
        Earning result = null;
        if (this.earnings != null && this.earnings.size() > 0) {
            for (Earning e : this.earnings) {
                if ("USD".equalsIgnoreCase(e.getCurrency())) {
                    result = e;
                    break;
                } else if (result == null) {
                    result = e;
                }
            }
        }
        return result;
    }
}

package io.itch.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class Game implements Parcelable {

    private static final int FIELD_PUBLISHED_AT = 1 << 0;

    private boolean pOsx;
    private boolean pAndroid;
    private boolean pWindows;
    private boolean pLinux;
    private Float minPrice;
    private Long id;
    private boolean published;
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

    public boolean getPOsx() {
        return pOsx;
    }

    public void setPOsx(boolean pOSX) {
        this.pOsx = pOSX;
    }

    public boolean getPAndroid() {
        return pAndroid;
    }

    public void setPAndroid(boolean pAndroid) {
        this.pAndroid = pAndroid;
    }

    public boolean getPWindows() {
        return pWindows;
    }

    public void setPWindows(boolean pWindows) {
        this.pWindows = pWindows;
    }

    public boolean getPLinux() {
        return pLinux;
    }

    public void setPLinux(boolean pLinux) {
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

    public boolean getPublished() {
        return published;
    }

    public void setPublished(boolean published) {
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
                if (result == null || e.getAmount() > result.getAmount()) {
                    result = e;
                }
            }
        }
        return result;
    }

    @Override
    public int describeContents() {
        int result = 0;
        if (this.getPublishedAt() != null) {
            result |= FIELD_PUBLISHED_AT;
        }
        return result;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        int contents = describeContents();
        parcel.writeInt(contents);
        parcel.writeValue(getPOsx());
        parcel.writeValue(getPWindows());
        parcel.writeValue(getPLinux());
        parcel.writeValue(getPAndroid());
        parcel.writeFloat(getMinPrice());
        parcel.writeLong(getId());
        parcel.writeValue(getPublished());
        parcel.writeLong(getViewsCount());
        parcel.writeLong(getCreatedAt().getTime());
        if ((contents & FIELD_PUBLISHED_AT) == FIELD_PUBLISHED_AT) {
            parcel.writeLong(getPublishedAt().getTime());
        }
        parcel.writeLong(getDownloadsCount());
        parcel.writeString(getTitle());
        parcel.writeString(getUrl());
        parcel.writeLong(getPurchasesCount());
        parcel.writeString(getShortText());
        parcel.writeString(getType());
        parcel.writeString(getCoverUrl());
        parcel.writeTypedList(getEarnings());
    }

    public static final Parcelable.Creator<Game> CREATOR = new Parcelable.Creator<Game>() {

        @Override
        public Game createFromParcel(Parcel source) {
            Game result = new Game();
            int contents = source.readInt();
            result.setPOsx((Boolean) source.readValue(null));
            result.setPWindows((Boolean) source.readValue(null));
            result.setPLinux((Boolean) source.readValue(null));
            result.setPAndroid((Boolean) source.readValue(null));
            result.setMinPrice(source.readFloat());
            result.setId(source.readLong());
            result.setPublished((Boolean) source.readValue(null));
            result.setViewsCount(source.readLong());
            result.setCreatedAt(dateFrom(source.readLong()));
            if ((contents & FIELD_PUBLISHED_AT) == FIELD_PUBLISHED_AT) {
                result.setPublishedAt(dateFrom(source.readLong()));
            }
            result.setDownloadsCount(source.readLong());
            result.setTitle(source.readString());
            result.setUrl(source.readString());
            result.setPurchasesCount(source.readLong());
            result.setShortText(source.readString());
            result.setType(source.readString());
            result.setCoverUrl(source.readString());

            List<Earning> earnings = new ArrayList<Earning>();
            source.readTypedList(earnings, Earning.CREATOR);
            result.setEarnings(earnings);

            return result;
        }

        private Date dateFrom(Long time) {
            Date result = new Date();
            result.setTime(time);
            return result;
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };
}

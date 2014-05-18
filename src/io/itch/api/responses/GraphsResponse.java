package io.itch.api.responses;

import io.itch.models.GraphPoint;

import java.util.List;

public class GraphsResponse {

    private List<GraphPoint> purchases;
    private List<GraphPoint> views;
    private List<GraphPoint> downloads;

    public List<GraphPoint> getPurchases() {
        return purchases;
    }

    public void setPurchases(List<GraphPoint> purchases) {
        this.purchases = purchases;
    }

    public List<GraphPoint> getViews() {
        return views;
    }

    public void setViews(List<GraphPoint> views) {
        this.views = views;
    }

    public List<GraphPoint> getDownloads() {
        return downloads;
    }

    public void setDownloads(List<GraphPoint> downloads) {
        this.downloads = downloads;
    }

}

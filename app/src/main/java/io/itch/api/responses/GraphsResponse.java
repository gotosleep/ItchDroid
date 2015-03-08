package io.itch.api.responses;

import io.itch.models.GraphPoint;

import java.util.List;

public class GraphsResponse {

    private List<GraphPoint> purchases;
    private List<GraphPoint> views;
    private List<GraphPoint> downloads;
    private Integer max;

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

    public boolean hasData() {
        return this.purchases != null || this.views != null || this.downloads != null;
    }

    public int getMax() {
        if (this.max == null && this.hasData()) {
            this.max = 0;
            this.max = Math.max(this.max, getMax(this.views));
            this.max = Math.max(this.max, getMax(this.purchases));
            this.max = Math.max(this.max, getMax(this.downloads));
        }
        return this.max;
    }

    private int getMax(List<GraphPoint> points) {
        int max = 0;
        if (points != null) {
            for (GraphPoint p : points) {
                if (p.getCount() > max) {
                    max = p.getCount();
                }
            }
        }
        return max;
    }

}

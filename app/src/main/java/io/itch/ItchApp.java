package io.itch;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

public class ItchApp extends Application {

    private static final Object LOCK = new Object();
    private Tracker tracker;

    public Tracker getTracker() {
        if (this.tracker == null) {
            synchronized (LOCK) {
                if (this.tracker == null) {
                    this.tracker = GoogleAnalytics.getInstance(this).newTracker(R.xml.ga);
                }
            }
        }
        return this.tracker;
    }
}

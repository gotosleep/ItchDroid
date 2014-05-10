package io.itch;

import io.itch.api.ItchApiClient;
import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

public class ItchApp extends Application {

    private static final Object LOCK = new Object();
    private Tracker tracker;

    @Override
    public void onCreate() {
        super.onCreate();
        ItchApiClient.loadKeyStore(this);
    }

    public Tracker getTracker() {
        if (this.tracker == null) {
            synchronized (LOCK) {
                if (this.tracker == null) {
                    GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
                    this.tracker = analytics.newTracker(R.xml.ga);
                }
            }
        }
        return this.tracker;
    }
}

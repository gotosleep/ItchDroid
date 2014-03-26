package io.itch;

import io.itch.api.ItchApiClient;
import android.app.Application;

public class ItchApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ItchApiClient.loadKeyStore(this);
    }

}

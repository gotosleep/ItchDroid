package io.itch.compat;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ActionBarHelper {

    public static void hideActionBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            activity.getActionBar().hide();
        }
    }

}

package io.itch.views;

import android.content.Context;

public class ViewHelper {

    public static int dpToPixels(Context context, int dp) {
        // Get the screen's density scale
        final float scale = context.getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (dp * scale + 0.5f);
    }

}

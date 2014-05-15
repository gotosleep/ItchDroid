package io.itch.views;

import io.itch.Constants;
import io.itch.R;
import io.itch.models.tumblr.Post;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.TextView;

public class PostViewHelper {

    private static final String PREF_LAST_VIEWED_POST = "io.itch.news.latest.id";

    public static void populateView(Context context, View result, Post post) {
        TextView text = (TextView) result.findViewById(R.id.textViewNewsTitle);
        if (text != null) {
            text.setText(post.getTitle());
        }
        text = (TextView) result.findViewById(R.id.textViewNewsBody);
        if (text != null) {
            Spanned body = null;
            if ("text".equals(post.getType())) {
                body = Html.fromHtml(post.getBody());
            } else if ("link".equals(post.getType())) {
                body = Html.fromHtml(post.getDescription());
            }
            text.setText(body);
        }
    }

    public static boolean hasBeenSeen(Context context, Post post) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREF_STORE, Context.MODE_PRIVATE);
        long last = prefs.getLong(PREF_LAST_VIEWED_POST, 0);
        return (post.getId() == last);
    }

    public static void setHasBeenSeen(Context context, Post post) {
        Editor edit = context.getSharedPreferences(Constants.PREF_STORE, Context.MODE_PRIVATE).edit();
        edit.putLong(PREF_LAST_VIEWED_POST, post.getId());
        edit.commit();
    }
}

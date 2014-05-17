package io.itch.views;

import io.itch.Constants;
import io.itch.R;
import io.itch.models.tumblr.Post;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

public class PostViewHelper {

    private static final String PREF_LAST_VIEWED_POST = "io.itch.news.latest.id";

    public static void populateView(Context context, View result, Post post) {
        String titleText = null;
        String bodyText = null;

        switch (post.getPostType()) {
        case TEXT:
            titleText = post.getTitle();
            bodyText = post.getBody();
            break;
        case LINK:
            titleText = post.getTitle();
            bodyText = post.getDescription();
            break;
        case AUDIO:
            titleText = post.getCaption();
            break;
        case VIDEO:
            titleText = post.getCaption();
            break;
        default:
            break;
        }

        TextView titleView = (TextView) result.findViewById(R.id.textViewNewsTitle);
        setText(titleView, titleText, false);

        TextView bodyView = (TextView) result.findViewById(R.id.textViewNewsBody);
        setText(bodyView, bodyText, true);
    }

    private static void setText(TextView view, String text, boolean keepHtml) {
        CharSequence displayText = null;
        if (text != null) {
            displayText = keepHtml ? Html.fromHtml(text) : Html.fromHtml(text).toString();
        }
        if (view != null) {
            if (displayText != null && !"".equals(displayText)) {
                view.setVisibility(View.VISIBLE);
                view.setText(displayText);
            } else {
                view.setVisibility(View.GONE);
            }
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

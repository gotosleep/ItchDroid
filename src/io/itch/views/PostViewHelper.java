package io.itch.views;

import io.itch.R;
import io.itch.models.tumblr.Post;
import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.TextView;

public class PostViewHelper {

    public static void populateView(Context context, View result, Post post) {
        TextView text = (TextView) result.findViewById(R.id.textViewNewsTitle);
        text.setText(post.getTitle());
        text = (TextView) result.findViewById(R.id.textViewNewsBody);
        Spanned body = null;
        if ("text".equals(post.getType())) {
            body = Html.fromHtml(post.getBody());
        } else if ("link".equals(post.getType())) {
            body = Html.fromHtml(post.getDescription());
        }
        text.setText(body);
    }
}

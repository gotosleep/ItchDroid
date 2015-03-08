package io.itch.views;

import io.itch.R;
import io.itch.models.Earning;
import io.itch.models.Game;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class GameViewHelper {

    private static final DateFormat DATE_FORMAT = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT);
    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance();

    public static void populateBasics(Context context, View result, Game game) {
        TextView title = (TextView) result.findViewById(R.id.textViewGameTitle);
        if (title != null) {
            title.setText(game.getTitle());
        }

        TextView views = (TextView) result.findViewById(R.id.textViewViewsValue);
        if (views != null) {
            views.setText("" + game.getViewsCount());
        }

        TextView purchases = (TextView) result.findViewById(R.id.textViewPurchasesValue);
        if (purchases != null) {
            purchases.setText("" + game.getPurchasesCount());
        }

        TextView downloads = (TextView) result.findViewById(R.id.textViewDownloadsValue);
        if (downloads != null) {
            downloads.setText("" + game.getDownloadsCount());
        }

        ImageView cover = (ImageView) result.findViewById(R.id.imageViewGameImage);
        if (cover != null) {
            if (game.getCoverUrl() != null) {
                Picasso.with(context).load(game.getCoverUrl()).into(cover);
            } else {
                cover.setImageResource(R.drawable.ic_launcher);
            }
        }

        TextView description = (TextView) result.findViewById(R.id.textViewGameDescription);
        if (description != null) {
            if (game.getShortText() != null && game.getShortText().length() > 0) {
                description.setVisibility(View.VISIBLE);
                description.setText(game.getShortText());
            } else {
                description.setVisibility(View.GONE);
            }
        }
    }

    public static void showAllFields(Context context, View result, Game game) {
        showPublished(context, result, game);
        showEarnings(context, result, game);
        populateGameField(context, result, R.string.game_created_at_label, DATE_FORMAT.format(game.getCreatedAt()));
        populateGameField(context, result, R.string.game_type_label, game.getType());
        showPlatforms(context, result, game);
    }

    public static void showPlatforms(Context context, View result, Game game) {
        // StringBuilder platforms = new StringBuilder();
        List<String> platforms = new ArrayList<String>(10);
        if (game.getPAndroid()) {
            platforms.add("Android");
        }
        if (game.getPWindows()) {
            platforms.add("Windows");
        }
        if (game.getPOsx()) {
            platforms.add("OSX");
        }
        if (game.getPLinux()) {
            platforms.add("Linux");
        }

        if (platforms.size() > 0) {
            StringBuilder value = new StringBuilder();
            Iterator<String> iter = platforms.iterator();
            while (iter.hasNext()) {
                value.append(iter.next());
                if (iter.hasNext()) {
                    value.append(", ");
                }
            }
            populateGameField(context, result, R.string.game_platforms_label, value.toString());
        }
    }

    public static void showPublished(Context context, View result, Game game) {
        if (game.getPublished()) {
            populateGameField(context, result, R.string.game_published_label, DATE_FORMAT.format(game.getPublishedAt()));
        } else {
            populateGameField(context, result, R.string.game_published_label,
                    context.getString(R.string.game_published_not_value));
        }
    }

    public static void showEarnings(Context context, View result, Game game) {
        Earning earnings = game.getDefaultEarnings();
        if (earnings != null) {
            populateGameField(context, result, R.string.game_earnings_label, earnings.getAmountFormatted());
        } else {
            populateGameField(context, result, R.string.game_earnings_label, CURRENCY_FORMAT.format(0));
        }
    }

    private static void populateGameField(Context context, View result, int label, String value) {
        if (value == null) {
            return;
        }
        LinearLayout group = (LinearLayout) result.findViewById(R.id.viewGroupFields);
        if (group != null) {
            View field = LayoutInflater.from(context).inflate(R.layout.partial_game_field, null);
            TextView labelView = (TextView) field.findViewById(R.id.textViewGameFieldLabel);
            if (labelView != null) {
                labelView.setText(label);
            }

            TextView valueView = (TextView) field.findViewById(R.id.textViewGameFieldValue);
            if (valueView != null) {
                valueView.setText(value);
            }

            group.addView(field);
        }
    }
}

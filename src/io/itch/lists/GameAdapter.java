package io.itch.lists;

import io.itch.R;
import io.itch.models.Game;

import java.text.DateFormat;
import java.text.NumberFormat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class GameAdapter extends ArrayAdapter<Game> {

    public GameAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Game game = this.getItem(position);
        if (game == null) {
            return null;
        }

        View result = null;
        if (convertView != null) {
            result = convertView.findViewById(R.id.listItemGame);
        }

        if (result == null) {
            result = LayoutInflater.from(this.getContext()).inflate(R.layout.list_item_game, null);
        }

        if (result != null) {
            DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT);
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

            TextView title = (TextView) result.findViewById(R.id.textViewGameTitle);
            title.setText(game.getTitle());

            TextView views = (TextView) result.findViewById(R.id.textViewViewsValue);
            views.setText("" + game.getViewsCount());

            TextView purchases = (TextView) result.findViewById(R.id.textViewPurchasesValue);
            purchases.setText("" + game.getPurchasesCount());

            TextView downloads = (TextView) result.findViewById(R.id.textViewDownloadsValue);
            downloads.setText("" + game.getDownloadsCount());

            TextView published = (TextView) result.findViewById(R.id.textViewPublishedValue);
            if (game.getPublished()) {
                published.setText(df.format(game.getPublishedAt()));
            } else {
                published.setText(R.string.game_published_not_value);
            }

            ImageView cover = (ImageView) result.findViewById(R.id.imageViewGameImage);
            if (game.getCoverUrl() != null) {
                Picasso.with(getContext()).load(game.getCoverUrl()).into(cover);
            } else {
                // cover.setImageBitmap(null);
            }

            TextView earnings = (TextView) result.findViewById(R.id.textViewEarningsValue);
            earnings.setText(currencyFormat.format(0));
        }
        return result;
    }
}

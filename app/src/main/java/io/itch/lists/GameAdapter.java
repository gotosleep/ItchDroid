package io.itch.lists;

import io.itch.R;
import io.itch.models.Game;
import io.itch.views.GameViewHelper;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

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
            LinearLayout fields = (LinearLayout) result.findViewById(R.id.viewGroupFields);
            if (fields != null) {
                // when recycling a view, need to make sure to remove all fields because they
                // are created dynamically
                fields.removeAllViews();
            }
        }

        if (result == null) {
            result = LayoutInflater.from(this.getContext()).inflate(R.layout.list_item_game, null);
        }

        if (result != null) {
            GameViewHelper.populateBasics(getContext(), result, game);
            GameViewHelper.showPublished(getContext(), result, game);
            GameViewHelper.showEarnings(getContext(), result, game);
        }
        return result;
    }
}

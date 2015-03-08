package io.itch.activities;

import io.itch.Extras;
import io.itch.R;
import io.itch.models.Game;
import io.itch.views.GameViewHelper;
import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ShareActionProvider;

public class GameActivity extends BaseActivity {

    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.game = (Game) getIntent().getParcelableExtra(Extras.EXTRA_GAME);
        if (this.game == null) {
            return;
        }

        setContentView(R.layout.activity_game);
        ActionBar actionBar = getActionBar();
        actionBar.setTitle(game.getTitle());
        actionBar.setDisplayHomeAsUpEnabled(true);
        GameViewHelper.populateBasics(this, findViewById(android.R.id.content), game);
        GameViewHelper.showAllFields(this, findViewById(android.R.id.content), game);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.game, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_item_share);
        // Fetch and store ShareActionProvider
        ShareActionProvider provider = (ShareActionProvider) item.getActionProvider();
        if (provider != null) {

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, game.getTitle() + " " + game.getUrl());
            sendIntent.setType("text/plain");
            provider.setShareIntent(sendIntent);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_item_open:
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(game.getUrl()));
            startActivity(i);
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected String getScreenPath() {
        return "Game";
    }

}

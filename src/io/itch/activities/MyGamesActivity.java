package io.itch.activities;

import io.itch.Extras;
import io.itch.R;
import io.itch.R.id;
import io.itch.api.ItchApi;
import io.itch.api.ItchApiClient;
import io.itch.api.responses.GamesResponse;
import io.itch.authentication.SessionHelper;
import io.itch.authentication.SessionHelper.SessionCallback;
import io.itch.lists.GameAdapter;
import io.itch.models.Game;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MyGamesActivity extends BaseActivity {

    private ListView gamesList;
    private ArrayAdapter<Game> gamesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_games);
        this.gamesList = (ListView) findViewById(id.listViewGames);
        this.gamesAdapter = new GameAdapter(this, R.layout.list_item_game);
        this.gamesList.setAdapter(this.gamesAdapter);
        this.gamesList.setEmptyView(getEmptyView());
        this.gamesList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> list, View item, int position, long id) {
                Game game = gamesAdapter.getItem(position);
                if (game != null) {
                    Intent i = new Intent(MyGamesActivity.this, GameActivity.class);
                    i.putExtra(Extras.EXTRA_GAME, game);
                    // Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(game.getUrl()));
                    startActivity(i);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.updateGames();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_games, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.action_login:
            // SessionHelper.getInstance().login(this);
            break;
        case R.id.action_logout:
            SessionHelper.getInstance().logout(this, new SessionCallback() {

                @Override
                public void onSuccess() {
                    super.onSuccess();
                    startActivity(new Intent(MyGamesActivity.this, ItchActivity.class));
                    finish();
                }

            });
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (SessionHelper.getInstance().isLoggedIn()) {
            menu.findItem(R.id.action_login).setVisible(false);
            menu.findItem(R.id.action_logout).setVisible(true);
        } else {
            menu.findItem(R.id.action_login).setVisible(true);
            menu.findItem(R.id.action_logout).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public int getEmptyViewMessageId() {
        return R.string.my_games_activity_empty;
    }

    private void updateGames() {
        ItchApi api = ItchApiClient.getClient();
        api.listMyGames(new Callback<GamesResponse>() {

            @Override
            public void success(GamesResponse result, Response arg1) {
                if (gamesAdapter != null && result != null && result.getGames() != null) {
                    gamesAdapter.clear();
                    for (Game game : result.getGames()) {
                        gamesAdapter.add(game);
                    }
                }
            }

            @Override
            public void failure(RetrofitError e) {
                Log.e("Itch", "Failed to retrieve games", e);
            }
        });
    }

}

package io.itch.activities;

import io.itch.Extras;
import io.itch.R;
import io.itch.R.id;
import io.itch.api.ItchApi;
import io.itch.api.ItchApiClient;
import io.itch.api.TumblrApi;
import io.itch.api.TumblrApiClient;
import io.itch.api.responses.GamesResponse;
import io.itch.api.responses.GraphsResponse;
import io.itch.api.responses.PostsResponse;
import io.itch.authentication.SessionHelper;
import io.itch.authentication.SessionHelper.SessionCallback;
import io.itch.lists.GameAdapter;
import io.itch.models.Game;
import io.itch.models.tumblr.Post;
import io.itch.views.GraphHelper;
import io.itch.views.PostViewHelper;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LineGraphView;

public class MyGamesActivity extends BaseActivity {

    private ListView gamesList;
    private ArrayAdapter<Game> gamesAdapter;
    private View header;
    private ProgressBar progress;
    private Post latestNews;
    private boolean preventCollapse;
    private GraphView graphView;
    private ViewGroup graphContainer;
    private static final int GRAPH_DAYS = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_games);
        this.progress = (ProgressBar) findViewById(R.id.progressBarLoading);
        this.gamesList = (ListView) findViewById(id.listViewGames);
        this.gamesAdapter = new GameAdapter(this, R.layout.list_item_game);
        loadNewsView(null);
        loadGraphView();
        this.gamesList.setAdapter(this.gamesAdapter);
        this.gamesList.setEmptyView(getEmptyView());
        this.gamesList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> list, View item, int position, long id) {
                Integer headerCount = gamesList.getHeaderViewsCount();
                if (headerCount <= 0 || position >= headerCount) {
                    position -= headerCount;
                    Game game = gamesAdapter.getItem(position);
                    if (game != null) {
                        Intent i = new Intent(MyGamesActivity.this, GameActivity.class);
                        i.putExtra(Extras.EXTRA_GAME, game);
                        startActivity(i);
                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.updateNews();
        this.updateGraphs();
        this.updateGames();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Post post = this.getLatestNews();
        if (post != null && !PostViewHelper.hasBeenSeen(this, post)) {
            this.setPreventCollapse(true);
            PostViewHelper.setHasBeenSeen(this, post);
        }
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

    public void onNewsClick(View v) {
        Intent i = new Intent(MyGamesActivity.this, NewsActivity.class);
        startActivity(i);
        setPreventCollapse(false);
    }

    private void updateGames() {
        if (gamesAdapter.getCount() == 0) {
            this.progress.setVisibility(View.VISIBLE);
        }
        ItchApi api = ItchApiClient.getClient();
        api.listMyGames(new Callback<GamesResponse>() {

            @Override
            public void success(GamesResponse result, Response arg1) {
                if (gamesAdapter != null && result != null && result.getGames() != null) {
                    ScrollPosition p = preserveScroll(gamesList);
                    gamesAdapter.clear();
                    gamesAdapter.addAll(result.getGames());
                    gamesAdapter.notifyDataSetChanged();
                    progress.setVisibility(View.GONE);
                    restoreScroll(gamesList, p);
                }
            }

            @Override
            public void failure(RetrofitError e) {
                Log.e("Itch", "Failed to retrieve games", e);
            }
        });
    }

    private void updateGraphs() {
        ItchApi api = ItchApiClient.getClient();
        api.listGraphs(GRAPH_DAYS, new Callback<GraphsResponse>() {

            @Override
            public void success(GraphsResponse result, Response response) {
                graphView.removeAllSeries();
                if (result.hasData()) {
                    getGraphContainer().setVisibility(View.VISIBLE);
                    GraphHelper.configureYAxis(graphView, result.getMax(), 4);
                    if (result.getViews() != null) {
                        graphView.addSeries(GraphHelper.generateSeries(MyGamesActivity.this, "Views",
                                result.getViews(),
                                GRAPH_DAYS,
                                Color.argb(255, 255, 128, 128)));
                    }
                    if (result.getPurchases() != null) {
                        graphView.addSeries(GraphHelper.generateSeries(MyGamesActivity.this, "Purchases",
                                result.getPurchases(), GRAPH_DAYS,
                                Color.GREEN));
                    }
                    if (result.getDownloads() != null) {
                        graphView.addSeries(GraphHelper.generateSeries(MyGamesActivity.this, "Downloads",
                                result.getDownloads(), GRAPH_DAYS,
                                Color.MAGENTA));
                    }
                } else {
                    getGraphContainer().setVisibility(View.GONE);
                }
            }

            @Override
            public void failure(RetrofitError e) {
                Log.e("Itch", "Failed to retrieve graphs", e);
            }

        });
    }

    private void updateNews() {
        TumblrApi api = TumblrApiClient.getClient();
        api.listPosts(1, new Callback<PostsResponse>() {

            @Override
            public void failure(RetrofitError e) {
                Log.e("Itch", "Failed to retrieve news", e);
            }

            @Override
            public void success(PostsResponse result, Response arg1) {
                if (result != null && result.getResponse() != null && result.getResponse().getPosts() != null
                        && result.getResponse().getPosts().size() > 0) {
                    setLatestNews(result.getResponse().getPosts().get(0));
                    loadNewsView(getLatestNews());
                }
            }
        });
    }

    private void loadGraphView() {
        this.getGraphContainer().setVisibility(View.GONE);
        this.graphView = new LineGraphView(this, "");
        this.graphView.setCustomLabelFormatter(GraphHelper.getLabelFormatter(GRAPH_DAYS));
        this.graphView.setShowLegend(true);
        if (GRAPH_DAYS <= 5) {
            this.graphView.getGraphViewStyle().setNumHorizontalLabels(GRAPH_DAYS);
        }

        ViewGroup graph = (ViewGroup) this.getGraphContainer().findViewById(R.id.viewGroupHeaderGraph);
        graph.addView(graphView);
    }

    private void loadNewsView(Post post) {
        View header = getHeader();
        View content = header.findViewById(R.id.viewGroupNewsHeaderContent);
        View collapsed = header.findViewById(R.id.viewGroupNewsHeaderContentCollapsed);
        if (post != null) {
            View view;
            if (PostViewHelper.hasBeenSeen(this, post) && !this.preventCollapse()) {
                content.setVisibility(View.GONE);
                view = collapsed;
            } else {
                collapsed.setVisibility(View.GONE);
                view = content;
            }
            view.setVisibility(View.VISIBLE);
            PostViewHelper.populateView(this, view, post);
            header.invalidate();
            gamesAdapter.notifyDataSetChanged();
        } else {
            content.setVisibility(View.GONE);
            collapsed.setVisibility(View.GONE);
        }
    }

    private ViewGroup getGraphContainer() {
        if (this.graphContainer == null) {
            this.graphContainer = (ViewGroup) this.getHeader().findViewById(R.id.viewGroupHeaderGraphContainer);
        }
        return this.graphContainer;
    }

    private View getHeader() {
        if (this.header == null) {
            this.header = LayoutInflater.from(this).inflate(R.layout.news_header, null);
            ListView list = (ListView) findViewById(R.id.listViewGames);
            list.addHeaderView(this.header);
        }
        return this.header;
    }

    public Post getLatestNews() {
        return latestNews;
    }

    public void setLatestNews(Post latestNews) {
        this.latestNews = latestNews;
    }

    public boolean preventCollapse() {
        return preventCollapse;
    }

    public void setPreventCollapse(boolean flag) {
        this.preventCollapse = flag;
    }

    @Override
    protected String getScreenPath() {
        return "My Games";
    }

}

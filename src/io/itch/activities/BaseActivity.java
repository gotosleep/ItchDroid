package io.itch.activities;

import io.itch.ItchApp;
import io.itch.R;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

abstract class BaseActivity extends Activity {

    public View getEmptyView() {
        View result = LayoutInflater.from(this).inflate(R.layout.activity_empty, null);
        TextView message = (TextView) result.findViewById(R.id.textViewEmptyMessage);
        if (message != null) {
            message.setText(getEmptyViewMessageId());
        }
        return result;
    }

    public int getEmptyViewMessageId() {
        return R.string.activity_empty_default;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Tracker t = ((ItchApp) this.getApplication()).getTracker();
        t.setScreenName(getScreenPath());
        t.send(new HitBuilders.AppViewBuilder().build());
    }

    protected abstract String getScreenPath();
}

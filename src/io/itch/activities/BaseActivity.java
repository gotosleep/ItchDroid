package io.itch.activities;

import io.itch.ItchApp;
import io.itch.R;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
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

    protected ScrollPosition preserveScroll(ListView list) {
        int index = list.getFirstVisiblePosition();
        View v = list.getChildAt(0);
        int top = (v == null) ? 0 : v.getTop();
        return new ScrollPosition(index, top);
    }

    protected void restoreScroll(ListView list, ScrollPosition position) {
        list.setSelectionFromTop(position.getIndex(), position.getTop());
    }

    protected abstract String getScreenPath();

    protected static final class ScrollPosition {
        private final Integer index;
        private final Integer top;

        private ScrollPosition(Integer index, Integer top) {
            super();
            this.index = index;
            this.top = top;
        }

        public Integer getIndex() {
            return index;
        }

        public Integer getTop() {
            return top;
        }

    }
}

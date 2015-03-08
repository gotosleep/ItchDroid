package io.itch.models;

import io.itch.views.GraphHelper;

import java.util.Calendar;
import java.util.List;

import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;

public class GraphData {

    private final int days;
    private final List<GraphPoint> points;
    private GraphViewSeries series;
    private final String label;
    private final GraphViewSeriesStyle style;

    public GraphData(String label, GraphViewSeriesStyle style, int days, List<GraphPoint> points) {
        super();
        this.days = days;
        this.points = points;
        this.label = label;
        this.style = style;
    }

    public GraphViewSeries getSeries() {
        if (this.series == null && this.points != null) {
            GraphViewData data[] = new GraphViewData[days];
            Calendar iter = GraphHelper.startDate(days);
            for (int i = 0; i < days; ++i) {
                String key = GraphHelper.DATE_FORMAT.format(iter.getTime());
                GraphPoint point = null;
                for (GraphPoint p : this.points) {
                    if (key.equals(p.getDate())) {
                        point = p;
                        break;
                    }
                }
                if (point != null) {
                    data[i] = new GraphViewData(i, point.getCount());
                } else {
                    data[i] = new GraphViewData(i, 0);
                }
                iter.add(Calendar.DATE, 1);
            }
            this.series = new GraphViewSeries(label, style, data);
        }
        return this.series;
    }

}
